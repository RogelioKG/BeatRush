package org.notiva.beatrush.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * <h2>音檔管理器</h2>
 */
public class MediaManager {
    private final Map<String, AudioClip> clipCache = new ConcurrentHashMap<>();
    private final Map<String, Media> mediaCache = new ConcurrentHashMap<>();

    private final MediaPlayer bgmPlayer;
    private Timeline bgmfadeInTimeline;
    private Timeline bgmfadeOutTimeline;

    private static class Holder {
        private static final MediaManager INSTANCE = new MediaManager();
    }

    /**
     * 取得 {@link MediaManager} 的唯一實例。
     *
     * @return 全域唯一的 {@link MediaManager} 實例。
     */
    public static MediaManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 預設建構子，載入所有預定的音檔資源。
     */
    private MediaManager() {
        loadClip("/media/sound/ui-menu-sound-1.mp3");
        loadClip("/media/sound/ui-menu-sound-2.mp3");
        loadMedia("/media/sound/background-music.mp3");
        bgmPlayer = getMediaPlayer("/media/sound/background-music.mp3");
        bgmPlayer.setVolume(GameSetting.Audio.BGM_VOLUME_RATIO);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * 載入指定路徑的音效資源為 {@link AudioClip}，並加入快取。
     *
     * @param path 資源內部路徑或 URL。
     * @return 對應的 {@link AudioClip} 實例。
     */
    public AudioClip loadClip(String path) {
        AudioClip clip = ResourceLoader.loadAudio(path);
        clipCache.put(path, clip);
        return clip;
    }

    /**
     * 載入指定路徑的音樂資源為 {@link Media}，並加入快取。
     *
     * @param path 資源內部路徑或 URL。
     * @return 對應的 {@link Media} 實例。
     */
    public Media loadMedia(String path) {
        Media media = ResourceLoader.loadMedia(path);
        mediaCache.put(path, media);
        return media;
    }

    /**
     * 取得對應路徑的 {@link AudioClip}。若尚未載入，將自動載入並快取。
     *
     * @param path 資源內部路徑或 URL。
     * @return 對應的 {@link AudioClip} 實例。
     */
    public AudioClip getClip(String path) {
        AudioClip clip = clipCache.get(path);
        if (clip == null) {
            clip = loadClip(path);
            AudioClip existing = clipCache.putIfAbsent(path, clip);
            if (existing != null) {
                clip = existing; // 可能其他執行緒已經放入
            }
        }
        return clip;
    }

    /**
     * 取得對應路徑的 {@link Media}。若尚未載入，將自動載入並快取。
     *
     * @param path 資源內部路徑或 URL。
     * @return 對應的 {@link Media} 實例。
     */
    public Media getMedia(String path) {
        Media media = mediaCache.get(path);
        if (media == null) {
            media = loadMedia(path);
            Media existing = mediaCache.putIfAbsent(path, media);
            if (existing != null) {
                media = existing; // 可能其他執行緒已經放入
            }
        }
        return media;
    }

    /**
     * 取得對應路徑的 {@link MediaPlayer}。若尚未載入，將自動載入並快取。
     * 註：因為 {@link MediaPlayer} 用久了會不乾淨，所以最好是按需建立。
     *
     * @param path 資源內部路徑或 URL。
     * @return 對應的 {@link MediaPlayer} 實例。
     */
    public MediaPlayer getMediaPlayer(String path) {
        Media media = getMedia(path);
        return new MediaPlayer(media);
    }

    /**
     * 取得 BGM 播放器
     *
     * @return BGM 播放器
     */
    public MediaPlayer getBgmPlayer() {
        return bgmPlayer;
    }

    /**
     * BGM 淡入
     */
    public void bgmFadeIn() {
        if (bgmfadeOutTimeline != null) bgmfadeOutTimeline.stop();
        bgmPlayer.setVolume(0);
        bgmPlayer.play();
        bgmfadeInTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(bgmPlayer.volumeProperty(), 0)),
                new KeyFrame(Duration.millis(GameSetting.Audio.BGM_FADE_MS), new KeyValue(bgmPlayer.volumeProperty(), GameSetting.Audio.BGM_VOLUME_RATIO))
        );
        bgmfadeInTimeline.play();
    }

    /**
     * BGM 淡出
     */
    public void bgmFadeOut() {
        if (bgmfadeInTimeline != null) bgmfadeInTimeline.stop();
        bgmfadeOutTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(bgmPlayer.volumeProperty(), bgmPlayer.getVolume())),
                new KeyFrame(Duration.millis(GameSetting.Audio.BGM_FADE_MS), new KeyValue(bgmPlayer.volumeProperty(), 0))
        );
        bgmfadeOutTimeline.setOnFinished(e -> bgmPlayer.pause());
        bgmfadeOutTimeline.play();
    }
}

