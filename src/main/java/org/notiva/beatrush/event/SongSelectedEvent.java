package org.notiva.beatrush.event;

import javafx.event.Event;
import javafx.event.EventType;
import org.notiva.beatrush.util.Song;

/**
 * 在點歌介面選擇一首歌曲時，所發出的自訂 JavaFX 事件。
 *
 * 使用範例：
 * <pre>{@code
 * Song song = new Song("Aruarian Dance", "Nujabes");
 * node.fireEvent(new SongSelectedEvent(song));
 * }</pre>
 */
public class SongSelectedEvent extends Event {

    /**
     * 事件型別常數，用於註冊與辨識 SongSelectedEvent。
     */
    public static final EventType<SongSelectedEvent> SONG_SELECTED =
            new EventType<>(Event.ANY, "SONG_SELECTED");

    private final Song song;

    /**
     * 建立一個新的 SongSelectedEvent 實例。
     *
     * @param song 使用者所選的歌曲物件
     */
    public SongSelectedEvent(Song song) {
        super(SONG_SELECTED);
        this.song = song;
    }

    /**
     * 取得使用者所選擇的歌曲。
     *
     * @return 被選擇的 {@link Song} 實例
     */
    public Song getSong() {
        return song;
    }
}

