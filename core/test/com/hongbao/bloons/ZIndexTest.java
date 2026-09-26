package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.comparators.SortByZIndex;
import com.hongbao.bloons.helpers.ZIndex;
import org.junit.Assert;
import org.junit.Test;

public class ZIndexTest {

    static class DummyActor extends RenderableActor {
        public DummyActor(int zIndex) {
            setZIndex(zIndex);
        }
    }

    @Test
    public void testBaseLayerConstants() {
        Assert.assertEquals(100000, ZIndex.SPELL_CARD_Z_INDEX);
        Assert.assertEquals(200000, ZIndex.GIRL_Z_INDEX);
        Assert.assertEquals(300000, ZIndex.BLOON_Z_INDEX);
        Assert.assertEquals(400000, ZIndex.BULLET_Z_INDEX);
        Assert.assertEquals(1000000, ZIndex.MENU_Z_INDEX);
        Assert.assertEquals(1001000, ZIndex.MENU_ITEM_Z_INDEX);
    }

    @Test
    public void testDynamicZIndexYOrdering() {
        // Lower Y screen position (bottom) should render in front = higher Z-index
        int zIndexHighY = ZIndex.calculateDynamicZIndex(ZIndex.BLOON_Z_INDEX, 800f, 100f, 1L); // top of screen
        int zIndexLowY = ZIndex.calculateDynamicZIndex(ZIndex.BLOON_Z_INDEX, 100f, 100f, 1L);  // bottom of screen

        Assert.assertTrue("Lower Y screen position must have higher Z-index", zIndexLowY > zIndexHighY);
    }

    @Test
    public void testDynamicZIndexDistanceOrdering() {
        // Higher distance traveled = higher Z-index
        int zIndexLessDist = ZIndex.calculateDynamicZIndex(ZIndex.BLOON_Z_INDEX, 500f, 100f, 1L);
        int zIndexMoreDist = ZIndex.calculateDynamicZIndex(ZIndex.BLOON_Z_INDEX, 500f, 500f, 1L);

        Assert.assertTrue("Greater distance traveled must yield higher Z-index", zIndexMoreDist > zIndexLessDist);
    }

    @Test
    public void testBaseLayerHeadroomIsolation() {
        // Test extreme inputs to ensure Z-index never bleeds into next category
        int extremeZIndex = ZIndex.calculateDynamicZIndex(ZIndex.BLOON_Z_INDEX, -1000f, 999999f, 999999L);

        Assert.assertTrue("Z-index must be at least base layer", extremeZIndex >= ZIndex.BLOON_Z_INDEX);
        Assert.assertTrue("Z-index must not bleed into next base layer", extremeZIndex < ZIndex.BULLET_Z_INDEX);
    }

    @Test
    public void testSortByZIndexNonZeroForDistinctActorsInIdenticalLayer() {
        SortByZIndex comparator = new SortByZIndex();
        DummyActor actor1 = new DummyActor(300000);
        DummyActor actor2 = new DummyActor(300000);

        int result1 = comparator.compare(actor1, actor2);
        int result2 = comparator.compare(actor2, actor1);

        Assert.assertNotEquals("Comparison for distinct actors must not be zero", 0, result1);
        Assert.assertNotEquals("Comparison for distinct actors must not be zero", 0, result2);
        Assert.assertEquals("Comparison must be anti-symmetric", -result1, result2);
    }

    @Test
    public void testSortByZIndexZeroForSameActor() {
        SortByZIndex comparator = new SortByZIndex();
        DummyActor actor = new DummyActor(300000);

        Assert.assertEquals("Comparing same actor instance must return 0", 0, comparator.compare(actor, actor));
    }

    @Test
    public void testSortByZIndexProperOrdering() {
        SortByZIndex comparator = new SortByZIndex();
        DummyActor lowerActor = new DummyActor(200000);
        DummyActor higherActor = new DummyActor(300000);

        Assert.assertTrue("Lower Z-index actor compares smaller", comparator.compare(lowerActor, higherActor) < 0);
        Assert.assertTrue("Higher Z-index actor compares larger", comparator.compare(higherActor, lowerActor) > 0);
    }
}
