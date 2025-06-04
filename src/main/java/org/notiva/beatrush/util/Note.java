package org.notiva.beatrush.util;

/**
 * <h2>音符</h2>
 * <p>
 * 表示一個節奏遊戲中的音符，包括音符類型、軌道、時間戳與持續時間等屬性。
 * TapNote 的持續時間為 0，HoldNote 則大於 0。
 * </p>
 *
 * <p>使用範例：</p>
 * <pre>{@code
 * Note tapNote = new Note(NoteType.TAP, TrackType.LEFT, 1500, 0);
 * Note holdNote = new Note(NoteType.HOLD, TrackType.RIGHT, 2000, 800);
 * }</pre>
 */
public class Note {
    /** 音符類型（Tap 或 Hold） */
    private NoteType noteType;

    /** 所屬軌道（例如：左、中、右） */
    private TrackType trackType;

    /** 音符時間戳（ms） */
    private double timestamp;

    /** 持續時間（ms），若為 TapNote 則為 0 */
    private double duration;

    /**
     * 建立音符物件。
     *
     * @param noteType   音符類型（TAP 或 HOLD）
     * @param trackType  所屬軌道
     * @param timestamp  音符時間戳（ms）
     * @param duration   持續時間（ms），若為 Tap 則為 0
     */
    public Note(NoteType noteType, TrackType trackType, double timestamp, double duration) {
        this.noteType = noteType;
        this.trackType = trackType;
        this.timestamp = timestamp;
        this.duration = duration;
    }

    /**
     * 取得音符類型。
     *
     * @return 音符類型
     */
    public NoteType getNoteType() {
        return noteType;
    }

    /**
     * 設定音符類型。
     *
     * @param noteType 音符類型
     */
    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    /**
     * 取得軌道類型。
     *
     * @return 軌道類型
     */
    public TrackType getTrackType() {
        return trackType;
    }

    /**
     * 設定軌道類型。
     *
     * @param trackType 軌道類型
     */
    public void setTrackType(TrackType trackType) {
        this.trackType = trackType;
    }

    /**
     * 取得音符的時間戳（ms）。
     *
     * @return 時間戳
     */
    public double getTimestamp() {
        return timestamp;
    }

    /**
     * 設定音符的時間戳（ms）。
     *
     * @param timestamp 時間戳
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 取得音符的持續時間（ms）。
     *
     * @return 持續時間
     */
    public double getDuration() {
        return duration;
    }

    /**
     * 設定音符的持續時間（ms）。
     *
     * @param duration 持續時間
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * 回傳音符資訊的字串表示（序列化用）。
     *
     * @return 字串形式的音符資訊
     */
    @Override
    public String toString() {
        return "Note{" +
                "noteType='" + noteType.getSerializedName() + "', " +
                "trackType='" + trackType.getSerializedName() + "', " +
                "timestamp=" + timestamp + ", " +
                "duration=" + duration +
                "}";
    }
}
