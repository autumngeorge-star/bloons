package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.StatusType;
import org.junit.Test;

import static org.junit.Assert.*;

public class BloonStatusTest {

	@Test
	public void testPrimitiveStatusFieldsAndDefaults() {
		Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
		assertEquals(0f, bloon.getSlowTimer(), 0.001f);
		assertEquals(0f, bloon.getFreezeTimer(), 0.001f);
		assertEquals(0f, bloon.getDotTimer(), 0.001f);
		assertEquals(0f, bloon.getDotInterval(), 0.001f);
		assertEquals(0f, bloon.getDotTickTimer(), 0.001f);
		assertEquals(0, bloon.getDotDamage());
		assertFalse(bloon.isSlowed());
		assertFalse(bloon.isFrozen());
		assertFalse(bloon.hasDot());
		assertEquals(5, bloon.getSpeed());
	}

	@Test
	public void testSlowStatusEffect() {
		Bloon bloon = new Bloon(Bloon.Color.YELLOW, 4, false, false); // Base speed 8
		assertEquals(8, bloon.getSpeed());

		bloon.applyStatusEffect(StatusType.SLOW, 3.0f);
		assertTrue(bloon.isSlowed());
		assertEquals(3.0f, bloon.getSlowTimer(), 0.001f);
		assertEquals(4, bloon.getSpeed()); // Speed halved (8 / 2 = 4)

		int damage = bloon.updateStatusTimers(1.5f);
		assertEquals(0, damage);
		assertTrue(bloon.isSlowed());
		assertEquals(1.5f, bloon.getSlowTimer(), 0.001f);
		assertEquals(4, bloon.getSpeed());

		damage = bloon.updateStatusTimers(1.6f);
		assertEquals(0, damage);
		assertFalse(bloon.isSlowed());
		assertEquals(0f, bloon.getSlowTimer(), 0.001f);
		assertEquals(8, bloon.getSpeed()); // Speed restored
	}

	@Test
	public void testFreezeStatusEffect() {
		Bloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false); // Base speed 7
		assertEquals(7, bloon.getSpeed());

		bloon.applyStatusEffect(StatusType.FREEZE, 2.0f);
		assertTrue(bloon.isFrozen());
		assertEquals(0, bloon.getSpeed()); // Frozen bloons have 0 speed

		bloon.updateStatusTimers(1.0f);
		assertTrue(bloon.isFrozen());
		assertEquals(0, bloon.getSpeed());

		bloon.updateStatusTimers(1.0f);
		assertFalse(bloon.isFrozen());
		assertEquals(0f, bloon.getFreezeTimer(), 0.001f);
		assertEquals(7, bloon.getSpeed());
	}

	@Test
	public void testFreezeAndSlowPrecedence() {
		Bloon bloon = new Bloon(Bloon.Color.PINK, 5, false, false); // Base speed 9
		bloon.applyStatusEffect(StatusType.SLOW, 3.0f);
		bloon.applyStatusEffect(StatusType.FREEZE, 1.0f);

		// Freeze precedence over slow
		assertEquals(0, bloon.getSpeed());

		// Update 1 second: freeze expires, slow remains for 2s
		bloon.updateStatusTimers(1.0f);
		assertFalse(bloon.isFrozen());
		assertTrue(bloon.isSlowed());
		assertEquals(4, bloon.getSpeed()); // 9 / 2 = 4
	}

	@Test
	public void testDotStatusEffect() {
		Bloon bloon = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
		bloon.applyStatusEffect(StatusType.DOT, 3.0f, 2, 1.0f);

		assertTrue(bloon.hasDot());
		assertEquals(3.0f, bloon.getDotTimer(), 0.001f);

		// Delta 0.5s - no tick
		int dmg = bloon.updateStatusTimers(0.5f);
		assertEquals(0, dmg);

		// Delta 0.5s - 1st tick at 1.0s
		dmg = bloon.updateStatusTimers(0.5f);
		assertEquals(2, dmg);

		// Delta 1.0s - 2nd tick at 2.0s
		dmg = bloon.updateStatusTimers(1.0f);
		assertEquals(2, dmg);

		// Delta 1.0s - 3rd tick at 3.0s, timer expires
		dmg = bloon.updateStatusTimers(1.0f);
		assertEquals(2, dmg);
		assertFalse(bloon.hasDot());
		assertEquals(0f, bloon.getDotTimer(), 0.001f);
		assertEquals(0f, bloon.getDotInterval(), 0.001f);
		assertEquals(0, bloon.getDotDamage());
	}

	@Test
	public void testBulletStatusApplication() {
		Bullet bullet = new Bullet(20f, 1, 2, 500f, false, "red_spell_card.png", StatusType.BURN, 4.0f, 1, 0.5f);
		assertEquals(StatusType.BURN, bullet.getStatusType());
		assertEquals(4.0f, bullet.getStatusDuration(), 0.001f);
		assertEquals(1, bullet.getStatusDotDamage());
		assertEquals(0.5f, bullet.getStatusDotInterval(), 0.001f);

		Bloon bloon = new Bloon(Bloon.Color.MOAB, 200, false, false);
		bloon.applyStatusEffect(bullet.getStatusType(), bullet.getStatusDuration(), bullet.getStatusDotDamage(), bullet.getStatusDotInterval());

		assertTrue(bloon.hasDot());
		assertEquals(4.0f, bloon.getDotTimer(), 0.001f);
		assertEquals(0.5f, bloon.getDotInterval(), 0.001f);
		assertEquals(1, bloon.getDotDamage());
	}

	@Test
	public void testResetStatusCleanly() {
		Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
		bloon.applyStatusEffect(StatusType.SLOW, 5.0f);
		bloon.applyStatusEffect(StatusType.FREEZE, 5.0f);
		bloon.applyStatusEffect(StatusType.DOT, 5.0f, 2, 1.0f);

		bloon.resetStatus();

		assertFalse(bloon.isSlowed());
		assertFalse(bloon.isFrozen());
		assertFalse(bloon.hasDot());
		assertEquals(0f, bloon.getSlowTimer(), 0.001f);
		assertEquals(0f, bloon.getFreezeTimer(), 0.001f);
		assertEquals(0f, bloon.getDotTimer(), 0.001f);
		assertEquals(6, bloon.getSpeed()); // Blue bloon base speed 6
	}
}
