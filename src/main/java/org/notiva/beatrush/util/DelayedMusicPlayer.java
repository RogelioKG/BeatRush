package org.notiva.beatrush.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import org.notiva.beatrush.core.GameSetting;

/**
 * <h2>延遲音樂播放器</h2>
 * <p>
 * 在指定延遲（ms）後播放音樂的控制器，提供類似 {@link MediaPlayer} 的介面。
 * 支援多個 EndOfMedia 監聽器。
 * </p>
 */
public class DelayedMusicPlayer {

    /**
     * 原始延遲時間（ms）
     */
    private final double delayMillis;

    /**
     * 校正延遲時間（ms）
     */
    private final double correctedDelayMillis;

    /**
     * 音樂播放器
     */
    private final MediaPlayer mediaPlayer;

    /**
     * 內部計時器
     */
    private final GameTimer timer;

    /**
     * 每幀更新的外部監聽器
     */
    private DoubleConsumer updateListener;

    /**
     * EndOfMedia 監聽器列表
     */
    private final List<Runnable> endOfMediaListeners = new ArrayList<>();

    /**
     * 建構延遲音樂播放器。
     *
     * @param mediaPlayer 要播放的 {@link MediaPlayer}
     * @param delayMillis 延遲播放時間（ms）
     */
    public DelayedMusicPlayer(MediaPlayer mediaPlayer, double delayMillis) {
        this.mediaPlayer = mediaPlayer;
        this.delayMillis = delayMillis;
        this.correctedDelayMillis = delayMillis - GameSetting.Timing.DELAY_CORRECTION_MS;

        this.timer = new GameTimer() {
            @Override
            public void onUpdate(double elapsedMillis) {
                DelayedMusicPlayer.this.onTimerUpdate(elapsedMillis);
            }
        };

        // 音樂播放器暫停時，計時器要暫停
        this.mediaPlayer.setOnPaused(timer::stop);
        // 音樂播放器手動停止時，計時器要重置
        this.mediaPlayer.setOnStopped(timer::reset);
        // 音樂播放器因結束停止時
        this.mediaPlayer.setOnEndOfMedia(() -> {
            // 計時器要重置
            this.timer.reset();
            // 音樂播放器手動停止 (確保進入 STOPPED 狀態)
            this.mediaPlayer.stop();
            // 執行所有 endOfMediaListeners
            for (Runnable listener : endOfMediaListeners) {
                listener.run();
            }
        });
    }

    /**
     * 內部計時器更新處理。
     */
    private void onTimerUpdate(double elapsedMillis) {
        // 檢查是否到達延遲時間，且音樂尚未開始播放
        if (elapsedMillis >= correctedDelayMillis && !isPlaying()) {
            mediaPlayer.play();
        }

        if (updateListener != null) {
            updateListener.accept(elapsedMillis);
        }
    }

    /**
     * 添加 EndOfMedia 監聽器。
     *
     * @param listener 音樂播放結束時要執行的回調
     */
    public void addEndOfMediaListener(Runnable listener) {
        if (listener != null) {
            endOfMediaListeners.add(listener);
        }
    }

    /**
     * 移除 EndOfMedia 監聽器。
     *
     * @param listener 要移除的回調
     * @return 如果成功移除則回傳 true
     */
    public boolean removeEndOfMediaListener(Runnable listener) {
        return endOfMediaListeners.remove(listener);
    }

    /**
     * 清除所有 EndOfMedia 監聽器（包括內部的計時器停止監聽器）。
     */
    public void clearEndOfMediaListeners() {
        endOfMediaListeners.clear();
    }

    /**
     * 開始播放（延遲播放）。
     */
    public void play() {
        timer.start();
    }

    /**
     * 暫停播放。
     */
    public void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 停止播放。
     */
    public void stop() {
        mediaPlayer.stop();
    }

    /**
     * 恢復播放。
     */
    public void resume() {
        if (isPaused()) {
            mediaPlayer.play();
        }
        timer.start();
    }

    /**
     * 檢查是否已暫停。
     *
     * @return 若已暫停則為 true
     */
    public boolean isPaused() {
        return mediaPlayer.getStatus() == Status.PAUSED;
    }

    /**
     * 檢查是否已停止。
     *
     * @return 若已停止則為 true
     */
    public boolean isStopped() {
        Status status = mediaPlayer.getStatus();
        return status == Status.STOPPED || status == Status.READY;
    }

    /**
     * 檢查是否正在播放。
     *
     * @return 若正在播放則為 true
     */
    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * 設定每幀更新的 callback。
     *
     * @param listener 接收目前經過時間的監聽器
     */
    public void setUpdateListener(DoubleConsumer listener) {
        this.updateListener = listener;
    }

    /**
     * 取得原始延遲時間（ms）。
     *
     * @return 延遲時間
     */
    public double getDelayMillis() {
        return delayMillis;
    }

    /**
     * 取得校正後的延遲時間（ms）。
     *
     * @return 校正延遲時間
     */
    public double getCorrectedDelayMillis() {
        return correctedDelayMillis;
    }

    /**
     * 取得 {@link MediaPlayer} 實例。
     *
     * @return 音樂播放器
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}