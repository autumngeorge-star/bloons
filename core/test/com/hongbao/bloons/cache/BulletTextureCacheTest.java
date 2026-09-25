package com.hongbao.bloons.cache;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bullet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class BulletTextureCacheTest {

	private HeadlessApplication app;

	@Before
	public void setUp() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		app = new HeadlessApplication(new ApplicationAdapter() {}, config);
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = mock(GL20.class);
	}

	@After
	public void tearDown() {
		BulletTextureCache.dispose();
		if (app != null) {
			app.exit();
		}
	}

	@Test
	public void testNullFilePath() {
		assertNull(BulletTextureCache.getTextureRegion(null));
	}

	@Test
	public void testTextureCachingAndReuse() {
		String filePath = "img/projectiles/red_spell_card.png";

		TextureRegion region1 = BulletTextureCache.getTextureRegion(filePath);
		assertNotNull(region1);
		assertEquals(1, BulletTextureCache.getCacheSize());

		TextureRegion region2 = BulletTextureCache.getTextureRegion(filePath);
		assertSame(region1, region2);
		assertSame(region1.getTexture(), region2.getTexture());
		assertEquals(1, BulletTextureCache.getCacheSize());
	}

	@Test
	public void testDisposeClearsCache() {
		String filePath = "img/projectiles/red_spell_card.png";
		BulletTextureCache.getTextureRegion(filePath);
		assertEquals(1, BulletTextureCache.getCacheSize());

		BulletTextureCache.dispose();
		assertEquals(0, BulletTextureCache.getCacheSize());
	}

	@Test
	public void testBulletActorRemovalDoesNotDisposeTexture() {
		Bullet bullet = new Bullet(10f, 1, 1, 100f, false, "red_spell_card.png");
		BulletActor actor1 = new BulletActor(bullet, 0, 0, 1, 0);
		BulletActor actor2 = new BulletActor(bullet, 10, 10, 1, 0);

		assertSame(actor1.getTextureRegion(), actor2.getTextureRegion());

		Group parentGroup = new Group();
		parentGroup.addActor(actor1);
		parentGroup.addActor(actor2);

		assertTrue(actor1.remove());
		assertNull(actor1.getParent());
		assertNotNull(actor2.getTextureRegion().getTexture());
		assertEquals(1, BulletTextureCache.getCacheSize());
	}
}
