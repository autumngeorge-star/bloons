package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

public class SpawnEntry {

	private final Bloon bloon;
	private final long targetTick;

	public SpawnEntry(Bloon bloon, long targetTick) {
		this.bloon = bloon;
		this.targetTick = targetTick;
	}

	public Bloon getBloon() {
		return bloon;
	}

	public long getTargetTick() {
		return targetTick;
	}

	@Override
	public String toString() {
		return "SpawnEntry{" +
				"bloon=" + bloon +
				", targetTick=" + targetTick +
				'}';
	}
}
