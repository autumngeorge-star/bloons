package com.hongbao.bloons.entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class BloonTest {

    @Test
    public void testBloonDamageRecalculatesProperties() {
        // Create a Ceramic bloon (health 18, speed 7, color CERAMIC)
        Bloon ceramic = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
        assertEquals(18, ceramic.getHealth());
        assertEquals(Bloon.Color.CERAMIC, ceramic.getColor());
        assertEquals(7, ceramic.getSpeed());
        assertEquals("img/bloons/ceramic_bloon.png", ceramic.getImageFileName());

        // Apply non-popping damage of 1
        ceramic.damage(1);
        assertEquals(17, ceramic.getHealth());
        assertEquals(Bloon.Color.CERAMIC, ceramic.getColor());
        assertEquals(7, ceramic.getSpeed());
        assertEquals("img/bloons/ceramic_bloon.png", ceramic.getImageFileName());

        // Create a MOAB bloon (health 218, speed 5, color MOAB)
        Bloon moab = new Bloon(Bloon.Color.MOAB, 218, false, false);
        assertEquals(218, moab.getHealth());
        assertEquals(Bloon.Color.MOAB, moab.getColor());
        assertEquals(5, moab.getSpeed());
        assertEquals("img/bloons/moab_bloon.png", moab.getImageFileName());

        // Apply non-popping damage of 20
        moab.damage(20);
        assertEquals(198, moab.getHealth());
        assertEquals(Bloon.Color.MOAB, moab.getColor());
        assertEquals(5, moab.getSpeed());
        assertEquals("img/bloons/moab_bloon.png", moab.getImageFileName());
    }

    @Test
    public void testBloonDamageTransitionsColorAndSpeed() {
        // Create a Ceramic bloon with health 18
        Bloon bloon = new Bloon(Bloon.Color.CERAMIC, 18, false, false);

        // Apply damage of 10 -> health 8 (Rainbow bloon)
        bloon.damage(10);
        assertEquals(8, bloon.getHealth());
        assertEquals(Bloon.Color.RAINBOW, bloon.getColor());
        assertEquals(8, bloon.getSpeed());
        assertEquals("img/bloons/rainbow_bloon.png", bloon.getImageFileName());

        // Apply damage of 4 -> health 4 (Yellow bloon)
        bloon.damage(4);
        assertEquals(4, bloon.getHealth());
        assertEquals(Bloon.Color.YELLOW, bloon.getColor());
        assertEquals(8, bloon.getSpeed());
        assertEquals("img/bloons/yellow_bloon.png", bloon.getImageFileName());
    }

    @Test
    public void testWillPopBloon() {
        Bloon ceramic = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
        assertFalse(ceramic.willPopBloon(1));
        assertTrue(ceramic.willPopBloon(10));
        assertTrue(ceramic.willPopBloon(18));
    }

    @Test
    public void testGetColorFromHealthEdgeCases() {
        assertEquals(Bloon.Color.RED, Bloon.getColorFromHealth(1));
        assertEquals(Bloon.Color.RED, Bloon.getColorFromHealth(0));
        assertEquals(Bloon.Color.RED, Bloon.getColorFromHealth(-5));
        assertEquals(Bloon.Color.CERAMIC, Bloon.getColorFromHealth(18));
        assertEquals(Bloon.Color.MOAB, Bloon.getColorFromHealth(218));
        assertEquals(Bloon.Color.BFB, Bloon.getColorFromHealth(918));
        assertEquals(Bloon.Color.ZOMG, Bloon.getColorFromHealth(2000));
    }
}
