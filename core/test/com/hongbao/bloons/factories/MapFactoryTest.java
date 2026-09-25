package com.hongbao.bloons.factories;

import com.hongbao.bloons.GdxTestRunner;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.helpers.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapFactoryTest {

    @BeforeClass
    public static void setUp() {
        GdxTestRunner.initGdx();
    }

    @Test
    public void testInitializeEmptyDirections() {
        Pair<Float, Float>[][] directions = MapFactory.initializeEmptyDirections();

        assertNotNull("Directions matrix should not be null", directions);
        assertEquals("Should have 32 columns", 32, directions.length);

        for (int x = 0; x < 32; x++) {
            assertNotNull("Column " + x + " should not be null", directions[x]);
            assertEquals("Column " + x + " should have 18 rows", 18, directions[x].length);
            for (int y = 0; y < 18; y++) {
                Pair<Float, Float> dir = directions[x][y];
                assertNotNull("Direction at (" + x + ", " + y + ") should not be null", dir);
                assertEquals("Direction X at (" + x + ", " + y + ") should be 0f", 0f, dir.getFirst(), 0.0001f);
                assertEquals("Direction Y at (" + x + ", " + y + ") should be 0f", 0f, dir.getSecond(), 0.0001f);
            }
        }
    }

    @Test
    public void testCreateBasicMap() {
        Map map = MapFactory.createBasicMap(null);
        assertNotNull("Map should not be null", map);

        // Verify path at y=400 (row 8) across columns 0 to 31
        for (int col = 0; col < 32; col++) {
            float balloonX = (col * Map.TILE_LENGTH) - Map.TILE_LENGTH + 25f;
            Pair<Float, Float> dir = map.getDirection(balloonX, 400f);
            assertNotNull("Direction at column " + col + " should not be null", dir);
            assertEquals("Basic map col " + col + " dir X", 1f, dir.getFirst(), 0.0001f);
            assertEquals("Basic map col " + col + " dir Y", 0f, dir.getSecond(), 0.0001f);
        }

        // Verify column index 31 specifically
        Pair<Float, Float> col31Dir = map.getDirection(1500f, 400f);
        assertNotNull("Column 31 direction should not be null", col31Dir);
        assertEquals(1f, col31Dir.getFirst(), 0.0001f);
        assertEquals(0f, col31Dir.getSecond(), 0.0001f);
    }

    @Test
    public void testCreateMapWithTurn() {
        Map map = MapFactory.createMapWithTurn(null);
        assertNotNull("Map should not be null", map);

        // Columns 0..3 row 8 should be (1f, 0f)
        for (int col = 0; col <= 3; col++) {
            float balloonX = (col * Map.TILE_LENGTH) - Map.TILE_LENGTH + 25f;
            Pair<Float, Float> dir = map.getDirection(balloonX, 400f);
            assertEquals("Turn map col " + col + " dir X", 1f, dir.getFirst(), 0.0001f);
            assertEquals("Turn map col " + col + " dir Y", 0f, dir.getSecond(), 0.0001f);
        }

        // Turn tiles around col 4
        Pair<Float, Float> turnStart = map.getDirection((4 * Map.TILE_LENGTH) - Map.TILE_LENGTH + 25f, 400f);
        assertEquals(MapFactory.ROOT_2_OVER_2, turnStart.getFirst(), 0.0001f);
        assertEquals(MapFactory.ROOT_2_OVER_2, turnStart.getSecond(), 0.0001f);

        // Columns 5..31 row 11 should be (1f, 0f)
        for (int col = 5; col < 32; col++) {
            float balloonX = (col * Map.TILE_LENGTH) - Map.TILE_LENGTH + 25f;
            Pair<Float, Float> dir = map.getDirection(balloonX, 550f);
            assertEquals("Turn map col " + col + " row 11 dir X", 1f, dir.getFirst(), 0.0001f);
            assertEquals("Turn map col " + col + " row 11 dir Y", 0f, dir.getSecond(), 0.0001f);
        }
    }

    @Test
    public void testCreateHeaterMap() {
        Map map = MapFactory.createHeaterMap(null);
        assertNotNull("Map should not be null", map);

        // Start path: col 0..3 row 8 => (1f, 0f)
        for (int col = 0; col <= 3; col++) {
            float balloonX = (col * Map.TILE_LENGTH) - Map.TILE_LENGTH + 25f;
            Pair<Float, Float> dir = map.getDirection(balloonX, 400f);
            assertEquals(1f, dir.getFirst(), 0.0001f);
            assertEquals(0f, dir.getSecond(), 0.0001f);
        }

        // End path: col 26..31 row 8 => (1f, 0f)
        for (int col = 26; col < 32; col++) {
            float balloonX = (col * Map.TILE_LENGTH) - Map.TILE_LENGTH + 25f;
            Pair<Float, Float> dir = map.getDirection(balloonX, 400f);
            assertEquals("Heater map col " + col + " row 8 dir X", 1f, dir.getFirst(), 0.0001f);
            assertEquals("Heater map col " + col + " row 8 dir Y", 0f, dir.getSecond(), 0.0001f);
        }
    }
}
