package com.hongbao.bloons.statuseffects;

public class StatusEffect {

	private String name;
	private float duration;         // Remaining duration in seconds
	private float tickInterval;     // Time in seconds between damage ticks
	private float tickTimer;        // Accumulated time toward next tick
	private int damage;             // Damage dealt per tick
	private float speedMultiplier;  // Multiplier applied to bloon speed (e.g. 0.5f for 50% slow)

	public StatusEffect(String name, float duration, float tickInterval, int damage, float speedMultiplier) {
		this.name = name;
		this.duration = duration;
		this.tickInterval = tickInterval;
		this.tickTimer = 0f;
		this.damage = damage;
		this.speedMultiplier = speedMultiplier;
	}

	public StatusEffect(float duration, float tickInterval, int damage, float speedMultiplier) {
		this("StatusEffect", duration, tickInterval, damage, speedMultiplier);
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

	public float getTickTimer() {
		return tickTimer;
	}

	public void setTickTimer(float tickTimer) {
		this.tickTimer = tickTimer;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

	public void setSpeedMultiplier(float speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	public boolean isExpired() {
		return duration <= 0;
	}

	/**
	 * Updates the remaining duration and tick timer by the given frame delta time.
	 * @param delta Time step in seconds.
	 * @return The number of damage ticks triggered during this frame.
	 */
	public int update(float delta) {
		if (isExpired()) {
			return 0;
		}

		duration -= delta;
		if (duration < 0) {
			duration = 0;
		}

		int ticksTriggered = 0;
		if (tickInterval > 0 && damage > 0) {
			tickTimer += delta;
			while (tickTimer >= tickInterval) {
				tickTimer -= tickInterval;
				ticksTriggered++;
			}
		}

		return ticksTriggered;
	}
}
