package org.notiva.beatrush.util;

import javafx.animation.AnimationTimer;

/**
 * <h2>遊戲計時器</h2>
 * <p>
 * 這是一個計時器抽象類別，基於 JavaFX 的 {@link AnimationTimer}，提供簡易的遊戲時間追蹤功能。
 * 可透過繼承並實作 {@link #onUpdate(double)} 方法以取得每幀的經過時間（ms）。
 * 支援暫停、恢復與重設。
 * </p>
 *
 * <h3>使用範例：</h3>
 * <pre>{@code
 * GameTimer timer = new GameTimer() {
 *     @Override
 *     public void onUpdate(double elapsedMillis) {
 *         System.out.printf("Elapsed Time: %.2f ms%n", elapsedMillis);
 *     }
 * };
 * timer.start();
 * }</pre>
 */
public abstract class GameTimer extends AnimationTimer {

    /** 計時器開始時間（以 ns 為單位），若為 -1 表示尚未啟動或已暫停 */
    private long startTime = -1;

    /** 上一次停止後的時間偏移（用於繼續播放時補償） */
    private long pauseOffset = 0;

    /**
     * 每幀更新時會呼叫的方法。
     *
     * @param elapsedMillis 自啟動以來經過的時間（ms）
     */
    public abstract void onUpdate(double elapsedMillis);

    /**
     * 內部處理每一幀的更新，會自動呼叫 {@link #onUpdate(double)}。
     *
     * @param now 系統的目前時間（以 ns 為單位）
     */
    @Override
    public void handle(long now) {
        if (startTime < 0) return;
        double elapsedMillis = (now - startTime) / 1_000_000.0;
        onUpdate(elapsedMillis);
    }

    /**
     * 啟動計時器。
     */
    @Override
    public void start() {
        if (startTime < 0) {
            startTime = System.nanoTime() - pauseOffset;
        }
        super.start();
    }

    /**
     * 停止計時器。
     */
    @Override
    public void stop() {
        super.stop();
        if (startTime >= 0) {
            pauseOffset = System.nanoTime() - startTime;
            startTime = -1;
        }
    }

    /**
     * 重設計時器。
     */
    public void reset() {
        startTime = -1;
        pauseOffset = 0;
    }
}
