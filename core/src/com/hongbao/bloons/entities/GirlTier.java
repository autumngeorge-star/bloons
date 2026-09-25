package com.hongbao.bloons.entities;

public class GirlTier {

	private final int attackDelay;
	private final float bulletSpeed;
	private final int damage;
	private final int pierce;
	private final float range;
	private final float visualRange;
	private final boolean homing;
	private final int upgradeCost;

	public GirlTier(int attackDelay, float bulletSpeed, int damage, int pierce, float range, float visualRange, boolean homing, int upgradeCost) {
		this.attackDelay = attackDelay;
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		this.pierce = pierce;
		this.range = range;
		this.visualRange = visualRange;
		this.homing = homing;
		this.upgradeCost = upgradeCost;
	}

	public GirlTier(Integer attackDelay, Float bulletSpeed, Integer damage, Integer pierce, Float range, Float visualRange, Boolean homing, Integer upgradeCost) {
		if (attackDelay == null || bulletSpeed == null || damage == null || pierce == null ||
				range == null || visualRange == null || homing == null || upgradeCost == null) {
			throw new IllegalArgumentException("GirlTier stat parameters cannot be null");
		}
		this.attackDelay = attackDelay;
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		this.pierce = pierce;
		this.range = range;
		this.visualRange = visualRange;
		this.homing = homing;
		this.upgradeCost = upgradeCost;
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

	public int getUpgradeCost() {
		return upgradeCost;
	}

}
