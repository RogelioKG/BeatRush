package org.notiva.beatrush.core;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class ModalManager {

    /**
     * 顯示一個標準的警告對話框，並等待用戶響應。
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
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}