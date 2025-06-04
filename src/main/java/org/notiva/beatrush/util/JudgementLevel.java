package org.notiva.beatrush.util;

/**
 * <h2>評分等級</h2>
 */
public enum JudgementLevel {
    PERFECT(100, "Perfect"),
    GREAT(70, "Great"),
    GOOD(30, "Good"),
    BAD(10, "Bad"),
    MISS(0, "Miss");

    private final int score;
    private final String displayName;

    /**
     * 建構一個判定等級。
     *
     * @param score        對應的分數
     * @param displayName  顯示用名稱
     */
    JudgementLevel(int score, String displayName) {
        this.score = score;
        this.displayName = displayName;
    }

    /**
     * 取得該判定等級對應的分數。
     *
     * @return 分數值（例如：100）
     */
    public int getScore() {
        return score;
    }

    /**
     * 取得該判定等級的顯示名稱。
     *
     * @return 顯示文字（例如："Perfect"）
     */
    public String getDisplayName() {
        return displayName;
    }
}