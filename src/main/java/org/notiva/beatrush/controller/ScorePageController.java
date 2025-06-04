package org.notiva.beatrush.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import org.notiva.beatrush.component.ScoreBanner;
import org.notiva.beatrush.core.MediaManager;
import org.notiva.beatrush.core.ScoreManager;
import org.notiva.beatrush.core.StageManager;

public class ScorePageController {
    @FXML
    private AnchorPane vaporwaveBackground;
    @FXML
    private Button continueButton;
    @FXML
    private Button replayButton;
    @FXML
    private ScoreBanner scoreBanner;

    private final StageManager stageManager = StageManager.getInstance();
    private final ScoreManager scoreManager = ScoreManager.getInstance();
    private final MediaManager mediaManager = MediaManager.getInstance();

    @FXML
    protected void initialize() {
        // BGM 重新播放
        mediaManager.bgmFadeIn();
        // 毛玻璃效果
        BoxBlur frostedGlassEffect = new BoxBlur(20, 20, 2);
        vaporwaveBackground.setEffect(frostedGlassEffect);
        // 按鈕
        continueButton.setOnAction(e -> stageManager.showStage("BeatRush", "/view/page/SongSelectPage.fxml"));
        replayButton.setOnAction(e -> stageManager.showStage("BeatRush", "/view/page/RhythmGamePage.fxml"));
        // 分數
        scoreBanner.setScore(scoreManager.getCurrentScore());
    }
}
