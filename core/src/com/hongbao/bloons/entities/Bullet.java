package com.hongbao.bloons.entities;

import com.hongbao.bloons.policies.BulletLifecyclePolicy;
import com.hongbao.bloons.policies.RangeLifecyclePolicy;

public class Bullet {
	
	public static final String IMAGE_FOLDER = "img/projectiles/";
	
	private float speed;
	private int damage;
	private int pierce;
	private float maxRange; // Because of how actions are evaluated, this is an approximation. But that's fine.
	private float distanceTraveled;
	private float maxDuration;
	private float duration;
	private BulletLifecyclePolicy lifecyclePolicy;
	private boolean homing;
	private String imageFileName;

	private float initialXOffset;

	private float initialYOffset;

	private float initialDXOverride;
	private float initialDYOverride;
	
	public Bullet() {
		speed = 20f;
		damage = 2;
		pierce = 2;
		maxRange = 500;
		distanceTraveled = 0;
		maxDuration = 10f;
		duration = 0;
		lifecyclePolicy = RangeLifecyclePolicy.DEFAULT;
		homing = false;
		imageFileName = IMAGE_FOLDER + "red_spell_card.png";
	}
	
	public Bullet(float speed, int damage, int pierce, float maxRange, boolean homing, String imageFileName) {
		this(speed, damage, pierce, maxRange, homing, imageFileName, RangeLifecyclePolicy.DEFAULT);
	}

	public Bullet(float speed, int damage, int pierce, float maxRange, boolean homing, String imageFileName, BulletLifecyclePolicy lifecyclePolicy) {
		this.speed = speed;
		this.damage = damage;
		this.pierce = pierce;
		this.maxRange = maxRange;
		this.imageFileName = IMAGE_FOLDER + imageFileName;
		distanceTraveled = 0;
		maxDuration = 10f;
		duration = 0;
		this.lifecyclePolicy = lifecyclePolicy != null ? lifecyclePolicy : RangeLifecyclePolicy.DEFAULT;
		this.homing = homing;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public int getPierce() {
		return pierce;
	}
	
	public void decrementPierce() {
		pierce--;
	}
	
	public float getMaxRange() {
		return maxRange;
	}
	
	public void setMaxRange(float maxRange) {
		this.maxRange = maxRange;
	}
	
	public float getDistanceTraveled() {
		return distanceTraveled;
	}
	
	public void incrementDistanceTraveled() {
		distanceTraveled += speed / 5;
	}

	public float getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(float maxDuration) {
		this.maxDuration = maxDuration;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public void incrementDuration(float delta) {
		duration += delta;
	}

	public BulletLifecyclePolicy getLifecyclePolicy() {
		return lifecyclePolicy;
	}

	public void setLifecyclePolicy(BulletLifecyclePolicy lifecyclePolicy) {
		this.lifecyclePolicy = lifecyclePolicy != null ? lifecyclePolicy : RangeLifecyclePolicy.DEFAULT;
	}

	public boolean isExpired() {
		if (lifecyclePolicy != null) {
			return lifecyclePolicy.isExpired(this);
		}
		return distanceTraveled >= maxRange;
	}
	
	public boolean isHoming() {
		return homing;
	}
	
	public void setHoming(boolean homing) {
		this.homing = homing;
	}
	
	public String getImageFileName() {
		return imageFileName;
	}
	
	public void setImageFileName(String imageFileName) {
		this.imageFileName = IMAGE_FOLDER + imageFileName;
	}

	public float getInitialXOffset() {
		return initialXOffset;
	}

	public void setInitialXOffset(float initialXOffset) {
		this.initialXOffset = initialXOffset;
	}

	public float getInitialYOffset() {
		return initialYOffset;
	}

	public void setInitialYOffset(float initialYOffset) {
		this.initialYOffset = initialYOffset;
	}

	public float getInitialDXOverride() {
		return initialDXOverride;
	}
	
	public void setInitialDXOverride(float initialDXOverride) {
		this.initialDXOverride = initialDXOverride;
	}
	
	public float getInitialDYOverride() {
		return initialDYOverride;
	}
	
	public void setInitialDYOverride(float initialDYOverride) {
		this.initialDYOverride = initialDYOverride;
	}
}
