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

public class TrackView extends StackPane {
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

    public TrackView() {
        ResourceLoader.loadComponentView(this, "/view/component/TrackView.fxml");
        // 創建節點
        Line judgementLine = getJudgementLine();
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

    public TrackView(Track track) {
        this();
        setTrack(track);
    }

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
     * 移除音符
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
     * 找到最接近判定線的音符
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

    private int calculateCycles() {
        double livingY = trackLayout.endY - trackLayout.startY;
        return (int) (livingY / GameSetting.ObjectMotion.FALL_DOWN_Y_PER_FRAME);
    }

    public double calculateDelayTime() {
        double delayY = trackLayout.judgementLineY - trackLayout.startY;
        return delayY / GameSetting.ObjectMotion.FALL_DOWN_Y_PER_MS;
    }

    private void addJudgementLine(Line line) {
        // 設定 judgementLine Y 位置
        line.setStartX(15);
        line.setStartY(trackLayout.judgementLineY);
        line.setEndX(getWidth() - 15);
        line.setEndY(trackLayout.judgementLineY);
        // 加入父容器
        notesBox.getChildren().add(line);
    }

    private Line getJudgementLine() {
        Line line = new Line();
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(15);
        line.setOpacity(0.5);
        return line;
    }

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

    public void clearNoteView() {
        notesBox.getChildren().clear();
    }

    public TrackLayout getTrackLayout() {
        return trackLayout;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}