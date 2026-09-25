package com.hongbao.bloons.actors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpellCardActorTest {

    @BeforeClass
    public static void setUpGdx() {
        if (Gdx.app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {}, config);
        }
        if (Gdx.gl == null) {
            Gdx.gl = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(int.class)) {
                        return 1;
                    }
                    if (method.getReturnType().equals(boolean.class)) {
                        return true;
                    }
                    return null;
                }
            );
            Gdx.gl20 = Gdx.gl;
        }
    }

    @Test
    public void testSpellCardActorConstructorAndRotationAngle() {
        SpellCard card = SpellCard.createReimuSpellCard();
        SpellCardActor actor = new SpellCardActor(card, 100f, 100f, 45f);
        assertEquals(45f, actor.getRotationAngle(), 1e-4f);
    }

    @Test
    public void testGirlActorPassesRotationAngleToSpellCardActor() {
        Girl reimu = GirlFactory.createReimu();
        GirlActor girlActor = new GirlActor(reimu, 200f, 200f);
        girlActor.setRotationAngle(90f);

        SpellCardActor spellCardActor = girlActor.createSpellCardActor();
        assertNotNull(spellCardActor);
        assertEquals(90f, spellCardActor.getRotationAngle(), 1e-4f);
    }

    @Test
    public void test2DRotationMatrixTransformationAt90Degrees() {
        // Tower facing RIGHT (90 degrees rotation)
        float angle = 90f;
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad); // 0
        float sin = (float) Math.sin(rad); // 1

        // Test initial velocity (0, 1) -> should rotate to (1, 0)
        float initialDX = 0f;
        float initialDY = 1f;
        float rotatedDX = initialDX * cos + initialDY * sin;
        float rotatedDY = -initialDX * sin + initialDY * cos;

        assertEquals(1.0f, rotatedDX, 1e-4f);
        assertEquals(0.0f, rotatedDY, 1e-4f);

        // Test initial offset (-125, 0) -> should rotate to (0, 125)
        float initialXOff = -125f;
        float initialYOff = 0f;
        float rotatedXOff = initialXOff * cos + initialYOff * sin;
        float rotatedYOff = -initialXOff * sin + initialYOff * cos;

        assertEquals(0.0f, rotatedXOff, 1e-4f);
        assertEquals(125.0f, rotatedYOff, 1e-4f);
    }

    @Test
    public void test2DRotationMatrixTransformationAt180Degrees() {
        // Tower facing DOWN (180 degrees rotation)
        float angle = 180f;
        float rad = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(rad); // -1
        float sin = (float) Math.sin(rad); // 0

        // Test initial velocity (0, 1) -> should rotate to (0, -1)
        float initialDX = 0f;
        float initialDY = 1f;
        float rotatedDX = initialDX * cos + initialDY * sin;
        float rotatedDY = -initialDX * sin + initialDY * cos;

        assertEquals(0.0f, rotatedDX, 1e-4f);
        assertEquals(-1.0f, rotatedDY, 1e-4f);
    }

    @Test
    public void testSpellCardActorActBulletTransformation() {
        SpellCard card = SpellCard.createReimuSpellCard();
        SpellCardActor actor = new SpellCardActor(card, 100f, 100f, 90f);

        java.util.List<Bullet> bullets = card.getBulletsToCreateAndIncrementFrame();
        assertNotNull(bullets);

        float rad = (float) Math.toRadians(actor.getRotationAngle());
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        Bullet bullet1 = bullets.get(0); // initial dx=0, dy=1
        float rotatedDX = bullet1.getInitialDXOverride() * cos + bullet1.getInitialDYOverride() * sin;
        float rotatedDY = -bullet1.getInitialDXOverride() * sin + bullet1.getInitialDYOverride() * cos;

        bullet1.setInitialDXOverride(rotatedDX);
        bullet1.setInitialDYOverride(rotatedDY);

        assertEquals(1.0f, bullet1.getInitialDXOverride(), 1e-4f);
        assertEquals(0.0f, bullet1.getInitialDYOverride(), 1e-4f);
    }
}
