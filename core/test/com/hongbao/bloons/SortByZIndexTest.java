package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.comparators.SortByZIndex;
import org.junit.Assert;
import org.junit.Test;

public class SortByZIndexTest {

    private static class TestRenderableActor extends RenderableActor {
        public TestRenderableActor(int zIndex) {
            setZIndex(zIndex);
        }
    }

    @Test
    public void testSingletonInstanceNotNull() {
        Assert.assertNotNull(SortByZIndex.INSTANCE);
    }

    @Test
    public void testSortByZIndexComparison() {
        RenderableActor actor1 = new TestRenderableActor(10);
        RenderableActor actor2 = new TestRenderableActor(20);
        RenderableActor actor3 = new TestRenderableActor(10);

        Assert.assertTrue(SortByZIndex.INSTANCE.compare(actor1, actor2) < 0);
        Assert.assertTrue(SortByZIndex.INSTANCE.compare(actor2, actor1) > 0);
        Assert.assertEquals(0, SortByZIndex.INSTANCE.compare(actor1, actor3));
    }

    @Test
    public void testNonRenderableActorComparison() {
        Actor actor1 = new Actor();
        Actor actor2 = new Actor();

        Assert.assertEquals(0, SortByZIndex.INSTANCE.compare(actor1, actor2));
    }
}
