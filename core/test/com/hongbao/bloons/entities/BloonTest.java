package com.hongbao.bloons.entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class BloonTest {

    @Test
    public void testDefaultStatusEffectsAreFalse() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertFalse(bloon.isFrozen());
        assertFalse(bloon.isSlowed());
        assertFalse(bloon.isBurned());
    }

    @Test
    public void testSetAndGetFrozen() {
        Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        bloon.setFrozen(true);
        assertTrue(bloon.isFrozen());
        bloon.setFrozen(false);
        assertFalse(bloon.isFrozen());
    }

    @Test
    public void testSetAndGetSlowed() {
        Bloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false);
        bloon.setSlowed(true);
        assertTrue(bloon.isSlowed());
        bloon.setSlowed(false);
        assertFalse(bloon.isSlowed());
    }

    @Test
    public void testSetAndGetBurned() {
        Bloon bloon = new Bloon(Bloon.Color.YELLOW, 4, false, false);
        bloon.setBurned(true);
        assertTrue(bloon.isBurned());
        bloon.setBurned(false);
        assertFalse(bloon.isBurned());
    }

    @Test
    public void testClearStatusEffects() {
        Bloon bloon = new Bloon(Bloon.Color.PINK, 5, false, false);
        bloon.setFrozen(true);
        bloon.setSlowed(true);
        bloon.setBurned(true);

        assertTrue(bloon.isFrozen());
        assertTrue(bloon.isSlowed());
        assertTrue(bloon.isBurned());

        bloon.clearStatusEffects();

        assertFalse(bloon.isFrozen());
        assertFalse(bloon.isSlowed());
        assertFalse(bloon.isBurned());
    }
}
