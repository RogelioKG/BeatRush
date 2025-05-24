package org.notiva.beatrush.core;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public class Loader {

    /**
     * 加載元件視圖。
     *
     * @param component 元件實例
     * @param fxmlPath FXML 文件的路徑 (以 resources 目錄為根目錄)
     * @return 加載後的父節點
     * @throws RuntimeException 如果 FXML 加載失敗
     */
    public static Parent loadComponentView(Object component, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(loadResource(fxmlPath));
            loader.setRoot(component); // 將此物件 (節點) 填入元件上標註 fx:root 的空缺位置
            loader.setController(component); // 此物件同時作為整個元件的控制器
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to load FXML: %s", fxmlPath), e);
        }
    }

    /**
     * 加載普通視圖。
     *
     * @param fxmlPath FXML 文件的路徑 (以 resources 目錄為根目錄)
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
     * 加載指定路徑的資源，並返回其 URL。
     *
     * @param path 資源路徑 (以 resources 目錄為根目錄)
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

    /**
     * 根據指定的 URL 建立一個 {@link Image} 物件。
     * <p>
     * 如果 URL 為 {@code null} 或空字串，則回傳 {@code null}。
     * 如果在載入圖片時發生例外，也會回傳 {@code null}，並在標準錯誤輸出中印出錯誤訊息。
     *
     * @param url 圖片的 URL 字串，通常是本地檔案或網路位址。
     * @return 成功建立的 {@link Image} 物件，或在失敗時回傳 {@code null}。
     */
    public static Image loadImage(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            return new Image(url);
        } catch (Exception e) {
            System.err.printf("Failed to load image: %s%n", url);
            return null;
        }
    }
}