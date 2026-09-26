package com.hongbao.bloons.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.Map;

import java.util.Arrays;


public class MapFactory {
	
	public static Map createBasicMap(Stage stage) {
		Map map = new Map("basic_map.png", stage);
		map.setWaypoints(Arrays.asList(
			new Vector2(-25f, 425f),
			new Vector2(1600f, 425f)
		));
		return map;
	}
	
	public static Map createMapWithTurn(Stage stage) {
		Map map = new Map("map_with_turn.png", stage);
		map.setWaypoints(Arrays.asList(
			new Vector2(-25f, 425f),
			new Vector2(225f, 425f),
			new Vector2(225f, 575f),
			new Vector2(1600f, 575f)
		));
		return map;
	}
	
	public static Map createHeaterMap(Stage stage) {
		Map map = new Map("heater.png", stage);
		map.setWaypoints(Arrays.asList(
			new Vector2(-25f, 425f),
			new Vector2(225f, 425f),
			new Vector2(225f, 825f),
			new Vector2(375f, 825f),
			new Vector2(375f, 125f),
			new Vector2(525f, 125f),
			new Vector2(525f, 825f),
			new Vector2(675f, 825f),
			new Vector2(675f, 125f),
			new Vector2(825f, 125f),
			new Vector2(825f, 825f),
			new Vector2(975f, 825f),
			new Vector2(975f, 125f),
			new Vector2(1125f, 125f),
			new Vector2(1125f, 825f),
			new Vector2(1275f, 825f),
			new Vector2(1275f, 425f),
			new Vector2(1600f, 425f)
		));
		return map;
	}
}
