package org.notiva.beatrush.component;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.notiva.beatrush.core.ResourceLoader;
import org.notiva.beatrush.core.GameSetting;

public class NoteView extends Pane {
    public NoteView() {
        ResourceLoader.loadComponentView(this, "/view/component/NoteView.fxml");
    }

    public double calculateTimeDiff(double judgementLineY) {
        return (this.getLayoutY() - judgementLineY) / GameSetting.ObjectMotion.FALL_DOWN_Y_PER_MS;
    }

    public Timeline createFallingAnimation(int cycles) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(GameSetting.Timing.FRAME_TIME_S), e -> {
                    setLayoutY(getLayoutY() + GameSetting.ObjectMotion.FALL_DOWN_Y_PER_FRAME);
                })
        );
        timeline.setCycleCount(cycles);
        return timeline;
    }
}
