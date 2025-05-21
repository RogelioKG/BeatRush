package org.notiva.beatrush.core;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Loader {

    /**
     * 加載元件視圖。
     *
     * @param component 作為根節點和控制器的元件實例
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
     * <p>
     * 此方法通過類別加載器查找資源，如果找不到資源則拋出異常。
     * </p>
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
}