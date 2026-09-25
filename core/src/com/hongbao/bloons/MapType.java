package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.factories.MapFactory;

public enum MapType {
    BASIC_MAP(0, "Basic Map", "img/maps/basic_map.png"),
    MAP_WITH_TURN(1, "Map with Turn", "img/maps/map_with_turn.png"),
    HEATER(2, "Heater Map", "img/maps/heater.png");

    private final int index;
    private final String displayName;
    private final String imagePath;

    MapType(int index, String displayName, String imagePath) {
        this.index = index;
        this.displayName = displayName;
        this.imagePath = imagePath;
    }

    public int getIndex() {
        return index;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImagePath() {
        return imagePath;
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

    public static MapType getByIndex(int index) {
        for (MapType type : values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return BASIC_MAP;
    }
}
