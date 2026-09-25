package com.hongbao.bloons.factories;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.MapGrid;
import com.hongbao.bloons.helpers.Pair;


public class MapFactory {
	
	public static final float ROOT_2_OVER_2 = 0.7071f;
	
	public static MapGrid createBasicMapGrid() {
		MapGrid mapGrid = new MapGrid();
		
		for (int x = 0; x < MapGrid.COLUMNS; x++) {
			mapGrid.setDirection(x, 8, new Pair<>(1f, 0f));
		}
		
		return mapGrid;
	}

	public static Map createBasicMap(Stage stage) {
		Map map = new Map("basic_map.png", stage);
		MapGrid mapGrid = createBasicMapGrid();
		map.setMapGrid(mapGrid);
		return map;
	}
	
	public static MapGrid createMapWithTurnGrid() {
		MapGrid mapGrid = new MapGrid();
		mapGrid.setDirection(0, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(1, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(2, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(3, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(4, 8, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(4, 9, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 10, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 11, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		
		for (int x = 5; x < MapGrid.COLUMNS; x++) {
			mapGrid.setDirection(x, 11, new Pair<>(1f, 0f));
		}
		
		return mapGrid;
	}

	public static Map createMapWithTurn(Stage stage) {
		Map map = new Map("map_with_turn.png", stage);
		MapGrid mapGrid = createMapWithTurnGrid();
		map.setMapGrid(mapGrid);
		return map;
	}
	
	public static MapGrid createHeaterMapGrid() {
		MapGrid mapGrid = new MapGrid();
		
		mapGrid.setDirection(0, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(1, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(2, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(3, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(4, 8, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(4, 9, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 10, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 11, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 12, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 13, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 14, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 15, new Pair<>(0f, 1f));
		mapGrid.setDirection(4, 16, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(5, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(6, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(7, 16, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(7, 15, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 14, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 13, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 12, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 11, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 10, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 9, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 8, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 7, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 6, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 5, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 4, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 3, new Pair<>(0f, -1f));
		mapGrid.setDirection(7, 2, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(8, 2, new Pair<>(1f, 0f));
		mapGrid.setDirection(9, 2, new Pair<>(1f, 0f));
		mapGrid.setDirection(10, 2, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(10, 3, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 4, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 5, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 6, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 7, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 8, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 9, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 10, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 11, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 12, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 13, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 14, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 15, new Pair<>(0f, 1f));
		mapGrid.setDirection(10, 16, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(11, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(12, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(13, 16, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(13, 15, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 14, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 13, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 12, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 11, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 10, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 9, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 8, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 7, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 6, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 5, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 4, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 3, new Pair<>(0f, -1f));
		mapGrid.setDirection(13, 2, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(14, 2, new Pair<>(1f, 0f));
		mapGrid.setDirection(15, 2, new Pair<>(1f, 0f));
		mapGrid.setDirection(16, 2, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(16, 3, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 4, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 5, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 6, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 7, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 8, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 9, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 10, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 11, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 12, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 13, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 14, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 15, new Pair<>(0f, 1f));
		mapGrid.setDirection(16, 16, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(17, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(18, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(19, 16, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(19, 15, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 14, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 13, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 12, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 11, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 10, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 9, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 8, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 7, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 6, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 5, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 4, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 3, new Pair<>(0f, -1f));
		mapGrid.setDirection(19, 2, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(20, 2, new Pair<>(1f, 0f));
		mapGrid.setDirection(21, 2, new Pair<>(1f, 0f));
		mapGrid.setDirection(22, 2, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(22, 3, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 4, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 5, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 6, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 7, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 8, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 9, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 10, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 11, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 12, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 13, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 14, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 15, new Pair<>(0f, 1f));
		mapGrid.setDirection(22, 16, new Pair<>(ROOT_2_OVER_2, ROOT_2_OVER_2));
		mapGrid.setDirection(23, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(24, 16, new Pair<>(1f, 0f));
		mapGrid.setDirection(25, 16, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(25, 15, new Pair<>(0f, -1f));
		mapGrid.setDirection(25, 14, new Pair<>(0f, -1f));
		mapGrid.setDirection(25, 13, new Pair<>(0f, -1f));
		mapGrid.setDirection(25, 12, new Pair<>(0f, -1f));
		mapGrid.setDirection(25, 11, new Pair<>(0f, -1f));
		mapGrid.setDirection(25, 10, new Pair<>(0f, -1f));
		mapGrid.setDirection(25, 9, new Pair<>(0f, -1f));
		mapGrid.setDirection(25, 8, new Pair<>(ROOT_2_OVER_2, -ROOT_2_OVER_2));
		mapGrid.setDirection(26, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(27, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(28, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(29, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(30, 8, new Pair<>(1f, 0f));
		mapGrid.setDirection(31, 8, new Pair<>(1f, 0f));
		
		return mapGrid;
	}

	public static Map createHeaterMap(Stage stage) {
		Map map = new Map("heater.png", stage);
		MapGrid mapGrid = createHeaterMapGrid();
		map.setMapGrid(mapGrid);
		return map;
	}
}
