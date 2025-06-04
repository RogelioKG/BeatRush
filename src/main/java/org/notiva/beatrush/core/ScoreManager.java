package org.notiva.beatrush.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.notiva.beatrush.util.JudgementLevel;
import org.notiva.beatrush.util.Score;

/**
 * <h2>評分管理器</h2>
 */
public class ScoreManager {
    private final Score currentScore = new Score();
    private final StringProperty currentJudgementText = new SimpleStringProperty("");

    private static class Holder {
        private static final ScoreManager INSTANCE = new ScoreManager();
    }

    /**
     * 取得 ScoreManager 的唯一實例
     */
    public static ScoreManager getInstance() {
        return Holder.INSTANCE;
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
        // 更新評分等級名稱
        currentJudgementText.set(null); // 為了確保每個 judgement 都能觸發外部 addListener 的新值替換 (做動畫時需要)
        currentJudgementText.set(judgement.getDisplayName());
        // 基礎分數
        int baseScore = judgement.getScore();
        // 連擊分數 (每 ? 連擊，此次增加額外分數，一次最多 ? 分)
        int comboBonus = Math.min(currentScore.getCombo() / GameSetting.Score.COMBO_BONUS_INTERVAL, GameSetting.Score.COMBO_BONUS_MAX);
        // 最終分數
        int finalScore = baseScore + comboBonus;
        // 更新分數
        currentScore.addScore(finalScore);
        // 更新連擊
        if (judgement == JudgementLevel.MISS || judgement == JudgementLevel.BAD) {
            currentScore.setCombo(0);
        } else {
            currentScore.setCombo(currentScore.getCombo() + 1);
        }
        // 更新統計
        currentScore.incrementCount(judgement);
    }

    /**
     * 重置所有分數和統計
     */
    public void reset() {
        currentScore.reset();
        currentJudgementText.set("");
    }

    /**
     * 取得當前分數物件
     */
    public Score getCurrentScore() {
        return currentScore;
    }

    /**
     * 取得當前分數的快照（用於保存結果等）
     */
    public Score getScoreSnapshot() {
        return new Score(currentScore);
    }

    /**
     * 取得當前評分等級名稱屬性
     */
    public StringProperty currentJudgementTextProperty() {
        return currentJudgementText;
    }

    /**
     * 取得當前評分等級名稱
     */
    public String getCurrentJudgementText() {
        return currentJudgementText.get();
    }
}