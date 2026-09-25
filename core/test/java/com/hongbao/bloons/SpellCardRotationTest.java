package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.Scaling;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.entities.Girl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class SpellCardRotationTest {

    private static BloonsTouhouDefense mockApp;
    private static Stage headlessStage;
    private static Map testMap;

    @BeforeClass
    public static void setUpHeadless() {
        if (Gdx.app != null) return;

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();

        GL20 glMock = (GL20) Proxy.newProxyInstance(
            GL20.class.getClassLoader(),
            new Class<?>[]{GL20.class},
            (proxy, method, methodArgs) -> {
                if (method.getReturnType().equals(boolean.class)) return false;
                if (method.getReturnType().equals(int.class)) return 1;
                if (method.getReturnType().equals(float.class)) return 0f;
                if (method.getReturnType().equals(String.class)) return "";
                return null;
            }
        );
        Gdx.gl = glMock;
        Gdx.gl20 = glMock;

        Batch mockBatch = (Batch) Proxy.newProxyInstance(
            Batch.class.getClassLoader(),
            new Class<?>[]{Batch.class},
            (proxy, method, methodArgs) -> null
        );

        mockApp = new BloonsTouhouDefense() {
            @Override
            public Map getMap() {
                return testMap;
            }

            @Override
            public void render() {
                // No-op
            }
        };

        new HeadlessApplication(mockApp, config);

        headlessStage = new Stage(new ScalingViewport(Scaling.stretch, 1800, 900), mockBatch);
        testMap = new Map("img/maps/map1.png", headlessStage);
    }

    @Test
    public void testGirlActorSuppliesRotationAngle() {
        Girl reimu = GirlFactory.createReimu();
        GirlActor reimuActor = new GirlActor(reimu, 500, 500);
        reimuActor.setRotationAngle(90.0f);

        SpellCardActor spellCardActor = reimuActor.createSpellCardActor();
        Assert.assertNotNull("SpellCardActor should not be null", spellCardActor);
        Assert.assertEquals("Expected rotationAngle 90.0", 90.0f, spellCardActor.getRotationAngle(), 0.001f);
    }

    @Test
    public void testSpellCardActorRotationAngleFieldAndAccessors() {
        Girl reimu = GirlFactory.createReimu();
        SpellCardActor customActor = new SpellCardActor(reimu.createSpellCard(), 500, 500, 45.0f);
        Assert.assertEquals(45.0f, customActor.getRotationAngle(), 0.001f);
        customActor.setRotationAngle(180.0f);
        Assert.assertEquals(180.0f, customActor.getRotationAngle(), 0.001f);
    }

    @Test
    public void testReimuVelocityVectorTransformation() {
        Girl reimu = GirlFactory.createReimu();
        GirlActor reimuRight = new GirlActor(reimu, 500, 500);
        reimuRight.setRotationAngle(90.0f); // Facing RIGHT
        SpellCardActor reimuSpellRight = reimuRight.createSpellCardActor();

        int actorsBefore = headlessStage.getActors().size;
        reimuSpellRight.act(0.016f);
        int actorsAfter = headlessStage.getActors().size;

        Assert.assertTrue("Bullets should be added to stage on frame 0", actorsAfter > actorsBefore);
        List<BulletActor> bullets = getBulletActors(headlessStage);
        Assert.assertFalse("Bullets list should not be empty", bullets.isEmpty());

        BulletActor b0 = bullets.get(0);
        Assert.assertEquals(500.0f, b0.getCenterX(), 0.001f);
        Assert.assertEquals(500.0f, b0.getCenterY(), 0.001f);
    }

    @Test
    public void testYuyukoSpatialOffsetsTransformation() {
        Girl yuyuko = GirlFactory.createYuyuko();
        GirlActor yuyukoActor = new GirlActor(yuyuko, 500, 500);
        yuyukoActor.setRotationAngle(90.0f); // Facing RIGHT
        SpellCardActor yuyukoSpell = yuyukoActor.createSpellCardActor();

        headlessStage.clear();
        yuyukoSpell.act(0.016f);
        List<BulletActor> yuyukoBullets = getBulletActors(headlessStage);
        Assert.assertFalse("Yuyuko spell card should create bullets", yuyukoBullets.isEmpty());

        boolean foundTop = false;
        boolean foundBottom = false;
        for (BulletActor ba : yuyukoBullets) {
            if (Math.abs(ba.getCenterX() - 500.0f) < 1.0f && Math.abs(ba.getCenterY() - 625.0f) < 1.0f) {
                foundTop = true;
            }
            if (Math.abs(ba.getCenterX() - 500.0f) < 1.0f && Math.abs(ba.getCenterY() - 375.0f) < 1.0f) {
                foundBottom = true;
            }
        }
        Assert.assertTrue("Should find bullet with rotated offset (0, 125) at Y=625", foundTop);
        Assert.assertTrue("Should find bullet with rotated offset (0, -125) at Y=375", foundBottom);
    }

    private static List<BulletActor> getBulletActors(Stage stage) {
        List<BulletActor> list = new ArrayList<>();
        for (Actor actor : stage.getActors()) {
            if (actor instanceof BulletActor) {
                list.add((BulletActor) actor);
            }
        }
        return list;
    }
}
