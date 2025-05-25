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

/**
 * <h2>歌曲卡片元件</h2>
 * 用於顯示歌曲資訊包含歌名、歌手、長度和封面圖片。
 * 支援滑鼠懸停縮放效果和點擊彈出詳細資訊視窗。
 * 卡片高度同封面圖片高度，由 prefHeight 決定。
 * 封面圖片寬度由 songImageWidth 決定。
 * 封面圖片的尺寸，和你圖片實際尺寸，完全不合也沒關係。
 * 此元件會自動把圖片裁減到合適比例。
 * 註：歌名、歌手、長度和封面圖片，通通都是 responsive。
 */
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

    /**
     * 預設建構子，會載入對應的 FXML 版面。
     *
     * <p>FXML 使用範例：</p>
     * <pre>{@code
     * <SongItemCard songName="Beautiful Song"
     *               songAuthor="Famous Artist"
     *               songImageUrl="https://picsum.photos/400/400"
     *               scalingEffect="true" />
     * }</pre>
     */
    public SongItemCard() {
        Loader.loadComponentView(this, "/view/component/SongItemCard.fxml");
        bindProperty();
        initClickHandler();
        initScalingEffect();
        enableScalingEffect();
    }

    /**
     * 在 Java 內手動建立元件。
     *
     * <p>Java 使用範例：</p>
     * <pre>{@code
     * VBox container = new VBox();
     * SongItemCard card = new SongItemCard(
     *     "My Favorite Song",
     *     "Best Artist",
     *     Duration.minutes(3.5),
     *     "https://picsum.photos/400/400",
     *     true
     * );
     * container.getChildren().add(card);
     * }</pre>
     *
     * @param songName      歌曲名稱
     * @param songAuthor    歌手名稱
     * @param songLength    歌曲長度
     * @param songImageUrl  歌曲封面圖片 URL
     * @param scalingEffect 是否啟用滑鼠懸停縮放效果
     */
    public SongItemCard(String songName, String songAuthor, Duration songLength, String songImageUrl, boolean scalingEffect) {
        this();
        setSongName(songName);
        setSongAuthor(songAuthor);
        setSongLength(songLength);
        setSongImageUrl(songImageUrl);
        setScalingEffect(scalingEffect);
    }

    /**
     * 綁定屬性到對應的 UI 元素。
     */
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
        // 卡面上的封面圖片 <- 封面圖片 URL 屬性
        songImageView.imageProperty().bind(songImageUrlProperty().map(url -> {
            Image image = Loader.loadImage(url);
            if (image != null) {
                Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), getPrefHeight(), songImageWidth.get());
                songImageView.setViewport(viewport);
            }
            return image;
        }));
        // 卡面上的封面圖片的寬度屬性 <- 封面圖片寬度屬性 (圖片寬度不與卡片同寬，因為這是橫向卡片)
        songImageView.fitWidthProperty().bind(songImageWidth);
        // 封面圖片視圖的 viewport <- 卡面上的封面圖片的寬度屬性
        songImageView.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            Image image = songImageView.getImage();
            Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), getPrefHeight(), newVal.doubleValue());
            songImageView.setViewport(viewport);
        });
        // 卡面上的封面圖片的高度屬性 <- 卡片高度屬性 (圖片高度與卡片同高)
        songImageView.fitHeightProperty().bind(prefHeightProperty());
        // 封面圖片視圖的 viewport <- 卡面上的封面圖片的高度屬性
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

    /**
     * 初始化縮放動畫效果。
     */
    private void initScalingEffect() {
        // 改用 JavaFX 做 scale 特效，是因為 JavaFX CSS 不支援 scale transition
        scaleUpOnHover = new ScaleTransition(Duration.millis(200), this);
        scaleUpOnHover.setToX(1.1);
        scaleUpOnHover.setToY(1.1);

        scaleDownOnExit = new ScaleTransition(Duration.millis(200), this);
        scaleDownOnExit.setToX(1);
        scaleDownOnExit.setToY(1);
    }

    /**
     * 啟用滑鼠懸停縮放效果。
     */
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

    /**
     * 停用滑鼠懸停縮放效果。
     */
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

    /**
     * 初始化點擊事件處理器。
     */
    private void initClickHandler() {
        setOnMouseClicked(e -> showSongInfoPopup());
    }

    /**
     * 顯示歌曲資訊彈出視窗。
     */
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

    /**
     * 取得歌曲名稱屬性值。
     *
     * @return 目前設定的歌曲名稱
     */
    public String getSongName() {
        return songName.get();
    }

    /**
     * 設定歌曲名稱屬性值。
     *
     * @param name 新的歌曲名稱
     */
    public void setSongName(String name) {
        songName.set(name);
    }

    /**
     * 取得歌曲名稱的 StringProperty 物件。
     *
     * @return 歌曲名稱的 StringProperty 物件
     */
    public StringProperty songNameProperty() {
        return songName;
    }

    /**
     * 取得歌手名稱屬性值。
     *
     * @return 目前設定的歌手名稱
     */
    public String getSongAuthor() {
        return songAuthor.get();
    }

    /**
     * 設定歌手名稱屬性值。
     *
     * @param author 新的歌手名稱
     */
    public void setSongAuthor(String author) {
        songAuthor.set(author);
    }

    /**
     * 取得歌手名稱的 StringProperty 物件。
     *
     * @return 歌手名稱的 StringProperty 物件
     */
    public StringProperty songAuthorProperty() {
        return songAuthor;
    }

    /**
     * 取得歌曲長度屬性值。
     *
     * @return 目前設定的歌曲長度
     */
    public Duration getSongLength() {
        return songLength.get();
    }

    /**
     * 設定歌曲長度屬性值。
     *
     * @param length 新的歌曲長度
     */
    public void setSongLength(Duration length) {
        songLength.set(length);
    }

    /**
     * 取得歌曲長度的 ObjectProperty 物件。
     *
     * @return 歌曲長度的 ObjectProperty 物件
     */
    public ObjectProperty<Duration> songLengthProperty() {
        return songLength;
    }

    /**
     * 取得歌曲封面圖片 URL 屬性值。
     *
     * @return 目前設定的封面圖片 URL
     */
    public String getSongImageUrl() {
        return songImageUrl.get();
    }

    /**
     * 設定歌曲封面圖片 URL 屬性值。
     *
     * @param imageUrl 新的封面圖片 URL
     */
    public void setSongImageUrl(String imageUrl) {
        songImageUrl.set(imageUrl);
    }

    /**
     * 取得歌曲封面圖片 URL 的 StringProperty 物件。
     *
     * @return 封面圖片 URL 的 StringProperty 物件
     */
    public StringProperty songImageUrlProperty() {
        return songImageUrl;
    }

    /**
     * 取得歌曲圖片寬度屬性值。
     *
     * @return 目前設定的圖片寬度
     */
    public double getSongImageWidth() {
        return songImageWidth.get();
    }

    /**
     * 設定歌曲圖片寬度屬性值。
     *
     * @param width 新的圖片寬度
     */
    public void setSongImageWidth(double width) {
        songImageWidth.set(width);
    }

    /**
     * 取得歌曲圖片寬度的 DoubleProperty 物件。
     *
     * @return 圖片寬度的 DoubleProperty 物件
     */
    public DoubleProperty songImageWidthProperty() {
        return songImageWidth;
    }

    /**
     * 取得縮放效果啟用狀態屬性值。
     *
     * @return 目前縮放效果是否啟用
     */
    public boolean getScalingEffect() {
        return scalingEffect.get();
    }

    /**
     * 設定縮放效果啟用狀態屬性值。
     *
     * @param value 是否啟用縮放效果
     */
    public void setScalingEffect(boolean value) {
        scalingEffect.set(value);
    }

    /**
     * 取得縮放效果啟用狀態的 BooleanProperty 物件。
     *
     * @return 縮放效果啟用狀態的 BooleanProperty 物件
     */
    public BooleanProperty scalingEffectProperty() {
        return scalingEffect;
    }
}