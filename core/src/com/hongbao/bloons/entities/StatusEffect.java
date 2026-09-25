package com.hongbao.bloons.entities;

public class StatusEffect {

	public enum Type {
		FREEZE,
		SLOW,
		DAMAGE_OVER_TIME,
		CUSTOM
	}

	private Type type;
	private float duration;
	private float speedModifier;
	private int damagePerTick;
	private float tickInterval;
	private float tickTimer;

	public StatusEffect(Type type, float duration, float speedModifier, int damagePerTick, float tickInterval) {
		this.type = type;
		this.duration = duration;
		this.speedModifier = speedModifier;
		this.damagePerTick = damagePerTick;
		this.tickInterval = tickInterval;
		this.tickTimer = 0f;
	}

	public StatusEffect(Type type, float duration, float speedModifier) {
		this(type, duration, speedModifier, 0, 1.0f);
	}

	public StatusEffect(Type type, float duration, int damagePerTick, float tickInterval) {
		this(type, duration, 1.0f, damagePerTick, tickInterval);
	}

	public StatusEffect(StatusEffect other) {
		this.type = other.type;
		this.duration = other.duration;
		this.speedModifier = other.speedModifier;
		this.damagePerTick = other.damagePerTick;
		this.tickInterval = other.tickInterval;
		this.tickTimer = other.tickTimer;
	}

	public StatusEffect copy() {
		return new StatusEffect(this);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getSpeedModifier() {
		return speedModifier;
	}

	public void setSpeedModifier(float speedModifier) {
		this.speedModifier = speedModifier;
	}

	public int getDamagePerTick() {
		return damagePerTick;
	}

	public void setDamagePerTick(int damagePerTick) {
		this.damagePerTick = damagePerTick;
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

	public boolean isExpired() {
		return duration <= 0f;
	}

	public boolean isFreeze() {
		return type == Type.FREEZE || speedModifier == 0f;
	}

	public int update(float delta) {
		if (isExpired()) {
			return 0;
		}

		duration -= delta;

		int accumulatedDamage = 0;
		if (damagePerTick > 0 && tickInterval > 0f) {
			tickTimer += delta;
			while (tickTimer >= tickInterval) {
				tickTimer -= tickInterval;
				accumulatedDamage += damagePerTick;
			}
		}

		return accumulatedDamage;
	}
}
