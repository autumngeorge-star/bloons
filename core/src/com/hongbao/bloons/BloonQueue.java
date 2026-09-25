package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private List<List<Bloon>> bloons;
	private List<List<Long>> intervals;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals) {
		this.bloons = bloons;
		this.intervals = intervals;
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		if (bloons == null || currentLevel < 0 || currentLevel >= bloons.size()) {
			return generatedBloons;
		}
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

	public void setLevel(int level) {
		if (bloons == null || bloons.isEmpty()) {
			currentLevel = 0;
		} else if (level < 0) {
			currentLevel = 0;
		} else if (level >= bloons.size()) {
			currentLevel = bloons.size() - 1;
		} else {
			currentLevel = level;
		}
		currentIndex = 0;
		clock = 0;
	}

	public int getLevelsCount() {
		return bloons != null ? bloons.size() : 0;
	}

	public void nextLevel() {
		if (hasNextLevel()) {
			currentLevel++;
		}
		currentIndex = 0;
		clock = 0;
	}

	public boolean hasNextLevel() {
		if (bloons == null || bloons.isEmpty()) {
			return false;
		}
		return currentLevel < bloons.size() - 1;
	}

	public boolean isEmpty() {
		if (bloons == null || currentLevel < 0 || currentLevel >= bloons.size()) {
			return true;
		}
		return currentIndex == bloons.get(currentLevel).size();
	}
	
}
