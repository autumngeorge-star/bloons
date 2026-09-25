package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonFactoryTest {

	@Test
	public void testCamoBloonSpawnsCamoChildren() {
		Bloon parent = BloonFactory.createBlueCamoBloon();
		assertTrue(parent.isCamo());
		assertFalse(parent.isRegen());

		BloonPoppedResult result = parent.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertFalse(children.isEmpty());

		for (Bloon child : children) {
			assertTrue("Child bloon should inherit camo attribute", child.isCamo());
			assertFalse("Child bloon should not have regen attribute", child.isRegen());
			assertTrue("Child image name should contain camo denotation", child.getImageFileName().contains(Bloon.CAMO_BLOON_DENOTATION));
		}
	}

	@Test
	public void testRegenBloonSpawnsRegenChildren() {
		Bloon parent = BloonFactory.createBlueRegenBloon();
		assertFalse(parent.isCamo());
		assertTrue(parent.isRegen());

		BloonPoppedResult result = parent.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertFalse(children.isEmpty());

		for (Bloon child : children) {
			assertFalse("Child bloon should not have camo attribute", child.isCamo());
			assertTrue("Child bloon should inherit regen attribute", child.isRegen());
			assertTrue("Child image name should contain regrowth denotation", child.getImageFileName().contains(Bloon.REGROWTH_BLOON_DENOTATION));
		}
	}

	@Test
	public void testCamoRegenBloonSpawnsCamoRegenChildren() {
		Bloon parent = BloonFactory.createBlueCamoRegenBloon();
		assertTrue(parent.isCamo());
		assertTrue(parent.isRegen());

		BloonPoppedResult result = parent.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertFalse(children.isEmpty());

		for (Bloon child : children) {
			assertTrue("Child bloon should inherit camo attribute", child.isCamo());
			assertTrue("Child bloon should inherit regen attribute", child.isRegen());
			assertTrue("Child image name should contain camo denotation", child.getImageFileName().contains(Bloon.CAMO_BLOON_DENOTATION));
			assertTrue("Child image name should contain regrowth denotation", child.getImageFileName().contains(Bloon.REGROWTH_BLOON_DENOTATION));
		}
	}

	@Test
	public void testStandardBloonSpawnsStandardChildren() {
		Bloon parent = BloonFactory.createBlueBloon();
		assertFalse(parent.isCamo());
		assertFalse(parent.isRegen());

		BloonPoppedResult result = parent.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertFalse(children.isEmpty());

		for (Bloon child : children) {
			assertFalse("Standard bloon child should not have camo attribute", child.isCamo());
			assertFalse("Standard bloon child should not have regen attribute", child.isRegen());
		}
	}

	@Test
	public void testBloonFactoryParsesModifierSuffixesForAllColors() {
		String[] colors = {"red", "blue", "green", "yellow", "pink", "black", "lead", "zebra", "rainbow", "ceramic", "moab", "bfb", "zomg"};

		for (String color : colors) {
			// Base
			Bloon base = BloonFactory.createBloonOfType(color);
			assertNotNull("Base bloon should be created", base);
			assertFalse(base.isCamo());
			assertFalse(base.isRegen());

			// _camo
			Bloon camo = BloonFactory.createBloonOfType(color + "_camo");
			assertNotNull("Camo bloon should be created for " + color, camo);
			assertTrue(camo.isCamo());
			assertFalse(camo.isRegen());

			// _regen
			Bloon regen = BloonFactory.createBloonOfType(color + "_regen");
			assertNotNull("Regen bloon should be created for " + color, regen);
			assertFalse(regen.isCamo());
			assertTrue(regen.isRegen());

			// _regrowth
			Bloon regrowth = BloonFactory.createBloonOfType(color + "_regrowth");
			assertNotNull("Regrowth bloon should be created for " + color, regrowth);
			assertFalse(regrowth.isCamo());
			assertTrue(regrowth.isRegen());

			// _camo_regen
			Bloon camoRegen = BloonFactory.createBloonOfType(color + "_camo_regen");
			assertNotNull("Camo regen bloon should be created for " + color, camoRegen);
			assertTrue(camoRegen.isCamo());
			assertTrue(camoRegen.isRegen());

			// _camo_regrowth
			Bloon camoRegrowth = BloonFactory.createBloonOfType(color + "_camo_regrowth");
			assertNotNull("Camo regrowth bloon should be created for " + color, camoRegrowth);
			assertTrue(camoRegrowth.isCamo());
			assertTrue(camoRegrowth.isRegen());
		}
	}

	@Test
	public void testCreateChildBloonDirectly() {
		Bloon parent = new Bloon(Bloon.Color.BLACK, 6, true, true);

		Bloon childColor = BloonFactory.createChildBloon(parent, Bloon.Color.PINK, 5);
		assertEquals(Bloon.Color.PINK, childColor.getColor());
		assertEquals(5, childColor.getHealth());
		assertTrue(childColor.isCamo());
		assertTrue(childColor.isRegen());

		Bloon childType = BloonFactory.createChildBloon(parent, "pink", 5);
		assertEquals(Bloon.Color.PINK, childType.getColor());
		assertEquals(5, childType.getHealth());
		assertTrue(childType.isCamo());
		assertTrue(childType.isRegen());
	}

	@Test
	public void testBloonPoppedResultCashAndHealthCalculationsUnchanged() {
		Bloon standard = BloonFactory.createGreenBloon();
		Bloon camoRegen = BloonFactory.createBloonOfType("green_camo_regrowth");

		BloonPoppedResult standardResult = standard.pop(1);
		BloonPoppedResult camoRegenResult = camoRegen.pop(1);

		assertEquals(standardResult.getCashGenerated(), camoRegenResult.getCashGenerated());
		assertEquals(standardResult.getBloonsGenerated().size(), camoRegenResult.getBloonsGenerated().size());
	}
}
