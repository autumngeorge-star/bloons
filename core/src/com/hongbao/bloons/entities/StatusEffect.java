package com.hongbao.bloons.entities;

public class StatusEffect {

	private String name;
	private float duration; // remaining duration in seconds
	private float tickInterval; // interval between DOT ticks in seconds
	private int damageAmount; // damage per tick
	private float speedModification; // speed multiplier (e.g. 0.5f for slow, 0.0f for freeze)
	private float timeSinceLastTick;
	private boolean inheritable;

	public StatusEffect(String name, float duration, float tickInterval, int damageAmount, float speedModification) {
		this(name, duration, tickInterval, damageAmount, speedModification, true);
	}

	public StatusEffect(String name, float duration, float tickInterval, int damageAmount, float speedModification, boolean inheritable) {
		this.name = name;
		this.duration = duration;
		this.tickInterval = tickInterval;
		this.damageAmount = damageAmount;
		this.speedModification = speedModification;
		this.timeSinceLastTick = 0f;
		this.inheritable = inheritable;
	}

	public StatusEffect copy() {
		StatusEffect copy = new StatusEffect(name, duration, tickInterval, damageAmount, speedModification, inheritable);
		copy.timeSinceLastTick = this.timeSinceLastTick;
		return copy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getTickInterval() {
		return tickInterval;
	}

	public void setTickInterval(float tickInterval) {
		this.tickInterval = tickInterval;
	}

	public int getDamageAmount() {
		return damageAmount;
	}

	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}

	public float getSpeedModification() {
		return speedModification;
	}

	public void setSpeedModification(float speedModification) {
		this.speedModification = speedModification;
	}

	public float getTimeSinceLastTick() {
		return timeSinceLastTick;
	}

	public void setTimeSinceLastTick(float timeSinceLastTick) {
		this.timeSinceLastTick = timeSinceLastTick;
	}

	public boolean isInheritable() {
		return inheritable;
	}

	public void setInheritable(boolean inheritable) {
		this.inheritable = inheritable;
	}

	public boolean isExpired() {
		return duration <= 0.00001f;
	}

	/**
	 * Advances timer by delta. Returns true if a DOT damage tick should be executed.
	 */
	public boolean update(float delta) {
		duration -= delta;
		if (tickInterval > 0 && damageAmount > 0) {
			timeSinceLastTick += delta;
			if (timeSinceLastTick >= tickInterval) {
				timeSinceLastTick -= tickInterval;
				return true;
			}
		}
		return false;
	}
}
