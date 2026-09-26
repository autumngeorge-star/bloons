package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpellCardCooldownTest {

	@Test
	public void testGirlWithSpellCardInitialState() {
		Girl reimu = GirlFactory.createReimu();
		assertTrue("Reimu should have spell card", reimu.hasSpellCard());
		assertTrue("Reimu spell card should be ready initially", reimu.isSpellCardReady());
		assertEquals(0f, reimu.getSpellCardCooldownTimer(), 0.001f);
		assertEquals(15f, reimu.getSpellCardMaxCooldown(), 0.001f);
		assertEquals("Spell Card: READY [X]", reimu.getSpellCardStatusString());

		Girl yuyuko = GirlFactory.createYuyuko();
		assertTrue("Yuyuko should have spell card", yuyuko.hasSpellCard());
		assertTrue("Yuyuko spell card should be ready initially", yuyuko.isSpellCardReady());
		assertEquals(0f, yuyuko.getSpellCardCooldownTimer(), 0.001f);
		assertEquals(15f, yuyuko.getSpellCardMaxCooldown(), 0.001f);
		assertEquals("Spell Card: READY [X]", yuyuko.getSpellCardStatusString());
	}

	@Test
	public void testGirlWithoutSpellCardInitialState() {
		Girl yukari = GirlFactory.createYukari();
		assertFalse("Yukari should not have spell card", yukari.hasSpellCard());
		assertFalse("Yukari spell card should not be ready", yukari.isSpellCardReady());
		assertEquals("Spell Card: N/A", yukari.getSpellCardStatusString());

		Girl marisa = GirlFactory.createMarisa();
		assertFalse("Marisa should not have spell card", marisa.hasSpellCard());
		assertFalse("Marisa spell card should not be ready", marisa.isSpellCardReady());
		assertEquals("Spell Card: N/A", marisa.getSpellCardStatusString());
	}

	@Test
	public void testCooldownActivationAndDecrement() {
		Girl reimu = GirlFactory.createReimu();
		reimu.resetSpellCardCooldown();

		assertEquals(15f, reimu.getSpellCardCooldownTimer(), 0.001f);
		assertFalse("Reimu should not be ready on cooldown", reimu.isSpellCardReady());
		assertEquals("Spell Card: 15.0s", reimu.getSpellCardStatusString());

		reimu.decrementSpellCardCooldownTimer(2.6f);
		assertEquals(12.4f, reimu.getSpellCardCooldownTimer(), 0.001f);
		assertEquals("Spell Card: 12.4s", reimu.getSpellCardStatusString());

		reimu.decrementSpellCardCooldownTimer(13.0f);
		assertEquals(0f, reimu.getSpellCardCooldownTimer(), 0.001f);
		assertTrue("Reimu should be ready again when cooldown hits 0", reimu.isSpellCardReady());
		assertEquals("Spell Card: READY [X]", reimu.getSpellCardStatusString());
	}

	@Test
	public void testUpgradedStatsPreservesCooldownState() {
		Girl reimu = GirlFactory.createReimu();
		reimu.resetSpellCardCooldown();
		reimu.decrementSpellCardCooldownTimer(5.0f);

		Girl upgraded = reimu.getUpgradedStats();
		assertNotNull(upgraded);
		assertEquals(10.0f, upgraded.getSpellCardCooldownTimer(), 0.001f);
		assertEquals(15.0f, upgraded.getSpellCardMaxCooldown(), 0.001f);
		assertEquals("Spell Card: 10.0s", upgraded.getSpellCardStatusString());
	}
}
