package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class GirlFactoryTest {

	@Test
	public void testAllFactoryMethodsSucceed() {
		Girl reimu = GirlFactory.createReimu();
		assertNotNull(reimu);
		assertEquals("Reimu", reimu.getName());

		Girl yukari = GirlFactory.createYukari();
		assertNotNull(yukari);
		assertEquals("Yukari", yukari.getName());

		Girl marisa = GirlFactory.createMarisa();
		assertNotNull(marisa);
		assertEquals("Marisa", marisa.getName());

		Girl alice = GirlFactory.createAlice();
		assertNotNull(alice);
		assertEquals("Alice", alice.getName());

		Girl sakuya = GirlFactory.createSakuya();
		assertNotNull(sakuya);
		assertEquals("Sakuya", sakuya.getName());

		Girl remilia = GirlFactory.createRemilia();
		assertNotNull(remilia);
		assertEquals("Remilia", remilia.getName());

		Girl youmu = GirlFactory.createYoumu();
		assertNotNull(youmu);
		assertEquals("Youmu", youmu.getName());

		Girl yuyuko = GirlFactory.createYuyuko();
		assertNotNull(yuyuko);
		assertEquals("Yuyuko", yuyuko.getName());
	}

	@Test
	public void testStagnantDamageThrowsException() {
		try {
			new GirlFactory.GirlBuilder()
					.name("TestTower")
					.attackDelay(Arrays.asList(10, 10))
					.bulletSpeed(Arrays.asList(10f, 10f))
					.damage(Arrays.asList(1, 1)) // Stagnant damage!
					.pierce(Arrays.asList(1, 2))
					.range(Arrays.asList(100f, 200f))
					.visualRange(Arrays.asList(100f, 200f))
					.homing(Arrays.asList(false, false))
					.image("reimu.png")
					.bulletImage("red_spell_card.png")
					.cost(100)
					.upgradeCosts(Arrays.asList(100, Girl.NO_UPGRADES_AVAILABLE))
					.build();
			fail("Expected IllegalStateException for stagnant damage");
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().contains("TestTower"));
			assertTrue(e.getMessage().toLowerCase().contains("damage"));
		}
	}

	@Test
	public void testStagnantPierceThrowsException() {
		try {
			new GirlFactory.GirlBuilder()
					.name("TestTower")
					.attackDelay(Arrays.asList(10, 10))
					.bulletSpeed(Arrays.asList(10f, 10f))
					.damage(Arrays.asList(1, 2))
					.pierce(Arrays.asList(1, 1)) // Stagnant pierce!
					.range(Arrays.asList(100f, 200f))
					.visualRange(Arrays.asList(100f, 200f))
					.homing(Arrays.asList(false, false))
					.image("reimu.png")
					.bulletImage("red_spell_card.png")
					.cost(100)
					.upgradeCosts(Arrays.asList(100, Girl.NO_UPGRADES_AVAILABLE))
					.build();
			fail("Expected IllegalStateException for stagnant pierce");
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().contains("TestTower"));
			assertTrue(e.getMessage().toLowerCase().contains("pierce"));
		}
	}

	@Test
	public void testStagnantRangeThrowsException() {
		try {
			new GirlFactory.GirlBuilder()
					.name("TestTower")
					.attackDelay(Arrays.asList(10, 10))
					.bulletSpeed(Arrays.asList(10f, 10f))
					.damage(Arrays.asList(1, 2))
					.pierce(Arrays.asList(1, 2))
					.range(Arrays.asList(100f, 100f)) // Stagnant range!
					.visualRange(Arrays.asList(100f, 200f))
					.homing(Arrays.asList(false, false))
					.image("reimu.png")
					.bulletImage("red_spell_card.png")
					.cost(100)
					.upgradeCosts(Arrays.asList(100, Girl.NO_UPGRADES_AVAILABLE))
					.build();
			fail("Expected IllegalStateException for stagnant range");
		} catch (IllegalStateException e) {
			assertTrue(e.getMessage().contains("TestTower"));
			assertTrue(e.getMessage().toLowerCase().contains("range"));
		}
	}

	@Test
	public void testTowerStatProgressionOnUpgrade() {
		Girl reimu = GirlFactory.createReimu();
		int baseDmg = reimu.getDamage();
		int basePierce = reimu.getPierce();
		float baseRange = reimu.getRange();

		reimu.upgrade();
		assertTrue(reimu.getDamage() > baseDmg);
		assertTrue(reimu.getPierce() > basePierce);
		assertTrue(reimu.getRange() > baseRange);

		int tier1Dmg = reimu.getDamage();
		int tier1Pierce = reimu.getPierce();
		float tier1Range = reimu.getRange();

		reimu.upgrade();
		assertTrue(reimu.getDamage() > tier1Dmg);
		assertTrue(reimu.getPierce() > tier1Pierce);
		assertTrue(reimu.getRange() > tier1Range);
	}
}
