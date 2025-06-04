package org.notiva.beatrush.util;

import javafx.util.Duration;

/**
 * <h2>歌曲</h2>
 */
public class Song {
    private static final String SONG_PATH = "/media/song/";
    private final String songName;
    private final String songAuthor;
    private final Duration songLength;
    private final String songFilePath;
    private final String songImagePath;

    /**
     * 建構子，初始化歌曲資訊。
     *
     * @param songName      歌曲名稱
     * @param songAuthor    歌曲作者
     * @param songLength    歌曲長度
     * @param songImagePath 歌曲圖片
     */
    public Song(String songName, String songAuthor, Duration songLength, String songImagePath) {
        this.songName = songName;
        this.songAuthor = songAuthor;
        this.songLength = songLength;
        this.songImagePath = songImagePath;
        this.songFilePath = SONG_PATH + songName + ".mp3";
    }

    /**
     * 取得歌曲名稱。
     *
     * @return 歌曲名稱
     */
    public String getSongName() {
        return songName;
    }

    /**
     * 取得歌曲作者。
     *
     * @return 歌曲作者
     */
    public String getSongAuthor() {
        return songAuthor;
    }

    /**
     * 取得歌曲長度。
     *
     * @return 歌曲長度
     */
    public Duration getSongLength() {
        return songLength;
    }

    /**
     * 取得歌曲檔案路徑。
     *
     * @return 歌曲檔案路徑
     */
    public String getSongFilePath() {
        return songFilePath;
    }

    /**
     * 取得歌曲海報圖檔路徑。
     *
     * @return 歌曲海報圖檔路徑
     */
    public String getSongImagePath() {
        return songImagePath;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + songName + '\'' +
                ", author='" + songAuthor + '\'' +
                ", length=" + Misc.formatDuration(songLength) +
                ", filePath='" + songFilePath + '\'' +
                ", imagePath='" + songImagePath + '\'' +
                '}';
    }
}
