package com.hongbao.bloons.factories;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.GirlConfig;
import com.hongbao.bloons.entities.SpellCard;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GirlFactoryTest {

	@Before
	public void setUp() {
		GirlFactory.reloadTowers();
	}

	@Test
	public void testLoadTowersFromJSON() {
		List<GirlConfig> configs = GirlFactory.getLoadedConfigs();
		assertNotNull("Loaded configs list should not be null", configs);
		assertEquals("Should load all 8 towers from towers.json", 8, configs.size());

		GirlConfig reimuConfig = configs.get(0);
		assertEquals("Reimu", reimuConfig.getName());
		assertEquals(325, reimuConfig.getCost());
		assertEquals("reimu.png", reimuConfig.getImageFileName());
		assertEquals("red_spell_card.png", reimuConfig.getBulletFileName());
		assertEquals("reimu_box.png", reimuConfig.getShopIcon());
		assertEquals(3, reimuConfig.getAttackDelay().size());
		assertEquals(86, (int) reimuConfig.getAttackDelay().get(0));

		GirlConfig yuyukoConfig = configs.get(7);
		assertEquals("Yuyuko", yuyukoConfig.getName());
		assertEquals(2000, yuyukoConfig.getCost());
		assertEquals("yuyuko.png", yuyukoConfig.getImageFileName());
	}

	@Test
	public void testGirlCreationFromConfig() {
		Girl reimu = GirlFactory.createReimu();
		assertNotNull(reimu);
		assertEquals("Reimu", reimu.getName());
		assertEquals(325, reimu.getCost());
		assertEquals(86, reimu.getAttackDelay());
		assertEquals(1, reimu.getDamage());
		assertEquals(4, reimu.getPierce());
		assertTrue(reimu.isHoming());
		assertEquals(200, reimu.getUpgradeCost());

		Bullet bullet = reimu.createBullet();
		assertNotNull(bullet);
		assertEquals(1, bullet.getDamage());
		assertEquals(4, bullet.getPierce());
		assertTrue(bullet.getImageFileName().contains("red_spell_card.png"));
	}

	@Test
	public void testCreateGirlByNameAndIndex() {
		Girl marisa = GirlFactory.createGirlByName("Marisa");
		assertNotNull(marisa);
		assertEquals("Marisa", marisa.getName());
		assertEquals(200, marisa.getCost());

		Girl alice = GirlFactory.createGirlByIndex(3);
		assertNotNull(alice);
		assertEquals("Alice", alice.getName());
		assertEquals(450, alice.getCost());
	}

	@Test
	public void testMalformedJSONHandling() {
		String malformedJsonStr = "{\"name\": \"CustomGirl\", \"cost\": 150}";
		JsonReader reader = new JsonReader();
		JsonValue jsonValue = reader.parse(malformedJsonStr);

		GirlConfig config = GirlConfig.fromJsonValue(jsonValue);
		assertNotNull("Config should be parsed even with missing fields", config);
		assertEquals("CustomGirl", config.getName());
		assertEquals(150, config.getCost());
		assertNotNull("Missing array fields should fallback", config.getAttackDelay());
		assertFalse(config.getAttackDelay().isEmpty());

		Girl girl = GirlFactory.createGirl(config);
		assertNotNull(girl);
		assertEquals("CustomGirl", girl.getName());
		assertEquals(150, girl.getCost());
	}

	@Test
	public void testSpellCardCreationWithJSONFactory() {
		SpellCard reimuSpell = SpellCard.createReimuSpellCard();
		assertNotNull("Reimu spellcard should build seamlessly", reimuSpell);
		assertEquals("Reimu", reimuSpell.getOverrideName());

		SpellCard yuyukoSpell = SpellCard.createYuyukoSpellCard();
		assertNotNull("Yuyuko spellcard should build seamlessly", yuyukoSpell);
		assertEquals("Yuyuko", yuyukoSpell.getOverrideName());
	}

	@Test
	public void testUpgradedGirlStats() {
		Girl reimu = GirlFactory.createReimu();
		assertEquals(0, reimu.getLevel());
		int initialCost = reimu.getCost();

		boolean canUpgrade = reimu.canUpgrade(1000);
		assertTrue(canUpgrade);

		Girl upgraded = reimu.getUpgradedStats();
		assertNotNull(upgraded);
		assertEquals(1, upgraded.getLevel());

		int upgradeCost = reimu.upgrade();
		assertEquals(200, upgradeCost);
		assertEquals(1, reimu.getLevel());
	}

	@Test
	public void testNonExistentTowerFallback() {
		Girl unknown = GirlFactory.createGirlByName("NonExistentTower");
		assertNotNull("Factory should fallback to default character when name not found", unknown);
		assertEquals("Reimu", unknown.getName());
	}

}
