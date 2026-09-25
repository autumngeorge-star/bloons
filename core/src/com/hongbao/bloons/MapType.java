package com.hongbao.bloons;

public enum MapType {
	BASIC_MAP("Basic Map", "basic_map.png", 1, null, "Unlocked by default"),
	MAP_WITH_TURN("Map with Turn", "map_with_turn.png", 2, BASIC_MAP, "Clear Basic Map to unlock"),
	HEATER_MAP("Heater Map", "heater.png", 3, MAP_WITH_TURN, "Clear Map with Turn to unlock");

	private final String displayName;
	private final String imageFileName;
	private final int mapIndex;
	private final MapType prerequisiteMap;
	private final String unlockRequirementText;

	MapType(String displayName, String imageFileName, int mapIndex, MapType prerequisiteMap, String unlockRequirementText) {
		this.displayName = displayName;
		this.imageFileName = imageFileName;
		this.mapIndex = mapIndex;
		this.prerequisiteMap = prerequisiteMap;
		this.unlockRequirementText = unlockRequirementText;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public int getMapIndex() {
		return mapIndex;
	}

	public MapType getPrerequisiteMap() {
		return prerequisiteMap;
	}

	public String getUnlockRequirementText() {
		return unlockRequirementText;
	}

	public MapType getNextMap() {
		switch (this) {
			case BASIC_MAP:
				return MAP_WITH_TURN;
			case MAP_WITH_TURN:
				return HEATER_MAP;
			case HEATER_MAP:
			default:
				return null;
		}
	}
}
