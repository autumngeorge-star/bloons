package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.List;

public class Wave {

    private final int level;
    private final String title;
    private final String musicTrack;
    private final List<Bloon> bloons;
    private final List<Long> intervals;

    public Wave(int level, String title, String musicTrack, List<Bloon> bloons, List<Long> intervals) {
        this.level = level;
        this.title = title;
        this.musicTrack = musicTrack;
        this.bloons = bloons;
        this.intervals = intervals;
    }

    public int getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public String getMusicTrack() {
        return musicTrack;
    }

    public List<Bloon> getBloons() {
        return bloons;
    }

    public List<Long> getIntervals() {
        return intervals;
    }
}
