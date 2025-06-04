package org.notiva.beatrush.component;

import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.animation.Timeline;
import org.notiva.beatrush.core.ResourceLoader;
import org.notiva.beatrush.core.GameSetting;
import org.notiva.beatrush.core.ScoreManager;
import org.notiva.beatrush.util.JudgementLevel;
import org.notiva.beatrush.util.Note;
import org.notiva.beatrush.util.Track;
import org.notiva.beatrush.util.TrackLayout;

/**
 * <h2>音軌元件</h2>
 */
public class TrackView extends StackPane {
    /**
     * 容納 {@code NoteView} 的容器
     */
    @FXML
    private Pane notesBox;

    /**
     * {@code NoteView} 下落耗費幀數
     */
    private int cycles;

    /**
     * 此 {@code TrackView} 的佈局尺寸點
     */
    private TrackLayout trackLayout;

    /**
     * 此 {@code TrackView} 對應的 {@code Track} 實例
     */
    private Track track;

    /**
     * 分數管理器
     */
    private final ScoreManager scoreManager = ScoreManager.getInstance();

    /**
     * 預設建構子，會載入對應的 FXML 版面。
     */
    public TrackView() {
        ResourceLoader.loadComponentView(this, "/view/component/TrackView.fxml");
        // 創建節點
        Line judgementLine = createJudgementLine();
        Platform.runLater(() -> {
            // 父容器的相關尺寸點
            double topY = this.sceneToLocal(0, 0).getY();
            double bottomY = this.sceneToLocal(0, getScene().getHeight()).getY();
            trackLayout = new TrackLayout(topY, bottomY);
            // 先算好 cycles
            cycles = calculateCycles();
            // 節點加入父容器 (根據相關尺寸點找到顯示位置)
            addJudgementLine(judgementLine);
        });
    }

    /**
     * 建構子：使用指定的音軌初始化。
     *
     * @param track 音軌。
     */
    public TrackView(Track track) {
        this();
        setTrack(track);
    }

    /**
     * 更新音軌狀態，並根據時間產生新的音符。
     *
     * @param elapsedMillis 已經過的遊戲時間（ms）。
     */
    public void update(double elapsedMillis) {
        if (!track.isFinished()) {
            Note note = track.getCurrentNote();
            if (note.getTimestamp() <= elapsedMillis) {
                addNoteView(new NoteView());
                track.next();
            }
        }
    }

    /**
     * 移除最接近判定線的音符並進行評分。
     */
    public void removeClosestNote() {
        NoteView closestNote = findClosestNoteToJudgementLine();
        if (closestNote != null) {
            // 進入計分線
            if (closestNote.getLayoutY() > trackLayout.scoringWindowStartY) {
                // 計分並移除
                double timeDiff = closestNote.calculateTimeDiff(trackLayout.judgementLineY);
                JudgementLevel judgeResult = scoreManager.calculateJudgement(timeDiff);
                scoreManager.addScore(judgeResult);
                notesBox.getChildren().remove(closestNote);
            }
        }
    }

    /**
     * 找出目前最接近判定線的 {@link NoteView}。
     *
     * @return 最接近判定線的音符視圖；若無則為 {@code null}。
     */
    private NoteView findClosestNoteToJudgementLine() {
        List<Node> children = notesBox.getChildren();
        NoteView closestNote = null;
        double minDistance = Double.MAX_VALUE;
        for (Node child : children) {
            if (child instanceof NoteView noteView) {
                double distance = trackLayout.judgementLineY - noteView.getLayoutY();
                if (distance < minDistance) {
                    minDistance = distance;
                    closestNote = noteView;
                }
            }
        }
        return closestNote;
    }

    /**
     * 根據畫面高度與掉落速度計算音符掉落所需的幀數。
     *
     * @return 動畫幀數。
     */
    private int calculateCycles() {
        double livingY = trackLayout.endY - trackLayout.startY;
        return (int) (livingY / GameSetting.ObjectMotion.FALL_DOWN_Y_PER_FRAME);
    }

    /**
     * 計算音符從頂端到判定線所需的延遲時間（ms）。
     *
     * @return 下落延遲時間（ms）。
     */
    public double calculateDelayTimeMs() {
        double delayY = trackLayout.judgementLineY - trackLayout.startY;
        return delayY / GameSetting.ObjectMotion.FALL_DOWN_Y_PER_MS;
    }

    /**
     * 將判定線加入至音軌元件中。
     *
     * @param line 判定線物件。
     */
    private void addJudgementLine(Line line) {
        // 設定 judgementLine Y 位置
        line.setStartX(15);
        line.setStartY(trackLayout.judgementLineY);
        line.setEndX(getWidth() - 15);
        line.setEndY(trackLayout.judgementLineY);
        // 加入父容器
        notesBox.getChildren().add(line);
    }

    /**
     * 建立一條新的判定線物件。
     *
     * @return 判定線。
     */
    private Line createJudgementLine() {
        Line line = new Line();
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(15);
        line.setOpacity(0.5);
        return line;
    }

    /**
     * 將新的 {@link NoteView} 加入音軌元件並啟動其動畫。
     *
     * @param noteView 音符元件。
     */
    private void addNoteView(NoteView noteView) {
        // 設定 noteView Y 位置
        noteView.setLayoutY(trackLayout.startY);
        // 加入父容器
        notesBox.getChildren().add(noteView);
        // 動畫
        Timeline fallingAnimation = noteView.createFallingAnimation(cycles);
        // 動畫完成時，移除父容器中的此 NoteView
        fallingAnimation.setOnFinished(e -> {
            if (notesBox.getChildren().contains(noteView)) {
                double timeDiff = noteView.calculateTimeDiff(trackLayout.judgementLineY);
                JudgementLevel judgeResult = scoreManager.calculateJudgement(timeDiff);
                scoreManager.addScore(judgeResult);
                notesBox.getChildren().remove(noteView);
            }
        });
        // 動畫開始
        fallingAnimation.play();
    }

    /**
     * 取得此音軌佈局資訊。
     *
     * @return {@link TrackLayout} 物件。
     */
    public TrackLayout getTrackLayout() {
        return trackLayout;
    }


    /**
     * 取得對應的音軌。
     *
     * @return 音軌。
     */
    public Track getTrack() {
        return track;
    }

    /**
     * 設定對應的音軌。
     *
     * @param track 音軌。
     */
    public void setTrack(Track track) {
        this.track = track;
    }
}