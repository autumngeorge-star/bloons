package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private List<List<BloonDescriptor>> bloonDescriptors;
	private List<List<Long>> intervals;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<List<BloonDescriptor>> bloonDescriptors, List<List<Long>> intervals) {
		this.bloonDescriptors = bloonDescriptors;
		this.intervals = intervals;
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		while (currentIndex < bloonDescriptors.get(currentLevel).size()) {
			if (intervals.get(currentLevel).get(currentIndex) == clock) {
				BloonDescriptor descriptor = bloonDescriptors.get(currentLevel).get(currentIndex);
				Bloon bloon = BloonFactory.obtainBloonOfType(descriptor.getType());
				generatedBloons.add(bloon);
				currentIndex++;
			} else {
				break;
			}
		}
		clock++;
		return generatedBloons;
	}

	public List<List<BloonDescriptor>> getBloonDescriptors() {
		return bloonDescriptors;
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
		return currentLevel != bloonDescriptors.size() - 1;
	}

	public boolean isEmpty() {
		return currentIndex == bloonDescriptors.get(currentLevel).size();
	}
	
}
