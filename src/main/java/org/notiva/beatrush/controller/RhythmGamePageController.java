package org.notiva.beatrush.controller;

import java.util.Map;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.notiva.beatrush.component.TrackView;
import org.notiva.beatrush.core.MediaManager;
import org.notiva.beatrush.core.RhythmGameManager;
import org.notiva.beatrush.core.ScoreManager;
import org.notiva.beatrush.core.StageManager;
import org.notiva.beatrush.util.Score;
import org.notiva.beatrush.util.TrackType;

public class RhythmGamePageController {
    @FXML
    private HBox trackSection;
    @FXML
    private Label scoreText;
    @FXML
    private Label judgementLevelText;

    private final ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2));
    private final FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3));

    private final MediaManager mediaManager = MediaManager.getInstance();
    private final StageManager stageManager = StageManager.getInstance();
    private final ScoreManager scoreManager = ScoreManager.getInstance();
    private final RhythmGameManager rhythmGameManager = RhythmGameManager.getInstance();

    @FXML
    protected void initialize() {
        // 停止 BGM
        mediaManager.bgmFadeOut();
        // 音軌初始化
        Map<TrackType, TrackView> trackViewMap = rhythmGameManager.getTrackViewMap();
        for (TrackView trackView : trackViewMap.values()) {
            trackView.getTrack().reset(); // 重置 (使用者有可能 replay)
            trackSection.getChildren().add(trackView);
        }
        // 分數重置
        scoreManager.reset();
        // 開始遊戲
        rhythmGameManager.start();
        // 結束遊戲後，跳轉到 ScorePage
        rhythmGameManager.registerEndHook(
                () -> stageManager.showStage("BeatRush", "/view/page/ScorePage.fxml")
        );
        // 綁定屬性
        bindProperty();
        // 動畫初始化
        initJudgementLevelTextAnimation();
        // 設置鍵盤焦點和事件監聽
        setupKeyboardListener();
    }

    private void bindProperty() {
        scoreText.textProperty().bind(scoreManager.getCurrentScore().totalScoreProperty().asString());
        judgementLevelText.textProperty().bind(scoreManager.currentJudgementTextProperty());
    }

    private void initJudgementLevelTextAnimation() {
        judgementLevelText.setOpacity(0);

        fadeOut.setNode(judgementLevelText);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDuration(Duration.seconds(1.0));

        scaleUp.setNode(judgementLevelText);
        scaleUp.setFromX(1.0);
        scaleUp.setFromY(1.0);
        scaleUp.setToX(1.5);
        scaleUp.setToY(1.5);
        scaleUp.setCycleCount(1);
        scaleUp.setAutoReverse(true);
        scaleUp.setOnFinished(e -> fadeOut.play());

        judgementLevelText.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) return;
            judgementLevelText.setOpacity(1.0);
            judgementLevelText.setScaleX(1.0);
            judgementLevelText.setScaleY(1.0);
            fadeOut.stop();
            scaleUp.stop();
            scaleUp.playFromStart();
        });
    }

    private void setupKeyboardListener() {
        // 確保 trackSection 可以接收鍵盤焦點
        trackSection.setFocusTraversable(true);
        // 當點擊遊戲區域時請求焦點
        trackSection.setOnMouseClicked(e -> trackSection.requestFocus());
        // 添加鍵盤按下事件監聽器
        trackSection.setOnKeyPressed((KeyEvent event) -> {
            // 將鍵盤事件傳遞給 rhythmGameManager 處理
            rhythmGameManager.handleKeyPressed(event);
            // 吸收事件防止傳播
            event.consume();
        });
        // 初始化時請求焦點
        trackSection.requestFocus();
    }
}