package org.notiva.beatrush.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 計分管理器類，負責處理音樂遊戲中的分數相關操作。
 * 此類使用單例設計模式，確保整個應用程序中只有一個計分管理器實例，
 * 但允許每首歌曲擁有獨立的分數記錄。
 */
public class ScoreManager {
    /** 單例模式的唯一實例 */
    private static ScoreManager instance;

    /** 儲存每首歌曲對應的分數 */
    private Map<String, Integer> songScores;

    /** 當前選中的歌曲名稱 */
    private String currentSongName;

    /**
     * 私有構造函數，防止外部直接實例化。
     * 初始化歌曲分數映射。
     */
    private ScoreManager() {
        songScores = new HashMap<>();
        currentSongName = "";
    }

    /**
     * 獲取 ScoreManager 的單例實例。
     * 如果實例尚未創建，則初始化一個新實例。
     *
     * @return ScoreManager 的唯一實例
     */
    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    /**
     * 設置當前選中的歌曲。
     *
     * @param songName 歌曲的唯一識別碼
     */
    public void setCurrentSong(String songName) {
        this.currentSongName = songName;
        // 如果這首歌沒有記錄，初始化為0分
        if (!songScores.containsKey(songName)) {
            songScores.put(songName, 0);
        }
    }

    /**
     * 設置當前歌曲的分數。
     *
     * @param score 要設置的新分數
     * @throws IllegalStateException 如果當前沒有選擇歌曲
     */
    public void setScore(int score) {
        if (currentSongName.isEmpty()) {
            throw new IllegalStateException("必須先選擇一首歌才能設置分數");
        }
        songScores.put(currentSongName, score);
    }

    /**
     * 獲取當前歌曲的分數。
     *
     * @return 當前歌曲的分數值
     * @throws IllegalStateException 如果當前沒有選擇歌曲
     */
    public int getScore() {
        if (currentSongName.isEmpty()) {
            throw new IllegalStateException("必須先選擇一首歌才能獲取分數");
        }
        return songScores.get(currentSongName);
    }

    /**
     * 獲取指定歌曲的分數。
     *
     * @param songName 歌曲的唯一識別碼
     * @return 指定歌曲的分數，如果歌曲不存在則返回0
     */
    public int getScoreBySongName(String songName) {
        return songScores.getOrDefault(songName, 0);
    }

    /**
     * 重置當前歌曲的分數為0。
     * 通常在重新開始遊玩某首歌時調用。
     *
     * @throws IllegalStateException 如果當前沒有選擇歌曲
     */
    public void resetScore() {
        if (currentSongName.isEmpty()) {
            throw new IllegalStateException("必須先選擇一首歌才能重置分數");
        }
        songScores.put(currentSongName, 0);
    }

    /**
     * 重置所有歌曲的分數記錄。
     */
    public void resetAllScores() {
        songScores.clear();
    }

    /**
     * 獲取所有歌曲的分數記錄。
     *
     * @return 包含所有歌曲ID和對應分數的映射
     */
    public Map<String, Integer> getAllScores() {
        return new HashMap<>(songScores); // 返回副本以保護內部狀態
    }
}