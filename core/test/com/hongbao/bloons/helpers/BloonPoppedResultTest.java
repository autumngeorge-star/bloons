package com.hongbao.bloons.helpers;

import com.hongbao.bloons.HeadlessTestRunner;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BloonPoppedResultTest {

    @BeforeClass
    public static void setUp() {
        HeadlessTestRunner.initialize();
    }

    @Test
    public void testChildBloonsInheritParentDistance() {
        Bloon blueBloon = BloonFactory.createBlueBloon();
        int parentDistance = 350;
        blueBloon.setDistanceTravelled(parentDistance);

        BloonPoppedResult result = blueBloon.pop(1);

        assertFalse(result.getBloonsGenerated().isEmpty());
        for (Bloon child : result.getBloonsGenerated()) {
            assertEquals("Child bloon must inherit parent's distance travelled",
                    parentDistance, child.getDistanceTravelled());
        }
    }

    @Test
    public void testCeramicBloonChildInheritance() {
        Bloon ceramicBloon = BloonFactory.createCeramicBloon();
        int parentDistance = 720;
        ceramicBloon.setDistanceTravelled(parentDistance);

        BloonPoppedResult result = ceramicBloon.pop(10); // Damaged down to rainbow/other tier

        assertFalse(result.getBloonsGenerated().isEmpty());
        for (Bloon child : result.getBloonsGenerated()) {
            assertEquals("Child bloon from ceramic must inherit parent's distance travelled",
                    parentDistance, child.getDistanceTravelled());
        }
    }
}
