package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.*;


public class GirlCooldownTest {

	@Test
	public void testTowerWithSpellCardInitialState() {
		Girl reimu = GirlFactory.createReimu();
		assertTrue("Reimu should have a spell card", reimu.hasSpellCard());
		assertEquals("Max cooldown should be 600", 600, reimu.getSpellCardMaxCooldown());
		assertEquals("Timer should initialize to 0", 0, reimu.getSpellCardCooldownTimer());
		assertTrue("Spell card should be ready when timer is 0", reimu.isSpellCardReady());
		assertEquals("Cooldown percentage should be 1.0 when ready", 1.0f, reimu.getSpellCardCooldownPercent(), 0.001f);
	}

	@Test
	public void testTowerWithoutSpellCardState() {
		Girl alice = GirlFactory.createAlice();
		assertFalse("Alice should not have a spell card", alice.hasSpellCard());
		assertEquals("Max cooldown should be 0", 0, alice.getSpellCardMaxCooldown());
		assertFalse("Spell card should not be ready for tower without spell card", alice.isSpellCardReady());
		assertEquals("Cooldown percentage should be 0.0", 0.0f, alice.getSpellCardCooldownPercent(), 0.001f);
	}

	@Test
	public void testCooldownDecrementAndReset() {
		Girl reimu = GirlFactory.createReimu();
		reimu.resetSpellCardCooldownTimer();

		assertEquals("Timer should reset to max cooldown 600", 600, reimu.getSpellCardCooldownTimer());
		assertFalse("Spell card should not be ready immediately after reset", reimu.isSpellCardReady());
		assertEquals("Cooldown percentage should be 0.0 after reset", 0.0f, reimu.getSpellCardCooldownPercent(), 0.001f);

		// Decrement 300 frames (50% progress)
		for (int i = 0; i < 300; i++) {
			reimu.decrementSpellCardCooldownTimer();
		}
		assertEquals("Timer should be 300 after 300 decrements", 300, reimu.getSpellCardCooldownTimer());
		assertFalse("Spell card should not be ready at 50%", reimu.isSpellCardReady());
		assertEquals("Cooldown percentage should be 0.5 at midpoint", 0.5f, reimu.getSpellCardCooldownPercent(), 0.001f);

		// Decrement remaining 300 frames
		for (int i = 0; i < 300; i++) {
			reimu.decrementSpellCardCooldownTimer();
		}
		assertEquals("Timer should be 0 when fully charged", 0, reimu.getSpellCardCooldownTimer());
		assertTrue("Spell card should be ready when timer reaches 0", reimu.isSpellCardReady());
		assertEquals("Cooldown percentage should be 1.0 when fully charged", 1.0f, reimu.getSpellCardCooldownPercent(), 0.001f);
	}

	@Test
	public void testUpgradedStatsPreservesCooldownState() {
		Girl reimu = GirlFactory.createReimu();
		reimu.resetSpellCardCooldownTimer();
		for (int i = 0; i < 150; i++) {
			reimu.decrementSpellCardCooldownTimer();
		}

		Girl upgraded = reimu.getUpgradedStats();
		assertNotNull("Upgraded stats should not be null", upgraded);
		assertEquals("Upgraded tower should retain max cooldown", reimu.getSpellCardMaxCooldown(), upgraded.getSpellCardMaxCooldown());
		assertEquals("Upgraded tower should retain current timer", reimu.getSpellCardCooldownTimer(), upgraded.getSpellCardCooldownTimer());
		assertEquals("Upgraded tower should retain cooldown percent", reimu.getSpellCardCooldownPercent(), upgraded.getSpellCardCooldownPercent(), 0.001f);
	}

}
