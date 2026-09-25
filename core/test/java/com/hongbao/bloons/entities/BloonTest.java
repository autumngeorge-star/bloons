package com.hongbao.bloons.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BloonTest {

    @Test
    @DisplayName("Should initialize Bloon domain entity independently without LibGDX context")
    void testBloonInitializationWithoutLibGDX() {
        IBloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);

        assertNotNull(bloon);
        assertEquals(Bloon.Color.RED, bloon.getColor());
        assertEquals(1, bloon.getHealth());
        assertEquals(5, bloon.getSpeed());
        assertFalse(bloon.isCamo());
        assertFalse(bloon.isRegen());
        assertNotNull(bloon.getPathProgress());
        assertEquals(0, bloon.getDistanceTravelled());
    }

    @Test
    @DisplayName("Should update distance and path progress when incrementing distance")
    void testDistanceIncrementing() {
        IBloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        assertEquals(6, bloon.getSpeed());

        bloon.incrementDistanceTravelled();

        assertEquals(6, bloon.getDistanceTravelled());
        assertEquals(6.0f, bloon.getPathProgress().getAccumulatedDistance(), 0.0001f);
        assertEquals(0.12f, bloon.getPathProgress().getSegmentRatio(), 0.0001f);
    }

    @Test
    @DisplayName("Should update path progress with custom displacement and segment length")
    void testUpdatePathProgress() {
        IBloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false);

        bloon.updatePathProgress(25.0f, 50.0f);

        assertEquals(25, bloon.getDistanceTravelled());
        assertEquals(0, bloon.getPathProgress().getWaypointIndex());
        assertEquals(0.5f, bloon.getPathProgress().getSegmentRatio(), 0.0001f);
        assertEquals(0.5f, bloon.getPathProgress().getNormalizedProgress(), 0.0001f);
    }

    @Test
    @DisplayName("Should correctly identify blimp bloons")
    void testIsBlimp() {
        IBloon red = new Bloon(Bloon.Color.RED, 1, false, false);
        IBloon moab = new Bloon(Bloon.Color.MOAB, 218, false, false);
        IBloon bfb = new Bloon(Bloon.Color.BFB, 918, false, false);
        IBloon zomg = new Bloon(Bloon.Color.ZOMG, 4918, false, false);

        assertFalse(red.isBlimp());
        assertTrue(moab.isBlimp());
        assertTrue(bfb.isBlimp());
        assertTrue(zomg.isBlimp());
    }
}
