package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

public class SpawnEntry {

	private final Bloon bloon;
	private final float spawnTime;

	public SpawnEntry(Bloon bloon, float spawnTime) {
		this.bloon = bloon;
		this.spawnTime = spawnTime;
	}

	public Bloon getBloon() {
		return bloon;
	}

	public float getSpawnTime() {
		return spawnTime;
	}

}
