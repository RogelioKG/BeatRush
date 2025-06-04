package org.notiva.beatrush.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class MediaManager {
    private final Map<String, AudioClip> clipCache = new ConcurrentHashMap<>();
    private final Map<String, Media> mediaCache = new ConcurrentHashMap<>();
    private MediaPlayer bgmPlayer;

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
     * 載入所有預定的音檔資源。
     */
    public void loadAll() {
        loadClip("/media/sound/ui-menu-sound-1.mp3");
        loadClip("/media/sound/ui-menu-sound-2.mp3");
        loadMedia("/media/sound/background-music.mp3");
        bgmPlayer = getMediaPlayer("/media/sound/background-music.mp3");
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
     * 載入指定路徑的長音樂資源為 {@link Media}，並加入快取。
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
     * 取得背景音樂的 {@link MediaPlayer} 實例。
     * <p>需先呼叫 {@link #loadAll()} 初始化。</p>
     *
     * @return 背景音樂的 {@link MediaPlayer} 實例。
     */
    public MediaPlayer getBgmPlayer() {
        return bgmPlayer;
    }
}

