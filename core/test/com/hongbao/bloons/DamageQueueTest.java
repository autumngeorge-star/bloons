package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DamageQueueTest {

	@Test
	public void testSingleLayerPopRed() {
		Bloon red = BloonFactory.createRedBloon();
		BloonPoppedResult result = new BloonPoppedResult(red, 1);

		Assert.assertEquals(1, result.getCashGenerated());
		Assert.assertTrue(result.getBloonsGenerated().isEmpty());
	}

	@Test
	public void testBlueToRedPop() {
		Bloon blue = BloonFactory.createBlueBloon();
		BloonPoppedResult result = new BloonPoppedResult(blue, 1);

		Assert.assertEquals(1, result.getCashGenerated());
		List<Bloon> children = result.getBloonsGenerated();
		Assert.assertEquals(1, children.size());
		Assert.assertEquals(Bloon.Color.RED, children.get(0).getColor());
		Assert.assertEquals(1, children.get(0).getHealth());
	}

	@Test
	public void testMultiLayerDamageExcessPop() {
		// Blue bloon (health 2) receiving 3 damage -> layer 1 pops (1 cash, residual 2), Red takes 2 damage -> destroyed (1 cash).
		Bloon blue = BloonFactory.createBlueBloon();
		BloonPoppedResult result = new BloonPoppedResult(blue, 3);

		Assert.assertEquals(2, result.getCashGenerated());
		Assert.assertTrue(result.getBloonsGenerated().isEmpty());
	}

	@Test
	public void testBlackToPinkPop() {
		Bloon black = BloonFactory.createBlackBloon();
		BloonPoppedResult result = new BloonPoppedResult(black, 1);

		Assert.assertEquals(1, result.getCashGenerated());
		List<Bloon> children = result.getBloonsGenerated();
		Assert.assertEquals(2, children.size());
		for (Bloon child : children) {
			Assert.assertEquals(Bloon.Color.PINK, child.getColor());
			Assert.assertEquals(5, child.getHealth());
		}
	}

	@Test
	public void testLeadToBlackPop() {
		Bloon lead = BloonFactory.createLeadBloon();
		BloonPoppedResult result = new BloonPoppedResult(lead, 1);

		Assert.assertEquals(1, result.getCashGenerated());
		List<Bloon> children = result.getBloonsGenerated();
		Assert.assertEquals(2, children.size());
		for (Bloon child : children) {
			Assert.assertEquals(Bloon.Color.BLACK, child.getColor());
			Assert.assertEquals(6, child.getHealth());
		}
	}

	@Test
	public void testCeramicMultiDamage() {
		// Ceramic bloon (health 18: shell 10).
		// 12 damage: 10 pops Ceramic shell -> 2 Rainbows (health 8). Residual 2 damage hits each Rainbow.
		// Rainbow (8) with 2 damage: 1 damage pops Rainbow into 2 Zebras (residual 1), 1 damage pops each Zebra into 2 Blacks.
		// 2 Rainbows -> 8 Blacks (health 6) total.
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonPoppedResult result = new BloonPoppedResult(ceramic, 12);

		Assert.assertEquals(16, result.getCashGenerated());
		List<Bloon> children = result.getBloonsGenerated();
		Assert.assertEquals(8, children.size());
		for (Bloon child : children) {
			Assert.assertEquals(Bloon.Color.BLACK, child.getColor());
			Assert.assertEquals(6, child.getHealth());
		}
	}

	@Test
	public void testMOABExcessDamageToCeramics() {
		// MOAB (health 218: shell 200).
		// Hit with 205 damage: MOAB takes 200 damage to pop into 4 Ceramics.
		// Residual damage = 5 is propagated to EACH Ceramic.
		// Each Ceramic (health 18) takes 5 damage -> remaining health = 13.
		Bloon moab = BloonFactory.createMOAB();
		moab.setDistanceTravelled(100);

		BloonPoppedResult result = new BloonPoppedResult(moab, 205);

		Assert.assertEquals(220, result.getCashGenerated()); // 200 + 4 * 5
		List<Bloon> children = result.getBloonsGenerated();
		Assert.assertEquals(4, children.size());
		for (Bloon child : children) {
			Assert.assertEquals(Bloon.Color.CERAMIC, child.getColor());
			Assert.assertEquals(13, child.getHealth());
			Assert.assertEquals(100, child.getDistanceTravelled());
		}
	}

	@Test
	public void testZOMGExcessDamageToBFBs() {
		// ZOMG (health 4918: shell 4000).
		// Hit with 4010 damage: ZOMG takes 4000 damage to pop into 4 BFBs.
		// Residual damage = 10 is propagated to EACH BFB.
		// Each BFB (health 918) takes 10 damage -> remaining health = 908.
		Bloon zomg = BloonFactory.createZOMG();
		BloonPoppedResult result = new BloonPoppedResult(zomg, 4010);

		Assert.assertEquals(4040, result.getCashGenerated()); // 4000 + 4 * 10
		List<Bloon> children = result.getBloonsGenerated();
		Assert.assertEquals(4, children.size());
		for (Bloon child : children) {
			Assert.assertEquals(Bloon.Color.BFB, child.getColor());
			Assert.assertEquals(908, child.getHealth());
		}
	}

	@Test
	public void testBackwardTrackPositionStraight() {
		Map map = MapFactory.createBasicMap(null); // Basic map directions are (1, 0)
		Pair<Float, Float> pos = map.getBackwardTrackPosition(100f, 425f, 12f);

		Assert.assertEquals(88f, pos.getFirst(), 0.001f);
		Assert.assertEquals(425f, pos.getSecond(), 0.001f);
	}

	@Test
	public void testBackwardTrackPositionTurn() {
		Map map = MapFactory.createMapWithTurn(null);
		// Tile (4, 9) has direction (0, 1) going up.
		// Tile index x=4 corresponds to map X coordinate 175 (since xTile = (X + 50)/50).
		Pair<Float, Float> pos = map.getBackwardTrackPosition(175f, 475f, 10f);

		Assert.assertEquals(175f, pos.getFirst(), 0.001f);
		Assert.assertEquals(465f, pos.getSecond(), 0.001f);
	}

}
