package org.notiva.beatrush.util;

import org.notiva.beatrush.component.TrackView;
import org.notiva.beatrush.core.GameSetting;

/**
 * <h2>音軌相關尺寸點設定</h2>
 * <p>
 * 此類別提供與音軌顯示相關的 Y 座標配置，用於控制音符的垂直運動與判定區域。
 * 所有 Y 座標皆為 {@link TrackView} 的 local Y 座標。
 * </p>
 */
public class TrackLayout {

    /** Note 出現前的預留空間（從 topY 往上） */
    public static final double BEFORE_TOP_Y = 500;

    /** Note 消失後的預留空間（從 bottomY 往下） */
    public static final double AFTER_BOTTOM_Y = 100;

    /** 打擊線（judgementLine）距離 bottomY 的距離 */
    public static final double BEFORE_BOTTOM_Y = 100;

    /** {@link TrackView} 初始時視窗最上方的 local Y 座標 */
    public final double topY;

    /** {@link TrackView} 初始時視窗最下方的 local Y 座標 */
    public final double bottomY;

    /**
     * 音符的起始位置（在 topY 之上）
     */
    public final double startY;

    /**
     * 音符的終點位置（在 bottomY 之下）
     */
    public final double endY;

    /**
     * 打擊判定線位置（在 bottomY 之上）
     */
    public final double judgementLineY;

    /**
     * 最低可得分區域（BAD 判定）起點位置
     */
    public final double scoringWindowStartY;

    /**
     * 建構音軌相關尺寸點設定。
     *
     * @param topY    {@link TrackView} 初始時視窗最上方的 local Y 座標
     * @param bottomY {@link TrackView} 初始時視窗最下方的 local Y 座標
     */
    public TrackLayout(double topY, double bottomY) {
        this.topY = topY;
        this.bottomY = bottomY;
        this.startY = topY - BEFORE_TOP_Y;
        this.endY = bottomY + AFTER_BOTTOM_Y;
        this.judgementLineY = bottomY - BEFORE_BOTTOM_Y;
        this.scoringWindowStartY =
                judgementLineY + (GameSetting.ObjectMotion.FALL_DOWN_Y_PER_MS * GameSetting.JudgmentWindow.BAD_MIN_MS);
    }
}
