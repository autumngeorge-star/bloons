package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {

	// Defines a sequence of bloons appearing on the map as well as when they should appear

	private List<List<Bloon>> bloons;
	private List<List<Long>> intervals;
	private List<Wave> waves;
	private int currentLevel;
	private int currentIndex;
	private int clock;

	public BloonQueue(List<Wave> waves) {
		this.waves = waves != null ? waves : new ArrayList<>();
		this.bloons = new ArrayList<>();
		this.intervals = new ArrayList<>();
		for (Wave wave : this.waves) {
			this.bloons.add(wave.getBloons());
			this.intervals.add(wave.getIntervals());
		}
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}

	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals) {
		this.bloons = bloons != null ? bloons : new ArrayList<>();
		this.intervals = intervals != null ? intervals : new ArrayList<>();
		this.waves = new ArrayList<>();
		if (this.bloons != null) {
			for (int i = 0; i < this.bloons.size(); i++) {
				List<Bloon> bList = this.bloons.get(i);
				List<Long> iList = (i < this.intervals.size()) ? this.intervals.get(i) : new ArrayList<>();
				this.waves.add(new Wave(bList, iList, null, null, 0));
			}
		}
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}

	public Wave getCurrentWave() {
		if (currentLevel >= 0 && currentLevel < waves.size()) {
			return waves.get(currentLevel);
		}
		return null;
	}

	public Wave getWave(int level) {
		if (level >= 0 && level < waves.size()) {
			return waves.get(level);
		}
		return null;
	}

	public List<Wave> getWaves() {
		return waves;
	}

	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		if (currentLevel >= 0 && currentLevel < bloons.size()) {
			List<Bloon> currentBloons = bloons.get(currentLevel);
			List<Long> currentIntervals = intervals.get(currentLevel);
			while (currentIndex < currentBloons.size()) {
				if (currentIntervals.get(currentIndex) == clock) {
					generatedBloons.add(currentBloons.get(currentIndex));
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
		return currentLevel < bloons.size() - 1;
	}

	public boolean isEmpty() {
		if (currentLevel >= 0 && currentLevel < bloons.size()) {
			return currentIndex == bloons.get(currentLevel).size();
		}
		return true;
	}

}
