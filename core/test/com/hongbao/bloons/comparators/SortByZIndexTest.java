package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.helpers.ZIndex;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SortByZIndexTest {

    private SortByZIndex comparator;

    private static class TestRenderableActor extends RenderableActor {
        public TestRenderableActor(int zIndex, float y) {
            setZIndex(zIndex);
            setY(y);
        }
    }

    private static class TestNonRenderableActor extends Actor {
        public TestNonRenderableActor(float y) {
            setY(y);
        }
    }

    @Before
    public void setUp() {
        comparator = new SortByZIndex();
    }

    @Test
    public void testLayerZIndexConstantOrder() {
        assertTrue(ZIndex.DEFAULT_BACKGROUND_Z_INDEX <= ZIndex.BACKGROUND_Z_INDEX);
        assertTrue(ZIndex.BACKGROUND_Z_INDEX < ZIndex.SPELL_CARD_Z_INDEX);
        assertTrue(ZIndex.SPELL_CARD_Z_INDEX < ZIndex.GIRL_Z_INDEX);
        assertTrue(ZIndex.GIRL_Z_INDEX < ZIndex.BLOON_Z_INDEX);
        assertTrue(ZIndex.BLOON_Z_INDEX < ZIndex.BULLET_Z_INDEX);
        assertTrue(ZIndex.BULLET_Z_INDEX < ZIndex.MENU_Z_INDEX);
        assertTrue(ZIndex.MENU_Z_INDEX < ZIndex.MENU_ITEM_Z_INDEX);
    }

    @Test
    public void testNonRenderableActorDefaultZIndex() {
        Actor rawActor = new TestNonRenderableActor(100f);
        RenderableActor spellCard = new TestRenderableActor(ZIndex.SPELL_CARD_Z_INDEX, 100f);
        RenderableActor backgroundRenderable = new TestRenderableActor(ZIndex.BACKGROUND_Z_INDEX, 100f);

        // Non-renderable actor should have default background Z-index (0), which is equal to backgroundRenderable Z-index (0)
        assertEquals(0, comparator.compare(rawActor, rawActor));
        assertTrue(comparator.compare(rawActor, spellCard) < 0);
        assertTrue(comparator.compare(spellCard, rawActor) > 0);
    }

    @Test
    public void testContractReflexivitySymmetryTransitivity() {
        List<Actor> actors = new ArrayList<>();
        actors.add(new TestNonRenderableActor(10f));
        actors.add(new TestNonRenderableActor(10f));
        actors.add(new TestNonRenderableActor(20f));
        actors.add(new TestRenderableActor(ZIndex.SPELL_CARD_Z_INDEX, 50f));
        actors.add(new TestRenderableActor(ZIndex.SPELL_CARD_Z_INDEX, 50f));
        actors.add(new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 100f));
        actors.add(new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 50f));
        actors.add(new TestRenderableActor(ZIndex.BLOON_Z_INDEX, 100f));
        actors.add(new TestRenderableActor(ZIndex.BULLET_Z_INDEX, 100f));
        actors.add(new TestRenderableActor(ZIndex.MENU_Z_INDEX, 0f));

        for (Actor a : actors) {
            // Reflexivity
            assertEquals("Reflexivity failed for actor " + a, 0, comparator.compare(a, a));
            for (Actor b : actors) {
                // Anti-symmetry
                int compAB = comparator.compare(a, b);
                int compBA = comparator.compare(b, a);
                assertEquals("Anti-symmetry failed for " + a + " and " + b,
                        Integer.signum(compAB), -Integer.signum(compBA));

                for (Actor c : actors) {
                    // Transitivity
                    int compBC = comparator.compare(b, c);
                    if (compAB <= 0 && compBC <= 0) {
                        assertTrue("Transitivity failed for " + a + ", " + b + ", " + c,
                                comparator.compare(a, c) <= 0);
                    }
                }
            }
        }
    }

    @Test
    public void testYCoordinateTieBreaking() {
        // Equal Z-index, higher Y should render behind (drawn first in array), lower Y should render in front (drawn later)
        RenderableActor towerHighY = new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 300f);
        RenderableActor towerLowY = new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 100f);

        assertTrue("Higher Y should come before lower Y in sort order", comparator.compare(towerHighY, towerLowY) < 0);
        assertTrue("Lower Y should come after higher Y in sort order", comparator.compare(towerLowY, towerHighY) > 0);
    }

    @Test
    public void testUniqueInstanceTieBreakingStability() {
        // Equal Z-index and equal Y
        RenderableActor tower1 = new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 100f);
        RenderableActor tower2 = new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 100f);

        int firstCompare = comparator.compare(tower1, tower2);
        assertTrue("Distinct instances with equal Z and Y must have non-zero tie-break result", firstCompare != 0);

        // Verify stability across consecutive calls / frames
        for (int i = 0; i < 100; i++) {
            assertEquals("Tie-break comparison must be stable across multiple frames",
                    firstCompare, comparator.compare(tower1, tower2));
            assertEquals("Anti-symmetry must hold across multiple frames",
                    -firstCompare, comparator.compare(tower2, tower1));
        }
    }

    @Test
    public void testSortingWithoutExceptions() {
        List<Actor> stageActors = new ArrayList<>();
        stageActors.add(new TestNonRenderableActor(0f)); // Background map ImageButton
        stageActors.add(new TestRenderableActor(ZIndex.SPELL_CARD_Z_INDEX, 50f));
        stageActors.add(new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 100f));
        stageActors.add(new TestRenderableActor(ZIndex.GIRL_Z_INDEX, 200f));
        stageActors.add(new TestRenderableActor(ZIndex.BLOON_Z_INDEX, 150f));
        stageActors.add(new TestRenderableActor(ZIndex.BLOON_Z_INDEX, 150f));
        stageActors.add(new TestRenderableActor(ZIndex.BULLET_Z_INDEX, 300f));
        stageActors.add(new TestRenderableActor(ZIndex.MENU_Z_INDEX, 0f));

        // TimSort will throw IllegalArgumentException if comparator contract is violated
        Collections.sort(stageActors, comparator);

        // Verify sorted order logic
        // Background (Z=0) -> SpellCard (Z=100) -> Girl (Z=200, Y=200 then Y=100) -> Bloon (Z=300) -> Bullet (Z=400) -> Menu (Z=1000)
        assertTrue(stageActors.get(0) instanceof TestNonRenderableActor);
        assertEquals(ZIndex.SPELL_CARD_Z_INDEX, ((RenderableActor) stageActors.get(1)).getZIndex());
        assertEquals(ZIndex.GIRL_Z_INDEX, ((RenderableActor) stageActors.get(2)).getZIndex());
        assertEquals(200f, stageActors.get(2).getY(), 0.001f); // Y=200 comes before Y=100
        assertEquals(ZIndex.GIRL_Z_INDEX, ((RenderableActor) stageActors.get(3)).getZIndex());
        assertEquals(100f, stageActors.get(3).getY(), 0.001f);
        assertEquals(ZIndex.BLOON_Z_INDEX, ((RenderableActor) stageActors.get(4)).getZIndex());
        assertEquals(ZIndex.BLOON_Z_INDEX, ((RenderableActor) stageActors.get(5)).getZIndex());
        assertEquals(ZIndex.BULLET_Z_INDEX, ((RenderableActor) stageActors.get(6)).getZIndex());
        assertEquals(ZIndex.MENU_Z_INDEX, ((RenderableActor) stageActors.get(7)).getZIndex());
    }
}
