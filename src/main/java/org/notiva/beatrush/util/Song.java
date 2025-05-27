package org.notiva.beatrush.util;

import javafx.scene.media.Media;

import javafx.util.Duration;
import org.notiva.beatrush.core.Loader;
import org.notiva.beatrush.core.ScoreManager;

public class Song {
    private String songName;
    private String author;
    private Duration duration;
    private String songFilePath;
    private Media songFile;
    private String posterPath;

    /**
     * 建構子，初始化歌曲名稱和檔案路徑。
     *
     * @param songName 歌曲名稱
     */
    public Song(String songName, String author, Duration duration) {
        this.songName = songName;
        this.author = author;
        this.duration = duration;
        songFilePath = Loader.loadResource("/media/" + songName + ".mp3").toExternalForm();
        songFile = new Media(songFilePath);
        posterPath = Loader.loadResource("/icon/" + songName + ".jpg").toExternalForm();
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
