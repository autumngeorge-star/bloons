package com.hongbao.bloons.actors;

import com.hongbao.bloons.components.SpellCardAbility;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class GirlActorAbilityTest {

	@Test
	public void testSpellCardAbilityStateTransitionsAndListeners() {
		SpellCardAbility ability = new SpellCardAbility(10.0f, 2.0f);
		AtomicInteger stateChangeCount = new AtomicInteger(0);

		ability.addListener((oldState, newState) -> stateChangeCount.incrementAndGet());

		Assert.assertTrue(ability.isReady());
		Assert.assertEquals(0, stateChangeCount.get());

		// Trigger ability
		Assert.assertTrue(ability.trigger());
		Assert.assertTrue(ability.isActive());
		Assert.assertEquals(1, stateChangeCount.get());

		// Rapid re-trigger attempt rejected
		Assert.assertFalse(ability.trigger());
		Assert.assertEquals(1, stateChangeCount.get());

		// Active duration countdown
		ability.update(2.0f);
		Assert.assertTrue(ability.isRecharging());
		Assert.assertEquals(2, stateChangeCount.get());
		Assert.assertEquals(10.0f, ability.getCooldownRemaining(), 0.001f);

		// Cooldown countdown
		ability.update(5.0f);
		Assert.assertTrue(ability.isRecharging());
		Assert.assertEquals(5.0f, ability.getCooldownRemaining(), 0.001f);
		Assert.assertEquals(0.5f, ability.getRemainingCooldownRatio(), 0.001f);

		ability.update(5.0f);
		Assert.assertTrue(ability.isReady());
		Assert.assertEquals(3, stateChangeCount.get());
	}

	@Test
	public void testGirlEntitySpellCardCreation() {
		Girl reimu = GirlFactory.createReimu();
		Assert.assertNotNull(reimu.createSpellCard());
		Assert.assertEquals("reimu_spell.png", reimu.createSpellCard().getImageFileName().substring(reimu.createSpellCard().getImageFileName().lastIndexOf('/') + 1));

		Girl yuyuko = GirlFactory.createYuyuko();
		Assert.assertNotNull(yuyuko.createSpellCard());
	}
}
