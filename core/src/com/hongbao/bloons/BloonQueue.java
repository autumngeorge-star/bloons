package com.hongbao.bloons;

import com.hongbao.bloons.descriptors.LevelWaveDescriptor;
import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the active level's bloon spawn sequence and timing.
 * Wave definitions are streamed on demand per level rather than holding
 * all game levels in memory simultaneously.
 */
public class BloonQueue {

    private final WaveLoader waveLoader;
    private List<Bloon> activeBloons;
    private List<Long> activeIntervals;

    private int currentLevel;
    private int currentIndex;
    private int clock;

    public BloonQueue(WaveLoader waveLoader) {
        this(waveLoader, 0);
    }

    public BloonQueue(WaveLoader waveLoader, int initialLevel) {
        this.waveLoader = waveLoader;
        this.currentLevel = initialLevel;
        this.currentIndex = 0;
        this.clock = 0;
        loadActiveLevelData();
    }

    /**
     * Legacy constructor kept for backwards compatibility.
     */
    public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals) {
        this.waveLoader = null;
        this.currentLevel = 0;
        this.currentIndex = 0;
        this.clock = 0;
        if (bloons != null && !bloons.isEmpty() && currentLevel < bloons.size()) {
            this.activeBloons = new ArrayList<>(bloons.get(currentLevel));
            this.activeIntervals = new ArrayList<>(intervals.get(currentLevel));
        } else {
            this.activeBloons = new ArrayList<>();
            this.activeIntervals = new ArrayList<>();
        }
    }

    /**
     * Loads spawn descriptors strictly for the active level.
     */
    private void loadActiveLevelData() {
        if (waveLoader != null) {
            LevelWaveDescriptor wave = waveLoader.loadLevel(currentLevel);
            this.activeBloons = wave.getBloons();
            this.activeIntervals = wave.getIntervals();
        }
    }

    /**
     * Discards completed level wave descriptors to allow garbage collection,
     * advances the level index, and streams descriptors for the next level.
     */
    public void nextLevel() {
        if (activeBloons != null) {
            activeBloons.clear();
        }
        if (activeIntervals != null) {
            activeIntervals.clear();
        }

        currentLevel++;
        currentIndex = 0;
        clock = 0;

        loadActiveLevelData();
    }

    public Set<Bloon> getBloons() {
        HashSet<Bloon> generatedBloons = new HashSet<>();
        if (activeBloons == null || activeIntervals == null) {
            return generatedBloons;
        }

        while (currentIndex < activeBloons.size()) {
            if (activeIntervals.get(currentIndex) == clock) {
                generatedBloons.add(activeBloons.get(currentIndex));
                currentIndex++;
            } else {
                break;
            }
        }
        clock++;
        return generatedBloons;
    }

    public int getLevel() {
        return currentLevel;
    }

    public boolean hasNextLevel() {
        if (waveLoader != null) {
            return waveLoader.hasLevel(currentLevel + 1);
        }
        return false;
    }

    public boolean isEmpty() {
        if (activeBloons == null) {
            return true;
        }
        return currentIndex == activeBloons.size();
    }

    public WaveLoader getWaveLoader() {
        return waveLoader;
    }

    public List<Bloon> getActiveBloons() {
        return activeBloons;
    }

    public List<Long> getActiveIntervals() {
        return activeIntervals;
    }
}
