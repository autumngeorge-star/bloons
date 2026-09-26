package com.hongbao.bloons.strategies;

import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.helpers.Pair;
import org.junit.Assert;
import org.junit.Test;

public class TrajectoryStrategyTest {

	@Test
	public void testReimuTrajectoryStrategy() {
		ReimuTrajectoryStrategy strategy = new ReimuTrajectoryStrategy();

		// Phase 1: Straight line (< 20 frames)
		Pair<Float, Float> dir0 = strategy.calculateDirection(0, 0f, 1f);
		Assert.assertNotNull(dir0);
		Assert.assertEquals(0f, dir0.getFirst(), 0.0001f);
		Assert.assertEquals(1f, dir0.getSecond(), 0.0001f);

		Pair<Float, Float> dir19 = strategy.calculateDirection(19, 0f, 1f);
		Assert.assertNotNull(dir19);
		Assert.assertEquals(0f, dir19.getFirst(), 0.0001f);
		Assert.assertEquals(1f, dir19.getSecond(), 0.0001f);

		// Phase 2: Circular curving (20..200 frames)
		Pair<Float, Float> dir50 = strategy.calculateDirection(50, 0f, 1f);
		Assert.assertNotNull(dir50);

		Pair<Float, Float> dir200 = strategy.calculateDirection(200, 0f, 1f);
		Assert.assertNotNull(dir200);

		// Phase 3: Hand off to target homing (> 200 frames)
		Pair<Float, Float> dir201 = strategy.calculateDirection(201, 0f, 1f);
		Assert.assertNull("Reimu strategy must return null after frame 200 to hand off to homing", dir201);
	}

	@Test
	public void testYuyukoTrajectoryStrategy() {
		YuyukoTrajectoryStrategy strategy = new YuyukoTrajectoryStrategy();

		// Initial fan formation phase (<= 150 frames)
		Pair<Float, Float> dir0 = strategy.calculateDirection(0, 1f, 0f);
		Assert.assertNotNull(dir0);

		Pair<Float, Float> dir75 = strategy.calculateDirection(75, 1f, 0f);
		Assert.assertNotNull(dir75);

		Pair<Float, Float> dir150 = strategy.calculateDirection(150, 1f, 0f);
		Assert.assertNotNull(dir150);

		// Transition to target homing after initial phase (> 150 frames)
		Pair<Float, Float> dir151 = strategy.calculateDirection(151, 1f, 0f);
		Assert.assertNull("Yuyuko strategy must return null after frame 150 to hand off to homing", dir151);
	}

	@Test
	public void testSpellCardStrategyFactoryAssignment() {
		SpellCard reimuCard = SpellCard.createReimuSpellCard();
		Assert.assertTrue("Reimu spell card must assign ReimuTrajectoryStrategy",
				reimuCard.getTrajectoryStrategy() instanceof ReimuTrajectoryStrategy);

		SpellCard yuyukoCard = SpellCard.createYuyukoSpellCard();
		Assert.assertTrue("Yuyuko spell card must assign YuyukoTrajectoryStrategy",
				yuyukoCard.getTrajectoryStrategy() instanceof YuyukoTrajectoryStrategy);
	}
}
