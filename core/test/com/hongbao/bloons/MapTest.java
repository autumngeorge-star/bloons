package com.hongbao.bloons;

import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest extends HeadlessTestHelper {

    @Test
    @DisplayName("getDirection returns (0,0) safely when directions array is null")
    void testGetDirectionUninitializedMap() {
        Map map = new Map("basic_map.png", null);
        map.setDirections(null);

        Pair<Float, Float> dir = map.getDirection(0f, 0f);
        assertNotNull(dir);
        assertEquals(0f, dir.getFirst());
        assertEquals(0f, dir.getSecond());
    }

    @ParameterizedTest
    @CsvSource({
            "-100.0, 425.0",
            "-50.1, 425.0",
            "100.0, -10.0",
            "100.0, -0.1",
            "1600.0, 425.0",
            "100.0, 950.0",
            "-1000.0, -1000.0",
            "5000.0, 5000.0"
    })
    @DisplayName("getDirection handles off-screen and negative coordinates safely returning (0,0)")
    void testGetDirectionOutOfBounds(float balloonX, float balloonY) {
        Map map = MapFactory.createBasicMap(null);
        Pair<Float, Float> dir = assertDoesNotThrow(() -> map.getDirection(balloonX, balloonY));
        assertNotNull(dir);
        assertEquals(0f, dir.getFirst(), 1e-5f);
        assertEquals(0f, dir.getSecond(), 1e-5f);
    }

    @Test
    @DisplayName("getDirection handles NaN and Infinity input coordinates")
    void testGetDirectionNanAndInfinity() {
        Map map = MapFactory.createBasicMap(null);

        Pair<Float, Float> dirNan = map.getDirection(Float.NaN, Float.NaN);
        assertEquals(0f, dirNan.getFirst());
        assertEquals(0f, dirNan.getSecond());

        Pair<Float, Float> dirInf = map.getDirection(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
        assertEquals(0f, dirInf.getFirst());
        assertEquals(0f, dirInf.getSecond());
    }

    @Test
    @DisplayName("getDirection handles null cells in directions grid without NullPointerException")
    void testGetDirectionWithUninitializedNullCells() {
        Map map = new Map("basic_map.png", null);
        Pair<Float, Float>[][] incompleteDirections = new Pair[32][18];
        // Leave some cells as null
        incompleteDirections[5][5] = new Pair<>(1f, 0f);
        map.setDirections(incompleteDirections);

        // Querying null cell directly or adjacent to null cell
        Pair<Float, Float> dirNullCell = assertDoesNotThrow(() -> map.getDirection(10 * 50 - 25, 10 * 50 + 25));
        assertEquals(0f, dirNullCell.getFirst());
        assertEquals(0f, dirNullCell.getSecond());

        // Querying position near cell (5,5) which interpolates with adjacent null cells
        Pair<Float, Float> dirNearValid = assertDoesNotThrow(() -> map.getDirection(5 * 50 - 25, 5 * 50 + 25));
        assertNotNull(dirNearValid);
        assertEquals(1f, dirNearValid.getFirst(), 1e-4f);
        assertEquals(0f, dirNearValid.getSecond(), 1e-4f);
    }

    @Test
    @DisplayName("getDirection returns exact directional vector at tile center")
    void testGetDirectionAtTileCenter() {
        Map map = MapFactory.createBasicMap(null);

        // Tile 0 center is x = -25, y = 425 (row 8)
        Pair<Float, Float> dirTile0 = map.getDirection(-25f, 425f);
        assertEquals(1f, dirTile0.getFirst(), 1e-4f);
        assertEquals(0f, dirTile0.getSecond(), 1e-4f);

        // Tile 31 center is x = 1525, y = 425 (row 8)
        Pair<Float, Float> dirTile31 = map.getDirection(1525f, 425f);
        assertEquals(1f, dirTile31.getFirst(), 1e-4f);
        assertEquals(0f, dirTile31.getSecond(), 1e-4f);
    }

    @Test
    @DisplayName("getDirection performs continuous bilinear spatial vector interpolation between tile centers")
    void testBilinearSpatialInterpolation() {
        Map map = new Map("basic_map.png", null);
        Pair<Float, Float>[][] customGrid = MapFactory.initializeEmptyDirections();

        // Tile (2, 2) center: x = 75, y = 125 -> direction (1, 0)
        // Tile (3, 2) center: x = 125, y = 125 -> direction (0, 1)
        customGrid[2][2] = new Pair<>(1f, 0f);
        customGrid[3][2] = new Pair<>(0f, 1f);
        map.setDirections(customGrid);

        // At midpoint between (2,2) and (3,2): x = 100, y = 125
        Pair<Float, Float> interpolatedMidpoint = map.getDirection(100f, 125f);

        // 0.5 * (1, 0) + 0.5 * (0, 1) = (0.5, 0.5) -> normalized is (1/sqrt(2), 1/sqrt(2)) ~ (0.7071, 0.7071)
        assertEquals(MapFactory.ROOT_2_OVER_2, interpolatedMidpoint.getFirst(), 1e-3f);
        assertEquals(MapFactory.ROOT_2_OVER_2, interpolatedMidpoint.getSecond(), 1e-3f);

        // Verify normalized length equals 1.0
        float len = (float) Math.sqrt(Math.pow(interpolatedMidpoint.getFirst(), 2) + Math.pow(interpolatedMidpoint.getSecond(), 2));
        assertEquals(1.0f, len, 1e-5f);
    }

    @Test
    @DisplayName("getDirection returns normalized vectors across fractional sub-tile offsets")
    void testVectorNormalization() {
        Map map = MapFactory.createMapWithTurn(null);

        // Sub-tile query near turn waypoint
        for (float offset = 0f; offset <= 50f; offset += 5f) {
            float x = 4 * 50 - 25 + offset;
            float y = 8 * 50 + 25;
            Pair<Float, Float> dir = map.getDirection(x, y);

            if (dir.getFirst() != 0f || dir.getSecond() != 0f) {
                float len = (float) Math.sqrt(dir.getFirst() * dir.getFirst() + dir.getSecond() * dir.getSecond());
                assertEquals(1.0f, len, 1e-4f, "Direction vector must be normalized to length 1.0 at offset " + offset);
            }
        }
    }
}
