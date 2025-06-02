package org.notiva.beatrush.util;

import java.util.function.DoubleConsumer;

import javafx.scene.media.MediaPlayer;
import org.notiva.beatrush.core.GameSetting;

public class DelayedMusicPlayer extends GameTimer {

    private final MediaPlayer mediaPlayer;
    private final double delayMillis;
    private boolean hasStarted = false;

    private DoubleConsumer updateListener;

    public DelayedMusicPlayer(MediaPlayer mediaPlayer, double delayMillis) {
        this.mediaPlayer = mediaPlayer;
        this.delayMillis = delayMillis - GameSetting.Timing.DELAY_CORRECTION_MS;
    }

    @Override
    public void onUpdate(double elapsedMillis) {
        // 延遲播放
        if (!hasStarted && elapsedMillis >= delayMillis) {
            mediaPlayer.play();
            hasStarted = true;
        }

        if (updateListener != null) {
            updateListener.accept(elapsedMillis);
        }
    }

    @Override
    public void start() {
        super.start();
    }

    public void resume() {
        mediaPlayer.play();
        mediaPlayer.setOnPlaying(super::start);
    }

    @Override
    public void stop() {
        mediaPlayer.pause();
        mediaPlayer.setOnPaused(super::stop);
    }

    @Override
    public void reset() {
        mediaPlayer.stop();
        mediaPlayer.setOnStopped(() -> {
            super.reset();
            hasStarted = false;
        });
    }

    public void setUpdateListener(DoubleConsumer listener) {
        this.updateListener = listener;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public double getDelayMillis() {
        return delayMillis;
    }

    public boolean hasStarted() {
        return hasStarted;
    }
}


