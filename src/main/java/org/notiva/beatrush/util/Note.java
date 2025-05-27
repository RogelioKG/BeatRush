package org.notiva.beatrush.util;


public class Note {
    private String type;
    private int track;
    private long timestamp;
    private double duration;  // 使用 double 對應 JSON 中的 1.5

    public Note() {
    }

    // Getter 和 Setter（Gson 需要）
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public long getTimestamp() {
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
                "type='" + type + '\'' +
                ", track=" + track +
                ", timestamp=" + timestamp +
                ", duration=" + duration +
                '}';
    }
}

