package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BloonDamagePipelineTest {

    @Test
    public void testNonPoppingHitUpdatesBloonState() {
        // Ceramic bloon initial health = 18, color = CERAMIC, speed = 7
        Bloon ceramic = BloonFactory.createCeramicBloon();
        assertEquals(18, ceramic.getHealth());
        assertEquals(Bloon.Color.CERAMIC, ceramic.getColor());
        assertEquals(7, ceramic.getSpeed());
        assertEquals("img/bloons/ceramic_bloon.png", ceramic.getImageFileName());

        // Ceramic taking 1 non-popping damage -> health becomes 17
        assertFalse(ceramic.willPopBloon(1));
        ceramic.damage(1);

        assertEquals(17, ceramic.getHealth());
        assertEquals(Bloon.Color.CERAMIC, ceramic.getColor());
        assertEquals(7, ceramic.getSpeed());
        assertEquals("img/bloons/ceramic_bloon.png", ceramic.getImageFileName());
    }

    @Test
    public void testMultiHitBlimpFocusFireStateUpdates() {
        // MOAB initial health = 218, color = MOAB, speed = 5
        Bloon moab = BloonFactory.createMOAB();
        assertEquals(218, moab.getHealth());
        assertEquals(Bloon.Color.MOAB, moab.getColor());
        assertEquals(5, moab.getSpeed());
        assertEquals("img/bloons/moab_bloon.png", moab.getImageFileName());

        // MOAB taking 10 non-popping damage
        assertFalse(moab.willPopBloon(10));
        moab.damage(10);

        assertEquals(208, moab.getHealth());
        assertEquals(Bloon.Color.MOAB, moab.getColor());
        assertEquals(5, moab.getSpeed());
        assertEquals("img/bloons/moab_bloon.png", moab.getImageFileName());
    }

    @Test
    public void testPoppingHitDetection() {
        Bloon red = BloonFactory.createRedBloon();
        assertTrue(red.willPopBloon(1));

        Bloon blue = BloonFactory.createBlueBloon();
        assertTrue(blue.willPopBloon(1)); // 2 - 1 = 1 (Red), color change triggers pop
    }
}
