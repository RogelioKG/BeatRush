package org.notiva.beatrush.component;

import java.util.stream.Stream;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.notiva.beatrush.core.Loader;
import org.notiva.beatrush.util.SoundEffectManager;


public class GlowingBorderButton extends Button {
    @FXML
    private StackPane button;

    @FXML
    private Text buttonText;

    private final double HOVER_SPEED = 1.8;
    private final double NORMAL_SPEED = 0.8;
    private final String SOUND_EFFECT = "ui-menu-sound-1.mp3";

    private Timeline glowingBorder;

    /**
     * 預設建構子，載入對應的 FXML 版面。
     *
     * <p>FXML 使用範例：</p>
     * <pre>{@code
     * <GlowingBorderButton />
     * }</pre>
     */
    public GlowingBorderButton() {
        Loader.loadComponentView(this, "/view/component/GlowingBorderButton.fxml");
        initGlowingBorderEffect();
        enableBorderSpeedUpEffect();
        enableSoundEffect();
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

        glowingBorder = new Timeline(keyFrames);
        glowingBorder.setCycleCount(Timeline.INDEFINITE);
        glowingBorder.setRate(NORMAL_SPEED); // 預設速度
        glowingBorder.play();
    }


    /**
     * 啟用 hover 時 UI 聲音。
     */
    private void enableSoundEffect() {
        addEventHandler(MouseEvent.MOUSE_ENTERED, e -> SoundEffectManager.play(SOUND_EFFECT, 0.3));
    }

    /**
     * 啟用 hover 時 glowing border 加速效果。
     */
    private void enableBorderSpeedUpEffect() {
        addEventHandler(MouseEvent.MOUSE_ENTERED, e -> glowingBorder.setRate(HOVER_SPEED)); // 加速
        addEventHandler(MouseEvent.MOUSE_EXITED, e -> glowingBorder.setRate(NORMAL_SPEED)); // 恢復
    }

    /**
     * 設定按鈕顯示的文字內容。
     *
     * @param text 要顯示的按鈕文字
     */
    public void setButtonText(String text) {
        buttonText.setText(text);
    }

    /**
     * 取得目前按鈕顯示的文字內容。
     *
     * @return 目前按鈕的文字內容
     */
    public String getButtonText() {
        return buttonText.getText();
    }

}