package org.notiva.beatrush.controller;

import javafx.fxml.FXML;
import org.notiva.beatrush.core.StageManager;


public class StartMenuPageController {

    @FXML
    protected void initialize() {
    }

    @FXML
    protected void onStartButtonClick() {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml");
    }
}
