package com.hongbao.bloons.entities;

public class Bullet {
	
	public static final String IMAGE_FOLDER = "img/projectiles/";
	
	private float speed;
	private int damage;
	private int pierce;
	private float maxLifetimeSeconds;
	private float elapsedTimeSeconds;
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
		maxLifetimeSeconds = 2.0833333f;
		elapsedTimeSeconds = 0f;
		homing = false;
		imageFileName = IMAGE_FOLDER + "red_spell_card.png";
	}
	
	public Bullet(float speed, int damage, int pierce, float maxLifetimeSeconds, boolean homing, String imageFileName) {
		this.speed = speed;
		this.damage = damage;
		this.pierce = pierce;
		this.maxLifetimeSeconds = maxLifetimeSeconds;
		this.imageFileName = IMAGE_FOLDER + imageFileName;
		this.elapsedTimeSeconds = 0f;
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
	
	public float getMaxLifetimeSeconds() {
		return maxLifetimeSeconds;
	}
	
	public void setMaxLifetimeSeconds(float maxLifetimeSeconds) {
		this.maxLifetimeSeconds = maxLifetimeSeconds;
	}
	
	public float getElapsedTimeSeconds() {
		return elapsedTimeSeconds;
	}
	
	public void setElapsedTimeSeconds(float elapsedTimeSeconds) {
		this.elapsedTimeSeconds = elapsedTimeSeconds;
	}
	
	public void updateElapsedTime(float delta) {
		this.elapsedTimeSeconds += delta;
	}
	
	public boolean isExpired() {
		return elapsedTimeSeconds >= maxLifetimeSeconds;
	}

	public float getMaxRange() {
		return maxLifetimeSeconds * 12f * speed;
	}
	
	public void setMaxRange(float maxRange) {
		this.maxLifetimeSeconds = maxRange / (12f * speed);
	}
	
	public float getDistanceTraveled() {
		return elapsedTimeSeconds * 12f * speed;
	}
	
	public void incrementDistanceTraveled() {
		this.elapsedTimeSeconds += (1f / 60f);
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
