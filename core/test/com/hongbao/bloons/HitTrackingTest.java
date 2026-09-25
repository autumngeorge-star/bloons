package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class HitTrackingTest {

    @BeforeClass
    public static void setUpGdx() {
        GdxNativesLoader.load();
        Gdx.files = new HeadlessFiles();
        GL20 glMock = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 1;
                    if (method.getReturnType().equals(float.class)) return 0f;
                    return null;
                }
        );
        Gdx.gl = glMock;
        Gdx.gl20 = glMock;
    }

    @Test
    public void testInstanceSpecificHitTracking() {
        Bullet bullet = new Bullet(10, 1, 3, 500, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 0, 0, 1, 0);

        Bloon parentBloonEntity = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor parentBloonActor = new BloonActor(parentBloonEntity, 0, 0, null);

        // Initial check: parent bloon not yet hit
        assertFalse("Bullet should not have hit parent bloon yet", bulletActor.hasDamagedBloon(parentBloonActor));

        // Mark parent bloon as damaged
        bulletActor.damageBloon(parentBloonActor);
        assertTrue("Bullet should have recorded parent bloon hit", bulletActor.hasDamagedBloon(parentBloonActor));

        // Spawn child bloon from parent
        Bloon childBloonEntity = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor childBloonActor = new BloonActor(childBloonEntity, 0, 0, parentBloonActor);

        // Child bloon must NOT be flagged as damaged based on parent's ID
        assertFalse("Child bloon must NOT inherit hit status from parent bloon",
                bulletActor.hasDamagedBloon(childBloonActor));
    }

    @Test
    public void testPierceDecrementAndHitTracking() {
        Bullet bullet = new Bullet(10, 1, 2, 500, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 0, 0, 1, 0);

        Bloon bloon1 = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor bloonActor1 = new BloonActor(bloon1, 0, 0, null);

        Bloon bloon2 = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor bloonActor2 = new BloonActor(bloon2, 0, 0, bloonActor1);

        assertEquals("Initial pierce should be 2", 2, bullet.getPierce());

        // Hit first bloon instance
        bulletActor.damageBloon(bloonActor1);
        bulletActor.decrementPierce();

        assertEquals("Pierce should be 1 after first hit", 1, bullet.getPierce());
        assertTrue("Bloon 1 marked as damaged", bulletActor.hasDamagedBloon(bloonActor1));
        assertFalse("Bloon 2 not marked as damaged", bulletActor.hasDamagedBloon(bloonActor2));

        // Hit second bloon instance (spawned from bloon 1)
        bulletActor.damageBloon(bloonActor2);
        bulletActor.decrementPierce();

        assertEquals("Pierce should be 0 after second hit", 0, bullet.getPierce());
        assertTrue("Bloon 2 marked as damaged", bulletActor.hasDamagedBloon(bloonActor2));
    }
}
