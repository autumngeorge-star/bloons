package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates bloon entities, spawn interval timings, and LevelMetadata for a wave level.
 */
public class Level {
	private final List<Bloon> bloons;
	private final List<Long> intervals;
	private final LevelMetadata metadata;

	public Level(List<Bloon> bloons, List<Long> intervals, LevelMetadata metadata) {
		this.bloons = bloons != null ? bloons : new ArrayList<>();
		this.intervals = intervals != null ? intervals : new ArrayList<>();
		this.metadata = metadata != null ? metadata : new LevelMetadata();
	}

	public List<Bloon> getBloons() {
		return bloons;
	}

	public List<Long> getIntervals() {
		return intervals;
	}

	public LevelMetadata getMetadata() {
		return metadata;
	}
}
