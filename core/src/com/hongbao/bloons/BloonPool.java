package com.hongbao.bloons;

import com.badlogic.gdx.utils.Pool;
import com.hongbao.bloons.entities.Bloon;

public class BloonPool extends Pool<Bloon> {

	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	public static final int DEFAULT_MAX_POOL_SIZE = 1000;

	public BloonPool() {
		super(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_POOL_SIZE);
	}

	public BloonPool(int initialCapacity, int max) {
		super(initialCapacity, max);
	}

	@Override
	protected Bloon newObject() {
		return new Bloon();
	}

	public Bloon obtain(Bloon.Color color, int health, boolean camo, boolean regen) {
		Bloon bloon = obtain();
		bloon.init(color, health, camo, regen);
		return bloon;
	}

	public Bloon obtain(Bloon.Color color, int health, boolean camo, boolean regen, int distanceTravelled) {
		Bloon bloon = obtain();
		bloon.init(color, health, camo, regen, distanceTravelled);
		return bloon;
	}
}
