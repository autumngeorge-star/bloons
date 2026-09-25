package com.hongbao.bloons;

public class WaveMetadata {

    private final String title;
    private final String musicTrack;

    public WaveMetadata(String title, String musicTrack) {
        this.title = title;
        this.musicTrack = musicTrack;
    }

    public String getTitle() {
        return title;
    }

    public String getMusicTrack() {
        return musicTrack;
    }

    @Override
    public String toString() {
        return "WaveMetadata{" +
                "title='" + title + '\'' +
                ", musicTrack='" + musicTrack + '\'' +
                '}';
    }
}
