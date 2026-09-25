package com.hongbao.bloons.entities;

/**
 * Represents the statistical payload for a character tower at a specific upgrade node/tier.
 */
public class GirlTier {

	private final int attackDelay;
	private final float bulletSpeed;
	private final int damage;
	private final int pierce;
	private final float range;
	private final float visualRange;
	private final boolean homing;

	public GirlTier(int attackDelay, float bulletSpeed, int damage, int pierce, float range, float visualRange, boolean homing) {
		this.attackDelay = attackDelay;
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		this.pierce = pierce;
		this.range = range;
		this.visualRange = visualRange;
		this.homing = homing;
	}

	public int getAttackDelay() {
		return attackDelay;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public int getDamage() {
		return damage;
	}

	public int getPierce() {
		return pierce;
	}

	public float getRange() {
		return range;
	}

	public float getVisualRange() {
		return visualRange;
	}

	public boolean isHoming() {
		return homing;
	}
}
