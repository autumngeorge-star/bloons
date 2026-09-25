package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Set;

import static org.junit.Assert.*;

public class CollisionTest {

    @BeforeClass
    public static void setUpGdx() {
        if (Gdx.app == null) {
            GL20 glMock = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("glGetShaderiv") || method.getName().equals("glGetProgramiv")) {
                        if (args.length >= 3 && args[2] instanceof java.nio.IntBuffer) {
                            ((java.nio.IntBuffer) args[2]).put(0, 1);
                        } else if (args.length >= 3 && args[2] instanceof int[]) {
                            ((int[]) args[2])[0] = 1;
                        }
                        return null;
                    }
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 1;
                    if (method.getReturnType().equals(String.class)) return "";
                    return null;
                }
            );
            Gdx.gl = glMock;
            Gdx.gl20 = glMock;
            Gdx.files = new HeadlessFiles();
            Gdx.audio = new MockAudio();
            BloonsTouhouDefense app = new BloonsTouhouDefense();
            try {
                Field playerField = BloonsTouhouDefense.class.getDeclaredField("player");
                playerField.setAccessible(true);
                playerField.set(app, new Player(100, 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Gdx.app = new HeadlessApplication(app);
        }
    }

    @Test
    public void testBulletPrevPositionTracking() {
        Bullet bullet = new Bullet(100f, 1, 1, 1000f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 100, 100, 1, 0);

        assertEquals(100f, bulletActor.getPrevCenterX(), 0.01f);
        assertEquals(100f, bulletActor.getPrevCenterY(), 0.01f);
    }

    @Test
    public void testHighSpeedBulletTrajectorySweep() throws Exception {
        // High speed bullet (speed = 250 -> step = 50 per frame)
        Bullet bullet = new Bullet(250f, 1, 1, 1000f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 100, 100, 1, 0);

        // Bloon positioned at (125, 100) - directly between P0 (100, 100) and P1 (150, 100)
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor bloonActor = new BloonActor(bloon, 125, 100, null);

        BloonManager bloonManager = new BloonManager(new Stage(), null);
        Field onstageField = BloonManager.class.getDeclaredField("onstageBloons");
        onstageField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<BloonActor> onstageBloons = (Set<BloonActor>) onstageField.get(bloonManager);
        onstageBloons.add(bloonActor);

        // Advance bullet position from (100, 100) to (150, 100)
        Field prevXField = BulletActor.class.getDeclaredField("prevCenterX");
        prevXField.setAccessible(true);
        prevXField.setFloat(bulletActor, 100f);
        bulletActor.setX(150f - bulletActor.getWidth() / 2f);

        assertEquals(100f, bulletActor.getPrevCenterX(), 0.01f);
        assertEquals(150f, bulletActor.getCenterX(), 0.01f);

        // Verify discrete distance check would miss the bloon
        float discreteDistance = Map.distanceBetweenActors(bulletActor, bloonActor);
        float collisionRadius = bloonActor.getCollisionRadius() + bulletActor.getCollisionRadius();
        assertTrue("Discrete check would miss", discreteDistance >= collisionRadius);

        // Run checkCollision with continuous raycast trajectory sweeping
        bloonManager.checkCollision(bulletActor);

        // Verify bloon was hit and damaged by bullet
        assertTrue("Bullet damaged bloon along trajectory", bulletActor.hasDamagedBloon(bloonActor));
        assertEquals(0, bullet.getPierce());
    }

    @Test
    public void testMultiPierceImpactSequenceOrder() throws Exception {
        // Bullet with pierce = 2 moving from (0, 100) to (300, 100)
        Bullet bullet = new Bullet(1500f, 1, 2, 2000f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 0, 100, 1, 0);

        Bloon bloon1 = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actor1 = new BloonActor(bloon1, 100, 100, null); // t = 0.33

        Bloon bloon2 = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actor2 = new BloonActor(bloon2, 200, 100, null); // t = 0.67

        Bloon bloon3 = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actor3 = new BloonActor(bloon3, 250, 100, null); // t = 0.83

        BloonManager bloonManager = new BloonManager(new Stage(), null);
        Field onstageField = BloonManager.class.getDeclaredField("onstageBloons");
        onstageField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<BloonActor> onstageBloons = (Set<BloonActor>) onstageField.get(bloonManager);
        onstageBloons.add(actor3); // add in non-sorted order to test sorting
        onstageBloons.add(actor1);
        onstageBloons.add(actor2);

        Field prevXField = BulletActor.class.getDeclaredField("prevCenterX");
        prevXField.setAccessible(true);
        prevXField.setFloat(bulletActor, 0f);
        bulletActor.setX(300f - bulletActor.getWidth() / 2f);

        bloonManager.checkCollision(bulletActor);

        // Bloon 1 and Bloon 2 should be hit first; Bloon 3 should NOT be hit because pierce=2
        assertTrue("First bloon along vector damaged", bulletActor.hasDamagedBloon(actor1));
        assertTrue("Second bloon along vector damaged", bulletActor.hasDamagedBloon(actor2));
        assertFalse("Third bloon not damaged because pierce exhausted", bulletActor.hasDamagedBloon(actor3));
        assertEquals(0, bullet.getPierce());
    }

    @Test
    public void testZeroMovementStepEdgeCase() throws Exception {
        Bullet bullet = new Bullet(0f, 1, 1, 1000f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 100, 100, 0, 0);

        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor bloonActor = new BloonActor(bloon, 100, 100, null);

        BloonManager bloonManager = new BloonManager(new Stage(), null);
        Field onstageField = BloonManager.class.getDeclaredField("onstageBloons");
        onstageField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<BloonActor> onstageBloons = (Set<BloonActor>) onstageField.get(bloonManager);
        onstageBloons.add(bloonActor);

        bloonManager.checkCollision(bulletActor);

        assertTrue("Collision detected at zero movement", bulletActor.hasDamagedBloon(bloonActor));
    }
}
