package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BloonTest {

    @Test
    public void testRedBloonCreation() {
        Bloon red = BloonFactory.createRedBloon();
        assertEquals(Bloon.Color.RED, red.getColor());
        assertEquals(1, red.getHealth());
        assertFalse(red.isCamo());
        assertFalse(red.isRegen());
        assertFalse(red.isBlimp());
        assertNotNull(red.getImageFileName());
    }

    @Test
    public void testBlimpClassification() {
        Bloon moab = BloonFactory.createMOAB();
        assertTrue(moab.isBlimp());

        Bloon red = BloonFactory.createRedBloon();
        assertFalse(red.isBlimp());
    }

    @Test
    public void testDistanceTravelled() {
        Bloon blue = BloonFactory.createBlueBloon();
        assertEquals(0, blue.getDistanceTravelled());
        int speed = blue.getSpeed();
        blue.incrementDistanceTravelled();
        assertEquals(speed, blue.getDistanceTravelled());
    }

    @Test
    public void testPopAndDamage() {
        Bloon green = BloonFactory.createGreenBloon();
        assertEquals(3, green.getHealth());

        assertTrue(green.willPopBloon(1));
        BloonPoppedResult result = green.pop(1);
        assertNotNull(result);

        green.damage(2);
        assertEquals(1, green.getHealth());
    }
}
