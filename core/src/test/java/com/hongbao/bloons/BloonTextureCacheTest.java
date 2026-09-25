package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Clipboard;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.entities.Bloon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class BloonTextureCacheTest {

	private static class DummyApplication implements Application {
		@Override public ApplicationListener getApplicationListener() { return null; }
		@Override public Graphics getGraphics() { return null; }
		@Override public Audio getAudio() { return null; }
		@Override public Input getInput() { return null; }
		@Override public Files getFiles() { return null; }
		@Override public Net getNet() { return null; }
		@Override public void log(String tag, String message) {}
		@Override public void log(String tag, String message, Throwable exception) {}
		@Override public void error(String tag, String message) {}
		@Override public void error(String tag, String message, Throwable exception) {}
		@Override public void debug(String tag, String message) {}
		@Override public void debug(String tag, String message, Throwable exception) {}
		@Override public void setLogLevel(int logLevel) {}
		@Override public int getLogLevel() { return 0; }
		@Override public void setApplicationLogger(ApplicationLogger applicationLogger) {}
		@Override public ApplicationLogger getApplicationLogger() { return null; }
		@Override public ApplicationType getType() { return ApplicationType.HeadlessDesktop; }
		@Override public int getVersion() { return 0; }
		@Override public long getJavaHeap() { return 0; }
		@Override public long getNativeHeap() { return 0; }
		@Override public Preferences getPreferences(String name) { return null; }
		@Override public Clipboard getClipboard() { return null; }
		@Override public void postRunnable(Runnable runnable) {}
		@Override public void exit() {}
		@Override public void addLifecycleListener(LifecycleListener listener) {}
		@Override public void removeLifecycleListener(LifecycleListener listener) {}
	}

	private static class DummyTextureData implements TextureData {
		@Override public TextureDataType getType() { return TextureDataType.Custom; }
		@Override public boolean isPrepared() { return true; }
		@Override public void prepare() {}
		@Override public Pixmap consumePixmap() { return null; }
		@Override public boolean disposePixmap() { return false; }
		@Override public void consumeCustomData(int target) {}
		@Override public int getWidth() { return 100; }
		@Override public int getHeight() { return 100; }
		@Override public Pixmap.Format getFormat() { return Pixmap.Format.RGBA8888; }
		@Override public boolean useMipMaps() { return false; }
		@Override public boolean isManaged() { return false; }
	}

	private static class MockTexture extends Texture {
		private boolean disposed = false;

		public MockTexture() {
			super(new DummyTextureData());
		}

		@Override
		public int getWidth() {
			return 100;
		}

		@Override
		public int getHeight() {
			return 100;
		}

		@Override
		public void dispose() {
			disposed = true;
		}

		public boolean isDisposed() {
			return disposed;
		}
	}

	private static class TestableBloonTextureCache extends BloonTextureCache {
		@Override
		protected Texture createTexture(String fileName) {
			return new MockTexture();
		}
	}

	private TestableBloonTextureCache cache;

	@Before
	public void setUp() {
		Gdx.app = new DummyApplication();
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
		cache = new TestableBloonTextureCache();
	}

	@Test
	public void testTextureReuseForSameFile() {
		String fileName = "img/bloons/red_bloon.png";

		Texture t1 = cache.getTexture(fileName);
		Texture t2 = cache.getTexture(fileName);

		assertNotNull(t1);
		assertSame("Texture instances must be identical for same file path", t1, t2);
		assertEquals(1, cache.size());
		assertTrue(cache.contains(fileName));
	}

	@Test
	public void testTextureRegionCreationSharesTexture() {
		String fileName = "img/bloons/blue_bloon.png";

		TextureRegion r1 = cache.getTextureRegion(fileName);
		TextureRegion r2 = cache.getTextureRegion(fileName);

		assertNotNull(r1);
		assertNotNull(r2);
		assertSame("TextureRegions should share the exact same underlying Texture", r1.getTexture(), r2.getTexture());
		assertEquals(1, cache.size());
	}

	@Test
	public void testCacheDisposalDisposesAllTextures() {
		String f1 = "img/bloons/red_bloon.png";
		String f2 = "img/bloons/green_bloon.png";

		MockTexture t1 = (MockTexture) cache.getTexture(f1);
		MockTexture t2 = (MockTexture) cache.getTexture(f2);

		assertEquals(2, cache.size());

		cache.dispose();

		assertTrue("Texture 1 should be disposed", t1.isDisposed());
		assertTrue("Texture 2 should be disposed", t2.isDisposed());
		assertEquals(0, cache.size());
	}

	@Test
	public void testRenderableActorRemoveDoesNotDisposeTexture() {
		MockTexture mockTexture = new MockTexture();
		TextureRegion region = new TextureRegion(mockTexture);

		RenderableActor actor = new RenderableActor() {};
		actor.setTextureRegion(region);

		actor.remove();

		assertTrue("Removing RenderableActor from stage must NOT dispose shared texture", !mockTexture.isDisposed());
	}

	@Test
	public void testBloonActorPopDoesNotDisposeTexture() {
		MockTexture mockTexture = new MockTexture();
		TextureRegion region = new TextureRegion(mockTexture);
		Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);

		BloonActor bloonActor = new BloonActor(bloon, region, 0, 0, null);
		bloonActor.pop(1);

		assertTrue("Popping BloonActor must NOT dispose shared texture", !mockTexture.isDisposed());
	}
}
