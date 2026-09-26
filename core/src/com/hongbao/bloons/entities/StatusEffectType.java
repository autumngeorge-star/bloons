package com.hongbao.bloons.entities;

public enum StatusEffectType {
	FREEZE("freeze"),
	GLUE("glue"),
	BURN("burn"),
	SLOW("slow"),
	STUN("stun");

	private final String name;

	StatusEffectType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
