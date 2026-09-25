package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

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

	public WaveMetadata getCurrentWaveMetadata() {
		if (metadataList != null && currentLevel >= 0 && currentLevel < metadataList.size()) {
			return metadataList.get(currentLevel);
		}
		return null;
	}

	public String getCurrentTitle() {
		WaveMetadata metadata = getCurrentWaveMetadata();
		if (metadata != null && metadata.getTitle() != null && !metadata.getTitle().trim().isEmpty()) {
			return metadata.getTitle();
		}
		return "Level " + currentLevel;
	}

	public String getCurrentMusicTrack() {
		WaveMetadata metadata = getCurrentWaveMetadata();
		if (metadata != null) {
			return metadata.getMusicTrack();
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
