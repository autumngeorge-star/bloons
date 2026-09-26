package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;
import org.junit.Assert;
import org.junit.Test;

public class SortByZIndexTest {

	@Test
	public void testSingletonInstance() {
		Assert.assertNotNull(SortByZIndex.INSTANCE);
		Assert.assertSame(SortByZIndex.INSTANCE, SortByZIndex.getInstance());
	}

	@Test
	public void testCompareRenderableActors() {
		TestRenderableActor actor1 = new TestRenderableActor(10);
		TestRenderableActor actor2 = new TestRenderableActor(20);
		TestRenderableActor actor3 = new TestRenderableActor(10);

		SortByZIndex comparator = SortByZIndex.INSTANCE;

		Assert.assertTrue(comparator.compare(actor1, actor2) < 0);
		Assert.assertTrue(comparator.compare(actor2, actor1) > 0);
		Assert.assertEquals(0, comparator.compare(actor1, actor3));
	}

	@Test
	public void testCompareNonRenderableActors() {
		Actor actor1 = new Actor();
		Actor actor2 = new Actor();
		SortByZIndex comparator = SortByZIndex.INSTANCE;

		Assert.assertEquals(0, comparator.compare(actor1, actor2));
	}

	private static class TestRenderableActor extends RenderableActor {
		public TestRenderableActor(int zIndex) {
			setZIndex(zIndex);
		}
	}
}
