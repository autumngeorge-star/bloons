package com.hongbao.bloons.entities;

import org.junit.Assert;
import org.junit.Test;

public class GirlTierTest {

	@Test
	public void testGirlTierGetters() {
		GirlTier tier = new GirlTier(50, 25.5f, 3, 5, 450f, 220f, true, 300);

		Assert.assertEquals(50, tier.getAttackDelay());
		Assert.assertEquals(25.5f, tier.getBulletSpeed(), 0.001f);
		Assert.assertEquals(3, tier.getDamage());
		Assert.assertEquals(5, tier.getPierce());
		Assert.assertEquals(450f, tier.getRange(), 0.001f);
		Assert.assertEquals(220f, tier.getVisualRange(), 0.001f);
		Assert.assertTrue(tier.isHoming());
		Assert.assertEquals(300, tier.getUpgradeCost());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGirlTierNullValidation() {
		new GirlTier(null, 25.5f, 3, 5, 450f, 220f, true, 300);
	}

}
