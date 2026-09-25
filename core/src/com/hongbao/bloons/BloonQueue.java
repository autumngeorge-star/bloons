package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines a sequence of bloon waves appearing on the map, along with per-level metadata.
 */
public class BloonQueue {
	
	private List<List<Bloon>> bloons;
	private List<List<Long>> intervals;
	private List<LevelMetadata> metadataLevels;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals) {
		this(bloons, intervals, null);
	}

	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals, List<LevelMetadata> metadataLevels) {
		this.bloons = bloons != null ? bloons : new ArrayList<>();
		this.intervals = intervals != null ? intervals : new ArrayList<>();
		if (metadataLevels != null) {
			this.metadataLevels = metadataLevels;
		} else {
			this.metadataLevels = new ArrayList<>();
			for (int i = 0; i < this.bloons.size(); i++) {
				this.metadataLevels.add(new LevelMetadata());
			}
		}
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}

	public BloonQueue(List<Level> levels) {
		this.bloons = new ArrayList<>();
		this.intervals = new ArrayList<>();
		this.metadataLevels = new ArrayList<>();
		if (levels != null) {
			for (Level lvl : levels) {
				this.bloons.add(lvl.getBloons());
				this.intervals.add(lvl.getIntervals());
				this.metadataLevels.add(lvl.getMetadata());
			}
		}
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		if (currentLevel < bloons.size()) {
			while (currentIndex < bloons.get(currentLevel).size()) {
				if (intervals.get(currentLevel).get(currentIndex) == clock) {
					generatedBloons.add(bloons.get(currentLevel).get(currentIndex));
					currentIndex++;
				} else {
					break;
				}
			}
		}
		clock++;
		return generatedBloons;
	}

	public int getLevel() {
		return currentLevel;
	}

	public LevelMetadata getCurrentLevelMetadata() {
		return getLevelMetadata(currentLevel);
	}

	public LevelMetadata getLevelMetadata(int levelIndex) {
		if (levelIndex >= 0 && levelIndex < metadataLevels.size()) {
			return metadataLevels.get(levelIndex);
		}
		return new LevelMetadata();
	}

	public List<LevelMetadata> getLevelMetadatas() {
		return metadataLevels;
	}

	public void nextLevel() {
		currentLevel++;
		currentIndex = 0;
		clock = 0;
	}

	public boolean hasNextLevel() {
		return currentLevel < bloons.size() - 1;
	}

	public boolean isEmpty() {
		if (currentLevel < bloons.size()) {
			return currentIndex == bloons.get(currentLevel).size();
		}
		return true;
	}
	
}
