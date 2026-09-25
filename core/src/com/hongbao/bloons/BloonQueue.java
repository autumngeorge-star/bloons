package com.hongbao.bloons;

import com.hongbao.bloons.descriptors.BloonSpawnDescriptor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private List<List<BloonSpawnDescriptor>> bloonLevels;
	private int currentLevel;
	private int currentIndex;
	private int clock;
	
	public BloonQueue(List<List<BloonSpawnDescriptor>> bloonLevels) {
		this.bloonLevels = bloonLevels;
		currentLevel = 0;
		currentIndex = 0;
		clock = 0;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		if (currentLevel < bloonLevels.size()) {
			List<BloonSpawnDescriptor> currentDescriptors = bloonLevels.get(currentLevel);
			while (currentIndex < currentDescriptors.size()) {
				BloonSpawnDescriptor descriptor = currentDescriptors.get(currentIndex);
				if (descriptor.getSpawnTick() == clock) {
					Bloon bloon = BloonFactory.createBloonFromDescriptor(descriptor);
					generatedBloons.add(bloon);
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
		return currentLevel < bloonLevels.size() - 1;
	}

	public boolean isEmpty() {
		if (currentLevel >= bloonLevels.size()) {
			return true;
		}
		return currentIndex == bloonLevels.get(currentLevel).size();
	}

	public List<List<BloonSpawnDescriptor>> getBloonLevels() {
		return bloonLevels;
	}

	public List<BloonSpawnDescriptor> getDescriptorsForLevel(int level) {
		if (level >= 0 && level < bloonLevels.size()) {
			return bloonLevels.get(level);
		}
		return null;
	}
	
}
