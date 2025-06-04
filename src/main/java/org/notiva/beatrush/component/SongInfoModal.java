package org.notiva.beatrush.component;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.notiva.beatrush.core.ResourceLoader;
import org.notiva.beatrush.util.Misc;

/**
 * <h2>歌曲資訊彈窗元件</h2>
 * <p>
 * 用於以彈窗形式顯示歌曲的詳細資訊，包含歌名、歌手、長度和封面圖片。
 * 此元件採用直向卡片設計，封面圖片位於上方，文字資訊位於下方。
 * 卡片寬度同封面圖片寬度，由 prefWidth 決定。
 * 封面圖片高度由 songImageHeight 決定。
 * 封面圖片的尺寸，和你圖片實際尺寸，完全不合也沒關係。
 * 此元件會自動把圖片裁減到合適比例。
 * 註：歌名、歌手、長度和封面圖片，通通都是 responsive。
 * </p>
 */
public class SongInfoModal extends AnchorPane {
    @FXML
    private ImageView songImageView;
    @FXML
    private Label songNameLabel;
    @FXML
    private Label songAuthorLabel;
    @FXML
    private Label songLengthLabel;
    @FXML
    private Button playButton;

    private final double DEFAULT_VIEW_HEIGHT = 300.0;

    private final StringProperty songName = new SimpleStringProperty();
    private final StringProperty songAuthor = new SimpleStringProperty();
    private final ObjectProperty<Duration> songLength = new SimpleObjectProperty<>();
    private final StringProperty songImagePath = new SimpleStringProperty();
    private final DoubleProperty songImageHeight = new SimpleDoubleProperty(DEFAULT_VIEW_HEIGHT);

    /**
     * 預設建構子，會載入對應的 FXML 版面。
     *
     * <p>FXML 使用範例：</p>
     * <pre>{@code
     * <SongInfoModal songName="Beautiful Song"
     *                songAuthor="Famous Artist"
     *                songImagePath="https://picsum.photos/400/400"
     *                songImageHeight="350" />
     * }</pre>
     */
    public SongInfoModal() {
        ResourceLoader.loadComponentView(this, "/view/component/SongInfoModal.fxml");
        bindProperty();
    }

    /**
     * 在 Java 內手動建立元件。
     *
     * <p>Java 使用範例：</p>
     * <pre>{@code
     * SongInfoModal modal = new SongInfoModal(
     *     "My Favorite Song",
     *     "Best Artist",
     *     Duration.minutes(3.5),
     *     "https://picsum.photos/400/400"
     * );
     * }</pre>
     *
     * @param songName       歌曲名稱
     * @param songAuthor     歌手名稱
     * @param songLength     歌曲長度
     * @param songImagePath  歌曲封面圖片路徑
     */
    public SongInfoModal(String songName, String songAuthor, Duration songLength, String songImagePath) {
        this();
        setSongName(songName);
        setSongAuthor(songAuthor);
        setSongLength(songLength);
        setSongImagePath(songImagePath);
    }

    /**
     * 綁定屬性到對應的 UI 元素。
     */
    private void bindProperty() {
        // 彈窗上的歌名 <- 歌名屬性
        songNameLabel.textProperty().bind(songNameProperty());
        // 彈窗上的歌手 <- 歌手屬性
        songAuthorLabel.textProperty().bind(songAuthorProperty());
        // 彈窗上的歌曲長度 <- 歌曲長度屬性
        songLength.addListener((obs, oldVal, newVal) -> {
            String songLengthString = Misc.formatDuration(newVal);
            songLengthLabel.setText(songLengthString);
        });
        // 彈窗上的封面圖片 <- 封面圖片路徑屬性
        songImageView.imageProperty().bind(songImagePathProperty().map(path -> {
            Image image = ResourceLoader.loadImage(path);
            if (image.getWidth() > 0) {
                // normal loading 時，直接更新 viewport
                updateViewport(image, songImageHeight.get(), getPrefWidth());
            } else {
                // background loading 在未載入時，圖片長寬為 0，我們要等載入完後再調整 viewport
                image.widthProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() > 0) {
                        updateViewport(image, songImageHeight.get(), getPrefWidth());
                    }
                });
            }
            return image;
        }));
        // 彈窗上的封面圖片的寬度屬性 <- 彈窗寬度屬性 (圖片寬度與彈窗同寬)
        songImageView.fitWidthProperty().bind(prefWidthProperty());
        // 封面圖片視圖的 viewport <- 彈窗上的封面圖片的寬度屬性
        songImageView.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            updateViewport(songImageView.getImage(), getPrefHeight(), newVal.doubleValue());
        });
        // 彈窗上的封面圖片的高度屬性 <- 封面圖片高度屬性 (圖片高度不與彈窗同高，因為這是直向卡片)
        songImageView.fitHeightProperty().bind(songImageHeight);
        // 封面圖片視圖的 viewport <- 彈窗上的封面圖片的高度屬性
        songImageView.fitHeightProperty().addListener((obs, oldVal, newVal) -> {
            updateViewport(songImageView.getImage(), newVal.doubleValue(), songImageHeight.get());
        });
    }

    /**
     * 點擊 PLAY 按鈕時所觸發的 EventHandler。
     *
     * @param e 要執行的 EventHandler。
     */
    public void setOnPlayButtonClick(EventHandler<ActionEvent> e) {
        playButton.setOnAction(e);
    }

    /**
     * 更新 viewport。
     *
     * @param image       歌曲封面圖片
     * @param viewHeight  目標高度
     * @param viewWidth   目標寬度
     */
    private void updateViewport(Image image, double viewHeight, double viewWidth) {
        double imageHeight = image.getHeight();
        double imageWidth = image.getWidth();
        Rectangle2D viewport = Misc.getCenteredCoverCrop(imageHeight, imageWidth, viewHeight, viewWidth);
        songImageView.setViewport(viewport);
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
     * 取得歌曲封面圖片路徑屬性值。
     *
     * @return 目前設定的封面圖片路徑
     */
    public String getSongImagePath() {
        return songImagePath.get();
    }

    /**
     * 設定歌曲封面圖片路徑屬性值。
     *
     * @param imagePath 新的封面圖片路徑
     */
    public void setSongImagePath(String imagePath) {
        songImagePath.set(imagePath);
    }

    /**
     * 取得歌曲封面圖片路徑的 StringProperty 物件。
     *
     * @return 封面圖片路徑的 StringProperty 物件
     */
    public StringProperty songImagePathProperty() {
        return songImagePath;
    }

    /**
     * 取得歌曲圖片高度屬性值。
     *
     * @return 目前設定的圖片高度
     */
    public double getSongImageHeight() {
        return songImageHeight.get();
    }

    /**
     * 設定歌曲圖片高度屬性值。
     *
     * @param height 新的圖片高度
     */
    public void setSongImageHeight(double height) {
        songImageHeight.set(height);
    }

    /**
     * 取得歌曲圖片高度的 DoubleProperty 物件。
     *
     * @return 圖片高度的 DoubleProperty 物件
     */
    public DoubleProperty songImageHeightProperty() {
        return songImageHeight;
    }
}