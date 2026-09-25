package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.lang.reflect.Proxy;

public class BloonTextureCacheTest {

	public static void main(String[] args) {
		System.out.println("Running BloonTextureCache Unit Tests...");

		testSingletonAndCache();
		testBloonActorConstructorAndRemoval();
		testBloonManagerCleanup();

		System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
	}

	private static void testSingletonAndCache() {
		System.out.println("- Test: Singleton and cache methods");
		BloonTextureCache cache1 = BloonTextureCache.getInstance();
		BloonTextureCache cache2 = BloonTextureCache.getInstance();
		assert cache1 != null : "Cache instance should not be null";
		assert cache1 == cache2 : "getInstance() should return singleton instance";

		assert !cache1.containsTexture("img/bloons/red.png") : "Cache should be empty initially";
		assert cache1.size() == 0 : "Cache size should be 0 initially";
	}

	private static void testBloonActorConstructorAndRemoval() {
		System.out.println("- Test: BloonActor constructor and texture preservation on removal");
		setupMockGdx();

		// Create mock texture
		MockTexture dummyTexture = new MockTexture();
		TextureRegion dummyRegion = new TextureRegion(dummyTexture);

		Bloon redBloon = BloonFactory.createRedBloon();
		BloonActor actor = new BloonActor(redBloon, dummyRegion, 100f, 100f, null);

		assert actor.getTextureRegion() == dummyRegion : "BloonActor should use passed TextureRegion";
		assert actor.getBloon() == redBloon : "BloonActor should reference given Bloon entity";

		// Test RenderableActor.remove() does NOT dispose texture
		actor.remove();
		assert !dummyTexture.disposed : "Actor removal must NOT dispose shared texture";

		// Test BloonActor.pop() does NOT dispose texture
		actor.pop(1);
		assert !dummyTexture.disposed : "BloonActor.pop() must NOT dispose shared texture";

		// Test BloonActor.release() does NOT dispose texture
		setupDummyApp();
		actor.release();
		assert !dummyTexture.disposed : "BloonActor.release() must NOT dispose shared texture";
	}

	private static void testBloonManagerCleanup() {
		System.out.println("- Test: BloonManager reset and cache disposal");

		BloonTextureCache cache = BloonTextureCache.getInstance();
		assert cache != null : "Cache should be initialized";

		BloonTextureCache.clear();
		assert BloonTextureCache.getInstance() != cache : "clear() should reset singleton instance";
	}

	private static void setupMockGdx() {
		if (Gdx.files == null) {
			Gdx.files = new LwjglFiles();
		}
		if (Gdx.gl == null) {
			GL20 glMock = (GL20) Proxy.newProxyInstance(
					GL20.class.getClassLoader(),
					new Class<?>[]{GL20.class},
					(proxy, method, methodArgs) -> 0
			);
			Gdx.gl = glMock;
			Gdx.gl20 = glMock;
		}
	}

	private static void setupDummyApp() {
		Gdx.app = (Application) Proxy.newProxyInstance(
				Application.class.getClassLoader(),
				new Class<?>[]{Application.class},
				(proxy, method, methodArgs) -> {
					if ("getApplicationListener".equals(method.getName())) {
						return createDummyDefenseApp();
					}
					return null;
				}
		);
	}

	private static BloonsTouhouDefense createDummyDefenseApp() {
		return new BloonsTouhouDefense() {
			private final Player p = new Player(1000, 100);
			@Override
			public Player getPlayer() {
				return p;
			}
		};
	}

	private static class MockTexture extends Texture {
		public boolean disposed = false;

		public MockTexture() {
			super(createDummyTextureData());
		}

		@Override
		public int getWidth() {
			return 32;
		}

		@Override
		public int getHeight() {
			return 32;
		}

		@Override
		public void dispose() {
			this.disposed = true;
		}
	}

	private static TextureData createDummyTextureData() {
		return (TextureData) Proxy.newProxyInstance(
				TextureData.class.getClassLoader(),
				new Class<?>[]{TextureData.class},
				(proxy, method, methodArgs) -> {
					if ("getType".equals(method.getName())) {
						return TextureData.TextureDataType.Custom;
					}
					if ("getFormat".equals(method.getName())) {
						return Pixmap.Format.RGBA8888;
					}
					if ("getWidth".equals(method.getName()) || "getHeight".equals(method.getName())) {
						return 32;
					}
					if ("isPrepared".equals(method.getName()) || "useMipMaps".equals(method.getName()) || "isManaged".equals(method.getName())) {
						return true;
					}
					return null;
				}
		);
	}
}
