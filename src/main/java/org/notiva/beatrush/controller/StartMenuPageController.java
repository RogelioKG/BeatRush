package org.notiva.beatrush.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import org.notiva.beatrush.component.GlowingBorderButton;
import org.notiva.beatrush.component.MaskLayer;
import org.notiva.beatrush.core.MediaManager;
import org.notiva.beatrush.core.GameSetting;
import org.notiva.beatrush.core.StageManager;


public class StartMenuPageController {
    @FXML
    private MaskLayer maskLayer;
    @FXML
    private GlowingBorderButton startButton;

    private final StageManager stageManager = StageManager.getInstance();
    private final MediaManager mediaManager = MediaManager.getInstance();

    @FXML
    protected void initialize() {
        maskLayer.show();
        startButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            maskLayer.hide();
            MediaPlayer bgmPlayer = mediaManager.getBgmPlayer();
            if (bgmPlayer.getStatus() == MediaPlayer.Status.READY) {
                bgmPlayer.setVolume(GameSetting.Audio.BGM_VOLUME_RATIO);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.play();
            }
        });
    }

    @FXML
    protected void onStartButtonClick() {
        stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml");
    }
}
