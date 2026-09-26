package com.hongbao.bloons.entities;

public class StatusEffectPayload {

	public enum Type {
		SLOW,
		FREEZE,
		BURN,
		POISON
	}

	private final Type type;
	private final float duration;
	private final float potency;
	private final float tickInterval;

	public StatusEffectPayload(Type type, float duration, float potency, float tickInterval) {
		this.type = type;
		this.duration = duration;
		this.potency = potency;
		this.tickInterval = tickInterval;
	}

	public StatusEffectPayload(Type type, float duration, float potency) {
		this(type, duration, potency, 0f);
	}

	public StatusEffectPayload(String typeStr, float duration, float potency, float tickInterval) {
		this(parseType(typeStr), duration, potency, tickInterval);
	}

	public StatusEffectPayload(String typeStr, float duration, float potency) {
		this(parseType(typeStr), duration, potency, 0f);
	}

	private static Type parseType(String typeStr) {
		if (typeStr == null) {
			return Type.SLOW;
		}
		try {
			return Type.valueOf(typeStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			return Type.SLOW;
		}
	}

	public Type getType() {
		return type;
	}

	public Type getEffectType() {
		return type;
	}

	public float getDuration() {
		return duration;
	}

	public float getPotency() {
		return potency;
	}

	public float getTickInterval() {
		return tickInterval;
	}

	@Override
	public String toString() {
		return "StatusEffectPayload{" +
				"type=" + type +
				", duration=" + duration +
				", potency=" + potency +
				", tickInterval=" + tickInterval +
				'}';
	}
}
