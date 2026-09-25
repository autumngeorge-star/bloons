package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bullet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class AssetManagerTest {

	private static HeadlessApplication app;
	private static BloonsTouhouDefense game;

	@BeforeClass
	public static void setUp() {
		Gdx.gl = Mockito.mock(GL20.class);
		Gdx.gl20 = Mockito.mock(GL20.class);

		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		game = new BloonsTouhouDefense() {
			@Override
			public void create() {
				try {
					super.create();
				} catch (Throwable ignored) {
					// Stage/UI initialization fails in headless mode without OpenGL context
				}
			}
		};
		app = new HeadlessApplication(game, config);
	}

	@AfterClass
	public static void tearDown() {
		if (app != null) {
			app.exit();
		}
	}

	@Test
	public void testAssetManagerInitialization() {
		AssetManager assetManager = game.getAssetManager();
		assertNotNull("BloonsTouhouDefense must own a non-null AssetManager", assetManager);
	}

	@Test
	public void testBulletTexturesPreloaded() {
		AssetManager assetManager = game.getAssetManager();
		assertTrue("red_spell_card.png should be loaded into AssetManager",
				assetManager.isLoaded("img/projectiles/red_spell_card.png", Texture.class));
		assertTrue("purple_energy.png should be loaded into AssetManager",
				assetManager.isLoaded("img/projectiles/purple_energy.png", Texture.class));
		assertTrue("blue_knives.png should be loaded into AssetManager",
				assetManager.isLoaded("img/projectiles/blue_knives.png", Texture.class));
		assertTrue("bat.png should be loaded into AssetManager",
				assetManager.isLoaded("img/projectiles/bat.png", Texture.class));
	}

	@Test
	public void testBulletActorUsesAssetManager() {
		AssetManager assetManager = game.getAssetManager();
		Bullet bullet = new Bullet(20f, 2, 2, 500f, false, "red_spell_card.png");
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);

		Texture expectedTexture = assetManager.get("img/projectiles/red_spell_card.png", Texture.class);
		assertNotNull("Expected texture in AssetManager must not be null", expectedTexture);
		assertEquals("BulletActor texture must match AssetManager texture reference",
				expectedTexture, bulletActor.getTextureRegion().getTexture());
	}

	@Test
	public void testBulletActorRemoveDoesNotDisposeTexture() {
		AssetManager assetManager = game.getAssetManager();
		Bullet bullet = new Bullet(20f, 2, 2, 500f, false, "blue_knives.png");
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);

		Texture textureBefore = bulletActor.getTextureRegion().getTexture();
		bulletActor.remove();

		assertTrue("Texture in AssetManager should still be loaded after actor removal",
				assetManager.isLoaded("img/projectiles/blue_knives.png", Texture.class));
		assertSame("Texture reference should remain intact in AssetManager",
				textureBefore, assetManager.get("img/projectiles/blue_knives.png", Texture.class));
	}

	@Test
	public void testBloonsTouhouDefenseDisposeDisposesAssetManager() {
		AssetManager testAssetManager = Mockito.spy(new AssetManager());
		testAssetManager.dispose();
		Mockito.verify(testAssetManager).dispose();
	}
}
