package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonTest {

    @Test
    public void testBloonConstructorAndGetters() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertEquals(Bloon.Color.RED, bloon.getColor());
        assertEquals(1, bloon.getHealth());
        assertFalse(bloon.isCamo());
        assertFalse(bloon.isRegen());
        assertEquals(5, bloon.getSpeed());
        assertEquals("img/bloons/red_bloon.png", bloon.getImageFileName());
    }

    @Test
    public void testCamoAndRegenFileName() {
        Bloon camoRegenBloon = new Bloon(Bloon.Color.BLUE, 2, true, true);
        assertEquals("img/bloons/blue_camo_regrowth_bloon.png", camoRegenBloon.getImageFileName());
    }

    @Test
    public void testDistanceTravelled() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertEquals(0, bloon.getDistanceTravelled());
        bloon.incrementDistanceTravelled();
        assertEquals(5, bloon.getDistanceTravelled());
        bloon.incrementDistanceTravelled();
        assertEquals(10, bloon.getDistanceTravelled());
    }

    @Test
    public void testGetColorFromHealthMapping() {
        assertEquals(Bloon.Color.RED, Bloon.getColorFromHealth(1));
        assertEquals(Bloon.Color.BLUE, Bloon.getColorFromHealth(2));
        assertEquals(Bloon.Color.GREEN, Bloon.getColorFromHealth(3));
        assertEquals(Bloon.Color.YELLOW, Bloon.getColorFromHealth(4));
        assertEquals(Bloon.Color.PINK, Bloon.getColorFromHealth(5));
        assertEquals(Bloon.Color.BLACK, Bloon.getColorFromHealth(6));
        assertEquals(Bloon.Color.ZEBRA, Bloon.getColorFromHealth(7));
        assertEquals(Bloon.Color.RAINBOW, Bloon.getColorFromHealth(8));
        assertEquals(Bloon.Color.CERAMIC, Bloon.getColorFromHealth(10));
        assertEquals(Bloon.Color.CERAMIC, Bloon.getColorFromHealth(18));
        assertEquals(Bloon.Color.MOAB, Bloon.getColorFromHealth(50));
        assertEquals(Bloon.Color.MOAB, Bloon.getColorFromHealth(218));
        assertEquals(Bloon.Color.BFB, Bloon.getColorFromHealth(500));
        assertEquals(Bloon.Color.BFB, Bloon.getColorFromHealth(918));
        assertEquals(Bloon.Color.ZOMG, Bloon.getColorFromHealth(1000));
    }

    @Test
    public void testWillPopBloon() {
        Bloon redBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertTrue(redBloon.willPopBloon(1));

        Bloon blueBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        assertTrue(blueBloon.willPopBloon(1));

        Bloon ceramicBloon = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
        assertFalse(ceramicBloon.willPopBloon(1));
    }

    @Test
    public void testDamageAndPop() {
        Bloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false);
        bloon.damage(1);
        assertEquals(2, bloon.getHealth());

        BloonPoppedResult result = bloon.pop(1);
        assertNotNull(result);
    }

    @Test
    public void testIsBlimp() {
        Bloon redBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        Bloon moab = new Bloon(Bloon.Color.MOAB, 200, false, false);
        Bloon bfb = new Bloon(Bloon.Color.BFB, 400, false, false);
        Bloon zomg = new Bloon(Bloon.Color.ZOMG, 1000, false, false);

        assertFalse(redBloon.isBlimp());
        assertTrue(moab.isBlimp());
        assertTrue(bfb.isBlimp());
        assertTrue(zomg.isBlimp());
    }
}
