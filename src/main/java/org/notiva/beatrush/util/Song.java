package org.notiva.beatrush.util;

import javafx.scene.media.Media;

import org.notiva.beatrush.core.Loader;

public class Song {
    private String songName;
    private String songFilePath;
    private Media songFile;

    /**
     * 建構子，初始化歌曲名稱和檔案路徑。
     *
     * @param songName 歌曲名稱
     */
    public Song(String songName) {
        this.songName = songName;
        songFilePath = Loader.loadResource("/media/" + songName + ".mp3").toString();
        songFile = new Media(songFilePath);
        ScoreManager.getInstance().setCurrentSong(songName);
    }

    /**
     * @return 歌曲名稱
     */
    public String getSongName() {
        return songName;
    }

    public Media getSongFile() {
        return songFile;
    }

}
