package org.notiva.beatrush.util;

import javafx.animation.AnimationTimer;

/**
 * <h2>遊戲計時器</h2>
 * 使用範例：
 * <pre>{@code
 * GameTimer timer = new GameTimer() {
 *     @Override
 *     public void onUpdate(double elapsedSeconds) {
 *         System.out.printf("Elapsed Time: %.2f s%n", elapsedSeconds);
 *     }
 * };
 * }</pre>
 */
public abstract class GameTimer extends AnimationTimer {

    private long startTime = -1;
    private long pauseOffset = 0;

    public abstract void onUpdate(double elapsedMillis);

    @Override
    public void handle(long now) {
        if (startTime < 0) return;
        double elapsedMillis = (now - startTime) / 1_000_000.0;
        onUpdate(elapsedMillis);
    }

    @Override
    public void start() {
        // 避免重複設定 startTime
        if (startTime < 0) {
            startTime = System.nanoTime() - pauseOffset;
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        if (startTime >= 0) {
            pauseOffset = System.nanoTime() - startTime;
            startTime = -1;
        }
    }

    public void reset() {
        startTime = -1;
        pauseOffset = 0;
    }
}

