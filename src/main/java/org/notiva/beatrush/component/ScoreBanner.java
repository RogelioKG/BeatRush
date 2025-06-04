package org.notiva.beatrush.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.notiva.beatrush.core.ResourceLoader;
import org.notiva.beatrush.util.Score;

/**
 * <h2>分數橫幅元件</h2>
 */
public class ScoreBanner extends StackPane {

    // 分數相關標籤
    @FXML
    private Label scoreTotal;

    @FXML
    private Label maxComboTotal;

    // 各等級計數標籤
    @FXML
    private Label perfectCount;

    @FXML
    private Label greatCount;

    @FXML
    private Label goodCount;

    @FXML
    private Label badCount;

    @FXML
    private Label missCount;

    // 圓形區域的分數資訊
    @FXML
    private Label scoreGrade;

    @FXML
    private Label scoreAccuracy;

    public ScoreBanner() {
        ResourceLoader.loadComponentView(this, "/view/component/ScoreBanner.fxml");
        clearDisplay();
    }

    /**
     * 設定要顯示的分數資料
     *
     * @param score 要顯示的分數物件
     */
    public void setScore(Score score) {
        if (score == null) {
            clearDisplay();
            return;
        }

        // 設定基本分數資訊
        scoreTotal.setText(String.valueOf(score.getTotalScore()));
        maxComboTotal.setText(String.valueOf(score.getMaxCombo()));

        // 設定各等級計數
        perfectCount.setText(String.valueOf(score.getPerfectCount()));
        greatCount.setText(String.valueOf(score.getGreatCount()));
        goodCount.setText(String.valueOf(score.getGoodCount()));
        badCount.setText(String.valueOf(score.getBadCount()));
        missCount.setText(String.valueOf(score.getMissCount()));

        // 設定評級和準確率
        scoreGrade.setText(score.getGrade());
        scoreAccuracy.setText(String.format("%.1f%%", score.getAccuracy()));
    }

    /**
     * 清空所有顯示內容
     */
    public void clearDisplay() {
        scoreTotal.setText("0");
        maxComboTotal.setText("0");
        perfectCount.setText("0");
        greatCount.setText("0");
        goodCount.setText("0");
        badCount.setText("0");
        missCount.setText("0");
        scoreGrade.setText("-");
        scoreAccuracy.setText("0.0%");
    }
}