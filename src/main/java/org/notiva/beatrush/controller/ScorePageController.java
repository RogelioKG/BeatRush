package org.notiva.beatrush.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ScorePageController {
    public Pane scorePane;
    public StackPane scoreTierPane;

    private int score = 0;

    /*
     * 可以使用
     * Parent root = Loader.loadView("/view/page/ScorePage.fxml");
     * ScorePageController controller = new ScorePageController();
     * controller.setScore(score);
     * StageManager.getInstance().showStage("BeatRush", root);
     *
     */
    public void setScore(int score) {
        this.score = score;
    }


    @FXML
    public void initialize() {

    }

    @FXML
    public void onContinueButtonClick(ActionEvent ignoreActionEvent) {

    }

    @FXML
    public void onReplayButtonClick(ActionEvent ignoreActionEvent) {

    }
}
