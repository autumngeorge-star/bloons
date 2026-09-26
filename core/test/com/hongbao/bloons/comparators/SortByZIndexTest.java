package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.helpers.ZIndex;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class SortByZIndexTest {

    static class TestActor extends RenderableActor {
        public TestActor(int zIndex) {
            setZIndex(zIndex);
        }
    }

    @Test
    public void testStaticComparatorInstance() {
        Assert.assertNotNull(SortByZIndex.INSTANCE);
        Assert.assertNotNull(SortByZIndex.COMPARATOR);
        Assert.assertSame(SortByZIndex.INSTANCE, SortByZIndex.COMPARATOR);
    }

    @Test
    public void testCompareZIndex() {
        TestActor actorSpell = new TestActor(ZIndex.SPELL_CARD_Z_INDEX); // -2
        TestActor actorGirl = new TestActor(ZIndex.GIRL_Z_INDEX); // -1
        TestActor actorBloon = new TestActor(ZIndex.BLOON_Z_INDEX); // 0
        TestActor actorBullet = new TestActor(ZIndex.BULLET_Z_INDEX); // 1
        Actor plainActor = new Actor();

        Assert.assertTrue(SortByZIndex.INSTANCE.compare(actorSpell, actorGirl) < 0);
        Assert.assertTrue(SortByZIndex.INSTANCE.compare(actorGirl, actorBloon) < 0);
        Assert.assertTrue(SortByZIndex.INSTANCE.compare(actorBloon, actorBullet) < 0);
        Assert.assertTrue(SortByZIndex.INSTANCE.compare(plainActor, actorSpell) < 0);
    }

    @Test
    public void testBinarySearchInsertionOrder() {
        Stage stage = Mockito.mock(Stage.class);
        Group root = new Group();

        Mockito.when(stage.getRoot()).thenReturn(root);
        Mockito.when(stage.getActors()).thenAnswer(invocation -> root.getChildren());

        TestActor menu = new TestActor(ZIndex.MENU_Z_INDEX); // 1000
        TestActor menuItem = new TestActor(ZIndex.MENU_ITEM_Z_INDEX); // 1001
        TestActor bloon1 = new TestActor(ZIndex.BLOON_Z_INDEX); // 0
        TestActor bloon2 = new TestActor(ZIndex.BLOON_Z_INDEX); // 0
        TestActor girl = new TestActor(ZIndex.GIRL_Z_INDEX); // -1
        TestActor spell = new TestActor(ZIndex.SPELL_CARD_Z_INDEX); // -2
        TestActor bullet = new TestActor(ZIndex.BULLET_Z_INDEX); // 1
        Actor background = new Actor(); // MIN_VALUE

        // Insert in arbitrary unsorted order
        SortByZIndex.addActorInOrder(stage, menu);
        SortByZIndex.addActorInOrder(stage, bloon1);
        SortByZIndex.addActorInOrder(stage, girl);
        SortByZIndex.addActorInOrder(stage, background);
        SortByZIndex.addActorInOrder(stage, bullet);
        SortByZIndex.addActorInOrder(stage, spell);
        SortByZIndex.addActorInOrder(stage, menuItem);
        SortByZIndex.addActorInOrder(stage, bloon2);

        Array<Actor> actors = stage.getActors();
        Assert.assertEquals(8, actors.size);

        // Verify sorted order by zIndex
        Assert.assertSame(background, actors.get(0));
        Assert.assertSame(spell, actors.get(1));
        Assert.assertSame(girl, actors.get(2));
        Assert.assertSame(bloon1, actors.get(3));
        Assert.assertSame(bloon2, actors.get(4));
        Assert.assertSame(bullet, actors.get(5));
        Assert.assertSame(menu, actors.get(6));
        Assert.assertSame(menuItem, actors.get(7));
    }
}
