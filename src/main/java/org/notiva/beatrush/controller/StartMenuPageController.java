package org.notiva.beatrush.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import org.notiva.beatrush.component.GlowingBorderButton;
import org.notiva.beatrush.component.MaskLayer;
import org.notiva.beatrush.core.MediaManager;
import org.notiva.beatrush.core.StageManager;


public class StartMenuPageController {
    @FXML
    private MaskLayer maskLayer;
    @FXML
    private GlowingBorderButton startButton;
    @FXML
    private MediaManager mediaManager = MediaManager.getInstance();

    private double BGM_VOLUME = 0.03;

    @FXML
    protected void initialize() {
        maskLayer.show();
        startButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            maskLayer.hide();
            MediaPlayer bgmPlayer = mediaManager.getBgmPlayer();
            if (bgmPlayer.getStatus() == MediaPlayer.Status.READY) {
                bgmPlayer.setVolume(BGM_VOLUME);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.play();
            }
        });
    }

    @FXML
    protected void onStartButtonClick() {
        StageManager stageManager = StageManager.getInstance();
        stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml");
    }
}
