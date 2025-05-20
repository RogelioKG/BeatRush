package org.notiva.beatrush.core;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Loader {

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

    public static Parent loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(loadResource(fxmlPath));
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to load FXML: %s", fxmlPath), e);
        }
    }

    public static void applyStyles(Scene scene, String[] cssPaths) {
        for (String path : cssPaths) {
            scene.getStylesheets().add(loadResource(path).toExternalForm());
        }
    }

    public static URL loadResource(String path) {
        URL url = Loader.class.getResource(path);
        if (url == null) {
            throw new RuntimeException(String.format("Cannot find resource: %s", path));
        }
        return url;
    }
}
