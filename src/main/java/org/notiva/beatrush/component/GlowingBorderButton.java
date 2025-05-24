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

    private Timeline glowBorderAnimation;

    /**
     * 預設建構子，載入對應的 FXML 版面。
     * <p>
     * 此建構子會載入 GlowingBorderButton.fxml 檔案。
     */
    public GlowingBorderButton() {
        Loader.loadComponentView(this, "/view/component/GlowingBorderButton.fxml");
        initGlowingBorderEffect();
        enableHoverSpeedUpEffect();
    }

    /**
     * 初始化 glowing border 特效。
     */
    private void initGlowingBorderEffect() {
        Color[] colors = Stream.of("#AF84FE", "#FF0068")
                .map(Color::web)
                .toArray(Color[]::new);

        int[] mills = {-500};

        KeyFrame[] keyFrames = Stream.iterate(0, i -> i + 1)
                .limit(100)
                .map(i -> new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, colors[i % colors.length]),
                        new Stop(1, colors[(i + 1) % colors.length])))
                .map(lg -> new Border(new BorderStroke(lg, BorderStrokeStyle.SOLID,
                        new CornerRadii(0), new BorderWidths(2))))
                .map(b -> new KeyFrame(Duration.millis(mills[0] += 500),
                        new KeyValue(button.borderProperty(), b, Interpolator.EASE_IN)))
                .toArray(KeyFrame[]::new);

        glowBorderAnimation = new Timeline(keyFrames);
        glowBorderAnimation.setCycleCount(Timeline.INDEFINITE);
        glowBorderAnimation.setRate(1.0); // 預設速度
        glowBorderAnimation.play();
    }

    /**
     * 設定按鈕顯示的文字內容。
     *
     * @param text 要顯示的按鈕文字，可為 null（會顯示為空白）
     */
    public void setButtonText(String text) {
        buttonText.setText(text);
    }

    /**
     * 取得目前按鈕顯示的文字內容。
     *
     * @return 目前按鈕的文字內容，可能為 null 或空字串
     */
    public String getButtonText() {
        return buttonText.getText();
    }

    private void enableHoverSpeedUpEffect() {
        this.setOnMouseEntered(e -> glowBorderAnimation.setRate(2.0)); // hover 時加速
        this.setOnMouseExited(e -> glowBorderAnimation.setRate(1.0));  // 離開時恢復
    }
}