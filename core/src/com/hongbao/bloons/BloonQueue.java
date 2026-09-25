package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private List<List<Bloon>> bloons;
	private List<List<Long>> intervals;
	private List<WaveMetadata> waveMetadatas;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals) {
		this(bloons, intervals, null);
	}

	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals, List<WaveMetadata> waveMetadatas) {
		this.bloons = bloons;
		this.intervals = intervals;
		this.waveMetadatas = waveMetadatas;
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}

	public WaveMetadata getCurrentWaveMetadata() {
		if (waveMetadatas != null && currentLevel >= 0 && currentLevel < waveMetadatas.size()) {
			return waveMetadatas.get(currentLevel);
		}
		return null;
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
