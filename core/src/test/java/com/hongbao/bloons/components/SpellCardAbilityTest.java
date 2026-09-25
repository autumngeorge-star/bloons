package com.hongbao.bloons.components;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SpellCardAbilityTest {

	@Test
	public void testInitialStateIsReady() {
		SpellCardAbility ability = new SpellCardAbility(10.0f, 2.0f);
		Assert.assertTrue(ability.isReady());
		Assert.assertFalse(ability.isActive());
		Assert.assertFalse(ability.isRecharging());
		Assert.assertEquals(SpellCardAbility.State.READY, ability.getState());
		Assert.assertEquals(1.0f, ability.getCooldownProgressRatio(), 0.001f);
		Assert.assertEquals(0.0f, ability.getRemainingCooldownRatio(), 0.001f);
	}

	@Test
	public void testTriggerTransitionsToActiveThenRechargingThenReady() {
		SpellCardAbility ability = new SpellCardAbility(10.0f, 2.0f);
		List<String> transitions = new ArrayList<>();
		ability.addListener((oldState, newState) -> transitions.add(oldState + "->" + newState));

		boolean triggered = ability.trigger();
		Assert.assertTrue(triggered);
		Assert.assertTrue(ability.isActive());
		Assert.assertFalse(ability.isReady());
		Assert.assertEquals(1, transitions.size());
		Assert.assertEquals("READY->ACTIVE", transitions.get(0));

		// Second trigger must fail while active
		boolean secondTrigger = ability.trigger();
		Assert.assertFalse(secondTrigger);

		// Advance time by 1 second - still active
		ability.update(1.0f);
		Assert.assertTrue(ability.isActive());

		// Advance time by another 1 second - active ends, recharging starts
		ability.update(1.0f);
		Assert.assertTrue(ability.isRecharging());
		Assert.assertEquals(2, transitions.size());
		Assert.assertEquals("ACTIVE->RECHARGING", transitions.get(1));
		Assert.assertEquals(10.0f, ability.getCooldownRemaining(), 0.001f);

		// Trigger while recharging must fail
		Assert.assertFalse(ability.trigger());

		// Advance time by 5 seconds
		ability.update(5.0f);
		Assert.assertTrue(ability.isRecharging());
		Assert.assertEquals(5.0f, ability.getCooldownRemaining(), 0.001f);
		Assert.assertEquals(0.5f, ability.getRemainingCooldownRatio(), 0.001f);
		Assert.assertEquals(0.5f, ability.getCooldownProgressRatio(), 0.001f);

		// Advance time by 5 more seconds -> returns to READY
		ability.update(5.0f);
		Assert.assertTrue(ability.isReady());
		Assert.assertEquals(3, transitions.size());
		Assert.assertEquals("RECHARGING->READY", transitions.get(2));
	}

	@Test
	public void testZeroMaxDurationDirectlyRecharges() {
		SpellCardAbility ability = new SpellCardAbility(5.0f, 0.0f);
		boolean triggered = ability.trigger();
		Assert.assertTrue(triggered);
		Assert.assertTrue(ability.isRecharging());
		Assert.assertEquals(5.0f, ability.getCooldownRemaining(), 0.001f);

		ability.update(5.0f);
		Assert.assertTrue(ability.isReady());
	}
}
