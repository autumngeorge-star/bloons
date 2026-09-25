package com.hongbao.bloons.factories;

import com.hongbao.bloons.HeadlessTestHelper;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.helpers.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapFactoryTest extends HeadlessTestHelper {

    @Test
    @DisplayName("initializeEmptyDirections populates all columns including x=31 without leaving null arrays or elements")
    void testInitializeEmptyDirectionsFullyPopulatesGrid() {
        Pair<Float, Float>[][] directions = MapFactory.initializeEmptyDirections();

        assertNotNull(directions, "Directions array should not be null");
        assertEquals(32, directions.length, "Grid should have 32 columns");

        for (int x = 0; x < 32; x++) {
            assertNotNull(directions[x], "Column " + x + " should not be null");
            assertEquals(18, directions[x].length, "Column " + x + " should have 18 rows");

            for (int y = 0; y < 18; y++) {
                assertNotNull(directions[x][y], "Cell [" + x + "][" + y + "] should not be null");
                assertEquals(0f, directions[x][y].getFirst(), 1e-5f, "Default X vector should be 0.0");
                assertEquals(0f, directions[x][y].getSecond(), 1e-5f, "Default Y vector should be 0.0");
            }
        }
    }

    @Test
    @DisplayName("createBasicMap initializes map and row 8 path correctly across column 31")
    void testCreateBasicMap() {
        Map map = MapFactory.createBasicMap(null);
        assertNotNull(map, "Map created should not be null");

        // Verify column 31, row 8 has path direction (1.0, 0.0)
        Pair<Float, Float> dirCol31 = map.getDirection(31 * 50 - 25, 8 * 50 + 25); // center of tile (31, 8)
        assertEquals(1f, dirCol31.getFirst(), 1e-4f);
        assertEquals(0f, dirCol31.getSecond(), 1e-4f);
    }

    @Test
    @DisplayName("createMapWithTurn initializes map and turn waypoints")
    void testCreateMapWithTurn() {
        Map map = MapFactory.createMapWithTurn(null);
        assertNotNull(map, "Map created should not be null");

        // Verify tile (0, 8) has direction (1.0, 0.0)
        Pair<Float, Float> startDir = map.getDirection(-25f, 425f);
        assertEquals(1f, startDir.getFirst(), 1e-4f);
        assertEquals(0f, startDir.getSecond(), 1e-4f);

        // Verify turn tile (4, 8) has diagonal direction
        Pair<Float, Float> turnDir = map.getDirection(4 * 50 - 25, 8 * 50 + 25);
        assertEquals(MapFactory.ROOT_2_OVER_2, turnDir.getFirst(), 1e-3f);
        assertEquals(MapFactory.ROOT_2_OVER_2, turnDir.getSecond(), 1e-3f);
    }

    @Test
    @DisplayName("createHeaterMap initializes map with winding turns")
    void testCreateHeaterMap() {
        Map map = MapFactory.createHeaterMap(null);
        assertNotNull(map, "Heater map created should not be null");

        Pair<Float, Float> dir = map.getDirection(-25f, 425f);
        assertEquals(1f, dir.getFirst(), 1e-4f);
        assertEquals(0f, dir.getSecond(), 1e-4f);
    }
}
