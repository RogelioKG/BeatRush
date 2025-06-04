package org.notiva.beatrush.core;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import org.notiva.beatrush.util.Misc;
import org.notiva.beatrush.util.Note;

/**
 * <h2>資源載入管理器</h2>
 */
public class ResourceLoader {

    /**
     * 載入自訂元件 FXML 視圖。
     *
     * @param component    元件實例。
     * @param resourcePath FXML 資源路徑，例如 {@code "/view/component/CustomControl.fxml"}。
     * @return 已載入的 {@link Parent} 根節點。
     * @throws RuntimeException FXML 載入失敗。
     */
    public static Parent loadComponentView(Object component, String resourcePath) {
        URL resourceUrl = ResourceLoader.class.getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("FXML resource not found: " + resourcePath);
        }
        try {
            FXMLLoader loader = new FXMLLoader(resourceUrl);
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
        URL resourceUrl = ResourceLoader.class.getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("FXML resource not found: " + resourcePath);
        }
        try {
            FXMLLoader loader = new FXMLLoader(resourceUrl);
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
     * @throws IllegalArgumentException 若資源找不到。
     */
    public static Image loadImage(String path) {
        if (Misc.isUrl(path)) {
            return new Image(path, true);
        } else {
            InputStream inputStream = ResourceLoader.class.getResourceAsStream(path);
            if (inputStream == null) {
                throw new IllegalArgumentException("Image resource not found: " + path);
            }
            return new Image(inputStream);
        }
    }

    /**
     * 載入音效資源。
     *
     * @param path 資源內部路徑或 URL。
     * @return 已載入的 {@link AudioClip} 實例。
     * @throws IllegalArgumentException 若資源找不到。
     */
    public static AudioClip loadAudio(String path) {
        if (Misc.isUrl(path)) {
            return new AudioClip(path);
        } else {
            URL resourceUrl = ResourceLoader.class.getResource(path);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Audio resource not found: " + path);
            }
            return new AudioClip(resourceUrl.toExternalForm());
        }
    }

    /**
     * 載入音樂資源。
     *
     * @param path 資源內部路徑或 URL。
     * @return 已載入的 {@link Media} 實例。
     * @throws IllegalArgumentException 若資源找不到。
     */
    public static Media loadMedia(String path) {
        if (Misc.isUrl(path)) {
            return new Media(path);
        } else {
            URL resourceUrl = ResourceLoader.class.getResource(path);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Media resource not found: " + path);
            }
            return new Media(resourceUrl.toExternalForm());
        }
    }

    /**
     * 載入檔案資源。
     *
     * @param resourcePath 資源相對路徑，例如 "/data/notes.json"
     * @return 對應的 {@link Reader}
     * @throws IllegalArgumentException 若找不到資源
     */
    public static Reader loadFile(String resourcePath) {
        InputStream inputStream = ResourceLoader.class.getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * 載入譜面 JSON 檔案。
     *
     * @param songName 歌曲名稱
     * @return 對應的 {@link List<Note>}
     */
    public static List<Note> loadChart(String songName) {
        Gson gson = new Gson();
        Reader reader = ResourceLoader.loadFile("/chart/" + songName + ".json");
        Type listType = new TypeToken<List<Note>>() {
        }.getType();
        return gson.fromJson(reader, listType);
    }
}