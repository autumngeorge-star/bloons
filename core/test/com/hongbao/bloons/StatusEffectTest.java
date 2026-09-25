package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.StatusEffect;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class StatusEffectTest {

	@Test
	public void testStatusEffectCreationAndCopy() {
		StatusEffect effect = new StatusEffect(StatusEffect.Type.SLOW, 5.0f, 0.5f, 1, 1.0f);
		assertEquals(StatusEffect.Type.SLOW, effect.getType());
		assertEquals(5.0f, effect.getDuration(), 0.001f);
		assertEquals(0.5f, effect.getSpeedModifier(), 0.001f);
		assertEquals(1, effect.getDamagePerTick());
		assertEquals(1.0f, effect.getTickInterval(), 0.001f);
		assertFalse(effect.isExpired());
		assertFalse(effect.isFreeze());

		StatusEffect copy = effect.copy();
		assertNotSame(effect, copy);
		assertEquals(effect.getType(), copy.getType());
		assertEquals(effect.getDuration(), copy.getDuration(), 0.001f);
		assertEquals(effect.getSpeedModifier(), copy.getSpeedModifier(), 0.001f);

		// Modify original and verify copy is unaffected
		effect.setDuration(2.0f);
		assertEquals(5.0f, copy.getDuration(), 0.001f);
	}

	@Test
	public void testBloonAddAndMaintainStatusEffects() {
		Bloon bloon = BloonFactory.createRedBloon();
		assertTrue(bloon.getStatusEffects().isEmpty());

		StatusEffect slow = new StatusEffect(StatusEffect.Type.SLOW, 3.0f, 0.5f);
		StatusEffect dot = new StatusEffect(StatusEffect.Type.DAMAGE_OVER_TIME, 4.0f, 2, 1.0f);

		bloon.addStatusEffect(slow);
		bloon.addStatusEffect(dot);

		assertEquals(2, bloon.getStatusEffects().size());
		assertTrue(bloon.getStatusEffects().contains(slow));
		assertTrue(bloon.getStatusEffects().contains(dot));

		bloon.removeStatusEffect(slow);
		assertEquals(1, bloon.getStatusEffects().size());
		assertFalse(bloon.getStatusEffects().contains(slow));

		bloon.clearStatusEffects();
		assertTrue(bloon.getStatusEffects().isEmpty());
	}

	@Test
	public void testSpeedCalculationAndFreeze() {
		Bloon bloon = BloonFactory.createRedBloon(); // Base speed is 5
		assertEquals(5, bloon.getBaseSpeed());
		assertEquals(5, bloon.getSpeed());

		// Apply 50% slow
		StatusEffect slow50 = new StatusEffect(StatusEffect.Type.SLOW, 5.0f, 0.5f);
		bloon.addStatusEffect(slow50);
		assertEquals(2.5f, bloon.getEffectiveSpeed(), 0.001f);
		assertEquals(3, bloon.getSpeed()); // Math.round(2.5) -> 3

		// Apply another 80% slow (combined multiplier = 0.5 * 0.8 = 0.4)
		StatusEffect slow80 = new StatusEffect(StatusEffect.Type.SLOW, 5.0f, 0.8f);
		bloon.addStatusEffect(slow80);
		assertEquals(2.0f, bloon.getEffectiveSpeed(), 0.001f);
		assertEquals(2, bloon.getSpeed());

		// Apply FREEZE effect
		StatusEffect freeze = new StatusEffect(StatusEffect.Type.FREEZE, 2.0f, 0.0f);
		bloon.addStatusEffect(freeze);
		assertTrue(bloon.isFrozen());
		assertEquals(0.0f, bloon.getEffectiveSpeed(), 0.001f);
		assertEquals(0, bloon.getSpeed());
	}

	@Test
	public void testDeltaTimeUpdatesAndPeriodicDamage() {
		Bloon bloon = BloonFactory.createBlueBloon(); // Health = 2
		StatusEffect dot = new StatusEffect(StatusEffect.Type.DAMAGE_OVER_TIME, 3.0f, 1, 1.0f);
		bloon.addStatusEffect(dot);

		// Advance 0.5 seconds -> no damage tick yet
		int damage1 = bloon.updateStatusEffects(0.5f);
		assertEquals(0, damage1);
		assertEquals(1, bloon.getStatusEffects().size());

		// Advance another 0.6 seconds (total 1.1s) -> 1 tick triggered
		int damage2 = bloon.updateStatusEffects(0.6f);
		assertEquals(1, damage2);
		assertEquals(1, bloon.getStatusEffects().size());

		// Advance 2.5 seconds -> effect duration expires and effect is automatically cleaned up
		int damage3 = bloon.updateStatusEffects(2.5f);
		assertEquals(2, damage3); // Ticks at 2.0s and 3.0s
		assertTrue(bloon.getStatusEffects().isEmpty()); // Automatically removed
	}

	@Test
	public void testBulletStatusEffectPayload() {
		Bullet bullet = new Bullet();
		assertFalse(bullet.hasStatusEffects());

		StatusEffect freeze = new StatusEffect(StatusEffect.Type.FREEZE, 2.0f, 0.0f);
		bullet.addStatusEffect(freeze);

		assertTrue(bullet.hasStatusEffects());
		assertEquals(1, bullet.getStatusEffects().size());

		Bloon bloon = BloonFactory.createGreenBloon();
		// Transfer payload
		for (StatusEffect payload : bullet.getStatusEffects()) {
			bloon.addStatusEffect(payload.copy());
		}

		assertEquals(1, bloon.getStatusEffects().size());
		assertTrue(bloon.isFrozen());
	}

	@Test
	public void testStatusEffectInheritanceOnPop() {
		Bloon parentBloon = BloonFactory.createBlueBloon(); // Health 2
		StatusEffect slow = new StatusEffect(StatusEffect.Type.SLOW, 4.0f, 0.5f);
		StatusEffect dot = new StatusEffect(StatusEffect.Type.DAMAGE_OVER_TIME, 4.0f, 1, 1.0f);
		parentBloon.addStatusEffect(slow);
		parentBloon.addStatusEffect(dot);

		// Pop parent bloon with 1 damage -> produces Red bloon (health 1)
		BloonPoppedResult result = parentBloon.pop(1);
		Set<Bloon> childBloons = result.getBloonsGenerated();
		assertFalse(childBloons.isEmpty());

		for (Bloon child : childBloons) {
			assertEquals(2, child.getStatusEffects().size());
			assertEquals(0.5f, child.getEffectiveSpeed() / child.getBaseSpeed(), 0.01f);

			// Verify deep copy: modifying child does not modify parent or other children
			child.getStatusEffects().get(0).setDuration(10.0f);
			assertNotEquals(10.0f, slow.getDuration(), 0.001f);
		}
	}

	@Test
	public void testSafeExpirationHandlingMultipleEffects() {
		Bloon bloon = BloonFactory.createYellowBloon();
		StatusEffect e1 = new StatusEffect(StatusEffect.Type.SLOW, 0.2f, 0.5f);
		StatusEffect e2 = new StatusEffect(StatusEffect.Type.DAMAGE_OVER_TIME, 0.5f, 1, 0.2f);
		StatusEffect e3 = new StatusEffect(StatusEffect.Type.SLOW, 1.0f, 0.8f);

		bloon.addStatusEffect(e1);
		bloon.addStatusEffect(e2);
		bloon.addStatusEffect(e3);

		assertEquals(3, bloon.getStatusEffects().size());

		// Update by 0.3s -> e1 should expire, e2 ticks once
		int damage = bloon.updateStatusEffects(0.3f);
		assertEquals(1, damage);
		assertEquals(2, bloon.getStatusEffects().size());
		assertFalse(bloon.getStatusEffects().contains(e1));

		// Update by 0.8s -> e2 and e3 expire
		bloon.updateStatusEffects(0.8f);
		assertTrue(bloon.getStatusEffects().isEmpty());
	}
}
