package org.notiva.beatrush.core;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

public class ModalManager {

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
