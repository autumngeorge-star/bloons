package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;

public class BloonPopJob {

	private BloonActor bloonActor;
	private int damage;
	private float x;
	private float y;

	public BloonPopJob(BloonActor bloonActor, int damage, float x, float y) {
		this.bloonActor = bloonActor;
		this.damage = damage;
		this.x = x;
		this.y = y;
	}

	public BloonActor getBloonActor() {
		return bloonActor;
	}

	public int getDamage() {
		return damage;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
