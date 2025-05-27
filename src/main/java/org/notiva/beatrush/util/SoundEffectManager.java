package org.notiva.beatrush.util;

import javafx.scene.media.AudioClip;
import org.notiva.beatrush.core.Loader;

import java.util.HashMap;
import java.util.Map;

public class SoundEffectManager {

    private static final String SOUND_EFFECT_PATH = "/sound/";
    private static final Map<String, AudioClip> soundMap = new HashMap<>();

    /**
     * 單例模式的唯一實例
     */
    private static SoundEffectManager instance;

    /**
     * 私有構造函數，防止外部直接實例化。
     * 初始化所有音效。
     */
    private SoundEffectManager() {
        instance = this;
        loadAll();
    }

    /**
     * 獲取 SoundEffectManager 的單例實例。
     * 如果實例尚未創建，則初始化一個新實例。
     *
     * @return SoundEffectManager 的唯一實例
     */
    public static SoundEffectManager getInstance() {
        if (instance == null) {
            instance = new SoundEffectManager();
        }
        return instance;
    }

    /**
     * 預載入所有音效。
     * 這些音效會在應用啟動時自動加載。
     */
    private static void loadAll() {
        load("ui-menu-sound-1.mp3");
        load("ui-menu-sound-2.mp3");
    }

    /**
     * 加載指定的音效文件。
     * 如果音效已經存在於 soundMap 中，則不會重複加載。
     *
     * @param filename 音效文件名
     */
    private static void load(String filename) {
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
    public static void play(String filename) {
        play(filename, 1.0);
    }

    /**
     * 播放指定的音效，並可以指定音量。
     *
     * @param filename 音效文件名
     * @param volume   音量，範圍從 0.0 到 1.0
     */
    public static void play(String filename, double volume) {
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
    public static void stop(String filename) {
        AudioClip clip = soundMap.get(filename);
        if (clip != null) {
            clip.stop();
        }
    }
}

