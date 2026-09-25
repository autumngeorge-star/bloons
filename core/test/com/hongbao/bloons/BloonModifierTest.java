package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonModifierTest {

	@Test
	public void testCreateBloonOfTypeWithExplicitModifiers() {
		Bloon bloon = BloonFactory.createBloonOfType("blue", 2, true, true);
		assertEquals(Bloon.Color.BLUE, bloon.getColor());
		assertEquals(2, bloon.getHealth());
		assertTrue(bloon.isCamo());
		assertTrue(bloon.isRegen());
		assertEquals("img/bloons/blue_camo_regrowth_bloon.png", bloon.getImageFileName());
	}

	@Test
	public void testCreateBloonOfTypeWithExplicitModifiersTwoParam() {
		Bloon bloon = BloonFactory.createBloonOfType("green", true, false);
		assertEquals(Bloon.Color.GREEN, bloon.getColor());
		assertEquals(3, bloon.getHealth());
		assertTrue(bloon.isCamo());
		assertFalse(bloon.isRegen());
		assertEquals("img/bloons/green_camo_bloon.png", bloon.getImageFileName());
	}

	@Test
	public void testCreateBloonOfTypeSuffixParsingAllColors() {
		String[] colors = {"red", "blue", "green", "yellow", "pink", "black", "lead", "zebra", "rainbow", "ceramic", "moab", "bfb", "zomg"};
		for (String color : colors) {
			// Test standard
			Bloon standard = BloonFactory.createBloonOfType(color);
			assertFalse(color + " standard camo", standard.isCamo());
			assertFalse(color + " standard regen", standard.isRegen());
			assertEquals("img/bloons/" + color + "_bloon.png", standard.getImageFileName());

			// Test _camo
			Bloon camo = BloonFactory.createBloonOfType(color + "_camo");
			assertTrue(color + " _camo flag", camo.isCamo());
			assertFalse(color + " _camo regen flag", camo.isRegen());
			assertEquals("img/bloons/" + color + "_camo_bloon.png", camo.getImageFileName());

			// Test _regen
			Bloon regen = BloonFactory.createBloonOfType(color + "_regen");
			assertFalse(color + " _regen camo flag", regen.isCamo());
			assertTrue(color + " _regen flag", regen.isRegen());
			assertEquals("img/bloons/" + color + "_regrowth_bloon.png", regen.getImageFileName());

			// Test _regrowth
			Bloon regrowth = BloonFactory.createBloonOfType(color + "_regrowth");
			assertFalse(color + " _regrowth camo flag", regrowth.isCamo());
			assertTrue(color + " _regrowth flag", regrowth.isRegen());
			assertEquals("img/bloons/" + color + "_regrowth_bloon.png", regrowth.getImageFileName());

			// Test _camo_regen
			Bloon camoRegen = BloonFactory.createBloonOfType(color + "_camo_regen");
			assertTrue(color + " _camo_regen camo flag", camoRegen.isCamo());
			assertTrue(color + " _camo_regen regen flag", camoRegen.isRegen());
			assertEquals("img/bloons/" + color + "_camo_regrowth_bloon.png", camoRegen.getImageFileName());

			// Test _camo_regrowth
			Bloon camoRegrowth = BloonFactory.createBloonOfType(color + "_camo_regrowth");
			assertTrue(color + " _camo_regrowth camo flag", camoRegrowth.isCamo());
			assertTrue(color + " _camo_regrowth regen flag", camoRegrowth.isRegen());
			assertEquals("img/bloons/" + color + "_camo_regrowth_bloon.png", camoRegrowth.getImageFileName());
		}
	}

	@Test
	public void testSetCamoAndSetRegenSyncImageFileName() {
		Bloon bloon = BloonFactory.createRedBloon();
		assertEquals("img/bloons/red_bloon.png", bloon.getImageFileName());
		assertFalse(bloon.isCamo());
		assertFalse(bloon.isRegen());

		bloon.setCamo(true);
		assertTrue(bloon.isCamo());
		assertEquals("img/bloons/red_camo_bloon.png", bloon.getImageFileName());

		bloon.setRegen(true);
		assertTrue(bloon.isRegen());
		assertEquals("img/bloons/red_camo_regrowth_bloon.png", bloon.getImageFileName());

		bloon.setCamo(false);
		assertFalse(bloon.isCamo());
		assertEquals("img/bloons/red_regrowth_bloon.png", bloon.getImageFileName());

		bloon.setRegen(false);
		assertFalse(bloon.isRegen());
		assertEquals("img/bloons/red_bloon.png", bloon.getImageFileName());
	}

	@Test
	public void testBloonPoppedResultInheritsParentModifiers() {
		// Create a Blue Camo Regrowth Bloon (health 2)
		Bloon parent = BloonFactory.createBlueCamoRegenBloon();
		assertTrue(parent.isCamo());
		assertTrue(parent.isRegen());

		// Pop by 1 damage -> should result in 1 Red bloon with camo and regen
		BloonPoppedResult result = parent.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertEquals(1, children.size());

		Bloon child = children.iterator().next();
		assertEquals(Bloon.Color.RED, child.getColor());
		assertEquals(1, child.getHealth());
		assertTrue("Child should inherit camo", child.isCamo());
		assertTrue("Child should inherit regen", child.isRegen());
		assertEquals("img/bloons/red_camo_regrowth_bloon.png", child.getImageFileName());
	}

	@Test
	public void testPoppingYellowCamoBloon() {
		// Yellow Bloon (health 4) popped by 1 damage -> Green Bloon (health 3)
		Bloon yellowCamo = BloonFactory.createYellowCamoBloon();
		assertTrue(yellowCamo.isCamo());
		assertFalse(yellowCamo.isRegen());

		BloonPoppedResult result = yellowCamo.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertEquals(1, children.size());

		Bloon child = children.iterator().next();
		assertEquals(Bloon.Color.GREEN, child.getColor());
		assertEquals(3, child.getHealth());
		assertTrue("Child should inherit camo", child.isCamo());
		assertFalse("Child should not have regen", child.isRegen());
		assertEquals("img/bloons/green_camo_bloon.png", child.getImageFileName());
	}

	@Test
	public void testPoppingBlackRegenBloon() {
		// Black Bloon (health 6) popped by 1 damage -> 2 Pink Bloons (health 5)
		Bloon blackRegen = BloonFactory.createBlackRegenBloon();
		assertFalse(blackRegen.isCamo());
		assertTrue(blackRegen.isRegen());

		BloonPoppedResult result = blackRegen.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertEquals(2, children.size());

		for (Bloon child : children) {
			assertEquals(Bloon.Color.PINK, child.getColor());
			assertEquals(5, child.getHealth());
			assertFalse("Child should not have camo", child.isCamo());
			assertTrue("Child should inherit regen", child.isRegen());
			assertEquals("img/bloons/pink_regrowth_bloon.png", child.getImageFileName());
		}
	}
}
