package com.hongbao.bloons.actors;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.hongbao.bloons.entities.Bullet;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class BulletActorTest {

    @BeforeClass
    public static void initGdx() {
        GL20 gl20 = (GL20) Proxy.newProxyInstance(
            GL20.class.getClassLoader(),
            new Class<?>[]{ GL20.class },
            (proxy, method, args) -> {
                if (method.getName().equals("glGenTexture")) {
                    return 1;
                }
                if (method.getReturnType().equals(int.class)) {
                    return 0;
                }
                return null;
            }
        );
        Gdx.gl = Gdx.gl20 = gl20;

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {}
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        }, config);
    }

    @Test
    public void testBulletActorStoresInitialCoordinates() {
        Bullet bullet = new Bullet(20f, 2, 2, 300f, false, "red_spell_card.png");
        BulletActor actor = new BulletActor(bullet, 500f, 400f, 1f, 0f);

        // Verify initial spawn coordinates are recorded
        assertEquals(actor.getX(), actor.getStartX(), 0.001f);
        assertEquals(actor.getY(), actor.getStartY(), 0.001f);
    }

    @Test
    public void testLinearBulletExpirationAtExactMaxRange() {
        float maxRange = 200f;
        float speed = 50f; // moves 10 units per frame (speed / 5)
        Bullet bullet = new Bullet(speed, 1, 1, maxRange, false, "red_spell_card.png");
        BulletActor actor = new BulletActor(bullet, 100f, 100f, 1f, 0f);
        Group parentGroup = new Group();
        parentGroup.addActor(actor);

        // Move 19 frames -> displacement = 19 * 10 = 190 < 200
        for (int i = 0; i < 19; i++) {
            actor.act(0.016f);
            assertNotNull("Bullet should remain active when displacement < maxRange", actor.getParent());
        }

        // At frame 20 -> displacement = 20 * 10 = 200 >= 200 -> should expire (removed from parent)
        actor.act(0.016f);
        assertNull("Bullet should be removed when displacement >= maxRange", actor.getParent());
    }

    @Test
    public void testSpeedUpgradesReachMaxRangeFasterWithoutShorteningDistance() {
        float maxRange = 300f;

        // Normal speed: 20 -> 4 units/frame -> 75 frames to cover 300 range
        Bullet bulletNormal = new Bullet(20f, 1, 1, maxRange, false, "red_spell_card.png");
        BulletActor actorNormal = new BulletActor(bulletNormal, 100f, 100f, 1f, 0f);
        Group parentNormal = new Group();
        parentNormal.addActor(actorNormal);

        // Upgraded speed: 100 -> 20 units/frame -> 15 frames to cover 300 range
        Bullet bulletUpgraded = new Bullet(100f, 1, 1, maxRange, false, "red_spell_card.png");
        BulletActor actorUpgraded = new BulletActor(bulletUpgraded, 100f, 100f, 1f, 0f);
        Group parentUpgraded = new Group();
        parentUpgraded.addActor(actorUpgraded);

        // Upgraded bullet reaches range 300 at frame 15
        for (int i = 0; i < 15; i++) {
            actorUpgraded.act(0.016f);
        }
        assertNull("Upgraded bullet should expire after reaching max radial distance", actorUpgraded.getParent());

        // Normal bullet is still active at frame 15 (displacement 60 < 300)
        for (int i = 0; i < 15; i++) {
            actorNormal.act(0.016f);
        }
        assertNotNull("Normal bullet should still be active at frame 15", actorNormal.getParent());

        // Both covered the exact same max range distance (300 units) from start position
        float normalDisplacement = (float) Math.hypot(actorNormal.getX() - actorNormal.getStartX(), actorNormal.getY() - actorNormal.getStartY());
        assertEquals(60f, normalDisplacement, 0.1f);
    }

    @Test
    public void testCurvedBulletStaysActiveWithinRadialBoundary() {
        // Range 500
        Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
        BulletActor actor = new BulletActor(bullet, 500f, 500f, 1f, 0f);
        Group parentGroup = new Group();
        parentGroup.addActor(actor);
        actor.setSpellCardOverride("Reimu");

        // Reimu circular trajectory for 100 frames
        for (int i = 0; i < 100; i++) {
            actor.act(0.016f);
        }

        float displacement = (float) Math.hypot(actor.getX() - actor.getStartX(), actor.getY() - actor.getStartY());
        assertTrue("Curved bullet displacement should be within 500 range boundary", displacement < 500f);
        assertNotNull("Curved bullet should remain active without needing 5000 maxRange override", actor.getParent());
    }
}
