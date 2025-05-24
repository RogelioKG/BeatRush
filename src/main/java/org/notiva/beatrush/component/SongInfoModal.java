package org.notiva.beatrush.component;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.notiva.beatrush.core.Loader;
import org.notiva.beatrush.util.MiscUtil;

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
    private Button actionButton;

    private final double DEFAULT_VIEW_HEIGHT = 300.0;

    private final StringProperty songName = new SimpleStringProperty();
    private final StringProperty songAuthor = new SimpleStringProperty();
    private final ObjectProperty<Duration> songLength = new SimpleObjectProperty<>();
    private final StringProperty songImageUrl = new SimpleStringProperty();
    private final DoubleProperty songImageHeight = new SimpleDoubleProperty(DEFAULT_VIEW_HEIGHT);

    public SongInfoModal() {
        Loader.loadComponentView(this, "/view/component/SongInfoModal.fxml");
        bindProperty();
    }

    public SongInfoModal(String songName, String songAuthor, Duration songLength, String songImageUrl) {
        this();
        setSongName(songName);
        setSongAuthor(songAuthor);
        setSongLength(songLength);
        setSongImageUrl(songImageUrl);
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
                Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), songImageHeight.get(), getPrefWidth());
                songImageView.setViewport(viewport);
            }
            return image;
        }));
        // 卡面上歌曲視圖的寬 <- 歌曲視圖寬屬性 (寬度不與卡片同寬)
        songImageView.fitWidthProperty().bind(prefWidthProperty());
        // 歌曲視圖圖片的 viewport 變動 <- 卡面上歌曲視圖的寬
        songImageView.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            Image image = songImageView.getImage();
            Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), getPrefHeight(), newVal.doubleValue());
            songImageView.setViewport(viewport);
        });
        // 卡面上歌曲視圖的高 <- 高度屬性 (高度不與卡片同高，因為這是直向卡片)
        songImageView.fitHeightProperty().bind(songImageHeight);
        // 歌曲視圖圖片的 viewport 變動 <- 卡面上歌曲視圖的高
        songImageView.fitHeightProperty().addListener((obs, oldVal, newVal) -> {
            Image image = songImageView.getImage();
            Rectangle2D viewport = MiscUtil.getCenteredCoverCrop(image.getHeight(), image.getWidth(), newVal.doubleValue(), songImageHeight.get());
            songImageView.setViewport(viewport);
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

    // songImageHeight
    public double getSongImageHeight() {
        return songImageHeight.get();
    }

    public void setSongImageHeight(double height) {
        songImageHeight.set(height);
    }

    public DoubleProperty songImageHeightProperty() {
        return songImageHeight;
    }
}
