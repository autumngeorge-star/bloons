package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private List<List<BloonSpawnSpec>> bloons;
	private List<List<Long>> intervals;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<List<BloonSpawnSpec>> bloons, List<List<Long>> intervals) {
		this.bloons = bloons;
		this.intervals = intervals;
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		while (currentIndex < bloons.get(currentLevel).size()) {
			if (intervals.get(currentLevel).get(currentIndex) == clock) {
				BloonSpawnSpec spec = bloons.get(currentLevel).get(currentIndex);
				Bloon createdBloon = BloonFactory.createBloonOfType(spec.getType());
				generatedBloons.add(createdBloon);
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
