package com.hongbao.bloons.descriptors;

import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.List;

public class LevelWaveDescriptor {
    private final List<Bloon> bloons;
    private final List<Long> intervals;

    public LevelWaveDescriptor(List<Bloon> bloons, List<Long> intervals) {
        this.bloons = bloons != null ? bloons : new ArrayList<>();
        this.intervals = intervals != null ? intervals : new ArrayList<>();
    }

    public static LevelWaveDescriptor empty() {
        return new LevelWaveDescriptor(new ArrayList<>(), new ArrayList<>());
    }

    public List<Bloon> getBloons() {
        return bloons;
    }

    public List<Long> getIntervals() {
        return intervals;
    }

    public boolean isEmpty() {
        return bloons.isEmpty();
    }
}
