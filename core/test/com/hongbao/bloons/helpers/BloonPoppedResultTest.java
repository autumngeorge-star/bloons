package com.hongbao.bloons.helpers;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BloonPoppedResultTest {

	@Test
	public void testSingleDamagePopping() {
		Bloon rainbow = BloonFactory.createRainbowBloon();
		BloonPoppedResult result = new BloonPoppedResult(rainbow, 1);
		
		assertEquals(1, result.getCashGenerated());
		Set<Bloon> generated = result.getBloonsGenerated();
		assertEquals(2, generated.size());
		for (Bloon child : generated) {
			assertEquals(Bloon.Color.ZEBRA, child.getColor());
			assertEquals(7, child.getHealth());
		}
	}

	@Test
	public void testMultiDamageLayerPropagationRainbow() {
		Bloon rainbow = BloonFactory.createRainbowBloon();
		// 1 damage pops Rainbow -> 2 Zebras (health 7)
		// 1 excess damage pops each Zebra -> 2 Blacks (health 6) each = 4 Blacks
		BloonPoppedResult result = new BloonPoppedResult(rainbow, 2);

		assertEquals(3, result.getCashGenerated());
		Set<Bloon> generated = result.getBloonsGenerated();
		assertEquals(4, generated.size());
		for (Bloon child : generated) {
			assertEquals(Bloon.Color.BLACK, child.getColor());
			assertEquals(6, child.getHealth());
		}
	}

	@Test
	public void testMultiDamageLayerPropagationCeramic() {
		Bloon ceramic = BloonFactory.createCeramicBloon(); // Health 18
		// Ceramic shell HP = 10 (18 down to 8).
		// 12 damage: 10 shell HP -> 2 Rainbows (health 8), excess damage = 2.
		// 2 excess damage on each Rainbow -> 2 Zebras -> 4 Blacks (health 6) per Rainbow = 8 Blacks total.
		BloonPoppedResult result = new BloonPoppedResult(ceramic, 12);

		assertEquals(16, result.getCashGenerated()); // Total health 104 - (8 * 11) = 16
		Set<Bloon> generated = result.getBloonsGenerated();
		assertEquals(8, generated.size());
		for (Bloon child : generated) {
			assertEquals(Bloon.Color.BLACK, child.getColor());
			assertEquals(6, child.getHealth());
		}
	}

	@Test
	public void testOverkillDamageDepletesAllChildren() {
		Bloon red = BloonFactory.createRedBloon();
		BloonPoppedResult resultRed = new BloonPoppedResult(red, 50);

		assertEquals(1, resultRed.getCashGenerated());
		assertTrue(resultRed.getBloonsGenerated().isEmpty());

		Bloon black = BloonFactory.createBlackBloon();
		BloonPoppedResult resultBlack = new BloonPoppedResult(black, 100);

		assertEquals(11, resultBlack.getCashGenerated());
		assertTrue(resultBlack.getBloonsGenerated().isEmpty());
	}

	@Test
	public void testAttributePreservation() {
		Bloon camoRegenRainbow = BloonFactory.createRainbowCamoRegenBloon();
		BloonPoppedResult result = new BloonPoppedResult(camoRegenRainbow, 1);

		Set<Bloon> generated = result.getBloonsGenerated();
		assertEquals(2, generated.size());
		for (Bloon child : generated) {
			assertTrue(child.isCamo());
			assertTrue(child.isRegen());
		}
	}
}
