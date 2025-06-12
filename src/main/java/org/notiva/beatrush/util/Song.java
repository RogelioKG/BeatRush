package org.notiva.beatrush.util;

import javafx.util.Duration;

/**
 * <h2>歌曲</h2>
 */
public class Song {
    private static final String SONG_PATH = "assets/song/";
    private String songName;
    private String songAuthor;
    private Duration songLength;
    private String songFilePath;
    private String songImagePath;

    /**
     * 預設建構子
     */
    public Song() {
    }

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
     * 設定歌曲名稱。
     *
     * @param songName 歌曲名稱
     */
    public void setSongName(String songName) {
        this.songName = songName;
        this.songFilePath = SONG_PATH + songName + ".mp3"; // 更新檔案路徑
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
     * 設定歌曲作者。
     *
     * @param songAuthor 歌曲作者
     */
    public void setSongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
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
     * 設定歌曲長度。
     *
     * @param songLength 歌曲長度
     */
    public void setSongLength(Duration songLength) {
        this.songLength = songLength;
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
     * 設定歌曲檔案路徑。
     *
     * @param songFilePath 歌曲檔案路徑
     */
    public void setSongFilePath(String songFilePath) {
        this.songFilePath = songFilePath;
    }

    /**
     * 取得歌曲海報圖檔路徑。
     *
     * @return 歌曲海報圖檔路徑
     */
    public String getSongImagePath() {
        return songImagePath;
    }

    /**
     * 設定歌曲海報圖檔路徑。
     *
     * @param songImagePath 歌曲海報圖檔路徑
     */
    public void setSongImagePath(String songImagePath) {
        this.songImagePath = songImagePath;
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