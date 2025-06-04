package org.notiva.beatrush;

import javafx.application.Application;
import javafx.stage.Stage;
import org.notiva.beatrush.core.StageManager;
import org.notiva.beatrush.core.MediaManager;

public class App extends Application {

    private final MediaManager mediaManager = MediaManager.getInstance();
    private final StageManager stageManager = StageManager.getInstance();

    @Override
    public void start(Stage primaryStage) {
        stageManager.registerStage("BeatRush", primaryStage);
        stageManager.showStage("BeatRush", "/view/page/StartMenuPage.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
