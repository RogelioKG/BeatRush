package org.notiva.beatrush;

import javafx.application.Application;
import javafx.stage.Stage;
import org.notiva.beatrush.core.StageManager;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.registerStage("BeatRush", primaryStage);
        stageManager.showStage("BeatRush", "/view/page/StartMenuPage.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
