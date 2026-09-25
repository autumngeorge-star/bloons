package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {

	public static final float TICK_DELTA = 1.0f / 60.0f;
	public static final int MAX_SUB_STEPS = 100;

	private final List<List<SpawnEntry>> spawnLevels;
	private int currentLevel;
	private int currentIndex;
	private long clock;
	private float tickAccumulator;

	public BloonQueue(List<List<SpawnEntry>> spawnLevels) {
		this.spawnLevels = spawnLevels;
		this.currentLevel = 0;
		this.currentIndex = 0;
		this.clock = 0;
		this.tickAccumulator = 0f;
	}

	public BloonQueue(List<List<Bloon>> bloons, List<List<Long>> intervals) {
		this.spawnLevels = convertToSpawnLevels(bloons, intervals);
		this.currentLevel = 0;
		this.currentIndex = 0;
		this.clock = 0;
		this.tickAccumulator = 0f;
	}

	private static List<List<SpawnEntry>> convertToSpawnLevels(List<List<Bloon>> bloons, List<List<Long>> intervals) {
		List<List<SpawnEntry>> converted = new ArrayList<>();
		if (bloons != null && intervals != null) {
			for (int i = 0; i < bloons.size(); i++) {
				List<Bloon> bList = bloons.get(i);
				List<Long> iList = intervals.get(i);
				List<SpawnEntry> levelEntries = new ArrayList<>();
				if (bList != null && iList != null) {
					for (int j = 0; j < bList.size(); j++) {
						long interval = j < iList.size() ? iList.get(j) : 0L;
						levelEntries.add(new SpawnEntry(bList.get(j), interval));
					}
				}
				converted.add(levelEntries);
			}
		}
		return converted;
	}

	public Set<Bloon> getBloons() {
		float delta = (Gdx.graphics != null) ? Gdx.graphics.getDeltaTime() : TICK_DELTA;
		return getBloons(delta);
	}

	public Set<Bloon> getBloons(float delta) {
		Set<Bloon> generatedBloons = new HashSet<>();
		if (currentLevel >= spawnLevels.size()) {
			return generatedBloons;
		}

		tickAccumulator += delta;

		int subSteps = 0;
		List<SpawnEntry> currentQueue = spawnLevels.get(currentLevel);

		while (tickAccumulator >= TICK_DELTA && subSteps < MAX_SUB_STEPS) {
			tickAccumulator -= TICK_DELTA;
			subSteps++;

			while (currentIndex < currentQueue.size()) {
				SpawnEntry entry = currentQueue.get(currentIndex);
				if (entry.getTargetTick() <= clock) {
					generatedBloons.add(entry.getBloon());
					currentIndex++;
				} else {
					break;
				}
			}

			clock++;
		}

		if (subSteps >= MAX_SUB_STEPS) {
			tickAccumulator = 0f;
		}

		return generatedBloons;
	}

	public int getLevel() {
		return currentLevel;
	}

	public void nextLevel() {
		currentLevel++;
		currentIndex = 0;
		clock = 0;
		tickAccumulator = 0f;
	}

	public boolean hasNextLevel() {
		return currentLevel < spawnLevels.size() - 1;
	}

	public boolean isEmpty() {
		if (currentLevel >= spawnLevels.size()) {
			return true;
		}
		return currentIndex >= spawnLevels.get(currentLevel).size();
	}

	public List<List<SpawnEntry>> getSpawnLevels() {
		return spawnLevels;
	}

	public long getClock() {
		return clock;
	}

	public float getTickAccumulator() {
		return tickAccumulator;
	}
}
