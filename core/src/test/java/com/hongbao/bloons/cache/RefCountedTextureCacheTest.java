package com.hongbao.bloons.cache;

import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RefCountedTextureCacheTest {

    private TestableRefCountedTextureCache cache;

    private static class TestableRefCountedTextureCache extends RefCountedTextureCache {
        @Override
        protected Texture loadTextureFromFile(String fileName) {
            return mock(Texture.class);
        }
    }

    @Before
    public void setUp() {
        cache = new TestableRefCountedTextureCache();
        RefCountedTextureCache.setInstance(cache);
    }

    @Test
    public void testAcquireAndIncrementRefCount() {
        String file = "img/bloons/red.png";
        Texture tex1 = cache.getTexture(file);
        assertNotNull(tex1);
        assertEquals(1, cache.getRefCount(file));
        assertTrue(cache.isLoaded(file));
        assertEquals(1, cache.getLoadedTextureCount());

        Texture tex2 = cache.getTexture(file);
        assertSame(tex1, tex2);
        assertEquals(2, cache.getRefCount(file));
        assertEquals(1, cache.getLoadedTextureCount());
    }

    @Test
    public void testReleaseAndDisposeOnZero() {
        String file = "img/bloons/blue.png";
        Texture tex = cache.getTexture(file);
        assertEquals(1, cache.getRefCount(file));

        int remaining = cache.release(file);
        assertEquals(0, remaining);
        assertEquals(0, cache.getRefCount(file));
        assertFalse(cache.isLoaded(file));
        assertEquals(0, cache.getLoadedTextureCount());

        verify(tex, times(1)).dispose();
    }

    @Test
    public void testMultipleAcquireAndRelease() {
        String file = "img/bloons/green.png";
        Texture tex = cache.getTexture(file);
        cache.getTexture(file);
        assertEquals(2, cache.getRefCount(file));

        int rem1 = cache.release(file);
        assertEquals(1, rem1);
        assertTrue(cache.isLoaded(file));
        verify(tex, never()).dispose();

        int rem2 = cache.release(file);
        assertEquals(0, rem2);
        assertFalse(cache.isLoaded(file));
        verify(tex, times(1)).dispose();
    }

    @Test
    public void testPreventDoubleDisposal() {
        String file = "img/bloons/yellow.png";
        Texture tex = cache.getTexture(file);
        cache.release(file);
        verify(tex, times(1)).dispose();

        // Second release when not in cache should return 0 and not throw/dispose again
        int remaining = cache.release(file);
        assertEquals(0, remaining);
        verify(tex, times(1)).dispose();
    }

    @Test
    public void testGracePeriodPreventsReloadThrashing() {
        cache.setGracePeriodSeconds(1.0f);
        String file = "img/bullets/reimu_bullet.png";

        Texture tex1 = cache.getTexture(file);
        assertEquals(1, cache.getRefCount(file));

        // Release texture, ref count drops to 0
        cache.release(file);
        assertEquals(0, cache.getRefCount(file));
        verify(tex1, never()).dispose();

        // Re-acquire before grace period expires -> reuses same texture
        Texture tex2 = cache.getTexture(file);
        assertSame(tex1, tex2);
        assertEquals(1, cache.getRefCount(file));

        // Release again
        cache.release(file);
        assertEquals(0, cache.getRefCount(file));

        // Advance time past grace period
        cache.update(1.5f);
        verify(tex1, times(1)).dispose();
        assertFalse(cache.isLoaded(file));
    }

    @Test
    public void testClearDisposesAllTextures() {
        Texture tex1 = cache.getTexture("img/1.png");
        Texture tex2 = cache.getTexture("img/2.png");
        assertEquals(2, cache.getLoadedTextureCount());

        cache.clear();
        assertEquals(0, cache.getLoadedTextureCount());
        verify(tex1, times(1)).dispose();
        verify(tex2, times(1)).dispose();
    }

    @Test
    public void testNullOrEmptyFileNameHandling() {
        assertNull(cache.getTexture(null));
        assertNull(cache.getTexture(""));
        assertEquals(0, cache.release((String) null));
        assertEquals(0, cache.release((Texture) null));
    }
}
