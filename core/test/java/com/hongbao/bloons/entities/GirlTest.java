package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GirlTest {

	@Test
	public void testGirlConstructorValidation() {
		GirlTier tier = new GirlTier(50, 20f, 1, 2, 300f, 150f, false, 100);

		try {
			new Girl("Test", null, "test.png", "bullet.png", 100);
			Assert.fail("Expected NullPointerException for null tiers");
		} catch (NullPointerException expected) {
		}

		try {
			new Girl("Test", Collections.emptyList(), "test.png", "bullet.png", 100);
			Assert.fail("Expected IllegalArgumentException for empty tiers");
		} catch (IllegalArgumentException expected) {
		}

		try {
			List<GirlTier> tiersWithNull = new ArrayList<>();
			tiersWithNull.add(tier);
			tiersWithNull.add(null);
			new Girl("Test", tiersWithNull, "test.png", "bullet.png", 100);
			Assert.fail("Expected NullPointerException for tier element being null");
		} catch (NullPointerException expected) {
		}
	}

	@Test
	public void testVariableTierProgression5Tiers() {
		List<GirlTier> tiers = Arrays.asList(
				new GirlTier(60, 10f, 1, 1, 100f, 100f, false, 100),
				new GirlTier(50, 15f, 2, 2, 200f, 200f, false, 200),
				new GirlTier(40, 20f, 3, 3, 300f, 300f, false, 300),
				new GirlTier(30, 25f, 4, 4, 400f, 400f, true, 400),
				new GirlTier(20, 30f, 5, 5, 500f, 500f, true, Girl.NO_UPGRADES_AVAILABLE)
		);

		Girl girl = new Girl("FiveTierGirl", tiers, "girl.png", "bullet.png", 500);

		Assert.assertEquals(5, girl.getTiers().size());
		Assert.assertEquals(0, girl.getLevel());

		// Tier 0
		Assert.assertEquals(60, girl.getAttackDelay());
		Assert.assertEquals(1, girl.getDamage());
		Assert.assertEquals(1, girl.getPierce());
		Assert.assertEquals(100f, girl.getRange(), 0.001f);
		Assert.assertFalse(girl.isHoming());
		Assert.assertEquals(100, girl.getUpgradeCost());
		Assert.assertEquals("$100", girl.getUpgradeCostString());
		Assert.assertTrue(girl.canUpgrade(150));
		Assert.assertFalse(girl.canUpgrade(50));

		// Preview stats for level 1
		Girl preview1 = girl.getUpgradedStats();
		Assert.assertNotNull(preview1);
		Assert.assertEquals(1, preview1.getLevel());
		Assert.assertEquals(2, preview1.getDamage());
		Assert.assertEquals(200, preview1.getUpgradeCost());

		// Perform upgrade to Tier 1
		int spent = girl.upgrade();
		Assert.assertEquals(100, spent);
		Assert.assertEquals(1, girl.getLevel());
		Assert.assertEquals(50, girl.getAttackDelay());
		Assert.assertEquals(2, girl.getDamage());

		// Upgrade to Tier 2
		girl.upgrade();
		Assert.assertEquals(2, girl.getLevel());
		Assert.assertEquals(3, girl.getDamage());

		// Upgrade to Tier 3
		girl.upgrade();
		Assert.assertEquals(3, girl.getLevel());
		Assert.assertEquals(4, girl.getDamage());
		Assert.assertTrue(girl.isHoming());
		Assert.assertEquals(400, girl.getUpgradeCost());

		// Preview stats for final Tier (Tier 4)
		Girl previewFinal = girl.getUpgradedStats();
		Assert.assertNotNull(previewFinal);
		Assert.assertEquals(4, previewFinal.getLevel());
		Assert.assertEquals(5, previewFinal.getDamage());
		Assert.assertEquals(Girl.NO_UPGRADES_AVAILABLE, previewFinal.getUpgradeCost());
		Assert.assertEquals("N/A", previewFinal.getUpgradeCostString());

		// Upgrade to final Tier 4
		girl.upgrade();
		Assert.assertEquals(4, girl.getLevel());
		Assert.assertEquals(5, girl.getDamage());

		// At max tier
		Assert.assertEquals(Girl.NO_UPGRADES_AVAILABLE, girl.getUpgradeCost());
		Assert.assertEquals("N/A", girl.getUpgradeCostString());
		Assert.assertFalse(girl.canUpgrade(10000));
		Assert.assertNull(girl.getUpgradedStats());
	}

	@Test
	public void testSingleTierTower() {
		List<GirlTier> tiers = Collections.singletonList(
				new GirlTier(30, 20f, 10, 5, 600f, 300f, true, Girl.NO_UPGRADES_AVAILABLE)
		);

		Girl girl = new Girl("SingleTierGirl", tiers, "girl.png", "bullet.png", 1000);

		Assert.assertEquals(1, girl.getTiers().size());
		Assert.assertEquals(0, girl.getLevel());
		Assert.assertEquals(Girl.NO_UPGRADES_AVAILABLE, girl.getUpgradeCost());
		Assert.assertEquals("N/A", girl.getUpgradeCostString());
		Assert.assertFalse(girl.canUpgrade(100000));
		Assert.assertNull(girl.getUpgradedStats());
	}

	@Test
	public void testBulletCreationAndCooldown() {
		Girl girl = GirlFactory.createReimu();

		Assert.assertEquals(86, girl.getCooldown());
		girl.decrementCooldown();
		Assert.assertEquals(85, girl.getCooldown());
		girl.resetCooldown();
		Assert.assertEquals(86, girl.getCooldown());

		Bullet bullet = girl.createBullet();
		Assert.assertNotNull(bullet);
		Assert.assertEquals(1, bullet.getDamage());
		Assert.assertEquals(4, bullet.getPierce());
		Assert.assertEquals(500f, bullet.getMaxRange(), 0.001f);
		Assert.assertTrue(bullet.isHoming());
	}

	@Test
	public void testFactoryMethods() {
		Girl reimu = GirlFactory.createReimu();
		Girl yukari = GirlFactory.createYukari();
		Girl marisa = GirlFactory.createMarisa();
		Girl alice = GirlFactory.createAlice();
		Girl sakuya = GirlFactory.createSakuya();
		Girl remilia = GirlFactory.createRemilia();
		Girl youmu = GirlFactory.createYoumu();
		Girl yuyuko = GirlFactory.createYuyuko();

		Assert.assertNotNull(reimu);
		Assert.assertNotNull(yukari);
		Assert.assertNotNull(marisa);
		Assert.assertNotNull(alice);
		Assert.assertNotNull(sakuya);
		Assert.assertNotNull(remilia);
		Assert.assertNotNull(youmu);
		Assert.assertNotNull(yuyuko);

		Assert.assertEquals(3, reimu.getTiers().size());
		Assert.assertEquals(3, yukari.getTiers().size());
		Assert.assertEquals(3, marisa.getTiers().size());
		Assert.assertEquals(3, alice.getTiers().size());
		Assert.assertEquals(3, sakuya.getTiers().size());
		Assert.assertEquals(3, remilia.getTiers().size());
		Assert.assertEquals(3, youmu.getTiers().size());
		Assert.assertEquals(3, yuyuko.getTiers().size());
	}

}
