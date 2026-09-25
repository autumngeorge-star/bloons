package com.hongbao.bloons.entities;

import com.hongbao.bloons.HeadlessTestRunner;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BloonTest {

    @BeforeClass
    public static void setUp() {
        HeadlessTestRunner.initialize();
    }

    @Test
    public void testIncrementDistanceTravelledWhenMoving() {
        Bloon bloon = BloonFactory.createRedBloon();
        int initialDistance = bloon.getDistanceTravelled();
        int speed = bloon.getSpeed();

        bloon.incrementDistanceTravelled();
        assertEquals(initialDistance + speed, bloon.getDistanceTravelled());

        bloon.incrementDistanceTravelled(new Pair<>(1f, 0f));
        assertEquals(initialDistance + 2 * speed, bloon.getDistanceTravelled());

        bloon.incrementDistanceTravelled(0f, 1f);
        assertEquals(initialDistance + 3 * speed, bloon.getDistanceTravelled());
    }

    @Test
    public void testSuppressDistanceAccumulationWhenStationary() {
        Bloon bloon = BloonFactory.createRedBloon();
        bloon.setDistanceTravelled(100);

        bloon.incrementDistanceTravelled(new Pair<>(0f, 0f));
        assertEquals(100, bloon.getDistanceTravelled());

        bloon.incrementDistanceTravelled(0f, 0f);
        assertEquals(100, bloon.getDistanceTravelled());
    }
}
