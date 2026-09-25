package com.hongbao.bloons.entities;

import java.util.Objects;

public class StatusEffect {

	private String name;
	private float speedMultiplier;
	private float duration;

	public StatusEffect() {
		this.speedMultiplier = 1.0f;
		this.duration = -1.0f;
	}

	public StatusEffect(String name, float speedMultiplier, float duration) {
		this.name = name;
		this.speedMultiplier = speedMultiplier;
		this.duration = duration;
	}

	public StatusEffect(String name, float speedMultiplier) {
		this(name, speedMultiplier, -1.0f);
	}

	public StatusEffect(StatusEffect other) {
		if (other != null) {
			this.name = other.name;
			this.speedMultiplier = other.speedMultiplier;
			this.duration = other.duration;
		} else {
			this.speedMultiplier = 1.0f;
			this.duration = -1.0f;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

	public void setSpeedMultiplier(float speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public void update(float delta) {
		if (duration > 0) {
			duration -= delta;
			if (duration < 0) {
				duration = 0;
			}
		}
	}

	public boolean isExpired() {
		return duration == 0;
	}

	public StatusEffect copy() {
		return new StatusEffect(this.name, this.speedMultiplier, this.duration);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StatusEffect that = (StatusEffect) o;
		return Float.compare(that.speedMultiplier, speedMultiplier) == 0 &&
				Float.compare(that.duration, duration) == 0 &&
				Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, speedMultiplier, duration);
	}

	@Override
	public String toString() {
		return "StatusEffect{" +
				"name='" + name + '\'' +
				", speedMultiplier=" + speedMultiplier +
				", duration=" + duration +
				'}';
	}
}
