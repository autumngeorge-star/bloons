package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {

	// Defines a sequence of bloons appearing on the map as well as when they should appear

	private final List<List<SpawnEntry>> spawnEntries;
	private int currentLevel;
	private int currentIndex;
	private float spawnTime;

	public BloonQueue(List<List<SpawnEntry>> spawnEntries) {
		this.spawnEntries = spawnEntries;
		this.currentLevel = 0;
		this.currentIndex = 0;
		this.spawnTime = 0f;
	}

	public Set<Bloon> getBloons(float delta) {
		spawnTime += delta;
		return getBloons();
	}

	public Set<Bloon> getBloons() {
		Set<Bloon> generatedBloons = new HashSet<>();
		if (currentLevel >= spawnEntries.size()) {
			return generatedBloons;
		}
		List<SpawnEntry> currentLevelEntries = spawnEntries.get(currentLevel);
		while (currentIndex < currentLevelEntries.size()) {
			SpawnEntry entry = currentLevelEntries.get(currentIndex);
			if (entry.getSpawnTime() <= spawnTime + 1e-4f) {
				generatedBloons.add(entry.getBloon());
				currentIndex++;
			} else {
				break;
			}
		}
		return generatedBloons;
	}

	public int getLevel() {
		return currentLevel;
	}

	public void nextLevel() {
		currentLevel++;
		currentIndex = 0;
		spawnTime = 0f;
	}

	public boolean hasNextLevel() {
		return currentLevel < spawnEntries.size() - 1;
	}

	public boolean isEmpty() {
		if (currentLevel >= spawnEntries.size()) {
			return true;
		}
		return currentIndex == spawnEntries.get(currentLevel).size();
	}

	public float getSpawnTime() {
		return spawnTime;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public List<List<SpawnEntry>> getSpawnEntries() {
		return spawnEntries;
	}

}
