package org.notiva.beatrush.core;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class ModalManager {

    /**
     * 顯示一個標準的警告對話框，並等待用戶響應。
     * <p>
     * 此方法創建一個指定類型的警告對話框，設置其標題、頭部文本和內容，
     * 應用全域樣式表，並以應用程序模態顯示。對話框將阻塞其他窗口的操作，
     * 直到用戶關閉該對話框。
     * </p>
     *
     * @param alertType 警告對話框的類型（如 INFORMATION, WARNING, ERROR, CONFIRMATION）
     * @param title 對話框的標題
     * @param header 對話框的頭部文本，可以為 null 表示無頭部文本
     * @param content 對話框的主要內容文本
     */
    public static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Loader.applyStyles(alert.getDialogPane().getScene(), new String[]{"/css/all.css"});
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}