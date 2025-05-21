package org.notiva.beatrush.controller;

import javafx.event.ActionEvent;

import org.notiva.beatrush.core.StageManager;

public class GameOverPageController {
    public void onContinueButtonClick(ActionEvent ignoredActionEvent) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml");
    }

    public void onReplayButtonClick(ActionEvent ignoredActionEvent) {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "/view/page/RhythmGamePage.fxml");
    }
}
