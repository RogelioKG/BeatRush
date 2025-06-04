package org.notiva.beatrush.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * 分數
 */
public class Score {
    // 基本分數
    private final IntegerProperty totalScore = new SimpleIntegerProperty(0);

    // 連擊相關
    private final IntegerProperty combo = new SimpleIntegerProperty(0);
    private final IntegerProperty maxCombo = new SimpleIntegerProperty(0);

    // 各等級統計
    private final IntegerProperty perfectCount = new SimpleIntegerProperty(0);
    private final IntegerProperty greatCount = new SimpleIntegerProperty(0);
    private final IntegerProperty goodCount = new SimpleIntegerProperty(0);
    private final IntegerProperty badCount = new SimpleIntegerProperty(0);
    private final IntegerProperty missCount = new SimpleIntegerProperty(0);

    public Score() {
    }

    /**
     * 複製建構子，用於建立分數快照
     */
    public Score(Score other) {
        this.totalScore.set(other.getTotalScore());
        this.combo.set(other.getCombo());
        this.maxCombo.set(other.getMaxCombo());
        this.perfectCount.set(other.getPerfectCount());
        this.greatCount.set(other.getGreatCount());
        this.goodCount.set(other.getGoodCount());
        this.badCount.set(other.getBadCount());
        this.missCount.set(other.getMissCount());
    }

    /**
     * 重置所有分數數據
     */
    public void reset() {
        totalScore.set(0);
        combo.set(0);
        maxCombo.set(0);
        perfectCount.set(0);
        greatCount.set(0);
        goodCount.set(0);
        badCount.set(0);
        missCount.set(0);
    }

    /**
     * 計算準確率
     * @return 準確率百分比 (0-100)
     */
    public double getAccuracy() {
        int totalHits = getTotalHits();
        if (totalHits == 0) return 100.0;

        int weightedScore = perfectCount.get() * 100 +
                greatCount.get() * 70 +
                goodCount.get() * 30 +
                badCount.get() * 10;
        return (double) weightedScore / (totalHits * 100) * 100;
    }

    /**
     * 取得總擊中次數
     */
    public int getTotalHits() {
        return perfectCount.get() + greatCount.get() + goodCount.get() +
                badCount.get() + missCount.get();
    }

    /**
     * 取得評分等級（S, A, B, C, D）
     */
    public String getGrade() {
        double accuracy = getAccuracy();
        if (accuracy >= 95.0) return "S";
        if (accuracy >= 90.0) return "A";
        if (accuracy >= 80.0) return "B";
        if (accuracy >= 70.0) return "C";
        return "D";
    }

    /**
     * 增加分數
     */
    public void addScore(int score) {
        totalScore.set(totalScore.get() + score);
    }

    /**
     * 更新連擊數
     */
    public void setCombo(int newCombo) {
        combo.set(newCombo);
        if (newCombo > maxCombo.get()) {
            maxCombo.set(newCombo);
        }
    }

    /**
     * 增加特定等級的計數
     */
    public void incrementCount(JudgementLevel level) {
        switch (level) {
            case PERFECT -> perfectCount.set(perfectCount.get() + 1);
            case GREAT -> greatCount.set(greatCount.get() + 1);
            case GOOD -> goodCount.set(goodCount.get() + 1);
            case BAD -> badCount.set(badCount.get() + 1);
            case MISS -> missCount.set(missCount.get() + 1);
        }
    }

    // Getter 方法
    public int getTotalScore() { return totalScore.get(); }
    public IntegerProperty totalScoreProperty() { return totalScore; }

    public int getCombo() { return combo.get(); }
    public IntegerProperty comboProperty() { return combo; }

    public int getMaxCombo() { return maxCombo.get(); }
    public IntegerProperty maxComboProperty() { return maxCombo; }

    public int getPerfectCount() { return perfectCount.get(); }
    public IntegerProperty perfectCountProperty() { return perfectCount; }

    public int getGreatCount() { return greatCount.get(); }
    public IntegerProperty greatCountProperty() { return greatCount; }

    public int getGoodCount() { return goodCount.get(); }
    public IntegerProperty goodCountProperty() { return goodCount; }

    public int getBadCount() { return badCount.get(); }
    public IntegerProperty badCountProperty() { return badCount; }

    public int getMissCount() { return missCount.get(); }
    public IntegerProperty missCountProperty() { return missCount; }

    @Override
    public String toString() {
        return String.format("Score{total=%d, combo=%d, maxCombo=%d, perfect=%d, great=%d, good=%d, bad=%d, miss=%d, accuracy=%.2f%%, grade=%s}",
                getTotalScore(), getCombo(), getMaxCombo(),
                getPerfectCount(), getGreatCount(), getGoodCount(), getBadCount(), getMissCount(),
                getAccuracy(), getGrade());
    }
}
