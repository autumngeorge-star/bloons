package com.hongbao.bloons.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.hongbao.bloons.entities.Bullet;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BulletFrameLifetimeTest {

    private Group container;

    @Before
    public void setUp() {
        container = new Group();
    }

    @Test
    public void testDefaultMaxFramesIsZero() {
        Bullet bullet = new Bullet();
        assertEquals(0, bullet.getMaxFrames());

        Bullet bulletParam = new Bullet(10f, 1, 1, 100f, false, "red_spell_card.png");
        assertEquals(0, bulletParam.getMaxFrames());
    }

    @Test
    public void testSetAndGetMaxFrames() {
        Bullet bullet = new Bullet();
        bullet.setMaxFrames(120);
        assertEquals(120, bullet.getMaxFrames());

        Bullet bulletWithMaxFrames = new Bullet(10f, 1, 1, 100f, false, "red_spell_card.png", 300);
        assertEquals(300, bulletWithMaxFrames.getMaxFrames());
    }

    @Test
    public void testStaticBulletExpiresAtMaxFrames() {
        // Create a stationary bullet (speed = 0) with maxFrames = 50
        Bullet bullet = new Bullet(0f, 1, 1, 1000f, false, "red_spell_card.png", 50);
        // Position it well within stage bounds (e.g. x=500, y=500)
        BulletActor bulletActor = new BulletActor(bullet, 500f, 500f, 0f, 0f);
        container.addActor(bulletActor);

        assertNotNull(bulletActor.getParent());
        assertEquals(0, bulletActor.getFrames());

        // Run for 49 frames
        for (int i = 0; i < 49; i++) {
            bulletActor.act(0.016f);
            assertEquals(i + 1, bulletActor.getFrames());
            assertNotNull("Bullet should remain in parent before maxFrames is reached", bulletActor.getParent());
        }

        // On the 50th frame, frame count reaches maxFrames (50)
        bulletActor.act(0.016f);
        assertEquals(50, bulletActor.getFrames());
        assertNull("Bullet should be removed from parent when maxFrames is reached", bulletActor.getParent());
    }

    @Test
    public void testUnconfiguredMaxFramesAllowsUnlimitedFrames() {
        // Create a stationary bullet with maxFrames = 0 (unconfigured)
        Bullet bullet = new Bullet(0f, 1, 1, 1000f, false, "red_spell_card.png", 0);
        BulletActor bulletActor = new BulletActor(bullet, 500f, 500f, 0f, 0f);
        container.addActor(bulletActor);

        // Run for 200 frames
        for (int i = 0; i < 200; i++) {
            bulletActor.act(0.016f);
            assertNotNull("Bullet without maxFrames should not expire via frame count", bulletActor.getParent());
        }
        assertEquals(200, bulletActor.getFrames());
    }

    @Test
    public void testStandardMovingBulletExpiresViaMaxRange() {
        // Create a moving bullet (speed = 50, maxRange = 100) with maxFrames = 0
        Bullet bullet = new Bullet(50f, 1, 1, 100f, false, "red_spell_card.png", 0);
        BulletActor bulletActor = new BulletActor(bullet, 500f, 500f, 1f, 0f);
        container.addActor(bulletActor);

        // distanceTraveled increments by speed / 5 = 10 per act call.
        // Needs 10 frames to reach maxRange 100.
        for (int i = 0; i < 9; i++) {
            bulletActor.act(0.016f);
            assertNotNull("Moving bullet should not expire before maxRange", bulletActor.getParent());
        }

        // 10th frame: distanceTraveled reaches 100 >= maxRange
        bulletActor.act(0.016f);
        assertNull("Moving bullet should expire upon reaching maxRange", bulletActor.getParent());
    }
}
