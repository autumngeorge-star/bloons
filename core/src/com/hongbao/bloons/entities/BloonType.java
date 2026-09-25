package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.BloonFactory;

import java.util.HashMap;
import java.util.Map;

public enum BloonType {

	RED("red"),
	RED_CAMO("red_camo"),
	RED_REGEN("red_regen"),
	RED_CAMO_REGEN("red_camo_regen"),

	BLUE("blue"),
	BLUE_CAMO("blue_camo"),
	BLUE_REGEN("blue_regen"),
	BLUE_CAMO_REGEN("blue_camo_regen"),

	GREEN("green"),
	GREEN_CAMO("green_camo"),
	GREEN_REGEN("green_regen"),
	GREEN_CAMO_REGEN("green_camo_regen"),

	YELLOW("yellow"),
	YELLOW_CAMO("yellow_camo"),
	YELLOW_REGEN("yellow_regen"),
	YELLOW_CAMO_REGEN("yellow_camo_regen"),

	PINK("pink"),
	PINK_CAMO("pink_camo"),
	PINK_REGEN("pink_regen"),
	PINK_CAMO_REGEN("pink_camo_regen"),

	BLACK("black"),
	BLACK_CAMO("black_camo"),
	BLACK_REGEN("black_regen"),
	BLACK_CAMO_REGEN("black_camo_regen"),

	LEAD("lead"),
	LEAD_CAMO("lead_camo"),
	LEAD_REGEN("lead_regen"),
	LEAD_CAMO_REGEN("lead_camo_regen"),

	ZEBRA("zebra"),
	ZEBRA_CAMO("zebra_camo"),
	ZEBRA_REGEN("zebra_regen"),
	ZEBRA_CAMO_REGEN("zebra_camo_regen"),

	RAINBOW("rainbow"),
	RAINBOW_CAMO("rainbow_camo"),
	RAINBOW_REGEN("rainbow_regen"),
	RAINBOW_CAMO_REGEN("rainbow_camo_regen"),

	CERAMIC("ceramic"),
	CERAMIC_CAMO("ceramic_camo"),
	CERAMIC_REGEN("ceramic_regen"),
	CERAMIC_CAMO_REGEN("ceramic_camo_regen"),

	MOAB("moab"),
	BFB("bfb"),
	ZOMG("zomg");

	private final String id;

	BloonType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	private static final Map<String, BloonType> BY_ID = new HashMap<>();

	static {
		for (BloonType type : values()) {
			BY_ID.put(type.id.toLowerCase(), type);
		}
	}

	public static BloonType fromString(String typeStr) {
		if (typeStr == null) {
			throw new IllegalArgumentException("Bloon type string cannot be null");
		}
		String clean = typeStr.trim().toLowerCase();
		if (clean.endsWith("\r")) {
			clean = clean.substring(0, clean.length() - 1);
		}
		BloonType type = BY_ID.get(clean);
		if (type == null) {
			throw new IllegalArgumentException("Unknown bloon type: '" + typeStr + "'");
		}
		return type;
	}

	public Bloon createBloon() {
		switch (this) {
			case RED: return BloonFactory.createRedBloon();
			case RED_CAMO: return BloonFactory.createRedCamoBloon();
			case RED_REGEN: return BloonFactory.createRedRegenBloon();
			case RED_CAMO_REGEN: return BloonFactory.createRedCamoRegenBloon();

			case BLUE: return BloonFactory.createBlueBloon();
			case BLUE_CAMO: return BloonFactory.createBlueCamoBloon();
			case BLUE_REGEN: return BloonFactory.createBlueRegenBloon();
			case BLUE_CAMO_REGEN: return BloonFactory.createBlueCamoRegenBloon();

			case GREEN: return BloonFactory.createGreenBloon();
			case GREEN_CAMO: return BloonFactory.createGreenCamoBloon();
			case GREEN_REGEN: return BloonFactory.createGreenRegenBloon();
			case GREEN_CAMO_REGEN: return BloonFactory.createGreenCamoRegenBloon();

			case YELLOW: return BloonFactory.createYellowBloon();
			case YELLOW_CAMO: return BloonFactory.createYellowCamoBloon();
			case YELLOW_REGEN: return BloonFactory.createYellowRegenBloon();
			case YELLOW_CAMO_REGEN: return BloonFactory.createYellowCamoRegenBloon();

			case PINK: return BloonFactory.createPinkBloon();
			case PINK_CAMO: return BloonFactory.createPinkCamoBloon();
			case PINK_REGEN: return BloonFactory.createPinkRegenBloon();
			case PINK_CAMO_REGEN: return BloonFactory.createPinkCamoRegenBloon();

			case BLACK: return BloonFactory.createBlackBloon();
			case BLACK_CAMO: return BloonFactory.createBlackCamoBloon();
			case BLACK_REGEN: return BloonFactory.createBlackRegenBloon();
			case BLACK_CAMO_REGEN: return BloonFactory.createBlackCamoRegenBloon();

			case LEAD: return BloonFactory.createLeadBloon();
			case LEAD_CAMO: return BloonFactory.createLeadCamoBloon();
			case LEAD_REGEN: return BloonFactory.createLeadRegenBloon();
			case LEAD_CAMO_REGEN: return BloonFactory.createLeadCamoRegenBloon();

			case ZEBRA: return BloonFactory.createZebraBloon();
			case ZEBRA_CAMO: return BloonFactory.createZebraCamoBloon();
			case ZEBRA_REGEN: return BloonFactory.createZebraRegenBloon();
			case ZEBRA_CAMO_REGEN: return BloonFactory.createZebraCamoRegenBloon();

			case RAINBOW: return BloonFactory.createRainbowBloon();
			case RAINBOW_CAMO: return BloonFactory.createRainbowCamoBloon();
			case RAINBOW_REGEN: return BloonFactory.createRainbowRegenBloon();
			case RAINBOW_CAMO_REGEN: return BloonFactory.createRainbowCamoRegenBloon();

			case CERAMIC: return BloonFactory.createCeramicBloon();
			case CERAMIC_CAMO: return BloonFactory.createCeramicCamoBloon();
			case CERAMIC_REGEN: return BloonFactory.createCeramicRegenBloon();
			case CERAMIC_CAMO_REGEN: return BloonFactory.createCeramicCamoRegenBloon();

			case MOAB: return BloonFactory.createMOAB();
			case BFB: return BloonFactory.createBFB();
			case ZOMG: return BloonFactory.createZOMG();

			default:
				throw new IllegalArgumentException("Unhandled bloon type enum: " + this);
		}
	}

}
