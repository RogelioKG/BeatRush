package org.notiva.beatrush.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.input.MouseEvent;
import org.notiva.beatrush.component.GlowingBorderButton;
import org.notiva.beatrush.component.MaskLayer;
import org.notiva.beatrush.core.MediaManager;
import org.notiva.beatrush.core.StageManager;

public class GameOverPageController {
    public GlowingBorderButton continueButton, replayButton;
    public MaskLayer maskLayer;

    private final StageManager stageManager = StageManager.getInstance();

    @FXML
    public void initialize() {
        maskLayer.show();
        continueButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> maskLayer.hide());
        continueButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> maskLayer.show());
        replayButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> maskLayer.hide());
        replayButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> maskLayer.show());
        continueButton.setOnAction(e -> stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml"));
        replayButton.setOnAction(e -> stageManager.showStage("BeatRush", "/view/page/RhythmGamePage.fxml"));
    }
}
