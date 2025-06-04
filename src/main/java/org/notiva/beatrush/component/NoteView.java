package org.notiva.beatrush.component;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.notiva.beatrush.core.ResourceLoader;
import org.notiva.beatrush.core.GameSetting;

/**
 * <h2>音符元件</h2>
 */
public class NoteView extends Pane {

    /**
     * 預設建構子，會載入對應的 FXML 版面。
     */
    public NoteView() {
        ResourceLoader.loadComponentView(this, "/view/component/NoteView.fxml");
    }

    /**
     * 計算此 Note 與判定線之間的時間差（以 ms 為單位）。
     * 可用於判定使用者的擊打準確度。
     *
     * @param judgementLineY 判定線在 Y 軸的位置。
     * @return 目前 Note 的位置與判定線之間對應的時間差（ms），
     *         正值代表尚未抵達判定線，負值代表已經超過。
     */
    public double calculateTimeDiff(double judgementLineY) {
        return (this.getLayoutY() - judgementLineY) / GameSetting.ObjectMotion.FALL_DOWN_Y_PER_MS;
    }

    /**
     * 建立一個讓 Note 依據每幀下落距離進行動畫的 {@link Timeline}。
     *
     * @param cycles 動畫執行的循環次數。通常是依照音符從起始點移動到判定線所需的幀數。
     * @return 配置完成的 {@link Timeline} 實例，用於控制下落動畫。
     */
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
