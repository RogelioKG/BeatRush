package org.notiva.beatrush.core;

import javafx.scene.media.AudioClip;

import java.util.HashMap;
import java.util.Map;

public class SoundEffectManager {

    private final String SOUND_EFFECT_PATH = "/media/sound/";
    private final Map<String, AudioClip> soundMap = new HashMap<>();

    private static class Holder {
        private static final SoundEffectManager INSTANCE = new SoundEffectManager();
    }

    /**
     * 獲取 SoundEffectManager 的唯一實例 (Singleton Patterns)。
     *
     * @return SoundEffectManager 的唯一實例
     */
    public static SoundEffectManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 載入所有音效。
     */
    public void loadAll() {
        load("ui-menu-sound-1.mp3");
        load("ui-menu-sound-2.mp3");
    }

    /**
     * 加載指定的音效。
     *
     * @param filename 音效文件名
     */
    private void load(String filename) {
        try {
            String fullPath = Loader.loadResource(SOUND_EFFECT_PATH + filename).toExternalForm();
            AudioClip clip = new AudioClip(fullPath);
            soundMap.put(filename, clip);
            clip.play(0.0); // 預熱
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + SOUND_EFFECT_PATH + filename);
        }
    }

    /**
     * 播放指定的音效。
     * 預設使用音量 1.0。
     *
     * @param filename 音效文件名
     */
    public void play(String filename) {
        play(filename, 1.0);
    }

    /**
     * 播放指定的音效，並可以指定音量。
     *
     * @param filename 音效文件名
     * @param volume   音量，範圍從 0.0 開始
     */
    public void play(String filename, double volume) {
        AudioClip clip = soundMap.get(filename);
        if (clip != null) {
            clip.play(volume);
        } else {
            System.err.println("Sound not loaded: " + filename);
        }
    }

    /**
     * 停止播放指定的音效。
     *
     * @param filename 音效文件名
     */
    public void stop(String filename) {
        AudioClip clip = soundMap.get(filename);
        if (clip != null) {
            clip.stop();
        }
    }
}

