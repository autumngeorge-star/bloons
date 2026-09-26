package com.hongbao.bloons.scores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardData {
    private List<LeaderboardEntry> entries;

    // Required zero-arg constructor for LibGDX Json serialization
    public LeaderboardData() {
        this.entries = new ArrayList<>();
    }

    public List<LeaderboardEntry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        return entries;
    }

    public void setEntries(List<LeaderboardEntry> entries) {
        this.entries = entries;
        sortAndTrim();
    }

    public void addEntry(LeaderboardEntry entry) {
        getEntries().add(entry);
        sortAndTrim();
    }

    public void sortAndTrim() {
        sortAndTrim(100); // Storage capped at top 100 entries max
    }

    public void sortAndTrim(int maxEntries) {
        if (entries == null) {
            entries = new ArrayList<>();
            return;
        }
        Collections.sort(entries);
        if (entries.size() > maxEntries) {
            entries = new ArrayList<>(entries.subList(0, maxEntries));
        }
    }

    public List<LeaderboardEntry> getTopEntries(int limit) {
        sortAndTrim();
        if (entries.isEmpty()) {
            return Collections.emptyList();
        }
        int count = Math.min(limit, entries.size());
        return new ArrayList<>(entries.subList(0, count));
    }

    public int getRankForScore(int score) {
        sortAndTrim();
        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            if (score < entry.getScore()) {
                rank++;
            } else {
                break;
            }
        }
        return rank;
    }
}
