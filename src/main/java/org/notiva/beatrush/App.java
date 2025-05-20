package org.notiva.beatrush;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.notiva.beatrush.core.Loader;
import org.notiva.beatrush.core.StageManager;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.registerStage("main", primaryStage);
        Stage stage = stageManager.getStage("main");
        Scene scene = stageManager.getScene("/view/page/StartMenuPage.fxml");
        Loader.applyStyles(scene, new String[] {
                "/css/all.css", "/css/card-style.css"
        });
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
