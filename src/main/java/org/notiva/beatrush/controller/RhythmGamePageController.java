package org.notiva.beatrush.controller;

import java.util.Map;

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
import org.notiva.beatrush.util.Song;
import org.notiva.beatrush.util.TrackType;

public class RhythmGamePageController {
    @FXML
    private HBox trackSection;
    @FXML
    private Label score;

    private final MediaManager mediaManager = MediaManager.getInstance();
    private final StageManager stageManager = StageManager.getInstance();
    private final ScoreManager scoreManager = ScoreManager.getInstance();
    private final RhythmGameManager rhythmGameManager = RhythmGameManager.getInstance();

    @FXML
    protected void initialize() {
        // 停止 BGM
        mediaManager.getBgmPlayer().pause();
        // 音軌初始化
        Map<TrackType, TrackView> trackViewMap = rhythmGameManager.getTrackViewMap();
        for (TrackView trackView : trackViewMap.values()) {
            trackSection.getChildren().add(trackView);
        }
        // 開始遊戲
        rhythmGameManager.start();
        // 結束遊戲後，跳轉到 ScorePage
        rhythmGameManager.registerEndHook(
                () -> stageManager.showStage("BeatRush", "/view/page/ScorePage.fxml")
        );
        // 設置鍵盤焦點和事件監聽
        setupKeyboardListener();
        // 分數管理
        bindScore();
    }

    private void bindScore() {
        score.textProperty().bind(scoreManager.totalScoreProperty().asString());
    }

    private void setupKeyboardListener() {
        // 確保 trackSection 可以接收鍵盤焦點
        trackSection.setFocusTraversable(true);
        // 當點擊遊戲區域時請求焦點
        trackSection.setOnMouseClicked(e -> trackSection.requestFocus());
        // 添加鍵盤按下事件監聽器
        trackSection.setOnKeyPressed(this::handleKeyPressed);
        // 初始化時請求焦點
        trackSection.requestFocus();
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        // 將鍵盤事件傳遞給遊戲管理器處理
        rhythmGameManager.handleKeyPressed(event);
        // 消費事件，防止傳播
        event.consume();
    }
}