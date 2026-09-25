package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpellCardCooldownTest {

	@Test
	public void testGirlSpellCardFieldsAndDefaults() {
		Girl reimu = GirlFactory.createReimu();
		assertTrue("Reimu should have spell card", reimu.hasSpellCard());
		assertEquals("Initial cooldown should be 0", 0, reimu.getSpellCardCooldown());
		assertEquals("Max cooldown should be 1800", 1800, reimu.getSpellCardMaxCooldown());
		assertTrue("Spell card should be ready initially", reimu.isSpellCardReady());
		assertEquals("SPELL CARD: READY", reimu.getSpellCardStatus());

		Girl yukari = GirlFactory.createYukari();
		assertFalse("Yukari should not have spell card", yukari.hasSpellCard());
		assertFalse("Yukari spell card should not be ready", yukari.isSpellCardReady());
		assertEquals("SPELL CARD: N/A", yukari.getSpellCardStatus());
	}

	@Test
	public void testSpellCardCooldownCountdownAndSeconds() {
		Girl reimu = GirlFactory.createReimu();
		reimu.resetSpellCardCooldown(); // sets cooldown to 1800

		assertEquals(1800, reimu.getSpellCardCooldown());
		assertFalse(reimu.isSpellCardReady());
		assertEquals(10, reimu.getSpellCardCooldownSeconds());
		assertEquals("SPELL CARD: 10s", reimu.getSpellCardStatus());

		// Decrement 1 tick
		reimu.decrementSpellCardCooldown();
		assertEquals(1799, reimu.getSpellCardCooldown());
		assertEquals(10, reimu.getSpellCardCooldownSeconds()); // ceil(1799 / 180) = 10

		// Decrement 180 ticks (1 second)
		for (int i = 0; i < 179; i++) {
			reimu.decrementSpellCardCooldown();
		}
		assertEquals(1620, reimu.getSpellCardCooldown());
		assertEquals(9, reimu.getSpellCardCooldownSeconds());
		assertEquals("SPELL CARD: 9s", reimu.getSpellCardStatus());

		// Fast-forward down to 1 tick
		reimu.setSpellCardCooldown(1);
		assertEquals(1, reimu.getSpellCardCooldownSeconds());
		assertEquals("SPELL CARD: 1s", reimu.getSpellCardStatus());

		// Decrement to 0
		reimu.decrementSpellCardCooldown();
		assertEquals(0, reimu.getSpellCardCooldown());
		assertTrue(reimu.isSpellCardReady());
		assertEquals("SPELL CARD: READY", reimu.getSpellCardStatus());

		// Extra decrements should stay at 0
		reimu.decrementSpellCardCooldown();
		assertEquals(0, reimu.getSpellCardCooldown());
	}

	@Test
	public void testPreserveCooldownOnUpgrade() {
		Girl reimu = GirlFactory.createReimu();
		reimu.setSpellCardCooldown(900); // 5 seconds remaining

		Girl upgraded = reimu.getUpgradedStats();
		assertNotNull(upgraded);
		assertEquals(900, upgraded.getSpellCardCooldown());
		assertEquals(1800, upgraded.getSpellCardMaxCooldown());
		assertEquals("SPELL CARD: 5s", upgraded.getSpellCardStatus());

		reimu.upgrade(); // in-place upgrade
		assertEquals(900, reimu.getSpellCardCooldown());
		assertEquals("SPELL CARD: 5s", reimu.getSpellCardStatus());
	}

	@Test
	public void testYuyukoSpellCardStatus() {
		Girl yuyuko = GirlFactory.createYuyuko();
		assertTrue(yuyuko.hasSpellCard());
		assertTrue(yuyuko.isSpellCardReady());
		assertEquals("SPELL CARD: READY", yuyuko.getSpellCardStatus());

		yuyuko.resetSpellCardCooldown();
		assertEquals("SPELL CARD: 10s", yuyuko.getSpellCardStatus());
	}

	@Test
	public void testDisabledTowersReturnDisabledStatus() {
		Girl marisa = GirlFactory.createMarisa();
		assertFalse(marisa.hasSpellCard());
		assertFalse(marisa.isSpellCardReady());
		assertEquals("SPELL CARD: N/A", marisa.getSpellCardStatus());

		Girl alice = GirlFactory.createAlice();
		assertFalse(alice.hasSpellCard());
		assertEquals("SPELL CARD: N/A", alice.getSpellCardStatus());

		Girl sakuya = GirlFactory.createSakuya();
		assertFalse(sakuya.hasSpellCard());
		assertEquals("SPELL CARD: N/A", sakuya.getSpellCardStatus());

		Girl remilia = GirlFactory.createRemilia();
		assertFalse(remilia.hasSpellCard());
		assertEquals("SPELL CARD: N/A", remilia.getSpellCardStatus());

		Girl youmu = GirlFactory.createYoumu();
		assertFalse(youmu.hasSpellCard());
		assertEquals("SPELL CARD: N/A", youmu.getSpellCardStatus());
	}
}
