package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class SortByZIndexTest {

    @Test
    public void testCompareRenderableActors() {
        RenderableActor actor1 = Mockito.mock(RenderableActor.class);
        RenderableActor actor2 = Mockito.mock(RenderableActor.class);

        Mockito.when(actor1.getZIndex()).thenReturn(5);
        Mockito.when(actor2.getZIndex()).thenReturn(10);

        SortByZIndex comparator = new SortByZIndex();
        assertTrue(comparator.compare(actor1, actor2) < 0);
        assertTrue(comparator.compare(actor2, actor1) > 0);
        assertEquals(0, comparator.compare(actor1, actor1));
    }

    @Test
    public void testCompareNonRenderableActors() {
        Actor actor1 = Mockito.mock(Actor.class);
        Actor actor2 = Mockito.mock(Actor.class);

        SortByZIndex comparator = new SortByZIndex();
        assertEquals(0, comparator.compare(actor1, actor2));
    }
}
