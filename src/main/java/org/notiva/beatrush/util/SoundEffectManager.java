package org.notiva.beatrush.util;

import javafx.scene.media.AudioClip;
import org.notiva.beatrush.core.Loader;

import java.util.HashMap;
import java.util.Map;

public class SoundEffectManager {

    private static final String SOUND_EFFECT_PATH = "/sound/";
    private static final Map<String, AudioClip> soundMap = new HashMap<>();

    public static void loadAll() {
        load("ui-menu-sound-1.mp3");
        load("ui-menu-sound-2.mp3");
    }

    public static void load(String filename) {
        try {
            String fullPath = Loader.loadResource(SOUND_EFFECT_PATH + filename).toExternalForm();
            AudioClip clip = new AudioClip(fullPath);
            soundMap.put(filename, clip);
            clip.play(0.0); // 預熱
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + filename);
        }
    }

    public static void play(String filename) {
        play(filename, 1.0);
    }

    public static void play(String filename, double volume) {
        AudioClip clip = soundMap.get(filename);
        if (clip != null) {
            clip.play(volume);
        } else {
            System.err.println("Sound not loaded: " + filename);
        }
    }

    public static void stop(String filename) {
        AudioClip clip = soundMap.get(filename);
        if (clip != null) {
            clip.stop();
        }
    }
}

