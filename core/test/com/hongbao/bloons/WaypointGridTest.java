package com.hongbao.bloons;

import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WaypointGridTest {

    @Test
    public void testDefaultInitialization() {
        WaypointGrid grid = new WaypointGrid();
        assertEquals(32, grid.getWidth());
        assertEquals(18, grid.getHeight());
        assertEquals(50, grid.getTileLength());
        assertEquals(50, grid.getTileHeight());

        Pair<Float, Float>[][] directions = grid.getDirections();
        assertNotNull(directions);
        assertEquals(32, directions.length);

        for (int x = 0; x < grid.getWidth(); x++) {
            assertEquals(18, directions[x].length);
            for (int y = 0; y < grid.getHeight(); y++) {
                Pair<Float, Float> dir = directions[x][y];
                assertNotNull(dir, "Direction at (" + x + ", " + y + ") should not be null");
                assertEquals(0f, dir.getFirst(), 0.0001f);
                assertEquals(0f, dir.getSecond(), 0.0001f);
            }
        }
    }

    @Test
    public void testMapFactoryInitializeEmptyDirectionsColumn31() {
        Pair<Float, Float>[][] directions = MapFactory.initializeEmptyDirections();
        assertNotNull(directions);
        assertEquals(32, directions.length);

        // Column 31 (index 31) must be completely initialized
        assertNotNull(directions[31], "Column 31 should be initialized");
        assertEquals(18, directions[31].length);

        for (int y = 0; y < 18; y++) {
            Pair<Float, Float> dir = directions[31][y];
            assertNotNull(dir, "Element at (31, " + y + ") should not be null");
            assertEquals(0f, dir.getFirst(), 0.0001f);
            assertEquals(0f, dir.getSecond(), 0.0001f);
        }
    }

    @Test
    public void testDiscreteTileLookups() {
        WaypointGrid grid = new WaypointGrid();
        grid.setDirectionAtTile(5, 8, new Pair<>(1f, 0f));
        grid.setDirectionAtTile(4, 9, new Pair<>(0.7071f, 0.7071f));

        // balloonX = 220f, balloonY = 425f -> xTile = (220+50)/50 = 5, yTile = 425/50 = 8
        Pair<Float, Float> dir1 = grid.getDirection(220f, 425f);
        assertNotNull(dir1);
        assertEquals(1f, dir1.getFirst(), 0.0001f);
        assertEquals(0f, dir1.getSecond(), 0.0001f);

        // balloonX = 170f, balloonY = 475f -> xTile = (170+50)/50 = 4, yTile = 475/50 = 9
        Pair<Float, Float> dir2 = grid.getDirection(170f, 475f);
        assertNotNull(dir2);
        assertEquals(0.7071f, dir2.getFirst(), 0.0001f);
        assertEquals(0.7071f, dir2.getSecond(), 0.0001f);
    }

    @Test
    public void testGuardClausesNegativeCoordinates() {
        WaypointGrid grid = new WaypointGrid();

        // Far off-screen to the left (e.g. negative xTile)
        Pair<Float, Float> dir1 = grid.getDirection(-100f, 100f);
        assertNotNull(dir1);
        assertEquals(0f, dir1.getFirst(), 0.0001f);
        assertEquals(0f, dir1.getSecond(), 0.0001f);

        // Negative y
        Pair<Float, Float> dir2 = grid.getDirection(100f, -100f);
        assertNotNull(dir2);
        assertEquals(0f, dir2.getFirst(), 0.0001f);
        assertEquals(0f, dir2.getSecond(), 0.0001f);

        // Negative x and y
        Pair<Float, Float> dir3 = grid.getDirection(-500f, -500f);
        assertNotNull(dir3);
        assertEquals(0f, dir3.getFirst(), 0.0001f);
        assertEquals(0f, dir3.getSecond(), 0.0001f);
    }

    @Test
    public void testGuardClausesOutOfBoundsCoordinates() {
        WaypointGrid grid = new WaypointGrid();

        // xTile = (1600 + 50) / 50 = 33 >= 32
        Pair<Float, Float> dir1 = grid.getDirection(1600f, 100f);
        assertNotNull(dir1);
        assertEquals(0f, dir1.getFirst(), 0.0001f);
        assertEquals(0f, dir1.getSecond(), 0.0001f);

        // yTile = 950 / 50 = 19 >= 18
        Pair<Float, Float> dir2 = grid.getDirection(100f, 950f);
        assertNotNull(dir2);
        assertEquals(0f, dir2.getFirst(), 0.0001f);
        assertEquals(0f, dir2.getSecond(), 0.0001f);

        // Extreme off-screen
        Pair<Float, Float> dir3 = grid.getDirection(10000f, 10000f);
        assertNotNull(dir3);
        assertEquals(0f, dir3.getFirst(), 0.0001f);
        assertEquals(0f, dir3.getSecond(), 0.0001f);
    }

    @Test
    public void testNullElementSafety() {
        @SuppressWarnings("unchecked")
        Pair<Float, Float>[][] nullDirections = new Pair[32][18];
        WaypointGrid grid = new WaypointGrid(nullDirections);

        Pair<Float, Float> dir = grid.getDirection(0f, 0f);
        assertNotNull(dir);
        assertEquals(0f, dir.getFirst(), 0.0001f);
        assertEquals(0f, dir.getSecond(), 0.0001f);
    }

    @Test
    public void testCustomGridDimensions() {
        WaypointGrid grid = new WaypointGrid(10, 10, 20, 20);
        assertEquals(10, grid.getWidth());
        assertEquals(10, grid.getHeight());
        assertEquals(20, grid.getTileLength());
        assertEquals(20, grid.getTileHeight());

        grid.setDirectionAtTile(2, 3, new Pair<>(0f, 1f));

        // xTile = (20 + 20)/20 = 2, yTile = 60/20 = 3
        Pair<Float, Float> dir = grid.getDirection(20f, 60f);
        assertNotNull(dir);
        assertEquals(0f, dir.getFirst(), 0.0001f);
        assertEquals(1f, dir.getSecond(), 0.0001f);

        // Out of bounds for 10x10 grid
        Pair<Float, Float> outDir = grid.getDirection(200f, 200f); // xTile = 11
        assertNotNull(outDir);
        assertEquals(0f, outDir.getFirst(), 0.0001f);
        assertEquals(0f, outDir.getSecond(), 0.0001f);
    }
}
