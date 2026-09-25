package com.hongbao.bloons;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.hongbao.bloons.actors.RenderableActor;
import org.junit.Assert;
import org.junit.Test;

public class GeometryIntegrationTest {

    private static class TestActor extends RenderableActor {
        private float radius;

        public TestActor(float x, float y, float radius) {
            this.radius = radius;
            setBounds(x - radius, y - radius, radius * 2f, radius * 2f);
        }

        @Override
        public float getCollisionRadius() {
            return radius;
        }

        public void setCollisionRadius(float radius) {
            this.radius = radius;
            updateCenterAndBounds();
        }
    }

    @Test
    public void testActorCenterAndCircleBoundsSynchronization() {
        TestActor actor = new TestActor(100f, 200f, 25f);

        Vector2 center = actor.getCenter();
        Circle circle = actor.getCircle();

        Assert.assertEquals(100f, center.x, 0.001f);
        Assert.assertEquals(200f, center.y, 0.001f);
        Assert.assertEquals(100f, circle.x, 0.001f);
        Assert.assertEquals(200f, circle.y, 0.001f);
        Assert.assertEquals(25f, circle.radius, 0.001f);

        // Move actor and verify center and circle update synchronously
        actor.setPosition(300f - 25f, 400f - 25f);

        Assert.assertEquals(300f, actor.getCenter().x, 0.001f);
        Assert.assertEquals(400f, actor.getCenter().y, 0.001f);
        Assert.assertEquals(300f, actor.getCircle().x, 0.001f);
        Assert.assertEquals(400f, actor.getCircle().y, 0.001f);
    }

    @Test
    public void testCircleOverlapsAndIntersector() {
        TestActor actor1 = new TestActor(100f, 100f, 20f);
        TestActor actor2 = new TestActor(130f, 100f, 20f); // Distance is 30, sum of radii is 40 -> overlap!
        TestActor actor3 = new TestActor(200f, 100f, 20f); // Distance is 100, sum of radii is 40 -> no overlap!

        Assert.assertTrue(Intersector.overlaps(actor1.getCircle(), actor2.getCircle()));
        Assert.assertTrue(actor1.getCircle().overlaps(actor2.getCircle()));

        Assert.assertFalse(Intersector.overlaps(actor1.getCircle(), actor3.getCircle()));
        Assert.assertFalse(actor1.getCircle().overlaps(actor3.getCircle()));
    }

    @Test
    public void testTowerVisualRangeCheck() {
        TestActor tower = new TestActor(100f, 100f, 10f);
        float visualRange = 50f;
        Circle rangeCircle = new Circle(tower.getCenter(), visualRange);

        TestActor targetInRange = new TestActor(140f, 100f, 15f); // Center dist = 40, sum of radii = 65 -> in range
        TestActor targetOutOfRange = new TestActor(200f, 100f, 15f); // Center dist = 100, sum of radii = 65 -> out of range

        Assert.assertTrue(Intersector.overlaps(rangeCircle, targetInRange.getCircle()));
        Assert.assertFalse(Intersector.overlaps(rangeCircle, targetOutOfRange.getCircle()));
    }

    @Test
    public void testHomingTargetSquaredDistanceComparison() {
        TestActor bullet = new TestActor(100f, 100f, 5f);
        TestActor bloonClose = new TestActor(120f, 100f, 10f); // dist^2 = 400
        TestActor bloonFar = new TestActor(150f, 100f, 10f); // dist^2 = 2500

        float dst2Close = bloonClose.getCenter().dst2(bullet.getCenter());
        float dst2Far = bloonFar.getCenter().dst2(bullet.getCenter());

        Assert.assertEquals(400f, dst2Close, 0.001f);
        Assert.assertEquals(2500f, dst2Far, 0.001f);
        Assert.assertTrue(dst2Close < dst2Far);
    }

    @Test
    public void testUnitVectorNormalization() {
        TestActor tower = new TestActor(0f, 0f, 10f);
        TestActor target = new TestActor(30f, 40f, 10f);

        Vector2 direction = new Vector2(target.getCenter()).sub(tower.getCenter()).nor();

        Assert.assertEquals(0.6f, direction.x, 0.001f);
        Assert.assertEquals(0.8f, direction.y, 0.001f);
        Assert.assertEquals(1.0f, direction.len(), 0.001f);
    }

    @Test
    public void testMapDistanceBetweenActors() {
        TestActor actor1 = new TestActor(0f, 0f, 5f);
        TestActor actor2 = new TestActor(3f, 4f, 5f);

        Assert.assertEquals(5.0f, Map.distanceBetweenActors(actor1, actor2), 0.001f);
        Assert.assertEquals(25.0f, Map.distanceSquaredBetweenActors(actor1, actor2), 0.001f);
    }
}
