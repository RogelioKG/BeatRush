package org.notiva.beatrush.controller;



import org.notiva.beatrush.util.Song; // <-- 在這裡導入 Song
import org.notiva.beatrush.util.Note;
import org.notiva.beatrush.component.NoteView;

import javafx.scene.media.MediaPlayer;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class RhythmGamePageController {

    private MediaPlayer mediaPlayer;
    private List<Note> chartNotes; // 從 JSON 載入的音符列表
    private Song currentSong;
    private int nextNoteIndex = 0; // 用於追蹤下一個要生成的音符

    // 遊戲常數，需要根據你的 UI 設計來調整
    private static final double GAME_HEIGHT = 800; // 遊戲區域的高度
    private static final double NOTE_DROP_DURATION_MS = 1500; // 音符從生成到判定線所需的時間 (毫秒)
    private static final double END_Y = 607.5; // 判定線的 Y 座標 (例如，在 80% 的高度)
    private static final double START_Y = - NoteView.NOTE_HIGH/2; // 音符生成時的 Y 座標 (從螢幕外開始)
    private static final double JUDGELINE_Y = 527.5;
    private static final double LANE_WIDTH = 1000; // 每條軌道的寬度
    private static final double START_X_OFFSET = 215; // 第一條軌道開始的 X 偏移
    private static final double LATE = -65;//處理延遲
    private static final int perfectjudgetime = 20;
    private static final int greatjudgetime = 40;
    private static final int goodjudgetime = 60;
    private static final int badjudgetime = 100;


    // 儲存當前活躍的音符 (在螢幕上，還未被擊中或錯過的音符)
    private List<NoteView> activeNoteViews = new ArrayList<>();

    @FXML
    private AnchorPane gamePane;

    @FXML
    private Label timeLabel;

    @FXML
    private Label judge;

    @FXML
    public void initialize() {
        // 通常在這裡初始化一些 UI 元素，但 MediaPlayer 的初始化可能更適合在設定譜面後進行。
        gamePane.setFocusTraversable(true); // 確保 gamePane 可以被聚焦
        gamePane.setOnKeyPressed(event -> {
            //keyPressStatus.put(event.getCode(), true);
            handleKeyPress(event.getCode().getName()); // 呼叫你的處理方法
            event.consume(); // 消費事件，防止它被其他 Node 再次處理
        });
    }

    // 在 GamePageController.java 的 setupGame(Song song) 方法內：
    public void setupGame(Song song) {
        this.currentSong = song; // 保存當前歌曲信息

        // 載入音樂
        try {
            mediaPlayer = new MediaPlayer(song.getSongFile());
            mediaPlayer.setVolume(0.2);

            // 構建譜面檔案路徑
            String chartPath = "/chart/" + song.getSongName() + ".json";
            System.out.println("正在載入譜面: " + chartPath);

            // 載入譜面 JSON
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Note>>() {
            }.getType();

            InputStream file = getClass().getResourceAsStream(chartPath);
            if (file == null) {
                System.err.println("錯誤: 找不到譜面檔案: " + chartPath);
                // 處理錯誤，例如跳回選歌頁面或顯示錯誤訊息
                return;
            }

            // 1. 構建譜面檔案路徑


            try (InputStreamReader reader = new InputStreamReader(file, StandardCharsets.UTF_8)) {
                this.chartNotes = gson.fromJson(reader, listType);
                if (this.chartNotes == null) {
                    this.chartNotes = new ArrayList<>(); // 避免 null 引用
                    System.err.println("警告: 譜面檔案 " + chartPath + " 為空或解析失敗。");
                } else {
                    // 建議對音符按時間點排序，確保處理順序正確
                    this.chartNotes.sort(Comparator.comparingDouble(Note::getTimestamp));
                    System.out.println("譜面載入完成，共 " + this.chartNotes.size() + " 個音符。");
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("讀取譜面檔案時發生錯誤: " + chartPath + " - " + e.getMessage());
                // 處理錯誤
            }

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                updateGame(newValue);
            });

            mediaPlayer.setOnReady(() -> {
                System.out.println("音樂準備完成，開始播放。");
                mediaPlayer.play();
                // 確保遊戲面板獲得焦點，以便接收鍵盤事件
                gamePane.requestFocus();
            });

        } catch (Exception e) { // 捕獲更廣泛的異常，例如文件轉換 URI 失敗
            e.printStackTrace();
            System.err.println("載入音樂或設定遊戲時發生錯誤: " + e.getMessage());
        }
    }

    /**
     * 根據當前音樂時間更新遊戲狀態
     */
    private void updateGame(Duration currentTime) {
        double currentMillis = currentTime.toMillis();

        if (timeLabel != null) {
            timeLabel.setText(formatDuration(currentTime));
        }

        // 1. 生成新的音符
        while (nextNoteIndex < chartNotes.size()) {
            Note nextNoteData = chartNotes.get(nextNoteIndex);
            double noteTimestamp = nextNoteData.getTimestamp(); // 獲取音符的時間戳

            // 計算音符應該開始動畫的時間點
            // 音符的 timestamp 是它到達判定線的時間
            double spawnTime = noteTimestamp - NOTE_DROP_DURATION_MS*JUDGELINE_Y/(END_Y - START_Y);

            if (currentMillis >= spawnTime) {
                // 計算音符的 X 座標 (根據軌道)
                // 假設有 4 條軌道 (0, 1, 2, 3)
                double laneX = START_X_OFFSET + nextNoteData.getTrack()*140 ;

                // 創建 NoteView 實例
                NoteView noteView = new NoteView(nextNoteData, START_Y, END_Y, NOTE_DROP_DURATION_MS , laneX);

                gamePane.getChildren().add(noteView); // 將音符添加到遊戲面板
                activeNoteViews.add(noteView); // 添加到活躍音符列表

                noteView.playDropAnimation(); // 啟動音符的掉落動畫

                System.out.println("在音樂時間 " + formatDuration(currentTime) +
                        " 生成音符 (" + nextNoteIndex + "): " + nextNoteData); // corrected variable name
                nextNoteIndex++;
            } else {
                break;
            }
        }

        // 2. 處理活躍音符的狀態 (判定、移除)
        // 遍歷 activeNoteViews 來檢查它們是否到達判定線或被擊中
        List<NoteView> notesToRemove = new ArrayList<>();
        for (NoteView noteView : activeNoteViews) {
            // 檢查音符是否已經錯過 (超過判定線)
            // 如果音符的動畫已經完成 (即 translateY 達到目標) 並且還在 activeNoteViews 中，說明它已經錯過
            // 或者直接檢查其當前 Y 座標
            if (noteView.getCurrentY() > JUDGELINE_Y + (END_Y-START_Y)*badjudgetime/NOTE_DROP_DURATION_MS) { // 稍微超過判定線
                System.out.println("音符錯過: " + noteView.getNoteData().getTimestamp());
                getHitJudgment(200);
                // TODO: 處理 Miss 判定，扣分等
                notesToRemove.add(noteView);
                //noteView.stopAnimation(); // 停止動畫
            }
        }

        // 移除已經錯過的音符
        gamePane.getChildren().removeAll(notesToRemove);
        activeNoteViews.removeAll(notesToRemove);
    }

    // 處理鍵盤按下的事件 (用於判定)
    public void handleKeyPress(String keyCode) { // 接收按下的按鍵碼
        // 假設你的按鍵對應軌道 (例如 'D' -> 軌道 0, 'F' -> 軌道 1, 等)
        int pressedLane = -1;
        System.out.println(1);
        switch (keyCode) {
            case "D": pressedLane = 0; break;
            case "F": pressedLane = 1; break;
            case "J": pressedLane = 2; break;
            case "K": pressedLane = 3; break;
            // ... 其他按鍵
        }

        if (pressedLane != -1) {
            // 獲取當前音樂時間
            double currentTimeMillis = mediaPlayer.getCurrentTime().toMillis();

            // 尋找最近的、未被擊中且在判定範圍內的音符
            NoteView bestHitNote = null;
            double minTimeDiff = Double.MAX_VALUE;
            System.out.println(currentTimeMillis);
            for (NoteView noteView : activeNoteViews) {
                if (noteView.getNoteData().getTrack() == pressedLane) {
                    double noteTimestamp = noteView.getNoteData().getTimestamp();
                    double timeDiff = Math.abs(currentTimeMillis - noteTimestamp + LATE);

                    // 定義一個判定範圍 (例如，正負 150 毫秒)
                    double hitWindow = 150; // 毫秒

                    if (timeDiff <= hitWindow && timeDiff < minTimeDiff) {
                        bestHitNote = noteView;
                        minTimeDiff = timeDiff;
                    }
                }
            }

            if (bestHitNote != null) {
                // 處理擊中判定 (例如 Perfect, Good, Bad)
                System.out.println("擊中音符！時間差: " + minTimeDiff + " ms, 判定: " + getHitJudgment(minTimeDiff));
                // TODO: 加分、顯示判定文字、移除音符視覺效果
                gamePane.getChildren().remove(bestHitNote);
                activeNoteViews.remove(bestHitNote);
                //bestHitNote.stopAnimation(); // 停止動畫
            } else {
                // 沒有擊中任何音符，或者擊中範圍外
                System.out.println("空擊或過早/過晚。");
                
            }
        }
    }

    // 簡單的判定邏輯範例
    public String getHitJudgment(double timeDiff) {
        String judgmentText;
        if (timeDiff <= perfectjudgetime) {
            judgmentText = "Perfect!";
        } else if (timeDiff <= greatjudgetime) {
            judgmentText = "great";
        }else if (timeDiff <= goodjudgetime) {
            judgmentText = "good";
        } else if (timeDiff <= badjudgetime) {
            judgmentText = "bad";
        } else if (timeDiff == 200) {
            judgmentText = "MISS";//若要設為miss則回傳200
        } else {
            judgmentText = "MISS";//裝飾用
        }

        // 設置判定文字
        judge.setText(judgmentText);

        // 創建一個延時動畫，讓文字在 0.4 秒後消失
        PauseTransition delay = new PauseTransition(Duration.millis(300)); // 0.4 秒 = 400 毫秒
        delay.setOnFinished(event -> {
            //judge.setText(""); // 清空文字，使其消失
        });
        delay.play(); // 啟動延時

        return judgmentText; // 返回判定文字，以便在控制台輸出
    }

    /**
     * 輔助方法：將 Duration 物件格式化為 MM:SS 格式的字串
     * @param duration 要格式化的 Duration 物件
     * @return 格式化後的時間字串
     */
    private String formatDuration(Duration duration) {
        if (duration == null || duration.isUnknown()) {
            return "00:00";
        }
        long millis = (long) duration.toMillis();
        long minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        long seconds = millis / 1000;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // 暫停遊戲/音樂
    public void pauseGame() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            System.out.println("遊戲暫停。");
        }
    }

    // 恢復遊戲/音樂
    public void resumeGame() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
            System.out.println("遊戲恢復。");
        }
    }

    // 停止遊戲/音樂並釋放資源
    public void stopGame() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        // 清理 chartNotes 和 activeNoteViews
        if (chartNotes != null) {
            chartNotes.clear();
        }
        if (activeNoteViews != null) {
            // 停止所有活躍音符的動畫並從面板中移除
            for (NoteView noteView : new ArrayList<>(activeNoteViews)) { // 創建副本以避免 ConcurrentModificationException
                noteView.stopAnimation();
                gamePane.getChildren().remove(noteView);
            }
            activeNoteViews.clear();
        }
        nextNoteIndex = 0; // 重置索引
        System.out.println("遊戲停止，資源已釋放。");

        // TODO: 跳轉回選歌頁面或結果頁面
        /*try {
            App.setRoot("/fxml/SongSelectPage.fxml"); // 示例跳轉
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("無法跳轉回選歌頁面。");
        }*/
    }

}
