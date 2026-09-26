package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.factories.MapFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.List;

import static org.junit.Assert.*;

public class MapTest {

	private static final GL20 mockGL;
	private static final Application mockApp;
	private static final Audio mockAudio;

	static {
		HeadlessNativesLoader.load();

		mockGL = (GL20) Proxy.newProxyInstance(
			GL20.class.getClassLoader(),
			new Class<?>[]{GL20.class},
			(proxy, method, args) -> {
				if (method.getName().equals("hashCode")) return System.identityHashCode(proxy);
				if (method.getName().equals("equals")) return args[0] == proxy;
				if (method.getReturnType().equals(boolean.class)) return false;
				if (method.getReturnType().equals(int.class)) return 1;
				if (method.getReturnType().equals(float.class)) return 0f;
				if (method.getReturnType().equals(String.class)) return "";
				return null;
			}
		);

		mockApp = (Application) Proxy.newProxyInstance(
			Application.class.getClassLoader(),
			new Class<?>[]{Application.class},
			(proxy, method, args) -> {
				if (method.getName().equals("hashCode")) return System.identityHashCode(proxy);
				if (method.getName().equals("equals")) return args[0] == proxy;
				if (method.getReturnType().equals(boolean.class)) return false;
				if (method.getReturnType().equals(int.class)) return 0;
				return null;
			}
		);

		mockAudio = (Audio) Proxy.newProxyInstance(
			Audio.class.getClassLoader(),
			new Class<?>[]{Audio.class},
			(proxy, method, args) -> {
				if (method.getName().equals("hashCode")) return System.identityHashCode(proxy);
				if (method.getName().equals("equals")) return args[0] == proxy;
				if (method.getReturnType().equals(boolean.class)) return false;
				if (method.getReturnType().equals(int.class)) return 0;
				return null;
			}
		);

		Gdx.gl = mockGL;
		Gdx.gl20 = mockGL;
		Gdx.files = new HeadlessFiles();
		Gdx.app = mockApp;
		Gdx.audio = mockAudio;
	}

	@Before
	public void setup() {
		Gdx.gl = mockGL;
		Gdx.gl20 = mockGL;
		if (Gdx.files == null) {
			Gdx.files = new HeadlessFiles();
		}
		if (Gdx.app == null) {
			Gdx.app = mockApp;
		}
		if (Gdx.audio == null) {
			Gdx.audio = mockAudio;
		}
	}

	@Test
	public void testPathRectanglesCachedOnMapCreation() {
		Map map = MapFactory.createBasicMap(null);
		List<Rectangle> rects = map.getPathRectangles();
		assertNotNull("Path rectangles should not be null", rects);
		assertFalse("Basic map should have cached path rectangles", rects.isEmpty());
		
		assertEquals(32, rects.size());
		
		Rectangle first = rects.get(0);
		assertEquals(-50f, first.x, 0.001f);
		assertEquals(400f, first.y, 0.001f);
		assertEquals(50f, first.width, 0.001f);
		assertEquals(50f, first.height, 0.001f);
	}

	@Test
	public void testTowerPlacementOnPathOverlapReturnsFalse() {
		Map map = MapFactory.createBasicMap(null);
		Girl girl = GirlFactory.createReimu();
		GirlActor actor = new GirlActor(girl, 200, 425);
		assertFalse("Placement directly on path tile should fail", map.canPlaceGirl(actor));
	}

	@Test
	public void testTowerPlacementNearCornerAllowsValidPlacement() {
		Map map = MapFactory.createMapWithTurn(null);
		Girl girl = GirlFactory.createReimu();
		// Path turn corner is at (200, 550).
		// Circle at (215, 535) with radius ~15 does not overlap path rectangles,
		// verifying elimination of false placement blocks near path corners.
		GirlActor actor = new GirlActor(girl, 215, 535);
		assertTrue("Placement near path corner without circle overlap should succeed", map.canPlaceGirl(actor));
	}

	@Test
	public void testScreenBoundaryCheckWithRadius() {
		Map map = MapFactory.createBasicMap(null);
		Girl girl = GirlFactory.createReimu();
		
		GirlActor centerActor = new GirlActor(girl, 500, 100);
		float radius = centerActor.getCollisionRadius();

		GirlActor leftActor = new GirlActor(girl, radius - 5f, 100);
		assertFalse("Tower extending past left screen boundary should fail", map.canPlaceGirl(leftActor));

		GirlActor rightActor = new GirlActor(girl, 1500f - radius + 5f, 100);
		assertFalse("Tower extending past right screen boundary should fail", map.canPlaceGirl(rightActor));

		GirlActor bottomActor = new GirlActor(girl, 500, radius - 5f);
		assertFalse("Tower extending past bottom screen boundary should fail", map.canPlaceGirl(bottomActor));

		GirlActor topActor = new GirlActor(girl, 500, 900f - radius + 5f);
		assertFalse("Tower extending past top screen boundary should fail", map.canPlaceGirl(topActor));

		GirlActor validActor = new GirlActor(girl, 500, 100);
		assertTrue("Tower completely within bounds and off path should succeed", map.canPlaceGirl(validActor));
	}

	@Test
	public void testTowerToTowerDistanceCheck() {
		Map map = MapFactory.createBasicMap(null);
		Girl girl1 = GirlFactory.createReimu();
		Girl girl2 = GirlFactory.createYukari();

		GirlActor actor1 = new GirlActor(girl1, 500, 100);
		assertTrue(map.canPlaceGirl(actor1));
		map.placeGirl(actor1);

		GirlActor actor2Close = new GirlActor(girl2, 500 + actor1.getCollisionRadius(), 100);
		assertFalse("Placing tower overlapping existing tower should fail", map.canPlaceGirl(actor2Close));

		GirlActor actor2Far = new GirlActor(girl2, 500 + actor1.getCollisionRadius() + 50f, 100);
		assertTrue("Placing tower sufficiently distant from existing tower should succeed", map.canPlaceGirl(actor2Far));
	}
}
