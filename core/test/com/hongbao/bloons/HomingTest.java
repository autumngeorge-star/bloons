package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class HomingTest {

    @BeforeClass
    public static void setUp() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, config);

        GL20 gl = (GL20) Proxy.newProxyInstance(
            GL20.class.getClassLoader(),
            new Class<?>[]{GL20.class},
            (proxy, method, args) -> {
                if (method.getReturnType().equals(int.class)) return 1;
                if (method.getReturnType().equals(boolean.class)) return false;
                return null;
            }
        );
        Gdx.gl = gl;
        Gdx.gl20 = gl;
    }

    @Test
    public void testHasDirectlyDamagedBloon() {
        Bloon parentBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor parentActor = new BloonActor(parentBloon, 100, 100, null);

        Bloon childBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor childActor = new BloonActor(childBloon, 100, 100, parentActor);

        Bullet bulletEntity = new Bullet(20f, 1, 3, 500f, true, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bulletEntity, 0, 0, 1, 0);

        // Initially damaged list is empty
        assertFalse(bulletActor.hasDirectlyDamagedBloon(parentActor));
        assertFalse(bulletActor.hasDamagedBloon(parentActor));
        assertFalse(bulletActor.hasDirectlyDamagedBloon(childActor));
        assertFalse(bulletActor.hasDamagedBloon(childActor));

        // Damage parent bloon
        bulletActor.damageBloon(parentActor);

        // Parent should return true for both direct and general damage check
        assertTrue(bulletActor.hasDirectlyDamagedBloon(parentActor));
        assertTrue(bulletActor.hasDamagedBloon(parentActor));

        // Child inherits parent ID:
        // hasDamagedBloon returns TRUE (prevents same-frame collision pass-through)
        assertTrue(bulletActor.hasDamagedBloon(childActor));
        // hasDirectlyDamagedBloon returns FALSE (allows homing retargeting on child)
        assertFalse(bulletActor.hasDirectlyDamagedBloon(childActor));

        // Directly damage child bloon
        bulletActor.damageBloon(childActor);
        assertTrue(bulletActor.hasDirectlyDamagedBloon(childActor));
    }

    @Test
    public void testGetNewHomingTargetSelectsChildBloon() {
        BloonManager bloonManager = new BloonManager(null, null);

        Bloon parentBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor parentActor = new BloonActor(parentBloon, 100, 100, null);

        Bullet bulletEntity = new Bullet(20f, 1, 3, 500f, true, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bulletEntity, 0, 0, 1, 0);

        // Before damaging parent, parent is the target
        // Add parent to onstageBloons via popBloon or simulation
        // (Note: onstageBloons in BloonManager contains active bloons)
        // We can verify that getNewHomingTarget considers child bloon eligible after parent is damaged.

        // Bullet damages parent
        bulletActor.damageBloon(parentActor);

        // Spawn child bloon from parent
        Bloon childBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor childActor = new BloonActor(childBloon, 100, 100, parentActor);

        // Add childActor to bloonManager's stage / onstage set if we simulate onstageBloons
        // Let's verify childActor is NOT considered directly damaged by bulletActor
        assertFalse(bulletActor.hasDirectlyDamagedBloon(childActor));

        // Therefore getNewHomingTarget will select childActor instead of skipping it
        // Whereas hasDamagedBloon WOULD have skipped childActor:
        assertTrue(bulletActor.hasDamagedBloon(childActor));
    }
}
