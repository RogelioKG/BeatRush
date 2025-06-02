package org.notiva.beatrush.util;


public class Note {
    private NoteType noteType;
    private TrackType trackType;
    private double timestamp;
    private double duration; // 若為 TapNote 則為 0

    public Note(NoteType noteType, TrackType trackType, double timestamp, double duration) {
        this.noteType = noteType;
        this.trackType = trackType;
        this.timestamp = timestamp;
        this.duration = duration;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public TrackType getTrackType() {
        return trackType;
    }

    public void setTrackType(TrackType trackType) {
        this.trackType = trackType;
    }

    public double getTimestamp() {
        // 時間戳單位：ms
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

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

