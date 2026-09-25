package com.hongbao.bloons.entities;

import com.hongbao.bloons.HeadlessTestRunner;
import org.junit.Test;

import static org.junit.Assert.*;

public class BloonTest extends HeadlessTestRunner {

    @Test
    public void testBloonProperties() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertEquals(1, bloon.getHealth());
        assertEquals(5, bloon.getSpeed());
        assertEquals(0, bloon.getDistanceTravelled());
        assertEquals(Bloon.Color.RED, bloon.getColor());
        assertFalse(bloon.isCamo());
        assertFalse(bloon.isRegen());

        bloon.setDistanceTravelled(150);
        assertEquals(150, bloon.getDistanceTravelled());

        bloon.incrementDistanceTravelled();
        assertEquals(155, bloon.getDistanceTravelled());
    }

    @Test
    public void testGetColorFromHealth() {
        assertEquals(Bloon.Color.RED, Bloon.getColorFromHealth(1));
        assertEquals(Bloon.Color.BLUE, Bloon.getColorFromHealth(2));
        assertEquals(Bloon.Color.GREEN, Bloon.getColorFromHealth(3));
        assertEquals(Bloon.Color.YELLOW, Bloon.getColorFromHealth(4));
        assertEquals(Bloon.Color.PINK, Bloon.getColorFromHealth(5));
        assertEquals(Bloon.Color.BLACK, Bloon.getColorFromHealth(6));
        assertEquals(Bloon.Color.ZEBRA, Bloon.getColorFromHealth(7));
        assertEquals(Bloon.Color.RAINBOW, Bloon.getColorFromHealth(8));
    }
}
