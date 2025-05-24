package org.notiva.beatrush.component;

import javafx.animation.ScaleTransition;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.notiva.beatrush.core.Loader;
import org.notiva.beatrush.util.MiscUtil;

public class SongItemCard extends AnchorPane {
    @FXML
    private ImageView songImage;

    @FXML
    private Label songNameLabel;

    @FXML
    private Label songAuthorLabel;

    @FXML
    private Label songLengthLabel;

    private ScaleTransition scaleUpOnHover;
    private ScaleTransition scaleDownOnExit;
    private Popup songInfoPopup;
    private final double VIEW_WIDTH = 200.0;
    private final double VIEW_HEIGHT = 150.0;

    private final StringProperty songName = new SimpleStringProperty();
    private final StringProperty songAuthor = new SimpleStringProperty();
    private final ObjectProperty<Duration> songLength = new SimpleObjectProperty<>();
    private final StringProperty songImageUrl = new SimpleStringProperty();
    private final BooleanProperty scalingEffect = new SimpleBooleanProperty(true);

    public SongItemCard() {
        Loader.loadComponentView(this, "/view/component/SongItemCard.fxml");
        initImageView();
        initClickHandler();
        initScalingEffect();
        enableScalingEffect();
        bindProperty();
    }

    public SongItemCard(String songName, String songAuthor, Duration songLength, String songImageUrl, boolean scalingEffect) {
        this();
        setSongName(songName);
        setSongAuthor(songAuthor);
        setSongLength(songLength);
        setSongImageUrl(songImageUrl);
        setScalingEffect(scalingEffect);
    }

    private void bindProperty() {
        songNameLabel.textProperty().bind(songNameProperty());
        songAuthorLabel.textProperty().bind(songAuthorProperty());
        songLength.addListener((obs, oldVal, newVal) -> {
            String songLengthString = MiscUtil.formatDuration(newVal);
            songLengthLabel.setText(songLengthString);
        });
        songImage.imageProperty().bind(songImageUrlProperty().map(url -> {
            Image image = Loader.loadImage(url);
            if (image != null) {
                Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), VIEW_HEIGHT, VIEW_WIDTH);
                songImage.setViewport(viewport);
            }
            return image;
        }));
        scalingEffect.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                enableScalingEffect();
            } else {
                disableScalingEffect();
            }
        });
    }

    private void initImageView() {
        songImage.setFitWidth(VIEW_WIDTH);
        songImage.setFitHeight(VIEW_HEIGHT);
    }

    private void initScalingEffect() {
        // 改用 JavaFX 做 scale 特效，是因為 JavaFX CSS 不支援 scale transition
        scaleUpOnHover = new ScaleTransition(Duration.millis(200), this);
        scaleUpOnHover.setToX(1.1);
        scaleUpOnHover.setToY(1.1);

        scaleDownOnExit = new ScaleTransition(Duration.millis(200), this);
        scaleDownOnExit.setToX(1);
        scaleDownOnExit.setToY(1);
    }

    private void enableScalingEffect() {
        setOnMouseEntered(e -> {
            scaleDownOnExit.stop();
            scaleUpOnHover.playFromStart();
        });

        setOnMouseExited(e -> {
            scaleUpOnHover.stop();
            scaleDownOnExit.playFromStart();
        });
    }

    private void disableScalingEffect() {
        // 停止動畫並重設大小
        if (scaleUpOnHover != null) scaleUpOnHover.stop();
        if (scaleDownOnExit != null) scaleDownOnExit.stop();

        setScaleX(1.0);
        setScaleY(1.0);

        // 移除滑鼠事件
        setOnMouseEntered(null);
        setOnMouseExited(null);
    }

    private void initClickHandler() {
        setOnMouseClicked(e -> showSongInfoPopup());
    }

    private void showSongInfoPopup() {
        System.out.println("showSongInfoPopup");
        if (songInfoPopup != null && songInfoPopup.isShowing()) {
            songInfoPopup.hide();
            return;
        }

        // 創建 Popup 內容
        VBox popupContent = createPopupContent();

        // 創建 Popup
        songInfoPopup = new Popup();
        songInfoPopup.getContent().add(popupContent);
        songInfoPopup.setAutoHide(true);
        songInfoPopup.setHideOnEscape(true);

        // 顯示 Popup
        Scene scene = this.getScene();
        if (scene != null && scene.getWindow() != null) {
            double centerX = scene.getWindow().getX() + scene.getWidth() / 2 - 150; // 150 是 popup 寬度的一半
            double centerY = scene.getWindow().getY() + scene.getHeight() / 2 - 200; // 200 是 popup 高度的估計值的一半
            songInfoPopup.show(scene.getWindow(), centerX, centerY);
        }
    }

    private VBox createPopupContent() {
        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #cccccc; " +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 1; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");
        content.setPrefWidth(300);

        // 歌曲圖片
        ImageView popupImage = new ImageView();
        popupImage.setFitWidth(200);
        popupImage.setFitHeight(200);
        popupImage.setPreserveRatio(true);

        // 綁定圖片
        if (getSongImageUrl() != null && !getSongImageUrl().isEmpty()) {
            try {
                Image image = new Image(getSongImageUrl());
                popupImage.setImage(image);
            } catch (Exception e) {
                System.err.printf("Failed to load popup image: %s%n", getSongImageUrl());
            }
        }

        // 歌曲名稱
        Label popupSongName = new Label(getSongName());
        popupSongName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // 歌手名稱
        Label popupArtist = new Label(getSongAuthor());
        popupArtist.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");

        // 歌曲長度
        Label popupLength = new Label();
        Duration length = getSongLength();
        if (length != null && !length.isUnknown()) {
            double totalSeconds = length.toSeconds();
            int minutes = (int) (totalSeconds / 60);
            int seconds = (int) (totalSeconds % 60);
            popupLength.setText(String.format("Length: %02d:%02d", minutes, seconds));
        } else {
            popupLength.setText("Length: Unknown");
        }
        popupLength.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

        // 按鈕
        Button actionButton = new Button("Play");
        actionButton.setStyle("-fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8 20; " +
                "-fx-background-radius: 5;");
        actionButton.setOnMouseEntered(e ->
                actionButton.setStyle("-fx-background-color: #45a049; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 5;"));
        actionButton.setOnMouseExited(e ->
                actionButton.setStyle("-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 5;"));

        actionButton.setOnAction(e -> {
            // 在這裡添加播放歌曲的邏輯
            System.out.println("Play song: " + getSongName());
            songInfoPopup.hide();
        });

        // 關閉按鈕
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #f44336; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 12px; " +
                "-fx-padding: 5 15; " +
                "-fx-background-radius: 5;");
        closeButton.setOnAction(e -> songInfoPopup.hide());

        // 組裝內容
        content.getChildren().addAll(
                popupImage,
                popupSongName,
                popupArtist,
                popupLength,
                actionButton,
                closeButton
        );

        return content;
    }

    // songName
    public String getSongName() {
        return songName.get();
    }

    public void setSongName(String name) {
        songName.set(name);
    }

    public StringProperty songNameProperty() {
        return songName;
    }

    // songAuthor
    public String getSongAuthor() {
        return songAuthor.get();
    }

    public void setSongAuthor(String author) {
        songAuthor.set(author);
    }

    public StringProperty songAuthorProperty() {
        return songAuthor;
    }

    // songLength
    public Duration getSongLength() {
        return songLength.get();
    }

    public void setSongLength(Duration length) {
        songLength.set(length);
    }

    public ObjectProperty<Duration> songLengthProperty() {
        return songLength;
    }

    // songImageUrl
    public String getSongImageUrl() {
        return songImageUrl.get();
    }

    public void setSongImageUrl(String imageUrl) {
        songImageUrl.set(imageUrl);
    }

    public StringProperty songImageUrlProperty() {
        return songImageUrl;
    }

    // scaling effect
    public boolean getScalingEffect() {
        return scalingEffect.get();
    }

    public void setScalingEffect(boolean value) {
        scalingEffect.set(value);
    }

    public BooleanProperty scalingEffectProperty() {
        return scalingEffect;
    }
}
