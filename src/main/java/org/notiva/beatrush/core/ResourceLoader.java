package org.notiva.beatrush.core;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import org.notiva.beatrush.util.Misc;


public class ResourceLoader {

    /**
     * 載入自訂元件 FXML 視圖。
     *
     * @param component 元件實例。
     * @param resourcePath FXML 資源路徑，例如 {@code "/view/component/CustomControl.fxml"}。
     * @return 已載入的 {@link Parent} 根節點。
     * @throws RuntimeException FXML 載入失敗。
     */
    public static Parent loadComponentView(Object component, String resourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceLoader.class.getResource(resourcePath));
            loader.setRoot(component);
            loader.setController(component);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to load FXML: %s", resourcePath), e);
        }
    }

    /**
     * 載入一般 FXML 視圖。
     *
     * @param resourcePath FXML 資源路徑，例如 {@code "/view/page/MainView.fxml"}。
     * @return 已載入的 {@link Parent} 根節點。
     * @throws RuntimeException FXML 載入失敗。
     */
    public static Parent loadView(String resourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceLoader.class.getResource(resourcePath));
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to load FXML: %s", resourcePath), e);
        }
    }

    /**
     * 載入圖片資源。
     *
     * @param path 資源內部路徑或 URL。
     * @return 已載入的 {@link Image} 實例。
     */
    public static Image loadImage(String path) {
        if (Misc.isUrl(path)) {
            return new Image(path, true);
        } else {
            return new Image(Objects.requireNonNull(ResourceLoader.class.getResourceAsStream(path)));
        }
    }

    /**
     * 載入音效資源。
     *
     * @param path 資源內部路徑或 URL。
     * @return 已載入的 {@link AudioClip} 實例。
     */
    public static AudioClip loadAudio(String path) {
        if (Misc.isUrl(path)) {
            return new AudioClip(path);
        } else {
            return new AudioClip(Objects.requireNonNull(ResourceLoader.class.getResource(path)).toExternalForm());
        }
    }

    /**
     * 載入音樂資源。
     *
     * @param path 資源內部路徑或 URL。
     * @return 已載入的 {@link Media} 實例。
     */
    public static Media loadMedia(String path) {
        if (Misc.isUrl(path)) {
            return new Media(path);
        } else {
            return new Media(Objects.requireNonNull(ResourceLoader.class.getResource(path)).toExternalForm());
        }
    }
}
