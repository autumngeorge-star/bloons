package com.hongbao.bloons;

import java.util.Objects;

public class BloonSpawnSpec {

	private final String type;

	public BloonSpawnSpec(String type) {
		if (type != null) {
			type = type.trim();
		}
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BloonSpawnSpec spec = (BloonSpawnSpec) o;
		return Objects.equals(type, spec.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}

	@Override
	public String toString() {
		return "BloonSpawnSpec{" +
				"type='" + type + '\'' +
				'}';
	}
}
