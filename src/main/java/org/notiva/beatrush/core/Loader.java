package org.notiva.beatrush.core;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Loader {

    /**
     * 加載元件視圖，將指定物件設置為 FXML 的根節點和控制器。
     * <p>
     * 此方法通常用於自定義元件，允許一個類同時作為 FXML 的根節點和控制器。
     * </p>
     *
     * @param component 作為根節點和控制器的元件實例
     * @param fxmlPath FXML 文件的路徑
     * @return 加載後的父節點
     * @throws RuntimeException 如果 FXML 加載失敗
     */
    public static Parent loadComponentView(Object component, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(loadResource(fxmlPath));
            loader.setRoot(component);
            loader.setController(component);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to load FXML: %s", fxmlPath), e);
        }
    }

    /**
     * 加載普通視圖 FXML 文件。
     * <p>
     * 此方法使用預設的 FXML 加載機制，讓 FXML 文件自行指定根節點和控制器。
     * </p>
     *
     * @param fxmlPath FXML 文件的路徑
     * @return 加載後的父節點
     * @throws RuntimeException 如果 FXML 加載失敗
     */
    public static Parent loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(loadResource(fxmlPath));
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to load FXML: %s", fxmlPath), e);
        }
    }

    /**
     * 將指定的 CSS 樣式表應用到場景中。
     * <p>
     * 此方法接受一個樣式表路徑字串陣列，並將每個樣式表添加到指定場景的樣式表集合中。
     * </p>
     *
     * @param scene 要應用樣式的場景
     * @param cssPaths CSS 樣式表路徑字串陣列
     * @throws RuntimeException 如果無法找到指定的資源
     */
    public static void applyStyles(Scene scene, String[] cssPaths) {
        for (String path : cssPaths) {
            scene.getStylesheets().add(loadResource(path).toExternalForm());
        }
    }

    /**
     * 加載指定路徑的資源，並返回其 URL。
     * <p>
     * 此方法通過類加載器查找資源，如果找不到資源則拋出異常。
     * </p>
     *
     * @param path 資源路徑
     * @return 資源的 URL
     * @throws RuntimeException 如果無法找到指定的資源
     */
    public static URL loadResource(String path) {
        URL url = Loader.class.getResource(path);
        if (url == null) {
            throw new RuntimeException(String.format("Cannot find resource: %s", path));
        }
        return url;
    }
}