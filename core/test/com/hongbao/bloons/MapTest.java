package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class MapTest {

	private static final float EPSILON = 1e-4f;

	@BeforeClass
	public static void initGdx() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new ApplicationAdapter() {}, config);
		Gdx.gl = (com.badlogic.gdx.graphics.GL20) java.lang.reflect.Proxy.newProxyInstance(
			com.badlogic.gdx.graphics.GL20.class.getClassLoader(),
			new Class<?>[]{ com.badlogic.gdx.graphics.GL20.class },
			(proxy, method, args) -> {
				if (method.getReturnType().equals(boolean.class)) return false;
				if (method.getReturnType().equals(int.class)) return 1;
				if (method.getReturnType().equals(float.class)) return 0f;
				return null;
			}
		);
		Gdx.gl20 = Gdx.gl;
	}

	@Test
	public void testBasicMapWaypointsAndDirections() {
		Map map = new Map("basic_map.png", null);
		map.setWaypoints(Arrays.asList(
			new Vector2(-25f, 425f),
			new Vector2(1600f, 425f)
		));

		Assert.assertNotNull(map.getWaypoints());
		Assert.assertEquals(2, map.getWaypoints().size());

		// Test directions along basic map
		Pair<Float, Float> dir1 = map.getDirection(-25f, 425f);
		Assert.assertEquals(1.0f, dir1.getFirst(), EPSILON);
		Assert.assertEquals(0.0f, dir1.getSecond(), EPSILON);

		Pair<Float, Float> dir2 = map.getDirection(500f, 425f);
		Assert.assertEquals(1.0f, dir2.getFirst(), EPSILON);
		Assert.assertEquals(0.0f, dir2.getSecond(), EPSILON);

		// Column 31 check (x = 1575, y = 425) - historically had null reference bug
		Pair<Float, Float> dir3 = map.getDirection(1575f, 425f);
		Assert.assertNotNull(dir3);
		Assert.assertEquals(1.0f, dir3.getFirst(), EPSILON);
		Assert.assertEquals(0.0f, dir3.getSecond(), EPSILON);
	}

	@Test
	public void testMapWithTurnWaypointsAndDirections() {
		Map map = new Map("map_with_turn.png", null);
		map.setWaypoints(Arrays.asList(
			new Vector2(-25f, 425f),
			new Vector2(225f, 425f),
			new Vector2(225f, 575f),
			new Vector2(1600f, 575f)
		));

		// First segment (moving right)
		Pair<Float, Float> dirSeg1 = map.getDirection(100f, 425f);
		Assert.assertEquals(1.0f, dirSeg1.getFirst(), EPSILON);
		Assert.assertEquals(0.0f, dirSeg1.getSecond(), EPSILON);

		// Second segment (moving up)
		Pair<Float, Float> dirSeg2 = map.getDirection(225f, 500f);
		Assert.assertEquals(0.0f, dirSeg2.getFirst(), EPSILON);
		Assert.assertEquals(1.0f, dirSeg2.getSecond(), EPSILON);

		// Third segment (moving right)
		Pair<Float, Float> dirSeg3 = map.getDirection(500f, 575f);
		Assert.assertEquals(1.0f, dirSeg3.getFirst(), EPSILON);
		Assert.assertEquals(0.0f, dirSeg3.getSecond(), EPSILON);
	}

	@Test
	public void testHeaterMapWaypointsAndDirections() {
		Map map = new Map("heater.png", null);
		map.setWaypoints(Arrays.asList(
			new Vector2(-25f, 425f),
			new Vector2(225f, 425f),
			new Vector2(225f, 825f),
			new Vector2(375f, 825f),
			new Vector2(375f, 125f),
			new Vector2(525f, 125f),
			new Vector2(525f, 825f),
			new Vector2(1600f, 425f)
		));

		// Test vertical down segment (375, 825) -> (375, 125)
		Pair<Float, Float> dirDown = map.getDirection(375f, 500f);
		Assert.assertEquals(0.0f, dirDown.getFirst(), EPSILON);
		Assert.assertEquals(-1.0f, dirDown.getSecond(), EPSILON);

		// Test vertical up segment (525, 125) -> (525, 825)
		Pair<Float, Float> dirUp = map.getDirection(525f, 500f);
		Assert.assertEquals(0.0f, dirUp.getFirst(), EPSILON);
		Assert.assertEquals(1.0f, dirUp.getSecond(), EPSILON);
	}

	@Test
	public void testDistanceToSegment() {
		// Segment from (0, 0) to (100, 0)
		float dist1 = Map.distanceToSegment(50f, 20f, 0f, 0f, 100f, 0f);
		Assert.assertEquals(20.0f, dist1, EPSILON);

		float dist2 = Map.distanceToSegment(120f, 0f, 0f, 0f, 100f, 0f);
		Assert.assertEquals(20.0f, dist2, EPSILON);

		float dist3 = Map.distanceToSegment(-10f, 0f, 0f, 0f, 100f, 0f);
		Assert.assertEquals(10.0f, dist3, EPSILON);
	}

	@Test
	public void testMapFactoryCreation() {
		Map basicMap = MapFactory.createBasicMap(null);
		Assert.assertNotNull(basicMap.getWaypoints());
		Assert.assertEquals(2, basicMap.getWaypoints().size());

		Map mapWithTurn = MapFactory.createMapWithTurn(null);
		Assert.assertNotNull(mapWithTurn.getWaypoints());
		Assert.assertEquals(4, mapWithTurn.getWaypoints().size());

		Map heaterMap = MapFactory.createHeaterMap(null);
		Assert.assertNotNull(heaterMap.getWaypoints());
		Assert.assertEquals(18, heaterMap.getWaypoints().size());
	}

	@Test
	public void testEmptyWaypointsSafety() {
		Map map = new Map("test", null);
		Pair<Float, Float> dir = map.getDirection(100f, 100f);
		Assert.assertNotNull(dir);
		Assert.assertEquals(0.0f, dir.getFirst(), EPSILON);
		Assert.assertEquals(0.0f, dir.getSecond(), EPSILON);
	}
}
