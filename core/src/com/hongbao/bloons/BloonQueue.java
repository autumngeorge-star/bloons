package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.loader.WaveMetadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private List<List<Bloon>> bloons;
	private List<List<Long>> intervals;
	private List<WaveMetadata> metadataList;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals) {
		this(bloons, intervals, null);
	}

	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals, List<WaveMetadata> metadataList) {
		this.bloons = bloons;
		this.intervals = intervals;
		this.metadataList = metadataList;
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		while (currentIndex < bloons.get(currentLevel).size()) {
			if (intervals.get(currentLevel).get(currentIndex) == clock) {
				generatedBloons.add(bloons.get(currentLevel).get(currentIndex));
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

	public List<WaveMetadata> getMetadataList() {
		return metadataList;
	}

	public WaveMetadata getCurrentWaveMetadata() {
		if (metadataList != null && currentLevel >= 0 && currentLevel < metadataList.size()) {
			return metadataList.get(currentLevel);
		}
		return null;
	}

	public WaveMetadata getWaveMetadata(int level) {
		if (metadataList != null && level >= 0 && level < metadataList.size()) {
			return metadataList.get(level);
		}
		return null;
	}

	public void nextLevel() {
		currentLevel++;
		currentIndex = 0;
		clock = 0;
	}

	public boolean hasNextLevel() {
		return currentLevel != bloons.size() - 1;
	}

	public boolean isEmpty() {
		return currentIndex == bloons.get(currentLevel).size();
	}
	
}
