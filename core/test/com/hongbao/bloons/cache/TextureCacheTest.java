package com.hongbao.bloons.cache;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TextureCacheTest {

    @BeforeClass
    public static void setUpGdx() {
        if (Gdx.app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {}, config);
            Gdx.gl = mock(GL20.class);
            Gdx.gl20 = Gdx.gl;
        }
    }

    @Before
    public void resetCache() {
        TextureCache.clear();
    }

    @Test
    public void testTextureCachingReturnsSameInstance() {
        String path = "img/projectiles/magic_spike.png";
        assertFalse(TextureCache.contains(path));

        Texture texture1 = TextureCache.getTexture(path);
        assertNotNull(texture1);
        assertTrue(TextureCache.contains(path));
        assertEquals(1, TextureCache.size());

        Texture texture2 = TextureCache.getTexture(path);
        assertSame("Subsequent calls must return the identical Texture instance", texture1, texture2);
        assertEquals(1, TextureCache.size());
    }

    @Test
    public void testMultipleSpawnsReuseTexture() {
        String path = "img/projectiles/magic_spike.png";
        
        // Simulate spawning 1000 bullets
        Texture initialTexture = TextureCache.get(path);
        int initialSize = TextureCache.size();

        for (int i = 0; i < 1000; i++) {
            Texture t = TextureCache.get(path);
            assertSame(initialTexture, t);
        }

        assertEquals("Cache size should remain 1 after 1000 requests for same texture", initialSize, TextureCache.size());
    }

    @Test
    public void testActorRemovalDoesNotDisposeSharedTexture() {
        Girl reimu = GirlFactory.createReimu();
        GirlActor girlActor = new GirlActor(reimu, 100, 100);
        Texture girlTexture = girlActor.getTextureRegion().getTexture();

        assertTrue(TextureCache.contains(reimu.getImageFileName()));
        assertSame(girlTexture, TextureCache.getTexture(reimu.getImageFileName()));

        // Removing girl actor (e.g. tower placement preview cancelled)
        girlActor.remove();

        // Texture MUST remain in cache and not disposed
        assertTrue("Texture must remain cached after actor removal", TextureCache.contains(reimu.getImageFileName()));
        assertSame("Cached texture must match original instance", girlTexture, TextureCache.getTexture(reimu.getImageFileName()));

        // Test BulletActor
        Bullet bullet = reimu.createBullet();
        BulletActor bulletActor = new BulletActor(bullet, 100, 100, 1, 0);
        bulletActor.remove();
        assertTrue(TextureCache.contains(bullet.getImageFileName()));

        // Test SpellCardActor
        SpellCard spellCard = reimu.createSpellCard();
        SpellCardActor spellCardActor = new SpellCardActor(spellCard, 100, 100);
        spellCardActor.remove();
        assertTrue(TextureCache.contains(spellCard.getImageFileName()));

        // Test BloonActor
        Bloon bloon = BloonFactory.createRedBloon();
        BloonActor bloonActor = new BloonActor(bloon, 100, 100, null);
        bloonActor.pop(10);
        assertTrue(TextureCache.contains(bloon.getImageFileName()));
    }

    @Test
    public void testDisposeClearsCache() {
        String path1 = "img/projectiles/magic_spike.png";
        String path2 = "img/projectiles/bat.png";

        Texture t1 = TextureCache.getTexture(path1);
        Texture t2 = TextureCache.getTexture(path2);
        assertEquals(2, TextureCache.size());

        TextureCache.dispose();
        assertEquals(0, TextureCache.size());
        assertFalse(TextureCache.contains(path1));
        assertFalse(TextureCache.contains(path2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFilePathThrowsException() {
        TextureCache.getTexture(null);
    }
}
