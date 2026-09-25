package com.hongbao.bloons;

import com.hongbao.bloons.helpers.Pair;

public class MapGrid {

	public static final int COLUMNS = 32;
	public static final int ROWS = 18;
	public static final int TILE_LENGTH = 50;
	public static final int TILE_HEIGHT = 50;

	private final Pair<Float, Float>[][] directions;

	@SuppressWarnings("unchecked")
	public MapGrid() {
		this.directions = (Pair<Float, Float>[][]) new Pair[COLUMNS][ROWS];
		for (int x = 0; x < COLUMNS; x++) {
			for (int y = 0; y < ROWS; y++) {
				this.directions[x][y] = new Pair<>(0f, 0f);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public MapGrid(Pair<Float, Float>[][] directions) {
		this.directions = (Pair<Float, Float>[][]) new Pair[COLUMNS][ROWS];
		for (int x = 0; x < COLUMNS; x++) {
			for (int y = 0; y < ROWS; y++) {
				if (directions != null && x < directions.length && directions[x] != null && y < directions[x].length && directions[x][y] != null) {
					this.directions[x][y] = directions[x][y];
				} else {
					this.directions[x][y] = new Pair<>(0f, 0f);
				}
			}
		}
	}

	public Pair<Float, Float> getDirection(float balloonX, float balloonY) {
		int xTile = getXTile(balloonX);
		int yTile = getYTile(balloonY);
		return getDirectionTile(xTile, yTile);
	}

	public Pair<Float, Float> getDirectionTile(int xTile, int yTile) {
		if (xTile < 0 || xTile >= COLUMNS || yTile < 0 || yTile >= ROWS) {
			return new Pair<>(0f, 0f);
		}
		Pair<Float, Float> dir = directions[xTile][yTile];
		return dir != null ? dir : new Pair<>(0f, 0f);
	}

	public void setDirection(int xTile, int yTile, Pair<Float, Float> direction) {
		if (xTile >= 0 && xTile < COLUMNS && yTile >= 0 && yTile < ROWS) {
			directions[xTile][yTile] = (direction != null) ? direction : new Pair<>(0f, 0f);
		}
	}

	public int getXTile(float x) {
		return (int) Math.floor((x + TILE_LENGTH) / (double) TILE_LENGTH);
	}

	public int getYTile(float y) {
		return (int) Math.floor(y / (double) TILE_HEIGHT);
	}

	public static float getCenterXOfTile(int tile) {
		return tile * TILE_LENGTH + (TILE_LENGTH / 2f);
	}

	public static float getCenterYOfTile(int tile) {
		return tile * TILE_HEIGHT + (TILE_HEIGHT / 2f);
	}

	public boolean isPathTile(int xTile, int yTile) {
		Pair<Float, Float> dir = getDirectionTile(xTile, yTile);
		return dir.getFirst() != 0f || dir.getSecond() != 0f;
	}

	public Pair<Float, Float>[][] getDirections() {
		return directions;
	}
}
