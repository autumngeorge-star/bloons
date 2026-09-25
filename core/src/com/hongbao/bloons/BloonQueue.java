package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private final List<Wave> waves;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<Wave> waves) {
		this.waves = waves;
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}

	public List<Wave> getWaves() {
		return waves;
	}

	public Wave getCurrentWave() {
		if (currentLevel > 0 && currentLevel <= waves.size()) {
			return waves.get(currentLevel - 1);
		}
		return null;
	}

	public String getCurrentMusicTrack() {
		Wave wave = getCurrentWave();
		return wave != null ? wave.getMusicTrack() : null;
	}

	public String getCurrentTitle() {
		Wave wave = getCurrentWave();
		return wave != null ? wave.getTitle() : null;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		Wave wave = getCurrentWave();
		if (wave != null) {
			List<Bloon> bloons = wave.getBloons();
			List<Long> intervals = wave.getIntervals();
			while (currentIndex < bloons.size()) {
				if (intervals.get(currentIndex) == clock) {
					generatedBloons.add(bloons.get(currentIndex));
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

	public void nextLevel() {
		currentLevel++;
		currentIndex = 0;
		clock = 0;
	}

	public boolean hasNextLevel() {
		return currentLevel < waves.size();
	}

	public boolean isEmpty() {
		Wave wave = getCurrentWave();
		if (wave == null) {
			return true;
		}
		return currentIndex == wave.getBloons().size();
	}
	
}
