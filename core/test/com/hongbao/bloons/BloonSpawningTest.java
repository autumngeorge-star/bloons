package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.MapFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BloonSpawningTest {

    @BeforeClass
    public static void initGdx() {
        if (Gdx.app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {}, config);
        }
        if (Gdx.gl == null) {
            GL20 gl = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if (name.equals("glCreateShader") || name.equals("glCreateProgram")) {
                        return 1;
                    }
                    if (name.equals("glGetShaderiv") || name.equals("glGetProgramiv")) {
                        if (args != null && args.length > 2 && args[2] instanceof java.nio.IntBuffer) {
                            int pname = (Integer) args[1];
                            java.nio.IntBuffer ib = (java.nio.IntBuffer) args[2];
                            if (pname == GL20.GL_COMPILE_STATUS || pname == GL20.GL_LINK_STATUS) {
                                ib.put(0, 1);
                            } else {
                                ib.put(0, 0);
                            }
                        }
                        return null;
                    }
                    if (name.endsWith("InfoLog")) {
                        return "";
                    }
                    if (method.getReturnType() == boolean.class) return false;
                    if (method.getReturnType() == int.class) return 0;
                    if (method.getReturnType() == float.class) return 0f;
                    return null;
                }
            );
            Gdx.gl = gl;
            Gdx.gl20 = gl;
        }
    }

    @Test
    public void testStraightLineChildBloonSpacingAndDistance() throws Exception {
        Stage stage = new Stage();
        Map map = MapFactory.createBasicMap(stage);
        BloonManager bloonManager = map.getBloonManager();

        // Create a parent Black bloon (pops into 2 Pink bloons)
        Bloon blackBloon = new Bloon(Bloon.Color.BLACK, 6, false, false);
        blackBloon.setDistanceTravelled(500);

        BloonActor parentActor = new BloonActor(blackBloon, 200f, 425f, null);
        bloonManager.popBloon(parentActor, 1);

        // Get onstage bloons using reflection
        Field onstageField = BloonManager.class.getDeclaredField("onstageBloons");
        onstageField.setAccessible(true);
        Set<BloonActor> onstageBloons = (Set<BloonActor>) onstageField.get(bloonManager);

        assertEquals(2, onstageBloons.size());

        List<BloonActor> children = new ArrayList<>(onstageBloons);
        // Sort children by distanceTravelled descending (lead child first)
        children.sort((a, b) -> Integer.compare(b.getBloon().getDistanceTravelled(), a.getBloon().getDistanceTravelled()));

        BloonActor leadChild = children.get(0);
        BloonActor trailingChild = children.get(1);

        // Requirement 1 & 2: Lead child at parent position, trailing child separated by collisionRadius * 1.5f
        assertEquals(200f, leadChild.getCenterX(), 0.01f);
        assertEquals(425f, leadChild.getCenterY(), 0.01f);
        assertEquals(500, leadChild.getBloon().getDistanceTravelled());

        float expectedSeparation = trailingChild.getCollisionRadius() * 1.5f;
        float actualDistance = (float) Math.hypot(leadChild.getCenterX() - trailingChild.getCenterX(),
                                                leadChild.getCenterY() - trailingChild.getCenterY());

        assertEquals(expectedSeparation, actualDistance, 0.01f);

        // Requirement 3: distanceTravelled accurately reflects physical separation
        int expectedTrailingDistance = 500 - Math.round(expectedSeparation);
        assertEquals(expectedTrailingDistance, trailingChild.getBloon().getDistanceTravelled());
    }

    @Test
    public void testCornerTurnChildBloonSpacing() throws Exception {
        Stage stage = new Stage();
        Map map = MapFactory.createMapWithTurn(stage);
        BloonManager bloonManager = map.getBloonManager();

        // Position parent bloon past the turn (at tile x=4, y=9 going UP)
        // Tile 4 center X is 4 * 50 + 25 - 50 = 175 (xTile = (175 + 50)/50 = 4.5 -> 4)
        // Tile 9 center Y is 9 * 50 + 25 = 475
        Bloon bfb = new Bloon(Bloon.Color.BFB, 400, false, false);
        bfb.setDistanceTravelled(300);

        BloonActor parentActor = new BloonActor(bfb, 175f, 475f, null);
        bloonManager.popBloon(parentActor, 200); // Pops BFB into 4 MOABs

        Field onstageField = BloonManager.class.getDeclaredField("onstageBloons");
        onstageField.setAccessible(true);
        Set<BloonActor> onstageBloons = (Set<BloonActor>) onstageField.get(bloonManager);

        assertEquals(4, onstageBloons.size());

        List<BloonActor> children = new ArrayList<>(onstageBloons);
        children.sort((a, b) -> Integer.compare(b.getBloon().getDistanceTravelled(), a.getBloon().getDistanceTravelled()));

        // Verify that trailing bloons follow the curve (x decreases as they go back past the turn)
        BloonActor lead = children.get(0);
        BloonActor last = children.get(children.size() - 1);

        assertTrue("Last child X should be less than or equal to lead child X due to turn", last.getCenterX() <= lead.getCenterX());
        assertTrue("Last child Y should be less than or equal to lead child Y due to turn", last.getCenterY() <= lead.getCenterY());
    }

    @Test
    public void testMapBoundaryZeroVectorFallback() throws Exception {
        Stage stage = new Stage();
        Map map = MapFactory.createBasicMap(stage);
        BloonManager bloonManager = map.getBloonManager();

        // Create parent bloon at start of map (-25, 425) where stepping backward goes beyond map limits
        Bloon blackBloon = new Bloon(Bloon.Color.BLACK, 6, false, false);
        blackBloon.setDistanceTravelled(10);

        BloonActor parentActor = new BloonActor(blackBloon, -25f, 425f, null);
        bloonManager.popBloon(parentActor, 1);

        Field onstageField = BloonManager.class.getDeclaredField("onstageBloons");
        onstageField.setAccessible(true);
        Set<BloonActor> onstageBloons = (Set<BloonActor>) onstageField.get(bloonManager);

        assertEquals(2, onstageBloons.size());
    }

    @Test
    public void testPierceBulletHitsSpacedBloonsIndividually() throws Exception {
        Stage stage = new Stage();
        Map map = MapFactory.createBasicMap(stage);
        BloonManager bloonManager = map.getBloonManager();

        // Pop Black bloon into 2 Pink bloons
        Bloon blackBloon = new Bloon(Bloon.Color.BLACK, 6, false, false);
        BloonActor parentActor = new BloonActor(blackBloon, 200f, 425f, null);
        bloonManager.popBloon(parentActor, 1);

        Field onstageField = BloonManager.class.getDeclaredField("onstageBloons");
        onstageField.setAccessible(true);
        Set<BloonActor> onstageBloons = (Set<BloonActor>) onstageField.get(bloonManager);

        List<BloonActor> children = new ArrayList<>(onstageBloons);
        children.sort((a, b) -> Float.compare(b.getCenterX(), a.getCenterX()));

        BloonActor leadChild = children.get(0);
        BloonActor trailingChild = children.get(1);

        // Fire a bullet with 1 pierce targeting lead child
        com.hongbao.bloons.entities.Bullet bullet = new com.hongbao.bloons.entities.Bullet(10f, 1, 1, 200f, false, "red_spell_card.png");
        com.hongbao.bloons.actors.BulletActor bulletActor = new com.hongbao.bloons.actors.BulletActor(bullet, leadChild.getCenterX(), leadChild.getCenterY(), 0f, 0f);

        bloonManager.checkCollision(bulletActor);

        // Bullet pierce should now be 0
        assertEquals(0, bullet.getPierce());
        // Lead child should have been popped
        assertTrue(!onstageBloons.contains(leadChild));
        // Trailing child should still be intact on stage
        assertTrue(onstageBloons.contains(trailingChild));
    }
}
