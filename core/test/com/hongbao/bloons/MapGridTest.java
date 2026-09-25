package com.hongbao.bloons;

import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapGridTest {

	private static final float DELTA = 0.0001f;

	@Test
	public void testGridInitialization() {
		MapGrid mapGrid = new MapGrid();
		for (int x = 0; x < MapGrid.COLUMNS; x++) {
			for (int y = 0; y < MapGrid.ROWS; y++) {
				Pair<Float, Float> dir = mapGrid.getDirectionTile(x, y);
				assertNotNull("Cell (" + x + ", " + y + ") should not be null", dir);
				assertEquals("Default cell X should be 0", 0f, dir.getFirst(), DELTA);
				assertEquals("Default cell Y should be 0", 0f, dir.getSecond(), DELTA);
			}
		}
	}

	@Test
	public void testColumn31Initialization() {
		MapGrid mapGrid = new MapGrid();
		for (int y = 0; y < MapGrid.ROWS; y++) {
			Pair<Float, Float> dir = mapGrid.getDirectionTile(31, y);
			assertNotNull("Column 31 cell (" + 31 + ", " + y + ") should not be null", dir);
		}

		// Look up coordinate at column 31 (x = 1500f, y = 400f)
		Pair<Float, Float> dirAtCoordinate = mapGrid.getDirection(1500f, 400f);
		assertNotNull("Column 31 coordinate lookup should not be null", dirAtCoordinate);
		assertEquals(0f, dirAtCoordinate.getFirst(), DELTA);
		assertEquals(0f, dirAtCoordinate.getSecond(), DELTA);
	}

	@Test
	public void testCoordinateMath() {
		MapGrid mapGrid = new MapGrid();

		assertEquals(0, mapGrid.getXTile(-50f));
		assertEquals(1, mapGrid.getXTile(0f));
		assertEquals(31, mapGrid.getXTile(1500f));
		assertEquals(32, mapGrid.getXTile(1550f));

		assertEquals(0, mapGrid.getYTile(0f));
		assertEquals(8, mapGrid.getYTile(400f));
		assertEquals(17, mapGrid.getYTile(850f));
		assertEquals(18, mapGrid.getYTile(900f));

		assertEquals(25f, MapGrid.getCenterXOfTile(0), DELTA);
		assertEquals(75f, MapGrid.getCenterXOfTile(1), DELTA);
		assertEquals(25f, MapGrid.getCenterYOfTile(0), DELTA);
		assertEquals(425f, MapGrid.getCenterYOfTile(8), DELTA);
	}

	@Test
	public void testBoundaryChecking() {
		MapGrid mapGrid = new MapGrid();

		// Negative X
		Pair<Float, Float> negX = mapGrid.getDirection(-150f, 400f);
		assertNotNull(negX);
		assertEquals(0f, negX.getFirst(), DELTA);
		assertEquals(0f, negX.getSecond(), DELTA);

		// Negative Y
		Pair<Float, Float> negY = mapGrid.getDirection(100f, -50f);
		assertNotNull(negY);
		assertEquals(0f, negY.getFirst(), DELTA);
		assertEquals(0f, negY.getSecond(), DELTA);

		// Out-of-bounds X right (x >= 1550f -> xTile >= 32)
		Pair<Float, Float> outRight = mapGrid.getDirection(1600f, 400f);
		assertNotNull(outRight);
		assertEquals(0f, outRight.getFirst(), DELTA);
		assertEquals(0f, outRight.getSecond(), DELTA);

		// Out-of-bounds Y top (y >= 900f -> yTile >= 18)
		Pair<Float, Float> outTop = mapGrid.getDirection(100f, 950f);
		assertNotNull(outTop);
		assertEquals(0f, outTop.getFirst(), DELTA);
		assertEquals(0f, outTop.getSecond(), DELTA);

		// Tile index boundary checks
		assertNotNull(mapGrid.getDirectionTile(-1, 0));
		assertNotNull(mapGrid.getDirectionTile(32, 0));
		assertNotNull(mapGrid.getDirectionTile(0, -1));
		assertNotNull(mapGrid.getDirectionTile(0, 18));
	}

	@Test
	public void testBasicMapLayout() {
		MapGrid basicMapGrid = MapFactory.createBasicMapGrid();

		// All tiles on row yTile = 8 across all 32 columns should point right (1f, 0f)
		for (int x = 0; x < MapGrid.COLUMNS; x++) {
			Pair<Float, Float> dir = basicMapGrid.getDirectionTile(x, 8);
			assertNotNull("Basic map path tile at x=" + x + " should not be null", dir);
			assertEquals("Basic map path tile at x=" + x + " X-direction", 1f, dir.getFirst(), DELTA);
			assertEquals("Basic map path tile at x=" + x + " Y-direction", 0f, dir.getSecond(), DELTA);
			assertTrue("Basic map x=" + x + " should be path tile", basicMapGrid.isPathTile(x, 8));
		}

		// Non-path tile
		Pair<Float, Float> nonPathDir = basicMapGrid.getDirectionTile(0, 0);
		assertEquals(0f, nonPathDir.getFirst(), DELTA);
		assertEquals(0f, nonPathDir.getSecond(), DELTA);
	}

	@Test
	public void testMapWithTurnLayout() {
		MapGrid turnMapGrid = MapFactory.createMapWithTurnGrid();

		// Check start of path
		Pair<Float, Float> startDir = turnMapGrid.getDirectionTile(0, 8);
		assertEquals(1f, startDir.getFirst(), DELTA);
		assertEquals(0f, startDir.getSecond(), DELTA);

		// Check turn tile (4, 8)
		Pair<Float, Float> turnDir = turnMapGrid.getDirectionTile(4, 8);
		assertEquals(MapFactory.ROOT_2_OVER_2, turnDir.getFirst(), DELTA);
		assertEquals(MapFactory.ROOT_2_OVER_2, turnDir.getSecond(), DELTA);

		// Check after turn path (5..31, 11)
		for (int x = 5; x < MapGrid.COLUMNS; x++) {
			Pair<Float, Float> dir = turnMapGrid.getDirectionTile(x, 11);
			assertEquals(1f, dir.getFirst(), DELTA);
			assertEquals(0f, dir.getSecond(), DELTA);
		}
	}

	@Test
	public void testHeaterMapLayout() {
		MapGrid heaterMapGrid = MapFactory.createHeaterMapGrid();

		// Check column 31 on heater map (31, 8)
		Pair<Float, Float> col31Dir = heaterMapGrid.getDirectionTile(31, 8);
		assertNotNull(col31Dir);
		assertEquals(1f, col31Dir.getFirst(), DELTA);
		assertEquals(0f, col31Dir.getSecond(), DELTA);
	}
}
