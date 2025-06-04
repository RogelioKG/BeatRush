package org.notiva.beatrush.core;

import java.util.Map;

import javafx.scene.input.KeyCode;
import org.notiva.beatrush.util.TrackType;

/**
 * <h2>遊戲設定</h2>
 */
public final class GameSetting {

    public static final class Timing {
        public static final double FRAME_PER_S = 100.0;
        public static final double FRAME_TIME_S = 1.0 / FRAME_PER_S;
        public static final double DELAY_CORRECTION_MS = 300.0;
    }

    public static final class ObjectMotion {
        public static final double FALL_DOWN_Y_PER_S = 600.0;
        public static final double FALL_DOWN_Y_PER_MS = FALL_DOWN_Y_PER_S / 1000.0;
        public static final double FALL_DOWN_Y_PER_FRAME = FALL_DOWN_Y_PER_S / Timing.FRAME_PER_S;
    }

    public static final class Audio {
        public static final double SONG_VOLUME_RATIO = 0.1;
        public static final double BGM_VOLUME_RATIO = 0.05;
        public static final double BGM_FADE_MS = 1000;
    }

    public static final class JudgmentWindow {
        public static final double PERFECT_MIN_MS = -70.0;
        public static final double PERFECT_MAX_MS = 70.0;
        public static final double GREAT_MIN_MS = -200.0;
        public static final double GREAT_MAX_MS = 150.0;
        public static final double GOOD_MIN_MS = -400.0;
        public static final double GOOD_MAX_MS = 200.0;
        public static final double BAD_MIN_MS = -800.0;
        public static final double BAD_MAX_MS = 300.0;
    }

    public static final class Score {
        public static final int COMBO_BONUS_INTERVAL = 10; // 每幾連擊增加一次分數
        public static final int COMBO_BONUS_MAX = 30;      // 最大加分
    }

    public static final class Control {
        public static final Map<KeyCode, TrackType> KEY_TO_TRACK = Map.of(
                KeyCode.D, TrackType.LEFT,
                KeyCode.F, TrackType.MIDDLE_LEFT,
                KeyCode.J, TrackType.MIDDLE_RIGHT,
                KeyCode.K, TrackType.RIGHT
        );
    }

    private GameSetting() {
    }
}
