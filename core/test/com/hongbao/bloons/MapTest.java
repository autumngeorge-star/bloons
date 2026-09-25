package com.hongbao.bloons;

import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapTest {

    @BeforeClass
    public static void setUp() {
        GdxTestRunner.initGdx();
    }

    @Test
    public void testTileCoordinateMapping() {
        Map map = MapFactory.createBasicMap(null);

        // balloonX = -50, balloonY = 0 => xTile = 0, yTile = 0
        Pair<Float, Float> dir0_0 = map.getDirection(-50f, 0f);
        assertNotNull(dir0_0);
        assertEquals(0f, dir0_0.getFirst(), 0.0001f);
        assertEquals(0f, dir0_0.getSecond(), 0.0001f);

        // balloonX = 0, balloonY = 400 => xTile = 1, yTile = 8
        Pair<Float, Float> dir1_8 = map.getDirection(0f, 400f);
        assertNotNull(dir1_8);
        assertEquals(1f, dir1_8.getFirst(), 0.0001f);
        assertEquals(0f, dir1_8.getSecond(), 0.0001f);

        // balloonX = 1500, balloonY = 400 => xTile = 31, yTile = 8
        Pair<Float, Float> dir31_8 = map.getDirection(1500f, 400f);
        assertNotNull(dir31_8);
        assertEquals(1f, dir31_8.getFirst(), 0.0001f);
        assertEquals(0f, dir31_8.getSecond(), 0.0001f);
    }

    @Test
    public void testNegativeCoordinatesBoundarySafety() {
        Map map = MapFactory.createBasicMap(null);

        // Negative X coordinate (balloonX = -100 => xTile = -1)
        Pair<Float, Float> dirNegX = map.getDirection(-100f, 400f);
        assertNotNull("Negative X should return non-null fallback Pair", dirNegX);
        assertEquals(0f, dirNegX.getFirst(), 0.0001f);
        assertEquals(0f, dirNegX.getSecond(), 0.0001f);

        // Large negative X coordinate
        Pair<Float, Float> dirLargeNegX = map.getDirection(-1000f, 400f);
        assertNotNull("Large negative X should return non-null fallback Pair", dirLargeNegX);
        assertEquals(0f, dirLargeNegX.getFirst(), 0.0001f);
        assertEquals(0f, dirLargeNegX.getSecond(), 0.0001f);

        // Negative Y coordinate (balloonY = -50 => yTile = -1)
        Pair<Float, Float> dirNegY = map.getDirection(200f, -50f);
        assertNotNull("Negative Y should return non-null fallback Pair", dirNegY);
        assertEquals(0f, dirNegY.getFirst(), 0.0001f);
        assertEquals(0f, dirNegY.getSecond(), 0.0001f);

        // Both X and Y negative
        Pair<Float, Float> dirNegBoth = map.getDirection(-200f, -200f);
        assertNotNull("Negative X and Y should return non-null fallback Pair", dirNegBoth);
        assertEquals(0f, dirNegBoth.getFirst(), 0.0001f);
        assertEquals(0f, dirNegBoth.getSecond(), 0.0001f);
    }

    @Test
    public void testOutOfBoundsCoordinatesUpperBounds() {
        Map map = MapFactory.createBasicMap(null);

        // X out of upper bound (balloonX = 1600 => xTile = 33 >= 32)
        Pair<Float, Float> dirOutX = map.getDirection(1600f, 400f);
        assertNotNull("Out of bounds X should return non-null fallback Pair", dirOutX);
        assertEquals(0f, dirOutX.getFirst(), 0.0001f);
        assertEquals(0f, dirOutX.getSecond(), 0.0001f);

        // Y out of upper bound (balloonY = 1000 => yTile = 20 >= 18)
        Pair<Float, Float> dirOutY = map.getDirection(200f, 1000f);
        assertNotNull("Out of bounds Y should return non-null fallback Pair", dirOutY);
        assertEquals(0f, dirOutY.getFirst(), 0.0001f);
        assertEquals(0f, dirOutY.getSecond(), 0.0001f);

        // Both X and Y out of upper bounds
        Pair<Float, Float> dirOutBoth = map.getDirection(2000f, 2000f);
        assertNotNull("Out of bounds X and Y should return non-null fallback Pair", dirOutBoth);
        assertEquals(0f, dirOutBoth.getFirst(), 0.0001f);
        assertEquals(0f, dirOutBoth.getSecond(), 0.0001f);
    }

    @Test
    public void testColumn31Lookup() {
        Map map = MapFactory.createBasicMap(null);

        // balloonX = 1500 yields xTile = (1500 + 50) / 50 = 31
        Pair<Float, Float> dirCol31Path = map.getDirection(1500f, 400f);
        assertNotNull("Column 31 lookup should not return null", dirCol31Path);
        assertEquals("Column 31 path dir X", 1f, dirCol31Path.getFirst(), 0.0001f);
        assertEquals("Column 31 path dir Y", 0f, dirCol31Path.getSecond(), 0.0001f);

        Pair<Float, Float> dirCol31OffPath = map.getDirection(1500f, 100f);
        assertNotNull("Column 31 off-path lookup should not return null", dirCol31OffPath);
        assertEquals("Column 31 off-path dir X", 0f, dirCol31OffPath.getFirst(), 0.0001f);
        assertEquals("Column 31 off-path dir Y", 0f, dirCol31OffPath.getSecond(), 0.0001f);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNullDirectionFallback() {
        Map map = new Map("basic_map.png", null);
        Pair<Float, Float>[][] directions = new Pair[32][18];
        // Leave directions[5][5] as null intentionally
        directions[5][5] = null;
        map.setDirections(directions);

        // balloonX = 200, balloonY = 250 => xTile = 5, yTile = 5
        Pair<Float, Float> dirNullCell = map.getDirection(200f, 250f);
        assertNotNull("Null grid cell should return non-null fallback Pair(0f, 0f)", dirNullCell);
        assertEquals(0f, dirNullCell.getFirst(), 0.0001f);
        assertEquals(0f, dirNullCell.getSecond(), 0.0001f);
    }
}
