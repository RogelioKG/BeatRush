package org.notiva.beatrush.util;

import org.notiva.beatrush.core.GameSetting;

/**
 * 此類別紀錄之 Y 座標，皆為 TrackView 容器 Local Y 座標！
 */
public class TrackLayout {
    public static final double BEFORE_TOP_Y = 500;
    public static final double AFTER_BOTTOM_Y = 100;
    public static final double BEFORE_BOTTOM_Y = 100;
    /**
     * 初始化時視窗最頂部位置
     */
    public final double topY;
    /**
     * 初始化時視窗最底部位置
     */
    public final double bottomY;
    /**
     * Note 的產生位置
     * {@code startY = topY - BEFORE_TOP_Y}
     */
    public final double startY;
    /**
     * Note 的消失位置
     */
    public final double endY;
    /**
     * 打擊線位置
     */
    public final double judgementLineY;
    /**
     * 計分線
     */
    public final double scoringWindowStartY;

    public TrackLayout(double topY, double bottomY) {
        this.topY = topY;
        this.bottomY = bottomY;
        this.startY = topY - BEFORE_TOP_Y;
        this.endY = bottomY + AFTER_BOTTOM_Y;
        this.judgementLineY = bottomY - BEFORE_BOTTOM_Y;
        this.scoringWindowStartY = judgementLineY + (GameSetting.ObjectMotion.FALL_DOWN_Y_PER_MS * GameSetting.JudgmentWindow.BAD_MIN_MS);
    }
}