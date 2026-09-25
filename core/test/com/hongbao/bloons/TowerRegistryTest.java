package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.factories.TowerDefinition;
import com.hongbao.bloons.factories.TowerRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TowerRegistryTest {

	@Before
	public void setUp() {
		TowerRegistry.clear();
		GirlFactory.registerDefaultTowers();
	}

	@Test
	public void testDefaultTowerRegistration() {
		List<TowerDefinition> towers = GirlFactory.getRegisteredTowers();
		assertEquals(8, towers.size());

		assertEquals("Reimu", towers.get(0).getId());
		assertEquals("Yukari", towers.get(1).getId());
		assertEquals("Marisa", towers.get(2).getId());
		assertEquals("Alice", towers.get(3).getId());
		assertEquals("Sakuya", towers.get(4).getId());
		assertEquals("Remilia", towers.get(5).getId());
		assertEquals("Youmu", towers.get(6).getId());
		assertEquals("Yuyuko", towers.get(7).getId());
	}

	@Test
	public void testCaseInsensitiveLookup() {
		TowerDefinition reimuUpper = TowerRegistry.getTower("Reimu");
		TowerDefinition reimuLower = TowerRegistry.getTower("reimu");
		assertNotNull(reimuUpper);
		assertNotNull(reimuLower);
		assertEquals(reimuUpper, reimuLower);
	}

	@Test
	public void testLookupByIndex() {
		TowerDefinition tower0 = TowerRegistry.getTower(0);
		assertNotNull(tower0);
		assertEquals("Reimu", tower0.getId());

		TowerDefinition tower7 = TowerRegistry.getTower(7);
		assertNotNull(tower7);
		assertEquals("Yuyuko", tower7.getId());

		assertNull(TowerRegistry.getTower(99));
	}

	@Test
	public void testDynamicTowerRegistration() {
		assertEquals(8, GirlFactory.getRegisteredTowers().size());

		GirlFactory.registerTower("Cirno", () -> new Girl(
				"Cirno",
				Arrays.asList(10, 10, 10),
				Arrays.asList(10f, 10f, 10f),
				Arrays.asList(1, 1, 1),
				Arrays.asList(1, 1, 1),
				Arrays.asList(100f, 100f, 100f),
				Arrays.asList(100f, 100f, 100f),
				Arrays.asList(false, false, false),
				"cirno.png",
				"icicle.png",
				9,
				Arrays.asList(99, 999, Girl.NO_UPGRADES_AVAILABLE)
		), "img/ui/cirno_box.png");

		List<TowerDefinition> towers = GirlFactory.getRegisteredTowers();
		assertEquals(9, towers.size());

		TowerDefinition cirnoDef = GirlFactory.getTower("Cirno");
		assertNotNull(cirnoDef);
		assertEquals("img/ui/cirno_box.png", cirnoDef.getUiIconPath());

		Girl cirnoInstance = cirnoDef.createGirl();
		assertEquals("Cirno", cirnoInstance.getName());
		assertEquals(9, cirnoInstance.getCost());
	}

	@Test
	public void testBackwardCompatibilityStaticMethods() {
		Girl reimu = GirlFactory.createReimu();
		assertNotNull(reimu);
		assertEquals("Reimu", reimu.getName());
		assertEquals(325, reimu.getCost());

		Girl yuyuko = GirlFactory.createYuyuko();
		assertNotNull(yuyuko);
		assertEquals("Yuyuko", yuyuko.getName());
		assertEquals(2000, yuyuko.getCost());
	}

}
