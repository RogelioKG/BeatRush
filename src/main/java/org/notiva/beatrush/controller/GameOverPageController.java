package org.notiva.beatrush.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import org.notiva.beatrush.core.StageManager;

public class GameOverPageController {
    @FXML
    public void onContinueButtonClick(ActionEvent ignoredActionEvent) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml");
    }

    @FXML
    public void onReplayButtonClick(ActionEvent ignoredActionEvent) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "/view/page/RhythmGamePage.fxml");
    }
}
