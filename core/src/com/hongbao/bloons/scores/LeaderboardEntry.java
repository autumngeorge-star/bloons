package com.hongbao.bloons.scores;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    private int score;
    private int level;
    private long timestamp;
    private String date;

    // Required zero-arg constructor for LibGDX Json serialization
    public LeaderboardEntry() {
    }

    public LeaderboardEntry(int score, int level, long timestamp) {
        this.score = score;
        this.level = level;
        this.timestamp = timestamp;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new Date(timestamp));
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(LeaderboardEntry o) {
        // Sort descending by score; if equal, descending by level; then descending by timestamp
        if (this.score != o.score) {
            return Integer.compare(o.score, this.score);
        }
        if (this.level != o.level) {
            return Integer.compare(o.level, this.level);
        }
        return Long.compare(o.timestamp, this.timestamp);
    }
}
