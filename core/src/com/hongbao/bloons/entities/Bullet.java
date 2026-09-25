package com.hongbao.bloons.entities;

public class Bullet {
	
	public static final String IMAGE_FOLDER = "img/projectiles/";
	
	private float speed;
	private int damage;
	private int pierce;
	private float maxRange; // Because of how actions are evaluated, this is an approximation. But that's fine.
	private float distanceTraveled;
	private boolean homing;
	private String imageFileName;

	private float initialXOffset;

	private float initialYOffset;

	private float initialDXOverride;
	private float initialDYOverride;

	private StatusType statusType = StatusType.NONE;
	private float statusDuration = 0f;
	private int statusDotDamage = 0;
	private float statusDotInterval = 0f;
	
	public Bullet() {
		speed = 20f;
		damage = 2;
		pierce = 2;
		maxRange = 500;
		distanceTraveled = 0;
		homing = false;
		imageFileName = IMAGE_FOLDER + "red_spell_card.png";
	}
	
	public Bullet(float speed, int damage, int pierce, float maxRange, boolean homing, String imageFileName) {
		this.speed = speed;
		this.damage = damage;
		this.pierce = pierce;
		this.maxRange = maxRange;
		this.imageFileName = IMAGE_FOLDER + imageFileName;
		distanceTraveled = 0;
		this.homing = homing;
	}

	public Bullet(float speed, int damage, int pierce, float maxRange, boolean homing, String imageFileName, StatusType statusType, float statusDuration) {
		this(speed, damage, pierce, maxRange, homing, imageFileName);
		this.statusType = statusType;
		this.statusDuration = statusDuration;
	}

	public Bullet(float speed, int damage, int pierce, float maxRange, boolean homing, String imageFileName, StatusType statusType, float statusDuration, int statusDotDamage, float statusDotInterval) {
		this(speed, damage, pierce, maxRange, homing, imageFileName, statusType, statusDuration);
		this.statusDotDamage = statusDotDamage;
		this.statusDotInterval = statusDotInterval;
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

	public StatusType getStatusType() {
		return statusType;
	}

	public void setStatusType(StatusType statusType) {
		this.statusType = statusType;
	}

	public float getStatusDuration() {
		return statusDuration;
	}

	public void setStatusDuration(float statusDuration) {
		this.statusDuration = statusDuration;
	}

	public int getStatusDotDamage() {
		return statusDotDamage;
	}

	public void setStatusDotDamage(int statusDotDamage) {
		this.statusDotDamage = statusDotDamage;
	}

	public float getStatusDotInterval() {
		return statusDotInterval;
	}

	public void setStatusDotInterval(float statusDotInterval) {
		this.statusDotInterval = statusDotInterval;
	}

	public void setStatusEffect(StatusType statusType, float duration) {
		setStatusEffect(statusType, duration, 0, 0f);
	}

	public void setStatusEffect(StatusType statusType, float duration, int dotDamage, float dotInterval) {
		this.statusType = statusType;
		this.statusDuration = duration;
		this.statusDotDamage = dotDamage;
		this.statusDotInterval = dotInterval;
	}
}
