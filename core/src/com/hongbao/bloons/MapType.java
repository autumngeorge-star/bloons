package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.factories.MapFactory;

public enum MapType {

	BASIC_MAP("Basic Map", "basic_map.png", null),
	MAP_WITH_TURN("Map with Turn", "map_with_turn.png", BASIC_MAP),
	HEATER("Heater Map", "heater.png", MAP_WITH_TURN);

	private final String displayName;
	private final String imageFileName;
	private final MapType requiredMap;

	MapType(String displayName, String imageFileName, MapType requiredMap) {
		this.displayName = displayName;
		this.imageFileName = imageFileName;
		this.requiredMap = requiredMap;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public String getImagePath() {
		return Map.BACKGROUND_MAPS_FOLDER + imageFileName;
	}

	public MapType getRequiredMap() {
		return requiredMap;
	}

	public Map createMap(Stage stage) {
		switch (this) {
			case BASIC_MAP:
				return MapFactory.createBasicMap(stage);
			case MAP_WITH_TURN:
				return MapFactory.createMapWithTurn(stage);
			case HEATER:
				return MapFactory.createHeaterMap(stage);
			default:
				return MapFactory.createBasicMap(stage);
		}
	}
}
