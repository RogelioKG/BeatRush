package org.notiva.beatrush.controller;

import javafx.event.ActionEvent;

import org.notiva.beatrush.core.StageManager;

public class GameOverPageController {
    public void onContinueButtonClick(ActionEvent actionEvent) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "SongSelectPage.fxml");

    }

    public void onReplayButtonClick(ActionEvent actionEvent) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "RhythmGamePage.fxml");
    }
}
