package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonQueue {
	
	// Defines a sequence of bloons appearing on the map as well as when they should appear
	
	private List<List<WaveSegment>> segments;
	private int currentLevel;
	private long clock;
	
	public BloonQueue(List<List<WaveSegment>> segments) {
		this.segments = segments;
		this.currentLevel = 0;
		this.clock = 0;
	}
	
	public Set<Bloon> getBloons() {
		HashSet<Bloon> generatedBloons = new HashSet<>();
		if (currentLevel < segments.size()) {
			List<WaveSegment> levelSegments = segments.get(currentLevel);
			for (WaveSegment segment : levelSegments) {
				int totalBloons = segment.getTotalBloons();
				if (totalBloons == 0) {
					continue;
				}
				long startTick = segment.getStartTick();
				long delay = segment.getDelay();
				
				if (delay == 0) {
					if (clock == startTick) {
						for (int x = 0; x < segment.getAmount(); x++) {
							for (String type : segment.getTypes()) {
								generatedBloons.add(BloonFactory.createBloonOfType(type));
							}
						}
					}
				} else {
					if (clock >= startTick && (clock - startTick) % delay == 0) {
						long k = (clock - startTick) / delay;
						if (k >= 0 && k < totalBloons) {
							List<String> types = segment.getTypes();
							int typeIndex = (int) (k % types.size());
							String type = types.get(typeIndex);
							generatedBloons.add(BloonFactory.createBloonOfType(type));
						}
					}
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
		clock = 0;
	}

	public boolean hasNextLevel() {
		return currentLevel != segments.size() - 1;
	}

	public boolean isEmpty() {
		if (currentLevel >= segments.size()) {
			return true;
		}
		List<WaveSegment> levelSegments = segments.get(currentLevel);
		boolean hasBloons = false;
		long maxLastSpawnTick = -1;
		for (WaveSegment segment : levelSegments) {
			if (segment.getTotalBloons() > 0) {
				hasBloons = true;
				long lastSpawn;
				if (segment.getDelay() == 0) {
					lastSpawn = segment.getStartTick();
				} else {
					lastSpawn = segment.getStartTick() + (long) (segment.getTotalBloons() - 1) * segment.getDelay();
				}
				if (lastSpawn > maxLastSpawnTick) {
					maxLastSpawnTick = lastSpawn;
				}
			}
		}
		if (!hasBloons) {
			return true;
		}
		return clock > maxLastSpawnTick;
	}
	
}
