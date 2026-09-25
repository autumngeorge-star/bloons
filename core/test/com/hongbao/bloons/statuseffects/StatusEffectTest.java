package com.hongbao.bloons.statuseffects;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatusEffectTest {

	@Test
	public void testStatusEffectDurationAndExpiration() {
		StatusEffect effect = new StatusEffect("test_slow", 2.0f, 0f, 0, 0.5f);
		assertFalse(effect.isExpired());
		assertEquals(2.0f, effect.getDuration(), 0.0001f);

		int ticks = effect.update(0.8f);
		assertEquals(0, ticks);
		assertEquals(1.2f, effect.getDuration(), 0.0001f);
		assertFalse(effect.isExpired());

		effect.update(1.2f);
		assertTrue(effect.isExpired());
		assertEquals(0f, effect.getDuration(), 0.0001f);
	}

	@Test
	public void testPeriodicDamageOverTimeTicks() {
		StatusEffect burn = new StatusEffect("burn", 3.0f, 1.0f, 2, 1.0f);
		assertEquals(2, burn.getDamage());
		assertEquals(1.0f, burn.getTickInterval(), 0.0001f);

		// First 0.5s: no tick yet
		int ticks1 = burn.update(0.5f);
		assertEquals(0, ticks1);

		// Another 0.6s (total 1.1s): 1 tick triggered
		int ticks2 = burn.update(0.6f);
		assertEquals(1, ticks2);

		// Another 2.0s: 2 ticks triggered
		int ticks3 = burn.update(2.0f);
		assertEquals(2, ticks3);

		// Effect is now expired
		assertTrue(burn.isExpired());
	}

	@Test
	public void testBloonSpeedMultiplierAndRestorationOnExpiration() {
		Bloon redBloon = new Bloon(Bloon.Color.RED, 1, false, false);
		assertEquals(5, redBloon.getSpeed());
		assertEquals(5.0f, redBloon.getEffectiveSpeed(), 0.0001f);

		StatusEffect slow50 = new StatusEffect("slow50", 2.0f, 0f, 0, 0.5f);
		redBloon.addStatusEffect(slow50);

		// Speed reduced to 50%
		assertEquals(2.5f, redBloon.getEffectiveSpeed(), 0.0001f);

		StatusEffect slow80 = new StatusEffect("slow80", 1.0f, 0f, 0, 0.8f);
		redBloon.addStatusEffect(slow80);

		// Stacking multipliers: 5 * 0.5 * 0.8 = 2.0
		assertEquals(2.0f, redBloon.getEffectiveSpeed(), 0.0001f);

		// Update effects by 1.1s -> slow80 expires
		for (StatusEffect effect : redBloon.getStatusEffects()) {
			effect.update(1.1f);
		}
		redBloon.getStatusEffects().removeIf(StatusEffect::isExpired);

		// Only slow50 remains -> speed is 2.5f
		assertEquals(1, redBloon.getStatusEffects().size());
		assertEquals(2.5f, redBloon.getEffectiveSpeed(), 0.0001f);

		// Update effects by 1.0s -> slow50 expires
		for (StatusEffect effect : redBloon.getStatusEffects()) {
			effect.update(1.0f);
		}
		redBloon.getStatusEffects().removeIf(StatusEffect::isExpired);

		// No status effects remain -> restored to original speed 5.0f
		assertTrue(redBloon.getStatusEffects().isEmpty());
		assertEquals(5.0f, redBloon.getEffectiveSpeed(), 0.0001f);
	}

	@Test
	public void testBloonDistanceTravelledDeltaIncrement() {
		Bloon greenBloon = new Bloon(Bloon.Color.GREEN, 3, false, false);
		assertEquals(0, greenBloon.getDistanceTravelled());

		greenBloon.incrementDistanceTravelled(3.5f);
		assertEquals(3, greenBloon.getDistanceTravelled());

		greenBloon.incrementDistanceTravelled(0.7f);
		assertEquals(4, greenBloon.getDistanceTravelled());
	}
}
