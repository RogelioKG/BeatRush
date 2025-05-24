package org.notiva.beatrush;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.notiva.beatrush.core.Loader;
import org.notiva.beatrush.controller.GamePageController; // 導入 GamePageController
import org.notiva.beatrush.util.Song; // 導入 Song 類別

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //primaryStage = stage;


    }

    public static void main(String[] args) {
        launch(args);
    }
}
