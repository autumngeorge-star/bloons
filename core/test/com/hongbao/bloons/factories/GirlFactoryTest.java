package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.Bullet;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GirlFactoryTest {

	@Test
	public void testYukariHomingAtAllLevels() {
		Girl yukari = GirlFactory.createYukari();
		assertTrue("Yukari should have homing at Level 0", yukari.isHoming());
		
		Bullet bulletLvl0 = yukari.createBullet();
		assertTrue("Yukari bullet should be homing at Level 0", bulletLvl0.isHoming());

		yukari.upgrade();
		assertTrue("Yukari should have homing at Level 1", yukari.isHoming());

		yukari.upgrade();
		assertTrue("Yukari should have homing at Level 2", yukari.isHoming());
	}

	@Test
	public void testRemiliaHomingAtAllLevels() {
		Girl remilia = GirlFactory.createRemilia();
		assertTrue("Remilia should have homing at Level 0", remilia.isHoming());

		Bullet bulletLvl0 = remilia.createBullet();
		assertTrue("Remilia bullet should be homing at Level 0", bulletLvl0.isHoming());

		remilia.upgrade();
		assertTrue("Remilia should have homing at Level 1", remilia.isHoming());

		remilia.upgrade();
		assertTrue("Remilia should have homing at Level 2", remilia.isHoming());
	}

	@Test
	public void testYoumuProgressiveHoming() {
		Girl youmu = GirlFactory.createYoumu();
		assertFalse("Youmu should NOT have homing at Level 0", youmu.isHoming());
		assertFalse("Youmu bullet should NOT be homing at Level 0", youmu.createBullet().isHoming());

		youmu.upgrade();
		assertTrue("Youmu should have homing at Level 1", youmu.isHoming());
		assertTrue("Youmu bullet should be homing at Level 1", youmu.createBullet().isHoming());

		youmu.upgrade();
		assertTrue("Youmu should have homing at Level 2", youmu.isHoming());
		assertTrue("Youmu bullet should be homing at Level 2", youmu.createBullet().isHoming());
	}

	@Test
	public void testSakuyaProgressiveHoming() {
		Girl sakuya = GirlFactory.createSakuya();
		assertFalse("Sakuya should NOT have homing at Level 0", sakuya.isHoming());
		assertFalse("Sakuya bullet should NOT be homing at Level 0", sakuya.createBullet().isHoming());

		sakuya.upgrade();
		assertTrue("Sakuya should have homing at Level 1", sakuya.isHoming());
		assertTrue("Sakuya bullet should be homing at Level 1", sakuya.createBullet().isHoming());

		sakuya.upgrade();
		assertTrue("Sakuya should have homing at Level 2", sakuya.isHoming());
		assertTrue("Sakuya bullet should be homing at Level 2", sakuya.createBullet().isHoming());
	}

	@Test
	public void testMarisaProgressiveHoming() {
		Girl marisa = GirlFactory.createMarisa();
		assertFalse("Marisa should NOT have homing at Level 0", marisa.isHoming());
		assertFalse("Marisa bullet should NOT be homing at Level 0", marisa.createBullet().isHoming());

		marisa.upgrade();
		assertTrue("Marisa should have homing at Level 1", marisa.isHoming());
		assertTrue("Marisa bullet should be homing at Level 1", marisa.createBullet().isHoming());

		marisa.upgrade();
		assertTrue("Marisa should have homing at Level 2", marisa.isHoming());
		assertTrue("Marisa bullet should be homing at Level 2", marisa.createBullet().isHoming());
	}

	@Test
	public void testBaselineTowersUnchanged() {
		Girl reimu = GirlFactory.createReimu();
		assertTrue(reimu.isHoming());

		Girl alice = GirlFactory.createAlice();
		assertFalse(alice.isHoming());
		alice.upgrade();
		assertTrue(alice.isHoming());

		Girl yuyuko = GirlFactory.createYuyuko();
		assertTrue(yuyuko.isHoming());
	}
}
