package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.factories.MapFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BloonManagerTest {

    private Stage stage;
    private Map map;
    private BloonManager bloonManager;

    @BeforeClass
    public static void setUpClass() {
        HeadlessTestRunner.initialize();
    }

    @Before
    public void setUp() {
        stage = new Stage();
        map = MapFactory.createHeaterMap(stage);
        bloonManager = map.getBloonManager();
    }

    @Test
    public void testAttackBloonIfInRangeSelectsFurthestBloon() {
        GirlActor girlActor = new GirlActor(GirlFactory.createReimu(), 100, 100);
        girlActor.setActive(true);

        Bloon bloonShort = BloonFactory.createRedBloon();
        bloonShort.setDistanceTravelled(50);
        BloonActor actorShort = new BloonActor(bloonShort, 100, 120, null);

        Bloon bloonFurthest = BloonFactory.createBlueBloon();
        bloonFurthest.setDistanceTravelled(300);
        BloonActor actorFurthest = new BloonActor(bloonFurthest, 100, 130, null);

        Bloon bloonMedium = BloonFactory.createGreenBloon();
        bloonMedium.setDistanceTravelled(150);
        BloonActor actorMedium = new BloonActor(bloonMedium, 100, 140, null);

        bloonManager.addBloonToStage(actorShort);
        bloonManager.addBloonToStage(actorFurthest);
        bloonManager.addBloonToStage(actorMedium);

        boolean attacked = bloonManager.attackBloonIfInRange(girlActor);
        assertTrue("Tower should attack when bloons are in range", attacked);

        float dx = actorFurthest.getCenterX() - girlActor.getCenterX();
        float dy = actorFurthest.getCenterY() - girlActor.getCenterY();
        float expectedAngle = (float) (Math.atan2(dx, dy) / Math.PI * 180);

        assertEquals("GirlActor should look at the furthest bloon", expectedAngle, girlActor.getRotationAngle(), 0.01f);
    }

    @Test
    public void testTargetSelectionAfterParentBloonPops() {
        GirlActor girlActor = new GirlActor(GirlFactory.createReimu(), 100, 100);
        girlActor.setActive(true);

        Bloon parentBloon = BloonFactory.createBlueBloon();
        parentBloon.setDistanceTravelled(400);
        BloonActor parentActor = new BloonActor(parentBloon, 100, 120, null);

        Bloon otherBloon = BloonFactory.createRedBloon();
        otherBloon.setDistanceTravelled(200);
        BloonActor otherActor = new BloonActor(otherBloon, 100, 140, null);

        bloonManager.addBloonToStage(parentActor);
        bloonManager.addBloonToStage(otherActor);

        bloonManager.popBloon(parentActor, 1);

        boolean attacked = bloonManager.attackBloonIfInRange(girlActor);
        assertTrue("Tower should attack child bloon", attacked);

        float dx = parentActor.getCenterX() - girlActor.getCenterX();
        float dy = parentActor.getCenterY() - girlActor.getCenterY();
        float expectedAngle = (float) (Math.atan2(dx, dy) / Math.PI * 180);

        assertEquals("GirlActor should target child bloon with inherited lead distance",
                expectedAngle, girlActor.getRotationAngle(), 0.01f);
    }
}
