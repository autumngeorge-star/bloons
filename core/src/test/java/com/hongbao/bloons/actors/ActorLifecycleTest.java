package com.hongbao.bloons.actors;

import com.badlogic.gdx.graphics.Texture;
import com.hongbao.bloons.cache.RefCountedTextureCache;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class ActorLifecycleTest {

    private TestCache cache;

    private static class TestCache extends RefCountedTextureCache {
        @Override
        protected Texture loadTextureFromFile(String fileName) {
            return mock(Texture.class);
        }
    }

    @Before
    public void setUp() {
        cache = new TestCache();
        RefCountedTextureCache.setInstance(cache);
    }

    @Test
    public void testBloonActorLifecycle() {
        Bloon redBloon = BloonFactory.createRedBloon();
        String file = redBloon.getImageFileName();

        assertEquals(0, cache.getRefCount(file));

        BloonActor actor1 = new BloonActor(redBloon, 0, 0, null);
        assertEquals(1, cache.getRefCount(file));

        BloonActor actor2 = new BloonActor(redBloon, 10, 10, null);
        assertEquals(2, cache.getRefCount(file));

        // Pop first bloon
        actor1.pop(1);
        assertEquals(1, cache.getRefCount(file));

        // Calling remove again on actor1 should NOT decrement ref count again
        actor1.remove();
        assertEquals(1, cache.getRefCount(file));

        // Release second bloon
        actor2.remove();
        assertEquals(0, cache.getRefCount(file));
        assertFalse(cache.isLoaded(file));
    }

    @Test
    public void testGirlActorLifecycle() {
        Girl reimu = GirlFactory.createReimu();
        String file = reimu.getImageFileName();

        assertEquals(0, cache.getRefCount(file));

        GirlActor girlActor = new GirlActor(reimu, 100, 100);
        assertEquals(1, cache.getRefCount(file));

        girlActor.remove();
        assertEquals(0, cache.getRefCount(file));
    }

    @Test
    public void testBulletActorLifecycle() {
        Bullet bullet = new Bullet(5.0f, 1, 1, 100.0f, false, "red_spell_card.png");
        String file = bullet.getImageFileName();

        assertEquals(0, cache.getRefCount(file));

        BulletActor bulletActor = new BulletActor(bullet, 50, 50, 1, 0);
        assertEquals(1, cache.getRefCount(file));

        bulletActor.remove();
        assertEquals(0, cache.getRefCount(file));
    }

    @Test
    public void testSpellCardActorLifecycle() {
        Map<Integer, List<Bullet>> bulletsMap = new HashMap<>();
        SpellCard spellCard = new SpellCard("Reimu", bulletsMap, "reimu_spell.png", 1.0f);
        String file = spellCard.getImageFileName();

        assertEquals(0, cache.getRefCount(file));

        SpellCardActor spellCardActor = new SpellCardActor(spellCard, 50, 50);
        assertEquals(1, cache.getRefCount(file));

        spellCardActor.remove();
        assertEquals(0, cache.getRefCount(file));
    }
}
