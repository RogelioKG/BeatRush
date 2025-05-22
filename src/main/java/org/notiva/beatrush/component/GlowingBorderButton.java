package org.notiva.beatrush.component;

import java.util.stream.Stream;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.notiva.beatrush.core.Loader;

public class GlowingBorderButton extends Button {
    @FXML
    private StackPane button;

    @FXML
    private Text buttonText;

    @FXML
    protected void initialize() {
        Color[] colors = Stream.of("#AF84FE", "#FF0068")
                .map(Color::web)
                .toArray(Color[]::new);

        int[] mills = {-500};
        KeyFrame[] keyFrames = Stream.iterate(0, i -> i + 1)
                .limit(100)
                .map(i -> new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, colors[i % colors.length]), new Stop(1, colors[(i + 1) % colors.length])))
                .map(lg -> new Border(new BorderStroke(lg, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))))
                .map(b -> new KeyFrame(Duration.millis(mills[0] += 500), new KeyValue(button.borderProperty(), b, Interpolator.EASE_IN)))
                .toArray(KeyFrame[]::new);

        Timeline timeline = new Timeline(keyFrames);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public GlowingBorderButton() {
        Loader.loadComponentView(this, "/view/component/GlowingBorderButton.fxml");
    }

    public void setButtonText(String text) {
        buttonText.setText(text);
    }

    public String getButtonText() {
        return buttonText.getText();
    }
}
