package com.hongbao.bloons.helpers;

import java.util.Objects;

public class BloonStatusEffect {

	private String name;
	private float speedMultiplier;
	private float duration;

	public BloonStatusEffect(String name, float speedMultiplier, float duration) {
		this.name = name;
		this.speedMultiplier = speedMultiplier;
		this.duration = duration;
	}

	public BloonStatusEffect(BloonStatusEffect other) {
		if (other != null) {
			this.name = other.name;
			this.speedMultiplier = other.speedMultiplier;
			this.duration = other.duration;
		}
	}

	public BloonStatusEffect copy() {
		return new BloonStatusEffect(this);
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
		this.duration -= delta;
	}

	public boolean isExpired() {
		return this.duration <= 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BloonStatusEffect that = (BloonStatusEffect) o;
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
		return "BloonStatusEffect{" +
				"name='" + name + '\'' +
				", speedMultiplier=" + speedMultiplier +
				", duration=" + duration +
				'}';
	}
}
