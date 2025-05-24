package org.notiva.beatrush.component;

import javafx.animation.PauseTransition;
import org.notiva.beatrush.controller.GamePageController;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle; // 範例使用矩形作為音符
import javafx.util.Duration;
import org.notiva.beatrush.util.Note; // 導入你的 Note 數據模型

public class NoteView extends StackPane { // 可以是 StackPane 或其他你喜歡的 Node 容器

    private Note noteData; // 儲存音符的數據模型
    private TranslateTransition dropAnimation;
    private double targetY; // 音符最終到達的 Y 座標 (判定線)

    // 定義一些常數
    public static final double LANE_WIDTH = 130; // 每條軌道的寬度
    public static final double NOTE_WIDTH = 130; // 音符的寬
    public static final double NOTE_HIGH = 15; // 音符的高
    public static final double SPAWN_OFFSET_Y = -100; // 音符生成時的 Y 偏移量 (保證從螢幕外開始)

    /**
     * 建構 NoteView
     * @param noteData 音符的數據 (timestamp, type, lane 等)
     * @param startY 音符開始下落的 Y 座標
     * @param endY  Y 座標 (判定線)
     * @param dropDuration 音符下落的持續時間 (毫秒)
     * @param laneX 該音符所在軌道的 X 座標
     */
    public NoteView(Note noteData, double startY, double endY, double dropDuration, double laneX)  {
        this.noteData = noteData;
        this.targetY = targetY; // 保存判定線的 Y 座標

        // 設置音符的視覺外觀 (這裡簡單用一個 Rectangle 或 Circle)

        Rectangle visualNote = new Rectangle(NOTE_WIDTH, NOTE_HIGH);


        // Circle visualNote = new Circle(NOTE_SIZE / 2, Color.BLUE); // 可以用圓形

        // 根據音符類型或軌道給予不同的顏色或形狀
        /*switch (noteData.getLane()) {
            case 0: visualNote.setFill(Color.RED); break;
            case 1: visualNote.setFill(Color.BLUE); break;
            case 2: visualNote.setFill(Color.GREEN); break;
            case 3: visualNote.setFill(Color.YELLOW); break;
            default: visualNote.setFill(Color.GRAY); break;
        }*/

        // 可以添加一些文字 (例如音符的 timestamp 或類型)
        // Text noteText = new Text(String.valueOf(noteData.getTimestamp()));
        // noteText.setFill(Color.BLACK);
        // this.getChildren().addAll(visualNote, noteText);

        this.getChildren().add(visualNote); // 只添加矩形


        // 設置音符的初始位置 (X 軸是軌道中心，Y 軸是開始下落點)
        this.setLayoutX(laneX ); // 減去一半寬度以使中心對齊軌道X
        this.setLayoutY(startY); // 初始 Y 座標

        // Calculate the total Y distance the note needs to travel to go off-screen
        double totalFallDistance = 800 - startY + NOTE_HIGH; // Add note height to fully go off-screen

        // Calculate the duration for this total distance based on the speed to hit line
        double speedPxPerMs = (endY - startY) / dropDuration; // Speed in pixels per millisecond
        double totalAnimationDurationMs = totalFallDistance / speedPxPerMs;
        // 創建下落動畫
        dropAnimation = new TranslateTransition(Duration.millis(dropDuration), this);
        dropAnimation.setFromY(startY); // 從當前位置開始 (相對於 setLayoutY)
        dropAnimation.setToY(endY);//判定線Y座標加上150毫秒(最晚有算判定的時間)*速度
        dropAnimation.setInterpolator(Interpolator.LINEAR); // 線性移動，勻速
        // dropAnimation.setCycleCount(1); // 預設就是一次，可不寫

        // 當動畫結束時，可能需要做一些清理 (例如移除音符)
        dropAnimation.setOnFinished(event -> {



            // 如果音符掉落到目標 Y 座標而沒有被擊中，則可能需要將其移除並判斷為 Miss
            // 這些邏輯通常會在 GamePageController 中判斷，然後透過調用 NoteView 的方法來移除
            // System.out.println("音符動畫結束: " + noteData.getTimestamp());
        });
    }

    public void playDropAnimation() {
        if (dropAnimation != null) {
            dropAnimation.play();
        }
    }

    public Note getNoteData() {
        return noteData;
    }

    // 你可能需要一個方法來停止動畫，例如當音符被擊中時
    public void stopAnimation() {
        if (dropAnimation != null) {
            dropAnimation.stop();
        }
    }

    // 獲取音符當前 Y 座標（相對於父容器）
    public double getCurrentY() {
        return this.getLayoutY() + this.getTranslateY();
    }
}