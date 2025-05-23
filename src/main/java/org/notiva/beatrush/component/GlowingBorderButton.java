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

    /**
     * 按鈕的容器元件。
     */
    @FXML
    private StackPane button;

    /**
     * 按鈕的文字元件。
     */
    @FXML
    private Text buttonText;


    /**
     * 預設建構子，載入對應的 FXML 版面。
     * <p>
     * 此建構子會載入 GlowingBorderButton.fxml 檔案，
     * 並在載入完成後自動觸發 initialize() 方法啟動動畫效果。
     */
    public GlowingBorderButton() {
        Loader.loadComponentView(this, "/view/component/GlowingBorderButton.fxml");
    }


    /**
     * FXML 控制器初始化方法，在 FXML 載入完成後自動呼叫。
     * <p>
     * 此方法會建立並啟動邊框的漸變色動畫效果。動畫使用兩種顏色
     * （紫色 #AF84FE 和粉紅色 #FF0068）建立 100 個關鍵幀的時間軸動畫，
     * 每個關鍵幀間隔 0.5 秒，形成平滑的顏色循環效果。
     */
    @FXML
    protected void initialize() {
        // 定義動畫使用的顏色陣列
        Color[] colors = Stream.of("#AF84FE", "#FF0068")
                .map(Color::web)
                .toArray(Color[]::new);

        // 時間計數器，用於計算每個關鍵幀的時間點
        int[] mills = {-500};

        // 建立關鍵幀陣列，生成 100 個漸變色動畫幀
        KeyFrame[] keyFrames = Stream.iterate(0, i -> i + 1)
                .limit(100)
                // 為每個索引建立線性漸變色
                .map(i -> new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, colors[i % colors.length]),
                        new Stop(1, colors[(i + 1) % colors.length])))
                // 將漸變色轉換為邊框物件
                .map(lg -> new Border(new BorderStroke(lg, BorderStrokeStyle.SOLID,
                        new CornerRadii(0), new BorderWidths(2))))
                // 建立關鍵幀，每幀間隔 0.5 秒
                .map(b -> new KeyFrame(Duration.millis(mills[0] += 500),
                        new KeyValue(button.borderProperty(), b, Interpolator.EASE_IN)))
                .toArray(KeyFrame[]::new);

        // 建立時間軸動畫並開始播放
        Timeline timeline = new Timeline(keyFrames);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * 設定按鈕顯示的文字內容。
     * <p>
     * 此方法會更新內部 Text 元件的文字內容，立即反映在按鈕的顯示上。
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
}