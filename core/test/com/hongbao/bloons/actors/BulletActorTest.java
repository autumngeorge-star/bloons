package com.hongbao.bloons.actors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BulletActorTest {

    private static BloonsTouhouDefense game;

    @BeforeClass
    public static void setUpClass() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        game = new BloonsTouhouDefense();
        new HeadlessApplication(game, config);
    }

    @Test
    public void testNormalSpeedSubStepCount() {
        Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
        float totalDisplacement = bullet.getSpeed() / 5.0f; // 4.0
        float safetyStride = BulletActor.MIN_TARGET_COLLISION_RADIUS; // 9.0
        int numSubSteps = (int) Math.ceil(totalDisplacement / safetyStride);
        assertEquals(1, numSubSteps);
    }

    @Test
    public void testHighSpeedSubStepCount() {
        Bullet bullet = new Bullet(100f, 1, 1, 500f, false, "red_spell_card.png");
        float totalDisplacement = bullet.getSpeed() / 5.0f; // 20.0
        float safetyStride = BulletActor.MIN_TARGET_COLLISION_RADIUS; // 9.0
        int numSubSteps = (int) Math.ceil(totalDisplacement / safetyStride);
        assertEquals(3, numSubSteps);
    }

    @Test
    public void testSubStepCapping() {
        Bullet bullet = new Bullet(1000f, 1, 1, 500f, false, "red_spell_card.png");
        float totalDisplacement = bullet.getSpeed() / 5.0f; // 200.0
        float safetyStride = BulletActor.MIN_TARGET_COLLISION_RADIUS; // 9.0
        int numSubSteps = (int) Math.ceil(totalDisplacement / safetyStride);
        if (numSubSteps > BulletActor.MAX_SUB_STEPS) {
            numSubSteps = BulletActor.MAX_SUB_STEPS;
        }
        assertEquals(BulletActor.MAX_SUB_STEPS, numSubSteps);
    }

    @Test
    public void testBulletMovementAndDistanceTraveled() {
        Gdx.app.postRunnable(() -> {
            Stage stage = new Stage();
            Map map = new Map("default_map.png", stage);
            // set directions so map is non-null
            map.setDirections(new com.hongbao.bloons.helpers.Pair[50][50]);
            
            Bullet bullet = new Bullet(100f, 1, 1, 500f, false, "red_spell_card.png");
            BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);
            stage.addActor(bulletActor);

            float initialX = bulletActor.getX();
            float initialY = bulletActor.getY();

            bulletActor.act(0.016f);

            // Total displacement = 100 / 5 = 20f in x direction
            assertEquals(initialX + 20f, bulletActor.getX(), 0.01f);
            assertEquals(initialY, bulletActor.getY(), 0.01f);
            assertEquals(20f, bullet.getDistanceTraveled(), 0.01f);
        });
    }

    @Test
    public void testPierceDepletionStopsSubSteppingEarly() {
        Gdx.app.postRunnable(() -> {
            Stage stage = new Stage();
            Map map = new Map("default_map.png", stage);
            map.setDirections(new com.hongbao.bloons.helpers.Pair[50][50]);

            // High speed bullet: 100f speed -> 3 sub-steps (20f / 3 = 6.6667f per sub-step)
            // Pierce = 1
            Bullet bullet = new Bullet(100f, 1, 1, 500f, false, "red_spell_card.png");
            BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);
            stage.addActor(bulletActor);

            // Place a bloon in the path of the 1st sub-step
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            BloonActor bloonActor = new BloonActor(bloon, 104f, 100f, null);
            stage.addActor(bloonActor);
            map.getBloonManager().containsBloon(bloonActor);

            bulletActor.act(0.016f);

            // Bullet pierce should be 0 and bullet should be removed after 1st sub-step
            assertEquals(0, bullet.getPierce());
            assertNull(bulletActor.getStage());
            // Position should be after 1 sub-step (~106.67), not 3 sub-steps (~120.0)
            assertEquals(bullet.getInitialXOffset() + 100f + 20f / 3f, bulletActor.getX(), 0.5f);
        });
    }
}
