package com.hongbao.bloons.entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class BloonTest {

    @Test
    public void testDefaultState() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertFalse(bloon.isFrozen());
        assertEquals(1.0f, bloon.getSpeedMultiplier(), 0.0001f);
        int baseSpeed = Bloon.COLOR_TO_SPEED.get(Bloon.Color.RED);
        assertEquals((float) baseSpeed, bloon.getEffectiveSpeed(), 0.0001f);
        assertEquals(0.0f, bloon.getDistanceTravelled(), 0.0001f);
    }

    @Test
    public void testSetFrozen() {
        Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        bloon.setFrozen(true);
        assertTrue(bloon.isFrozen());
        assertEquals(0.0f, bloon.getEffectiveSpeed(), 0.0001f);

        bloon.setFrozen(false);
        assertFalse(bloon.isFrozen());
        int baseSpeed = Bloon.COLOR_TO_SPEED.get(Bloon.Color.BLUE);
        assertEquals((float) baseSpeed, bloon.getEffectiveSpeed(), 0.0001f);
    }

    @Test
    public void testSetSpeedMultiplier() {
        Bloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false);
        int baseSpeed = Bloon.COLOR_TO_SPEED.get(Bloon.Color.GREEN);

        bloon.setSpeedMultiplier(0.5f);
        assertEquals(0.5f, bloon.getSpeedMultiplier(), 0.0001f);
        assertEquals(baseSpeed * 0.5f, bloon.getEffectiveSpeed(), 0.0001f);

        bloon.setSpeedMultiplier(1.5f);
        assertEquals(1.5f, bloon.getSpeedMultiplier(), 0.0001f);
        assertEquals(baseSpeed * 1.5f, bloon.getEffectiveSpeed(), 0.0001f);
    }

    @Test
    public void testSpeedMultiplierClampNegative() {
        Bloon bloon = new Bloon(Bloon.Color.YELLOW, 4, false, false);
        bloon.setSpeedMultiplier(-0.5f);
        assertEquals(0.0f, bloon.getSpeedMultiplier(), 0.0001f);
        assertEquals(0.0f, bloon.getEffectiveSpeed(), 0.0001f);
    }

    @Test
    public void testEffectiveSpeedWhenFrozenAndMultiplier() {
        Bloon bloon = new Bloon(Bloon.Color.PINK, 5, false, false);
        bloon.setSpeedMultiplier(0.5f);
        bloon.setFrozen(true);

        // Effective speed should be 0 when frozen even if multiplier is non-zero
        assertEquals(0.0f, bloon.getEffectiveSpeed(), 0.0001f);

        // Unfreezing restores baseSpeed * speedMultiplier
        bloon.setFrozen(false);
        int baseSpeed = Bloon.COLOR_TO_SPEED.get(Bloon.Color.PINK);
        assertEquals(baseSpeed * 0.5f, bloon.getEffectiveSpeed(), 0.0001f);
    }

    @Test
    public void testIncrementDistanceTravelled() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        int baseSpeed = Bloon.COLOR_TO_SPEED.get(Bloon.Color.RED);

        bloon.incrementDistanceTravelled();
        assertEquals((float) baseSpeed, bloon.getDistanceTravelled(), 0.0001f);

        // Apply slow multiplier
        bloon.setSpeedMultiplier(0.5f);
        bloon.incrementDistanceTravelled();
        assertEquals(baseSpeed + (baseSpeed * 0.5f), bloon.getDistanceTravelled(), 0.0001f);

        // Freeze bloon
        float distanceBeforeFreeze = bloon.getDistanceTravelled();
        bloon.setFrozen(true);
        bloon.incrementDistanceTravelled();
        assertEquals(distanceBeforeFreeze, bloon.getDistanceTravelled(), 0.0001f);
    }
}
