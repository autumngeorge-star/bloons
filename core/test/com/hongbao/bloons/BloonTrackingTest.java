package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.factories.BloonFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class BloonTrackingTest {

    @BeforeClass
    public static void setUpGdx() {
        if (Gdx.app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationListener() {
                @Override public void create() {}
                @Override public void resize(int width, int height) {}
                @Override public void render() {}
                @Override public void pause() {}
                @Override public void resume() {}
                @Override public void dispose() {}
            }, config);

            // Mock GL20 interface to prevent NullPointerException on texture creation in headless mode
            Gdx.gl20 = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 1;
                    return null;
                }
            );
            Gdx.gl = Gdx.gl20;
        }
    }

    @Test
    public void testBloonActorHasNoParentBloonIdsMethod() {
        try {
            Method method = BloonActor.class.getMethod("getParentBloonIds");
            fail("getParentBloonIds method should no longer exist on BloonActor");
        } catch (NoSuchMethodException e) {
            // Expected - method was removed
        }
    }

    @Test
    public void testBloonActorUniqueIds() {
        Bloon redBloon1 = BloonFactory.createRedBloon();
        Bloon redBloon2 = BloonFactory.createRedBloon();

        BloonActor actor1 = new BloonActor(redBloon1, 0, 0);
        BloonActor actor2 = new BloonActor(redBloon2, 0, 0);

        assertNotNull("bloonId must not be null", actor1.getBloonId());
        assertNotNull("bloonId must not be null", actor2.getBloonId());
        assertNotEquals("Each BloonActor must receive a unique bloonId", actor1.getBloonId(), actor2.getBloonId());
    }

    @Test
    public void testBulletActorDamagedBloonsDirectTracking() {
        Bloon parentBloon = BloonFactory.createBlueBloon();
        BloonActor parentActor = new BloonActor(parentBloon, 100, 100);

        Bullet bullet = new Bullet(10f, 1, 2, 500f, false, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(bullet, 0, 0, 1, 0);

        assertFalse("Bullet actor initially hasn't damaged parent bloon", bulletActor.hasDamagedBloon(parentActor));

        bulletActor.damageBloon(parentActor);
        assertTrue("Bullet actor has damaged parent bloon after damageBloon call", bulletActor.hasDamagedBloon(parentActor));

        // Create child bloon entity spawned from parent bloon
        Bloon childBloon = BloonFactory.createRedBloon();
        BloonActor childActor = new BloonActor(childBloon, 100, 100);

        assertFalse("Bullet actor has NOT damaged child bloon despite parent bloon being damaged", bulletActor.hasDamagedBloon(childActor));
    }

    @Test
    public void testHomingBulletRetargetsChildBloon() {
        Bullet homingBullet = new Bullet(10f, 1, 2, 500f, true, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(homingBullet, 0, 0, 1, 0);

        Bloon parentBloon = BloonFactory.createBlueBloon();
        BloonActor parentActor = new BloonActor(parentBloon, 50, 0);

        // Bullet damages parent bloon
        bulletActor.damageBloon(parentActor);

        // Spawn child bloon upon popping parent
        Bloon childBloon = BloonFactory.createRedBloon();
        BloonActor childActor = new BloonActor(childBloon, 50, 0);

        // Verify that bulletActor does not consider childBloon damaged
        assertFalse(bulletActor.hasDamagedBloon(childActor));
    }
}
