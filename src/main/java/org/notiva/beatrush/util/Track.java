package org.notiva.beatrush.util;

import java.util.List;

/**
 * <h2>音軌</h2>
 */
public class Track {
    private TrackType trackType;
    private List<Note> trackNotes;
    private int currentNoteIndex = 0;
    private boolean finished = false;

    public Track(TrackType trackType, List<Note> trackNotes) {
        this.trackType = trackType;
        this.trackNotes = trackNotes;
    }

    public Note getCurrentNote() {
        return trackNotes.get(currentNoteIndex);
    }

    public void next() {
        currentNoteIndex += 1;
        if (currentNoteIndex >= trackNotes.size()) {
            finished = true;
        }
    }

    public void reset() {
        currentNoteIndex = 0;
        finished = false;
    }

    public boolean isFinished() {
        return finished;
    }

    public TrackType getTrackType() {
        return trackType;
    }

    public void setTrackType(TrackType trackType) {
        this.trackType = trackType;
    }

    public List<Note> getTrackNotes() {
        return trackNotes;
    }

    public void setTrackNotes(List<Note> trackNotes) {
        this.trackNotes = trackNotes;
    }
}
