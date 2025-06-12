package org.notiva.beatrush.core;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.util.Duration;
import org.notiva.beatrush.util.Misc;
import org.notiva.beatrush.util.Note;
import org.notiva.beatrush.util.Song;

/**
 * <h2>資源載入管理器</h2>
 *
 * <p>支援三種路徑：</p>
 *
 * <h5>URL</h5>
 * <ul>
 *   <li>網路資源，例如：<code>https://example.com/image.png</code></li>
 * </ul>
 *
 * <h5>內部資源路徑</h5>
 * <ul>
 *   <li>以 <code>/src/main/resources</code> 為根目錄</li>
 *   <li>會被打包進 <code>JAR</code> 檔中</li>
 *   <li>使用字串表示時，需以 <code>/</code> 開頭，<br>例如：<code>/images/logo.png</code></li>
 * </ul>
 *
 * <h5>外部路徑</h5>
 * <ul>
 *   <li>以執行時的工作目錄（通常是 <code>/</code>）為根目錄</li>
 *   <li>不會被包進 <code>JAR</code> 中，適合使用者自訂或替換</li>
 *   <li>使用字串表示時，<strong>不需</strong>以 <code>/</code> 開頭，<br>例如：<code>config/settings.json</code></li>
 * </ul>
 */
public class ResourceLoader {

    /**
     * 載入自訂元件 FXML 視圖。
     *
     * @param component    元件實例。
     * @param resourcePath FXML 內部資源路徑，例如 {@code "/view/component/CustomControl.fxml"}。
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
     * @param resourcePath FXML 內部資源路徑，例如 {@code "/view/page/MainView.fxml"}。
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
     * 載入圖片資源 (background loading)。
     *
     * @param path URL、內部資源路徑。
     * @return 已載入的 {@link Image} 實例。
     * @throws IllegalArgumentException 若資源找不到。
     */
    public static Image loadImage(String path) {
        // URL
        if (Misc.isUrl(path)) {
            return new Image(path, true);
        }

        // 內部資源路徑
        InputStream inputStream = ResourceLoader.class.getResourceAsStream(path);
        if (inputStream != null) {
            return new Image(inputStream);
        }

        // 外部路徑
        Path externalPath = Paths.get(path).toAbsolutePath();
        if (Files.exists(externalPath)) {
            return new Image(externalPath.toUri().toString());
        }

        throw new IllegalArgumentException("Image resource not found: " + path);
    }

    /**
     * 載入音效資源。
     *
     * @param path 資源內部路徑或 URL。
     * @return 已載入的 {@link AudioClip} 實例。
     * @throws IllegalArgumentException 若資源找不到。
     */
    public static AudioClip loadAudio(String path) {
        // URL
        if (Misc.isUrl(path)) {
            return new AudioClip(path);
        }

        // 內部資源路徑
        URL resourceUrl = ResourceLoader.class.getResource(path);
        if (resourceUrl != null) {
            return new AudioClip(resourceUrl.toExternalForm());
        }

        // 外部路徑
        Path externalPath = Paths.get(path).toAbsolutePath();
        if (Files.exists(externalPath)) {
            return new AudioClip(externalPath.toUri().toString());
        }

        throw new IllegalArgumentException("Audio resource not found: " + path);
    }

    /**
     * 載入音樂資源。
     *
     * @param path URL、內部資源路徑、外部路徑。
     * @return 已載入的 {@link Media} 實例。
     * @throws IllegalArgumentException 若資源找不到。
     */
    public static Media loadMedia(String path) {
        // URL
        if (Misc.isUrl(path)) {
            return new Media(path);
        }

        // 內部資源路徑
        URL resourceUrl = ResourceLoader.class.getResource(path);
        if (resourceUrl != null) {
            return new Media(resourceUrl.toExternalForm());
        }

        // 外部路徑
        Path externalPath = Paths.get(path).toAbsolutePath();
        if (Files.exists(externalPath)) {
            return new Media(externalPath.toUri().toString());
        }

        throw new IllegalArgumentException("Media resource not found: " + path);
    }

    /**
     * 載入檔案資源。
     *
     * @param path 內部資源路徑、外部路徑
     * @return 對應的 {@link Reader}
     * @throws IllegalArgumentException 若找不到資源
     */
    public static Reader loadFile(String path) {
        // 內部資源路徑
        InputStream inputStream = ResourceLoader.class.getResourceAsStream(path);
        if (inputStream != null) {
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        }

        // 外部路徑
        Path externalPath = Paths.get(path).toAbsolutePath();
        if (Files.exists(externalPath)) {
            try {
                return Files.newBufferedReader(externalPath, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read external file: " + path, e);
            }
        }

        throw new IllegalArgumentException("Resource not found: " + path);
    }

    /**
     * 載入譜面 JSON 檔案中的 note 部分。
     *
     * @param songName 歌曲名稱
     * @return 對應的 {@link List<Note>}
     */
    public static List<Note> loadNotes(String songName) {
        Gson gson = new Gson();
        Reader reader = ResourceLoader.loadFile("assets/chart/" + songName + ".json");
        JsonObject json = gson.fromJson(reader, JsonObject.class);
        JsonArray noteArray = json.getAsJsonArray("note");
        Type listType = new TypeToken<List<Note>>() {
        }.getType();

        return gson.fromJson(noteArray, listType);
    }

    /**
     * 載入譜面 JSON 檔案中的 metadata 部分 (lazy loading)。
     *
     * @param songName 歌曲名稱
     * @return 對應的 {@link Song}
     */
    public static Song loadMetadata(String songName) {
        Reader reader = ResourceLoader.loadFile("assets/chart/" + songName + ".json");
        Song song = new Song();

        try (JsonReader jsonReader = new JsonReader(reader)) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals("metadata")) {
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String field = jsonReader.nextName();
                        switch (field) {
                            case "songName" -> song.setSongName(jsonReader.nextString());
                            case "songAuthor" -> song.setSongAuthor(jsonReader.nextString());
                            case "songLength" -> song.setSongLength(Duration.valueOf(jsonReader.nextString()));
                            case "songImagePath" -> song.setSongImagePath(jsonReader.nextString());
                            default -> jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
                } else {
                    jsonReader.skipValue();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load song metadata for: " + songName, e);
        }

        return song;
    }

    /**
     * 載入所有譜面 JSON 檔案中的 metadata 部分 (lazy loading)。
     *
     * @return {@code List<Song>}
     */
    public static List<Song> loadAllMetadata() {
        List<Song> songs = new ArrayList<>();
        Path chartDir = Paths.get("assets/chart/");

        try (Stream<Path> files = Files.walk(chartDir)) {
            List<String> jsonFiles = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .map(path -> path.getFileName().toString())
                    .map(fileName -> fileName.substring(0, fileName.lastIndexOf('.'))) // remove ext
                    .toList();
            for (String songName : jsonFiles) {
                try {
                    Song song = loadMetadata(songName);
                    songs.add(song);
                } catch (Exception e) {
                    System.err.println("Failed to load song: " + songName + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan directory: " + chartDir, e);
        }

        return songs;
    }
}