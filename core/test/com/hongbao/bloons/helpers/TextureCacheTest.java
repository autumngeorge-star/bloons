package com.hongbao.bloons.helpers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class TextureCacheTest {

	private static HeadlessApplication application;

	@BeforeClass
	public static void setUpClass() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		application = new HeadlessApplication(new ApplicationAdapter() {}, config);

		GL20 dummyGL = (GL20) Proxy.newProxyInstance(
			GL20.class.getClassLoader(),
			new Class<?>[]{GL20.class},
			(proxy, method, args) -> {
				if ("glGenTexture".equals(method.getName())) {
					return 1;
				}
				if ("glGetString".equals(method.getName())) {
					return "DummyGL";
				}
				Class<?> returnType = method.getReturnType();
				if (returnType == boolean.class) return false;
				if (returnType == int.class) return 0;
				if (returnType == float.class) return 0f;
				return null;
			}
		);

		Gdx.files = new LwjglFiles();
		Gdx.gl = dummyGL;
		Gdx.gl20 = dummyGL;
	}

	@Before
	public void setUp() {
		TextureCache.clear();
	}

	@AfterClass
	public static void tearDownClass() {
		if (application != null) {
			application.exit();
		}
	}

	@Test
	public void testGetTextureRegionNullAndEmpty() {
		assertNull(TextureCache.getTextureRegion(null));
		assertNull(TextureCache.getTextureRegion(""));
		assertNull(TextureCache.getTexture(null));
		assertNull(TextureCache.getTexture(""));
	}

	@Test
	public void testGetTextureRegionCaching() {
		String imagePath = "img/bloons/red_bloon.png";

		TextureRegion region1 = TextureCache.getTextureRegion(imagePath);
		assertNotNull(region1);
		assertNotNull(region1.getTexture());

		TextureRegion region2 = TextureCache.getTextureRegion(imagePath);
		assertSame("TextureRegion should be cached and return identical instance", region1, region2);

		Texture texture1 = TextureCache.getTexture(imagePath);
		assertSame("Underlying texture should match texture in region", region1.getTexture(), texture1);
	}

	@Test
	public void testActorTextureSharingAndLifecycle() {
		Bloon redBloon1 = BloonFactory.createRedBloon();
		Bloon redBloon2 = BloonFactory.createRedBloon();

		BloonActor actor1 = new BloonActor(redBloon1, 100, 100, null);
		BloonActor actor2 = new BloonActor(redBloon2, 200, 200, null);

		assertSame("Two BloonActor instances for the same bloon type must share TextureRegion",
			actor1.getTextureRegion(), actor2.getTextureRegion());

		assertSame("Two BloonActor instances must share the same underlying Texture object",
			actor1.getTextureRegion().getTexture(), actor2.getTextureRegion().getTexture());

		// Test popping actor1 does NOT dispose shared texture
		actor1.pop(1);
		assertNotNull("Texture must remain valid after actor pop", actor2.getTextureRegion().getTexture());

		// Test removing actor2 does NOT dispose shared texture
		actor2.remove();
		assertNotNull("Texture must remain valid after actor remove", actor2.getTextureRegion().getTexture());
	}

	@Test
	public void testGirlBulletAndSpellCardActorsFetchFromCache() {
		Girl reimu = GirlFactory.createReimu();
		GirlActor girlActor = new GirlActor(reimu, 100, 100);

		TextureRegion reimuRegion = TextureCache.getTextureRegion(reimu.getImageFileName());
		assertSame("GirlActor must fetch TextureRegion from TextureCache", reimuRegion, girlActor.getTextureRegion());

		Bullet bullet = reimu.createBullet();
		BulletActor bulletActor = new BulletActor(bullet, 100, 100, 1, 0);

		TextureRegion bulletRegion = TextureCache.getTextureRegion(bullet.getImageFileName());
		assertSame("BulletActor must fetch TextureRegion from TextureCache", bulletRegion, bulletActor.getTextureRegion());

		SpellCard spellCard = reimu.createSpellCard();
		SpellCardActor spellCardActor = new SpellCardActor(spellCard, 100, 100);

		TextureRegion spellRegion = TextureCache.getTextureRegion(spellCard.getImageFileName());
		assertSame("SpellCardActor must fetch TextureRegion from TextureCache", spellRegion, spellCardActor.getTextureRegion());

		// Clean up
		girlActor.remove();
		bulletActor.remove();
		spellCardActor.remove();
	}
}
