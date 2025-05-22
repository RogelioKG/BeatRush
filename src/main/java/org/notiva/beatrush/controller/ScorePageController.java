package org.notiva.beatrush.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.notiva.beatrush.component.GlowingBorderButton;
import org.notiva.beatrush.core.StageManager;

public class ScorePageController {
    public Pane scorePane;
    public StackPane scoreTierPane;
    public GlowingBorderButton continueButton, replayButton;
    public Label scoreTier;
    public Label score;

    /*
     * 可以使用
     * Parent root = Loader.loadView("/view/page/ScorePage.fxml");
     * ScorePageController controller = new ScorePageController();
     * controller.setScore(score);
     * StageManager.getInstance().showStage("BeatRush", root);
     *
     */
    public void setScore(int score) {
        this.score.setText(Integer.toString(score));
    }

    @FXML
    public void initialize() {
        continueButton.setButtonText("CONTINUE");
        replayButton.setButtonText("REPLAY");
    }

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
