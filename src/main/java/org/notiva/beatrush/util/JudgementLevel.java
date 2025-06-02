package org.notiva.beatrush.util;

// 評分等級枚舉
public enum JudgementLevel {
    PERFECT(100, "Perfect"),
    GREAT(70, "Great"),
    GOOD(30, "Good"),
    BAD(10, "Bad"),
    MISS(0, "Miss");

    private final int score;
    private final String displayName;

    JudgementLevel(int score, String displayName) {
        this.score = score;
        this.displayName = displayName;
    }

    public int getScore() {
        return score;
    }

    public String getDisplayName() {
        return displayName;
    }
}