package com.hongbao.bloons;

public enum MapType {
	BASIC("basic", "Basic Map", "basic_map.png", "A simple straight path for beginners."),
	TURN("turn", "Map With Turn", "map_with_turn.png", "A winding path with a 90-degree turn."),
	HEATER("heater", "Heater Map", "heater.png", "A complex multi-turn heater coil map.");

	private final String id;
	private final String displayName;
	private final String imageFileName;
	private final String description;

	MapType(String id, String displayName, String imageFileName, String description) {
		this.id = id;
		this.displayName = displayName;
		this.imageFileName = imageFileName;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public String getDescription() {
		return description;
	}

	public static MapType fromId(String id) {
		if (id != null) {
			for (MapType type : values()) {
				if (type.id.equalsIgnoreCase(id)) {
					return type;
				}
			}
		}
		return BASIC;
	}
}
