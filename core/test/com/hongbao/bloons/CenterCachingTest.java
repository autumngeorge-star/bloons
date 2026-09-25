package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.hongbao.bloons.actors.RenderableActor;
import org.junit.Assert;
import org.junit.Test;

public class CenterCachingTest {

    static class TestActor extends RenderableActor {
        public TestActor(float width, float height) {
            setSize(width, height);
        }
    }

    @Test
    public void testInitialCenterAndGettersDirectly() {
        TestActor actor = new TestActor(100f, 60f);
        actor.setPosition(10f, 20f);
        Assert.assertEquals(60f, actor.getCenterX(), 0.0001f);
        Assert.assertEquals(50f, actor.getCenterY(), 0.0001f);
    }

    @Test
    public void testPositionMutators() {
        TestActor actor = new TestActor(40f, 40f);
        actor.setPosition(0f, 0f);
        Assert.assertEquals(20f, actor.getCenterX(), 0.0001f);
        Assert.assertEquals(20f, actor.getCenterY(), 0.0001f);

        actor.setX(50f);
        Assert.assertEquals(70f, actor.getCenterX(), 0.0001f);

        actor.setY(100f);
        Assert.assertEquals(120f, actor.getCenterY(), 0.0001f);

        actor.moveBy(10f, -20f);
        Assert.assertEquals(80f, actor.getCenterX(), 0.0001f);
        Assert.assertEquals(100f, actor.getCenterY(), 0.0001f);

        actor.setBounds(10f, 10f, 80f, 80f);
        Assert.assertEquals(50f, actor.getCenterX(), 0.0001f);
        Assert.assertEquals(50f, actor.getCenterY(), 0.0001f);
    }

    @Test
    public void testLibGdxActionsSyncInAct() {
        TestActor actor = new TestActor(20f, 20f);
        actor.setPosition(0f, 0f);
        Assert.assertEquals(10f, actor.getCenterX(), 0.0001f);

        actor.addAction(Actions.moveBy(50f, 30f, 1f));
        actor.act(1f); // complete the action
        Assert.assertEquals(60f, actor.getCenterX(), 0.0001f);
        Assert.assertEquals(40f, actor.getCenterY(), 0.0001f);
    }

    @Test
    public void testSquaredDistanceMath() {
        TestActor actor1 = new TestActor(20f, 20f);
        actor1.setPosition(10f, 10f); // center (20, 20)

        TestActor actor2 = new TestActor(20f, 20f);
        actor2.setPosition(13f, 14f); // center (23, 24)

        // dx = 3, dy = 4, dx*dx + dy*dy = 25
        float dx = actor1.getCenterX() - actor2.getCenterX();
        float dy = actor1.getCenterY() - actor2.getCenterY();
        float distSq = dx * dx + dy * dy;

        Assert.assertEquals(25f, distSq, 0.0001f);
    }
}
