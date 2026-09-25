package com.hongbao.bloons;

public class MapBoundaryTest {

    public static void main(String[] args) {
        System.out.println("Running MapBoundaryTest...");

        // 1. Verify constants
        assert Map.PLAY_AREA_MIN_X == 0f : "PLAY_AREA_MIN_X should be 0f";
        assert Map.PLAY_AREA_MAX_X == 1500f : "PLAY_AREA_MAX_X should be 1500f";
        assert Map.PLAY_AREA_MIN_Y == 0f : "PLAY_AREA_MIN_Y should be 0f";
        assert Map.PLAY_AREA_MAX_Y == 900f : "PLAY_AREA_MAX_Y should be 900f";

        // 2. Test isWithinPlayArea method
        // Inside play area completely
        assertTrue(Map.isWithinPlayArea(750f, 450f, 25f), "Center (750, 450) with radius 25 should be inside play area");
        assertTrue(Map.isWithinPlayArea(25f, 25f, 25f), "Center (25, 25) with radius 25 touching borders should be inside");
        assertTrue(Map.isWithinPlayArea(1475f, 875f, 25f), "Center (1475, 875) with radius 25 touching borders should be inside");

        // Crossing left boundary (x < 0)
        assertFalse(Map.isWithinPlayArea(20f, 450f, 25f), "Center (20, 450) with radius 25 extends past x = 0");
        assertFalse(Map.isWithinPlayArea(-5f, 450f, 10f), "Center (-5, 450) extends past x = 0");

        // Crossing right boundary / UI sidebar (x >= 1500)
        assertFalse(Map.isWithinPlayArea(1480f, 450f, 25f), "Center (1480, 450) with radius 25 extends past x = 1500");
        assertFalse(Map.isWithinPlayArea(1500f, 450f, 1f), "Center (1500, 450) with radius 1 extends past x = 1500");
        assertFalse(Map.isWithinPlayArea(1510f, 450f, 10f), "Center (1510, 450) is outside play area");

        // Crossing bottom boundary (y < 0)
        assertFalse(Map.isWithinPlayArea(750f, 20f, 25f), "Center (750, 20) with radius 25 extends past y = 0");

        // Crossing top boundary (y > 900)
        assertFalse(Map.isWithinPlayArea(750f, 880f, 25f), "Center (750, 880) with radius 25 extends past y = 900");

        System.out.println("ALL MAP BOUNDARY TESTS PASSED SUCCESSFULLY!");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("ASSERTION FAILED: " + message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError("ASSERTION FAILED: " + message);
        }
    }
}
