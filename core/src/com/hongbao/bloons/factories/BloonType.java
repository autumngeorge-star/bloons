package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Bloon;

import java.util.HashMap;
import java.util.Map;

public enum BloonType {

	RED("red", Bloon.Color.RED, 1, false, false),
	RED_CAMO("red_camo", Bloon.Color.RED, 1, true, false),
	RED_REGEN("red_regen", Bloon.Color.RED, 1, false, true),
	RED_CAMO_REGEN("red_camo_regen", Bloon.Color.RED, 1, true, true),

	BLUE("blue", Bloon.Color.BLUE, 2, false, false),
	BLUE_CAMO("blue_camo", Bloon.Color.BLUE, 2, true, false),
	BLUE_REGEN("blue_regen", Bloon.Color.BLUE, 2, false, true),
	BLUE_CAMO_REGEN("blue_camo_regen", Bloon.Color.BLUE, 2, true, true),

	GREEN("green", Bloon.Color.GREEN, 3, false, false),
	GREEN_CAMO("green_camo", Bloon.Color.GREEN, 3, true, false),
	GREEN_REGEN("green_regen", Bloon.Color.GREEN, 3, false, true),
	GREEN_CAMO_REGEN("green_camo_regen", Bloon.Color.GREEN, 3, true, true),

	YELLOW("yellow", Bloon.Color.YELLOW, 4, false, false),
	YELLOW_CAMO("yellow_camo", Bloon.Color.YELLOW, 4, true, false),
	YELLOW_REGEN("yellow_regen", Bloon.Color.YELLOW, 4, false, true),
	YELLOW_CAMO_REGEN("yellow_camo_regen", Bloon.Color.YELLOW, 4, true, true),

	PINK("pink", Bloon.Color.PINK, 5, false, false),
	PINK_CAMO("pink_camo", Bloon.Color.PINK, 5, true, false),
	PINK_REGEN("pink_regen", Bloon.Color.PINK, 5, false, true),
	PINK_CAMO_REGEN("pink_camo_regen", Bloon.Color.PINK, 5, true, true),

	BLACK("black", Bloon.Color.BLACK, 6, false, false),
	BLACK_CAMO("black_camo", Bloon.Color.BLACK, 6, true, false),
	BLACK_REGEN("black_regen", Bloon.Color.BLACK, 6, false, true),
	BLACK_CAMO_REGEN("black_camo_regen", Bloon.Color.BLACK, 6, true, true),

	WHITE("white", Bloon.Color.WHITE, 6, false, false),
	WHITE_CAMO("white_camo", Bloon.Color.WHITE, 6, true, false),
	WHITE_REGEN("white_regen", Bloon.Color.WHITE, 6, false, true),
	WHITE_CAMO_REGEN("white_camo_regen", Bloon.Color.WHITE, 6, true, true),

	LEAD("lead", Bloon.Color.LEAD, 7, false, false),
	LEAD_CAMO("lead_camo", Bloon.Color.LEAD, 7, true, false),
	LEAD_REGEN("lead_regen", Bloon.Color.LEAD, 7, false, true),
	LEAD_CAMO_REGEN("lead_camo_regen", Bloon.Color.LEAD, 7, true, true),

	ZEBRA("zebra", Bloon.Color.ZEBRA, 7, false, false),
	ZEBRA_CAMO("zebra_camo", Bloon.Color.ZEBRA, 7, true, false),
	ZEBRA_REGEN("zebra_regen", Bloon.Color.ZEBRA, 7, false, true),
	ZEBRA_CAMO_REGEN("zebra_camo_regen", Bloon.Color.ZEBRA, 7, true, true),

	RAINBOW("rainbow", Bloon.Color.RAINBOW, 8, false, false),
	RAINBOW_CAMO("rainbow_camo", Bloon.Color.RAINBOW, 8, true, false),
	RAINBOW_REGEN("rainbow_regen", Bloon.Color.RAINBOW, 8, false, true),
	RAINBOW_CAMO_REGEN("rainbow_camo_regen", Bloon.Color.RAINBOW, 8, true, true),

	CERAMIC("ceramic", Bloon.Color.CERAMIC, 18, false, false),
	CERAMIC_CAMO("ceramic_camo", Bloon.Color.CERAMIC, 18, true, false),
	CERAMIC_REGEN("ceramic_regen", Bloon.Color.CERAMIC, 18, false, true),
	CERAMIC_CAMO_REGEN("ceramic_camo_regen", Bloon.Color.CERAMIC, 18, true, true),

	MOAB("moab", Bloon.Color.MOAB, 218, false, false),
	BFB("bfb", Bloon.Color.BFB, 918, false, false),
	ZOMG("zomg", Bloon.Color.ZOMG, 4918, false, false);

	private final String key;
	private final Bloon.Color color;
	private final int health;
	private final boolean camo;
	private final boolean regen;

	BloonType(String key, Bloon.Color color, int health, boolean camo, boolean regen) {
		this.key = key;
		this.color = color;
		this.health = health;
		this.camo = camo;
		this.regen = regen;
	}

	public String getKey() {
		return key;
	}

	public Bloon.Color getColor() {
		return color;
	}

	public int getHealth() {
		return health;
	}

	public boolean isCamo() {
		return camo;
	}

	public boolean isRegen() {
		return regen;
	}

	public Bloon createBloon() {
		return new Bloon(color, health, camo, regen);
	}

	private static final Map<String, BloonType> LOOKUP = new HashMap<>();

	static {
		for (BloonType type : BloonType.values()) {
			LOOKUP.put(type.key, type);
			if (type.key.contains("_regen")) {
				LOOKUP.put(type.key.replace("_regen", "_regrowth"), type);
			}
		}
	}

	public static BloonType fromKey(String rawKey) {
		if (rawKey == null) {
			return null;
		}
		String key = rawKey.trim();
		if (key.endsWith("\r")) {
			key = key.substring(0, key.length() - 1).trim();
		}
		key = key.toLowerCase();
		return LOOKUP.get(key);
	}

}
