package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.nio.IntBuffer;

import static org.junit.Assert.*;

public class HomingTargetingTest {

    private static BloonsTouhouDefense testApp;

    @BeforeClass
    public static void initLibGdx() {
        if (Gdx.app == null) {
            GL20 dummyGl = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[] { GL20.class },
                (proxy, method, args) -> {
                    if (method.getName().equals("glGetShaderiv") || method.getName().equals("glGetProgramiv")) {
                        Object lastArg = args[args.length - 1];
                        if (lastArg instanceof IntBuffer) {
                            ((IntBuffer) lastArg).put(0, 1);
                        } else if (lastArg instanceof int[]) {
                            ((int[]) lastArg)[0] = 1;
                        }
                        return null;
                    }
                    if (method.getName().equals("glGetShaderInfoLog") || method.getName().equals("glGetProgramInfoLog")) {
                        return "";
                    }
                    if (method.getName().equals("glCreateShader") || method.getName().equals("glCreateProgram")) {
                        return 1;
                    }
                    if (method.getReturnType() == int.class) return 1;
                    if (method.getReturnType() == boolean.class) return true;
                    if (method.getReturnType() == String.class) return "";
                    return null;
                }
            );
            Gdx.gl = Gdx.gl20 = dummyGl;

            testApp = new BloonsTouhouDefense();
            new HeadlessApplication(testApp, new HeadlessApplicationConfiguration());
        }
    }

    private BloonManager createTestBloonManager(Stage stage) {
        BloonManager bloonManager = new BloonManager(stage, null);
        Map testMap = new Map(bloonManager);
        testApp.setMap(testMap);
        return bloonManager;
    }

    @Test
    public void testForwardFilteringPrioritizesForwardOverBackward() {
        Stage stage = new Stage();
        BloonManager bloonManager = createTestBloonManager(stage);

        // Bullet at (200, 200) moving right (dx = 1, dy = 0)
        Bullet bullet = new Bullet(10f, 1, 1, 1000f, true, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1.0f, 0.0f);

        // Bloon 1: Behind bullet at (100, 200), high track progress
        Bloon bloonBehind = new Bloon(Bloon.Color.RED, 1, false, false);
        bloonBehind.setDistanceTravelled(1000);
        BloonActor actorBehind = new BloonActor(bloonBehind, 100, 200, null);
        bloonManager.addBloonToStage(actorBehind);

        // Bloon 2: In front of bullet at (300, 200), lower track progress
        Bloon bloonAhead = new Bloon(Bloon.Color.RED, 1, false, false);
        bloonAhead.setDistanceTravelled(500);
        BloonActor actorAhead = new BloonActor(bloonAhead, 300, 200, null);
        bloonManager.addBloonToStage(actorAhead);

        // Target acquisition must choose actorAhead despite actorBehind having higher track progress
        BloonActor selectedTarget = bloonManager.getNewHomingTarget(bulletActor);
        assertNotNull(selectedTarget);
        assertEquals(actorAhead, selectedTarget);
    }

    @Test
    public void testTrackProgressPrioritization() {
        Stage stage = new Stage();
        BloonManager bloonManager = createTestBloonManager(stage);

        // Bullet at (200, 200) moving right (dx = 1, dy = 0)
        Bullet bullet = new Bullet(10f, 1, 1, 1000f, true, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1.0f, 0.0f);

        // Both bloons are ahead of bullet
        // Bloon 1: Near bullet at (300, 200), low track progress
        Bloon bloonNear = new Bloon(Bloon.Color.RED, 1, false, false);
        bloonNear.setDistanceTravelled(200);
        BloonActor actorNear = new BloonActor(bloonNear, 300, 200, null);
        bloonManager.addBloonToStage(actorNear);

        // Bloon 2: Further away at (400, 200), high track progress
        Bloon bloonFar = new Bloon(Bloon.Color.RED, 1, false, false);
        bloonFar.setDistanceTravelled(800);
        BloonActor actorFar = new BloonActor(bloonFar, 400, 200, null);
        bloonManager.addBloonToStage(actorFar);

        // Target acquisition must prioritize actorFar due to significantly higher track progress
        BloonActor selectedTarget = bloonManager.getNewHomingTarget(bulletActor);
        assertNotNull(selectedTarget);
        assertEquals(actorFar, selectedTarget);
    }

    @Test
    public void testFallbackToBackwardTargetsWhenNoForwardTargetsExist() {
        Stage stage = new Stage();
        BloonManager bloonManager = createTestBloonManager(stage);

        // Bullet at (200, 200) moving right (dx = 1, dy = 0)
        Bullet bullet = new Bullet(10f, 1, 1, 1000f, true, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1.0f, 0.0f);

        // Only bloon behind bullet at (100, 200)
        Bloon bloonBehind = new Bloon(Bloon.Color.RED, 1, false, false);
        bloonBehind.setDistanceTravelled(300);
        BloonActor actorBehind = new BloonActor(bloonBehind, 100, 200, null);
        bloonManager.addBloonToStage(actorBehind);

        // Should fall back to actorBehind since no forward targets exist
        BloonActor selectedTarget = bloonManager.getNewHomingTarget(bulletActor);
        assertNotNull(selectedTarget);
        assertEquals(actorBehind, selectedTarget);
    }

    @Test
    public void testAngularTurnRateLimiting() {
        Stage stage = new Stage();
        BloonManager bloonManager = createTestBloonManager(stage);

        // Bullet at (200, 200) moving right (dx = 1, dy = 0)
        Bullet bullet = new Bullet(10f, 1, 1, 1000f, true, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1.0f, 0.0f);

        // Target bloon directly above at (200, 300) -> desired direction (0, 1) (90 degrees / PI/2 rad)
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actorAbove = new BloonActor(bloon, 200, 300, null);
        bloonManager.addBloonToStage(actorAbove);

        // Execute frame update
        bulletActor.act(1.0f / 60.0f);

        // The turn rate should be limited by MAX_TURN_RATE_PER_FRAME (0.10 rad)
        float expectedDx = (float) Math.cos(BulletActor.MAX_TURN_RATE_PER_FRAME);
        float expectedDy = (float) Math.sin(BulletActor.MAX_TURN_RATE_PER_FRAME);

        assertEquals(expectedDx, bulletActor.getDx(), 0.01f);
        assertEquals(expectedDy, bulletActor.getDy(), 0.01f);

        // Ensure it did NOT instantly snap to (0, 1)
        assertTrue(bulletActor.getDx() > 0.9f);
        assertTrue(bulletActor.getDy() < 0.2f);
    }

    @Test
    public void testRetargetingWhenTargetMovesBehind() {
        Stage stage = new Stage();
        BloonManager bloonManager = createTestBloonManager(stage);

        // Bullet at (200, 200) moving right (dx = 1, dy = 0)
        Bullet bullet = new Bullet(10f, 1, 1, 1000f, true, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1.0f, 0.0f);

        // Bloon 1: Behind bullet
        Bloon bloonBehind = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actorBehind = new BloonActor(bloonBehind, 100, 200, null);
        bloonManager.addBloonToStage(actorBehind);

        // Manually set target to actorBehind
        bulletActor.setTarget(actorBehind);

        // Bloon 2: In front of bullet
        Bloon bloonAhead = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actorAhead = new BloonActor(bloonAhead, 300, 200, null);
        bloonManager.addBloonToStage(actorAhead);

        // Frame update should detect target is behind and switch to actorAhead
        bulletActor.act(1.0f / 60.0f);

        assertEquals(actorAhead, bulletActor.getTarget());
    }

    @Test
    public void testNonHomingBulletsMaintainStraightLine() {
        Stage stage = new Stage();
        BloonManager bloonManager = createTestBloonManager(stage);

        // Non-homing bullet moving right
        Bullet bullet = new Bullet(10f, 1, 1, 1000f, false, "blue_magic_missile.png");
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1.0f, 0.0f);

        // Add bloon above
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actorAbove = new BloonActor(bloon, 200, 300, null);
        bloonManager.addBloonToStage(actorAbove);

        bulletActor.act(1.0f / 60.0f);

        // Non-homing bullet direction should remain exactly (1, 0)
        assertEquals(1.0f, bulletActor.getDx(), 0.001f);
        assertEquals(0.0f, bulletActor.getDy(), 0.001f);
    }
}
