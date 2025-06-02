package org.notiva.beatrush.core;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.notiva.beatrush.util.JudgementLevel;

/**
 * 評分管理器，負責處理節奏遊戲的評分邏輯
 */
public class ScoreManager {

    // 分數和統計屬性
    private final IntegerProperty totalScore = new SimpleIntegerProperty(0);
    private final IntegerProperty combo = new SimpleIntegerProperty(0);
    private final IntegerProperty maxCombo = new SimpleIntegerProperty(0);

    // 各等級統計
    private final IntegerProperty perfectCount = new SimpleIntegerProperty(0);
    private final IntegerProperty greatCount = new SimpleIntegerProperty(0);
    private final IntegerProperty goodCount = new SimpleIntegerProperty(0);
    private final IntegerProperty badCount = new SimpleIntegerProperty(0);
    private final IntegerProperty missCount = new SimpleIntegerProperty(0);

    private static class Holder {
        private static final ScoreManager INSTANCE = new ScoreManager();
    }

    /**
     * 取得 ScoreManager 的唯一實例
     */
    public static ScoreManager getInstance() {
        return Holder.INSTANCE;
    }

    private ScoreManager() {
    }

    /**
     * 根據時間差計算評分
     *
     * @param timingDifference 時間差（毫秒），負數表示提前，正數表示延遲
     * @return 評分等級
     */
    public JudgementLevel calculateJudgement(double timingDifference) {
        if (timingDifference >= GameSetting.JudgmentWindow.PERFECT_MIN_MS &&
                timingDifference <= GameSetting.JudgmentWindow.PERFECT_MAX_MS) {
            return JudgementLevel.PERFECT;
        }

        if (timingDifference >= GameSetting.JudgmentWindow.GREAT_MIN_MS &&
                timingDifference <= GameSetting.JudgmentWindow.GREAT_MAX_MS) {
            return JudgementLevel.GREAT;
        }

        if (timingDifference >= GameSetting.JudgmentWindow.GOOD_MIN_MS &&
                timingDifference <= GameSetting.JudgmentWindow.GOOD_MAX_MS) {
            return JudgementLevel.GOOD;
        }

        if (timingDifference >= GameSetting.JudgmentWindow.BAD_MIN_MS &&
                timingDifference <= GameSetting.JudgmentWindow.BAD_MAX_MS) {
            return JudgementLevel.BAD;
        }

        // 超出所有範圍則為 Miss
        return JudgementLevel.MISS;
    }

    /**
     * 添加評分並更新統計
     *
     * @param judgement 評分等級
     */
    public void addScore(JudgementLevel judgement) {
        // 更新總分
        int baseScore = judgement.getScore();
        int comboBonus = Math.min(combo.get() / 10, 30); // 每 10 連擊增加額外分數，最多 30 分
        int finalScore = baseScore + comboBonus;
        totalScore.set(totalScore.get() + finalScore);

        // 更新連擊
        if (judgement == JudgementLevel.MISS || judgement == JudgementLevel.BAD) {
            combo.set(0);
        } else {
            combo.set(combo.get() + 1);
            if (combo.get() > maxCombo.get()) {
                maxCombo.set(combo.get());
            }
        }

        // 更新各等級統計
        switch (judgement) {
            case PERFECT -> perfectCount.set(perfectCount.get() + 1);
            case GREAT -> greatCount.set(greatCount.get() + 1);
            case GOOD -> goodCount.set(goodCount.get() + 1);
            case BAD -> badCount.set(badCount.get() + 1);
            case MISS -> missCount.set(missCount.get() + 1);
        }
    }

    /**
     * 重置所有分數和統計
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
     *
     * @return 準確率百分比 (0-100)
     */
    public double getAccuracy() {
        int totalHits = perfectCount.get() + greatCount.get() + goodCount.get() + badCount.get() + missCount.get();
        if (totalHits == 0) return 100.0;

        int weightedScore = perfectCount.get() * 100 + greatCount.get() * 70 + goodCount.get() * 30 + badCount.get() * 10;
        return (double) weightedScore / (totalHits * 100) * 100;
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

    // Getter 方法
    public int getTotalScore() {
        return totalScore.get();
    }

    public IntegerProperty totalScoreProperty() {
        return totalScore;
    }

    public int getCombo() {
        return combo.get();
    }

    public IntegerProperty comboProperty() {
        return combo;
    }

    public int getMaxCombo() {
        return maxCombo.get();
    }

    public IntegerProperty maxComboProperty() {
        return maxCombo;
    }

    public int getPerfectCount() {
        return perfectCount.get();
    }

    public IntegerProperty perfectCountProperty() {
        return perfectCount;
    }

    public int getGreatCount() {
        return greatCount.get();
    }

    public IntegerProperty greatCountProperty() {
        return greatCount;
    }

    public int getGoodCount() {
        return goodCount.get();
    }

    public IntegerProperty goodCountProperty() {
        return goodCount;
    }

    public int getBadCount() {
        return badCount.get();
    }

    public IntegerProperty badCountProperty() {
        return badCount;
    }

    public int getMissCount() {
        return missCount.get();
    }

    public IntegerProperty missCountProperty() {
        return missCount;
    }
}
