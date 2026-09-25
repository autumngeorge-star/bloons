package com.hongbao.bloons;

import com.hongbao.bloons.actors.RenderableActor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MapTest {

    private static class TestActor extends RenderableActor {
        private final float centerX;
        private final float centerY;

        public TestActor(float centerX, float centerY) {
            this.centerX = centerX;
            this.centerY = centerY;
        }

        @Override
        public float getCenterX() {
            return centerX;
        }

        @Override
        public float getCenterY() {
            return centerY;
        }
    }

    @Test
    public void testDistanceSquaredBetweenActors() {
        RenderableActor actor1 = new TestActor(0f, 0f);
        RenderableActor actor2 = new TestActor(3f, 4f);

        float distanceSquared = Map.distanceSquaredBetweenActors(actor1, actor2);
        assertEquals(25f, distanceSquared, 0.0001f);
    }

    @Test
    public void testDistanceBetweenActors() {
        RenderableActor actor1 = new TestActor(10f, 20f);
        RenderableActor actor2 = new TestActor(13f, 24f);

        float distance = Map.distanceBetweenActors(actor1, actor2);
        assertEquals(5f, distance, 0.0001f);
    }

    @Test
    public void testDistanceSquaredConsistencyWithDistance() {
        RenderableActor actor1 = new TestActor(100f, 150f);
        RenderableActor actor2 = new TestActor(400f, 550f);

        float distSq = Map.distanceSquaredBetweenActors(actor1, actor2);
        float dist = Map.distanceBetweenActors(actor1, actor2);

        assertEquals(dist * dist, distSq, 0.01f);
    }
}
