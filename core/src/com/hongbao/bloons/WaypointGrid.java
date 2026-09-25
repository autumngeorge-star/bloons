package com.hongbao.bloons;

import com.hongbao.bloons.helpers.Pair;

public class WaypointGrid {

    public static final int DEFAULT_WIDTH = 32;
    public static final int DEFAULT_HEIGHT = 18;
    public static final int DEFAULT_TILE_LENGTH = 50;
    public static final int DEFAULT_TILE_HEIGHT = 50;

    private final int width;
    private final int height;
    private final int tileLength;
    private final int tileHeight;
    private Pair<Float, Float>[][] directions;

    public WaypointGrid() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TILE_LENGTH, DEFAULT_TILE_HEIGHT);
    }

    public WaypointGrid(int width, int height) {
        this(width, height, DEFAULT_TILE_LENGTH, DEFAULT_TILE_HEIGHT);
    }

    public WaypointGrid(int width, int height, int tileLength, int tileHeight) {
        this.width = width;
        this.height = height;
        this.tileLength = tileLength;
        this.tileHeight = tileHeight;
        initializeEmptyDirections();
    }

    public WaypointGrid(Pair<Float, Float>[][] directions) {
        this(directions, DEFAULT_TILE_LENGTH, DEFAULT_TILE_HEIGHT);
    }

    public WaypointGrid(Pair<Float, Float>[][] directions, int tileLength, int tileHeight) {
        if (directions != null && directions.length > 0 && directions[0] != null) {
            this.width = directions.length;
            this.height = directions[0].length;
        } else {
            this.width = DEFAULT_WIDTH;
            this.height = DEFAULT_HEIGHT;
        }
        this.tileLength = tileLength;
        this.tileHeight = tileHeight;
        setDirections(directions);
    }

    @SuppressWarnings("unchecked")
    public void initializeEmptyDirections() {
        this.directions = new Pair[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.directions[x][y] = new Pair<>(0f, 0f);
            }
        }
    }

    public Pair<Float, Float>[][] getDirections() {
        return directions;
    }

    public void setDirections(Pair<Float, Float>[][] directions) {
        if (directions == null) {
            initializeEmptyDirections();
            return;
        }
        this.directions = directions;
        for (int x = 0; x < Math.min(width, directions.length); x++) {
            if (directions[x] != null) {
                for (int y = 0; y < Math.min(height, directions[x].length); y++) {
                    if (directions[x][y] == null) {
                        directions[x][y] = new Pair<>(0f, 0f);
                    }
                }
            }
        }
    }

    public Pair<Float, Float> getDirection(float balloonX, float balloonY) {
        int xTile = (int)(balloonX + tileLength) / tileLength;
        int yTile = (int)balloonY / tileHeight;

        if (xTile >= 0 && xTile < width && yTile >= 0 && yTile < height) {
            Pair<Float, Float> dir = directions[xTile][yTile];
            if (dir != null) {
                return dir;
            }
        }
        return new Pair<>(0f, 0f);
    }

    public Pair<Float, Float> getDirectionAtTile(int xTile, int yTile) {
        if (xTile >= 0 && xTile < width && yTile >= 0 && yTile < height) {
            Pair<Float, Float> dir = directions[xTile][yTile];
            if (dir != null) {
                return dir;
            }
        }
        return new Pair<>(0f, 0f);
    }

    public void setDirectionAtTile(int xTile, int yTile, Pair<Float, Float> direction) {
        if (xTile >= 0 && xTile < width && yTile >= 0 && yTile < height) {
            directions[xTile][yTile] = direction != null ? direction : new Pair<>(0f, 0f);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileLength() {
        return tileLength;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
