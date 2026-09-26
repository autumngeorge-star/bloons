package com.hongbao.bloons.factories;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.helpers.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFactory {

	@FunctionalInterface
	public interface MapCreator {
		Map create(Stage stage);
	}
	
	public static final float ROOT_2_OVER_2 = 0.7071f;
	public static final String DEFAULT_MAP_KEY = "heater";

	private static final java.util.Map<String, MapCreator> registry = new HashMap<>();
	private static final java.util.Map<String, String> displayNames = new HashMap<>();
	private static final List<String> registeredKeys = new ArrayList<>();

	static {
		registerMap("basic", "Basic Map", MapFactory::createBasicMap);
		registerMap("map_with_turn", "Map With Turn", MapFactory::createMapWithTurn);
		registerMap("heater", "Heater Map", MapFactory::createHeaterMap);
	}

	public static void registerMap(String key, String displayName, MapCreator creator) {
		if (key == null || creator == null) return;
		String normalizedKey = key.toLowerCase().trim();
		if (!registry.containsKey(normalizedKey)) {
			registeredKeys.add(normalizedKey);
		}
		registry.put(normalizedKey, creator);
		displayNames.put(normalizedKey, displayName != null ? displayName : normalizedKey);
	}

	public static Map createMap(String key, Stage stage) {
		if (key == null || key.trim().isEmpty()) {
			key = DEFAULT_MAP_KEY;
		}
		String normalizedKey = key.toLowerCase().trim();
		if (normalizedKey.equals("basic_map")) {
			normalizedKey = "basic";
		}
		MapCreator creator = registry.get(normalizedKey);
		if (creator == null) {
			creator = registry.get(DEFAULT_MAP_KEY);
			normalizedKey = DEFAULT_MAP_KEY;
		}
		Map map = creator.create(stage);
		map.setKey(normalizedKey);
		return map;
	}

	public static List<String> getRegisteredMapKeys() {
		return new ArrayList<>(registeredKeys);
	}

	public static String getMapDisplayName(String key) {
		if (key == null) return "";
		String normalizedKey = key.toLowerCase().trim();
		if (normalizedKey.equals("basic_map")) normalizedKey = "basic";
		return displayNames.getOrDefault(normalizedKey, key);
	}
	
	public static Map createBasicMap(Stage stage) {
		Map map = new Map("basic_map.png", stage);
		map.setKey("basic");
		Pair<Float, Float>[][] directions = initializeEmptyDirections();
		
		for (int x = 0; x < 32; x++) {
			directions[x][8] = new Pair<>(1f, 0f);
		}
		
		map.setDirections(directions);
		return map;
	}
	
	public static Map createMapWithTurn(Stage stage) {
		Map map = new Map("map_with_turn.png", stage);
		map.setKey("map_with_turn");
		Pair<Float, Float>[][] directions = initializeEmptyDirections();
		directions[0][8] = new Pair<>(1f, 0f);
		directions[1][8] = new Pair<>(1f, 0f);
		directions[2][8] = new Pair<>(1f, 0f);
		directions[3][8] = new Pair<>(1f, 0f);
		directions[4][8] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[4][9] = new Pair<>(0f, 1f);
		directions[4][10] = new Pair<>(0f, 1f);
		directions[4][11] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		
		for (int x = 5; x < 32; x++) {
			directions[x][11] = new Pair<>(1f, 0f);
		}
		
		map.setDirections(directions);
		return map;
	}
	
	public static Map createHeaterMap(Stage stage) {
		Map map = new Map("heater.png", stage);
		map.setKey("heater");
		Pair<Float, Float>[][] directions = initializeEmptyDirections();
		
		directions[0][8] = new Pair<>(1f, 0f);
		directions[1][8] = new Pair<>(1f, 0f);
		directions[2][8] = new Pair<>(1f, 0f);
		directions[3][8] = new Pair<>(1f, 0f);
		directions[4][8] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[4][9] = new Pair<>(0f, 1f);
		directions[4][10] = new Pair<>(0f, 1f);
		directions[4][11] = new Pair<>(0f, 1f);
		directions[4][12] = new Pair<>(0f, 1f);
		directions[4][13] = new Pair<>(0f, 1f);
		directions[4][14] = new Pair<>(0f, 1f);
		directions[4][15] = new Pair<>(0f, 1f);
		directions[4][16] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[5][16] = new Pair<>(1f, 0f);
		directions[6][16] = new Pair<>(1f, 0f);
		directions[7][16] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[7][15] = new Pair<>(0f, -1f);
		directions[7][14] = new Pair<>(0f, -1f);
		directions[7][13] = new Pair<>(0f, -1f);
		directions[7][12] = new Pair<>(0f, -1f);
		directions[7][11] = new Pair<>(0f, -1f);
		directions[7][10] = new Pair<>(0f, -1f);
		directions[7][9] = new Pair<>(0f, -1f);
		directions[7][8] = new Pair<>(0f, -1f);
		directions[7][7] = new Pair<>(0f, -1f);
		directions[7][6] = new Pair<>(0f, -1f);
		directions[7][5] = new Pair<>(0f, -1f);
		directions[7][4] = new Pair<>(0f, -1f);
		directions[7][3] = new Pair<>(0f, -1f);
		directions[7][2] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[8][2] = new Pair<>(1f, 0f);
		directions[9][2] = new Pair<>(1f, 0f);
		directions[10][2] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[10][3] = new Pair<>(0f, 1f);
		directions[10][4] = new Pair<>(0f, 1f);
		directions[10][5] = new Pair<>(0f, 1f);
		directions[10][6] = new Pair<>(0f, 1f);
		directions[10][7] = new Pair<>(0f, 1f);
		directions[10][8] = new Pair<>(0f, 1f);
		directions[10][9] = new Pair<>(0f, 1f);
		directions[10][10] = new Pair<>(0f, 1f);
		directions[10][11] = new Pair<>(0f, 1f);
		directions[10][12] = new Pair<>(0f, 1f);
		directions[10][13] = new Pair<>(0f, 1f);
		directions[10][14] = new Pair<>(0f, 1f);
		directions[10][15] = new Pair<>(0f, 1f);
		directions[10][16] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[11][16] = new Pair<>(1f, 0f);
		directions[12][16] = new Pair<>(1f, 0f);
		directions[13][16] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[13][15] = new Pair<>(0f, -1f);
		directions[13][14] = new Pair<>(0f, -1f);
		directions[13][13] = new Pair<>(0f, -1f);
		directions[13][12] = new Pair<>(0f, -1f);
		directions[13][11] = new Pair<>(0f, -1f);
		directions[13][10] = new Pair<>(0f, -1f);
		directions[13][9] = new Pair<>(0f, -1f);
		directions[13][8] = new Pair<>(0f, -1f);
		directions[13][7] = new Pair<>(0f, -1f);
		directions[13][6] = new Pair<>(0f, -1f);
		directions[13][5] = new Pair<>(0f, -1f);
		directions[13][4] = new Pair<>(0f, -1f);
		directions[13][3] = new Pair<>(0f, -1f);
		directions[13][2] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[14][2] = new Pair<>(1f, 0f);
		directions[15][2] = new Pair<>(1f, 0f);
		directions[16][2] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[16][3] = new Pair<>(0f, 1f);
		directions[16][4] = new Pair<>(0f, 1f);
		directions[16][5] = new Pair<>(0f, 1f);
		directions[16][6] = new Pair<>(0f, 1f);
		directions[16][7] = new Pair<>(0f, 1f);
		directions[16][8] = new Pair<>(0f, 1f);
		directions[16][9] = new Pair<>(0f, 1f);
		directions[16][10] = new Pair<>(0f, 1f);
		directions[16][11] = new Pair<>(0f, 1f);
		directions[16][12] = new Pair<>(0f, 1f);
		directions[16][13] = new Pair<>(0f, 1f);
		directions[16][14] = new Pair<>(0f, 1f);
		directions[16][15] = new Pair<>(0f, 1f);
		directions[16][16] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[17][16] = new Pair<>(1f, 0f);
		directions[18][16] = new Pair<>(1f, 0f);
		directions[19][16] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[19][15] = new Pair<>(0f, -1f);
		directions[19][14] = new Pair<>(0f, -1f);
		directions[19][13] = new Pair<>(0f, -1f);
		directions[19][12] = new Pair<>(0f, -1f);
		directions[19][11] = new Pair<>(0f, -1f);
		directions[19][10] = new Pair<>(0f, -1f);
		directions[19][9] = new Pair<>(0f, -1f);
		directions[19][8] = new Pair<>(0f, -1f);
		directions[19][7] = new Pair<>(0f, -1f);
		directions[19][6] = new Pair<>(0f, -1f);
		directions[19][5] = new Pair<>(0f, -1f);
		directions[19][4] = new Pair<>(0f, -1f);
		directions[19][3] = new Pair<>(0f, -1f);
		directions[19][2] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[20][2] = new Pair<>(1f, 0f);
		directions[21][2] = new Pair<>(1f, 0f);
		directions[22][2] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[22][3] = new Pair<>(0f, 1f);
		directions[22][4] = new Pair<>(0f, 1f);
		directions[22][5] = new Pair<>(0f, 1f);
		directions[22][6] = new Pair<>(0f, 1f);
		directions[22][7] = new Pair<>(0f, 1f);
		directions[22][8] = new Pair<>(0f, 1f);
		directions[22][9] = new Pair<>(0f, 1f);
		directions[22][10] = new Pair<>(0f, 1f);
		directions[22][11] = new Pair<>(0f, 1f);
		directions[22][12] = new Pair<>(0f, 1f);
		directions[22][13] = new Pair<>(0f, 1f);
		directions[22][14] = new Pair<>(0f, 1f);
		directions[22][15] = new Pair<>(0f, 1f);
		directions[22][16] = new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2);
		directions[23][16] = new Pair<>(1f, 0f);
		directions[24][16] = new Pair<>(1f, 0f);
		directions[25][16] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[25][15] = new Pair<>(0f, -1f);
		directions[25][14] = new Pair<>(0f, -1f);
		directions[25][13] = new Pair<>(0f, -1f);
		directions[25][12] = new Pair<>(0f, -1f);
		directions[25][11] = new Pair<>(0f, -1f);
		directions[25][10] = new Pair<>(0f, -1f);
		directions[25][9] = new Pair<>(0f, -1f);
		directions[25][8] = new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2);
		directions[26][8] = new Pair<>(1f, 0f);
		directions[27][8] = new Pair<>(1f, 0f);
		directions[28][8] = new Pair<>(1f, 0f);
		directions[29][8] = new Pair<>(1f, 0f);
		directions[30][8] = new Pair<>(1f, 0f);
		directions[31][8] = new Pair<>(1f, 0f);
		
		map.setDirections(directions);
		return map;
	}
	
	
	private static Pair<Float, Float>[][] initializeEmptyDirections() {
		Pair<Float, Float>[][] directions = new Pair[32][18];
		for (int x = 0; x < directions.length - 1; x++) {
			for (int y = 0; y < directions[x].length; y++) {
				directions[x][y] = new Pair<>(0f, 0f);
			}
		}
		
		
		return directions;
	}
}
