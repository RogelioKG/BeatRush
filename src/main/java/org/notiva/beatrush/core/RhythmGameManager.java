package org.notiva.beatrush.core;

import java.util.*;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import org.notiva.beatrush.component.TrackView;
import org.notiva.beatrush.util.*;


public class RhythmGameManager {
    private static double delayTimeMs;

    private final Map<TrackType, TrackView> trackViewMap = new EnumMap<>(TrackType.class);
    private final List<Runnable> endHooks = new ArrayList<>();
    private final MediaManager mediaManager = MediaManager.getInstance();

    private final ObjectProperty<Song> currentSong = new SimpleObjectProperty<>();

    private DelayedMusicPlayer delayedMusicPlayer;

    private static class Holder {
        private static final RhythmGameManager INSTANCE = new RhythmGameManager();
    }

    /**
     * 取得 {@link RhythmGameManager} 的唯一實例。
     *
     * @return 全域唯一的 {@link RhythmGameManager} 實例。
     */
    public static RhythmGameManager getInstance() {
        return RhythmGameManager.Holder.INSTANCE;
    }

    /**
     * 預設建構子。
     */
    private RhythmGameManager() {
        bindProperty();
    }

    /**
     * 開始遊戲。
     */
    public void start() {
        delayedMusicPlayer.play();
    }

    /**
     * 暫停遊戲。
     */
    public void pause() {
        delayedMusicPlayer.pause();
    }

    /**
     * 結束遊戲。
     */
    public void end() {
        for (Runnable hook : endHooks) {
            hook.run();
        }
    }

    /**
     * 註冊 end hook。
     */
    public void registerEndHook(Runnable hook) {
        endHooks.add(hook);
    }

    /**
     * 清除所有 end hook。
     */
    public void clearEndHooks() {
        endHooks.clear();
    }

    /**
     * 處理鍵盤按下事件。
     */
    public void handleKeyPressed(KeyEvent event) {
        TrackType trackType = GameSetting.Control.KEY_TO_TRACK.get(event.getCode());
        if (trackType != null) {
            TrackView trackView = trackViewMap.get(trackType);
            if (trackView != null) {
                trackView.removeClosestNote();
            }
        }
    }

    /**
     * 綁定屬性。
     */
    private void bindProperty() {
        // 歌曲 -> 載入 trackViewMap 與 delayedMusicPlayer
        currentSong.addListener((observable, oldVal, newVal) -> {
            // 歌名不同才重載 (因為玩家有可能 replay)
            if (oldVal == null || !oldVal.getSongName().equals(newVal.getSongName())) {
                // 先載入 TrackView，算好 delayTime
                loadTrackView(newVal.getSongName());
                // 根據 delayTime，決定延遲多久播放
                loadDelayedMusicPlayer(newVal.getSongFilePath());
            }
        });
    }

    /**
     * 遊戲主更新函式，會根據經過時間更新所有音軌。
     *
     * @param elapsedMillis 經過的時間（ms）。
     */
    private void updateGame(double elapsedMillis) {
        for (TrackView trackView : trackViewMap.values()) {
            trackView.update(elapsedMillis);
        }
    }

    /**
     * 載入延遲播放的音樂播放器。
     *
     * @param songFilePath 音檔路徑。
     */
    private void loadDelayedMusicPlayer(String songFilePath) {
        MediaPlayer mediaPlayer = mediaManager.getMediaPlayer(songFilePath);
        delayedMusicPlayer = new DelayedMusicPlayer(mediaPlayer, delayTimeMs);
        // 音樂播放時，會不斷呼叫 updateGame
        delayedMusicPlayer.setUpdateListener(this::updateGame);
        // 設定音樂播放音量
        delayedMusicPlayer.getMediaPlayer().setVolume(GameSetting.Audio.SONG_VOLUME_RATIO);
        // 音樂播放結束時，結束遊戲
        delayedMusicPlayer.addEndOfMediaListener(this::end);
    }

    /**
     * 載入指定歌曲的音軌元件。
     *
     * @param songName 歌曲名稱。
     */
    private void loadTrackView(String songName) {
        List<Track> tracks = loadTrack(songName);
        for (Track track : tracks) {
            trackViewMap.put(track.getTrackType(), new TrackView(track));
        }
        Platform.runLater(() -> {
            // 隨便找一個音軌的尺寸點計算下落延遲
            delayTimeMs = trackViewMap.get(TrackType.LEFT).calculateDelayTimeMs();
        });
    }

    /**
     * 根據歌曲名稱載入音軌。
     *
     * @param songName 歌曲名稱。
     * @return 載入的音軌列表。
     */
    private List<Track> loadTrack(String songName) {
        List<Note> notes = ResourceLoader.loadNotes(songName);

        Map<TrackType, List<Note>> grouped = new EnumMap<>(TrackType.class);

        for (TrackType type : TrackType.values()) {
            grouped.put(type, new ArrayList<>());
        }

        for (Note note : notes) {
            grouped.get(note.getTrackType()).add(note);
        }

        for (List<Note> trackNotes : grouped.values()) {
            trackNotes.sort(Comparator.comparingDouble(Note::getTimestamp));
        }

        List<Track> tracks = new ArrayList<>();

        for (Map.Entry<TrackType, List<Note>> entry : grouped.entrySet()) {
            TrackType trackType = entry.getKey();
            List<Note> trackNotes = entry.getValue();
            tracks.add(new Track(trackType, trackNotes));
        }

        return tracks;
    }


    /**
     * 取得所有音軌元件。
     *
     * @return 所有音軌視圖元件。
     */
    public Map<TrackType, TrackView> getTrackViewMap() {
        return trackViewMap;
    }

    /**
     * 取得目前歌曲。
     *
     * @return 目前歌曲。
     */
    public Song getCurrentSong() {
        return currentSong.get();
    }

    /**
     * 設定目前歌曲。
     *
     * @param song 欲設定的歌曲。
     */
    public void setCurrentSong(Song song) {
        currentSong.set(song);
    }


    /**
     * 取得目前歌曲屬性。
     *
     * @return {@link ObjectProperty} 歌曲屬性。
     */
    public ObjectProperty<Song> currentSongProperty() {
        return currentSong;
    }
}