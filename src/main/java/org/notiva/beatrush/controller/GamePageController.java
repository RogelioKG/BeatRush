package org.notiva.beatrush.controller;


import javafx.fxml.FXML;
import org.notiva.beatrush.util.Song; // <-- 在這裡導入 Song
import org.notiva.beatrush.util.Note;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GamePageController {

    private MediaPlayer mediaPlayer;
    private List<Note> chartNotes; // 從 JSON 載入的音符列表
    private Song currentSong;
    private int nextNoteIndex = 0; // 用於追蹤下一個要生成的音符

    @FXML
    public void initialize() {
        // 通常在這裡初始化一些 UI 元素，但 MediaPlayer 的初始化可能更適合在設定譜面後進行。
    }


}
