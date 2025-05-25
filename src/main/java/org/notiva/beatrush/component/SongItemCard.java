package org.notiva.beatrush.component;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.notiva.beatrush.core.Loader;
import org.notiva.beatrush.event.MaskLayerHideEvent;
import org.notiva.beatrush.event.MaskLayerShowEvent;
import org.notiva.beatrush.util.MiscUtil;

public class SongItemCard extends AnchorPane {
    @FXML
    private ImageView songImageView;

    @FXML
    private Label songNameLabel;

    @FXML
    private Label songAuthorLabel;

    @FXML
    private Label songLengthLabel;

    private ScaleTransition scaleUpOnHover;
    private ScaleTransition scaleDownOnExit;
    private Popup songInfoPopup;

    private final double DEFAULT_VIEW_WIDTH = 200.0;

    private final StringProperty songName = new SimpleStringProperty();
    private final StringProperty songAuthor = new SimpleStringProperty();
    private final ObjectProperty<Duration> songLength = new SimpleObjectProperty<>();
    private final StringProperty songImageUrl = new SimpleStringProperty();
    private final BooleanProperty scalingEffect = new SimpleBooleanProperty(true);
    private final DoubleProperty songImageWidth = new SimpleDoubleProperty(DEFAULT_VIEW_WIDTH);

    public SongItemCard() {
        Loader.loadComponentView(this, "/view/component/SongItemCard.fxml");
        bindProperty();
        initClickHandler();
        initScalingEffect();
        enableScalingEffect();
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
        // 卡面上的歌名 <- 歌名屬性
        songNameLabel.textProperty().bind(songNameProperty());
        // 卡面上的歌手 <- 歌手屬性
        songAuthorLabel.textProperty().bind(songAuthorProperty());
        // 卡面上的歌曲長度 <- 歌曲長度屬性
        songLength.addListener((obs, oldVal, newVal) -> {
            String songLengthString = MiscUtil.formatDuration(newVal);
            songLengthLabel.setText(songLengthString);
        });
        // 卡面上的歌曲圖片 <- 歌曲視圖圖片屬性
        songImageView.imageProperty().bind(songImageUrlProperty().map(url -> {
            Image image = Loader.loadImage(url);
            if (image != null) {
                Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), getPrefHeight(), songImageWidth.get());
                songImageView.setViewport(viewport);
            }
            return image;
        }));
        // 卡面上歌曲視圖的寬 <- 歌曲視圖寬屬性 (寬度不與卡片同寬，因為這是橫向卡片)
        songImageView.fitWidthProperty().bind(songImageWidth);
        // 歌曲視圖圖片的 viewport 變動 <- 卡面上歌曲視圖的寬
        songImageView.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            Image image = songImageView.getImage();
            Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), getPrefHeight(), newVal.doubleValue());
            songImageView.setViewport(viewport);
        });
        // 卡面上歌曲視圖的高 <- 高度屬性 (高度與卡片同高)
        songImageView.fitHeightProperty().bind(prefHeightProperty());
        // 歌曲視圖圖片的 viewport 變動 <- 卡面上歌曲視圖的高
        songImageView.fitHeightProperty().addListener((obs, oldVal, newVal) -> {
            Image image = songImageView.getImage();
            Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), newVal.doubleValue(), songImageWidth.get());
            songImageView.setViewport(viewport);
        });
        // 縮放動效是否啟用 <- 縮放動效啟用屬性
        scalingEffect.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                enableScalingEffect();
            } else {
                disableScalingEffect();
            }
        });
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
        // === 0. 已彈窗 guard ===
        if (songInfoPopup != null && songInfoPopup.isShowing()) {
            songInfoPopup.hide();
            return;
        }

        // === 1. 淡入遮罩 ===
        fireEvent(new MaskLayerShowEvent());

        // === 2. 內容 ===
        SongInfoModal popupContent = new SongInfoModal(getSongName(), getSongAuthor(), getSongLength(), getSongImageUrl());
        popupContent.setOpacity(0);

        // === 3. popup ===
        songInfoPopup = new Popup();
        songInfoPopup.getContent().add(popupContent);
        songInfoPopup.setAutoHide(true);
        songInfoPopup.setHideOnEscape(true);

        // 顯示 popup
        Scene scene = this.getScene();
        songInfoPopup.show(scene.getWindow());

        // 淡入內容
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), popupContent);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // 當 popup 被關閉時，淡出遮罩
        songInfoPopup.setOnHidden(e -> {
            fireEvent(new MaskLayerHideEvent());
            songInfoPopup = null;
        });

        // === 4. 中央定位 ===
        Platform.runLater(() -> {
            double anchorX = scene.getWindow().getX() + scene.getWidth() / 2 - songInfoPopup.getWidth() / 2;
            double anchorY = scene.getWindow().getY() + scene.getHeight() / 2 - songInfoPopup.getHeight() / 2;
            songInfoPopup.setX(anchorX);
            songInfoPopup.setY(anchorY);
        });
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

    // songImageWidth
    public double getSongImageWidth() {
        return songImageWidth.get();
    }

    public void setSongImageWidth(double width) {
        songImageWidth.set(width);
    }

    public DoubleProperty songImageWidthProperty() {
        return songImageWidth;
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
