package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StatusPropagationTest {

	@Test
	public void testStatusTransferToChildBloons() {
		Bloon parent = BloonFactory.createCeramicBloon();
		StatusEffect slow = new StatusEffect("slow", 0.5f, 10.0f);
		parent.addStatusEffect(slow);

		assertTrue(parent.hasStatusEffect("slow"));

		BloonPoppedResult result = parent.pop(1);
		Set<Bloon> children = result.getBloonsGenerated();
		assertFalse(children.isEmpty());

		for (Bloon child : children) {
			assertTrue(child.hasStatusEffect("slow"));
			StatusEffect childSlow = child.getStatusEffect("slow");
			assertNotNull(childSlow);
			assertEquals(0.5f, childSlow.getSpeedMultiplier(), 0.001f);
			assertEquals(10.0f, childSlow.getDuration(), 0.001f);
		}
	}

	@Test
	public void testSpeedModificationProportionalScaling() {
		// Ceramic base speed = 7
		Bloon ceramic = BloonFactory.createCeramicBloon();
		ceramic.addStatusEffect(new StatusEffect("slow", 0.5f, 8.0f));

		assertEquals(7, ceramic.getBaseSpeed());
		assertEquals(0.5f, ceramic.getSpeedMultiplier(), 0.001f);
		assertEquals(4, ceramic.getSpeed()); // Math.round(7 * 0.5) = 4

		// Damage 10 pops Ceramic (health 18) down to health 8 (Rainbow, base speed 8)
		BloonPoppedResult result = ceramic.pop(10);
		Set<Bloon> children = result.getBloonsGenerated();

		for (Bloon child : children) {
			assertEquals(Bloon.Color.RAINBOW, child.getColor());
			assertEquals(8, child.getBaseSpeed());
			assertEquals(0.5f, child.getSpeedMultiplier(), 0.001f);
			// Child speed immediately adjusted upon creation
			assertEquals(4, child.getSpeed()); // Math.round(8 * 0.5) = 4
		}
	}

	@Test
	public void testDirectSpeedModificationProportionalScaling() {
		Bloon ceramic = BloonFactory.createCeramicBloon();
		ceramic.setSpeed(14); // Double speed (14 / 7 = 2.0f multiplier)

		assertEquals(2.0f, ceramic.getSpeedMultiplier(), 0.001f);
		assertEquals(14, ceramic.getSpeed());

		BloonPoppedResult result = ceramic.pop(10); // Pops into Rainbow (base speed 8)
		Set<Bloon> children = result.getBloonsGenerated();

		for (Bloon child : children) {
			assertEquals(8, child.getBaseSpeed());
			assertEquals(2.0f, child.getSpeedMultiplier(), 0.001f);
			assertEquals(16, child.getSpeed()); // Math.round(8 * 2.0) = 16
		}
	}

	@Test
	public void testChildWithoutStatusEffectsDefaultSpeed() {
		Bloon parent = BloonFactory.createCeramicBloon();
		BloonPoppedResult result = parent.pop(10); // Pops into Rainbow (base speed 8)

		Set<Bloon> children = result.getBloonsGenerated();
		assertFalse(children.isEmpty());

		for (Bloon child : children) {
			assertEquals(1.0f, child.getSpeedMultiplier(), 0.001f);
			assertEquals(child.getBaseSpeed(), child.getSpeed());
			assertTrue(child.getStatusEffects().isEmpty());
		}
	}

	@Test
	public void testMultiTierPopStatusPersistence() {
		Bloon parent = BloonFactory.createCeramicBloon();
		parent.addStatusEffect(new StatusEffect("slow", 0.5f, 15.0f));
		parent.addStatusEffect(new StatusEffect("glue", 0.8f, 12.0f));

		// First pop: Ceramic -> Rainbow
		BloonPoppedResult firstPop = parent.pop(10);
		Set<Bloon> firstChildren = firstPop.getBloonsGenerated();

		for (Bloon child : firstChildren) {
			assertTrue(child.hasStatusEffect("slow"));
			assertTrue(child.hasStatusEffect("glue"));
			assertEquals(0.4f, child.getSpeedMultiplier(), 0.001f); // 0.5 * 0.8 = 0.4

			// Second pop: Rainbow -> Zebra / Black / etc.
			BloonPoppedResult secondPop = child.pop(1);
			Set<Bloon> grandChildren = secondPop.getBloonsGenerated();
			for (Bloon grandChild : grandChildren) {
				assertTrue(grandChild.hasStatusEffect("slow"));
				assertTrue(grandChild.hasStatusEffect("glue"));
				assertEquals(0.4f, grandChild.getSpeedMultiplier(), 0.001f);
			}
		}
	}

	@Test
	public void testIndependentStatusEffectStatePerChild() {
		Bloon parent = BloonFactory.createCeramicBloon();
		parent.addStatusEffect(new StatusEffect("slow", 0.5f, 10.0f));

		BloonPoppedResult result = parent.pop(10); // Creates multiple children
		List<Bloon> childrenList = java.util.Arrays.asList(result.getBloonsGenerated().toArray(new Bloon[0]));
		assertTrue(childrenList.size() >= 2);

		Bloon child1 = childrenList.get(0);
		Bloon child2 = childrenList.get(1);

		child1.updateStatusEffects(4.0f); // child1 has 6.0s remaining

		assertEquals(6.0f, child1.getStatusEffect("slow").getDuration(), 0.001f);
		assertEquals(10.0f, child2.getStatusEffect("slow").getDuration(), 0.001f);
	}

	@Test
	public void testStatusEffectExpiry() {
		Bloon bloon = BloonFactory.createRedBloon(); // base speed 5
		bloon.addStatusEffect(new StatusEffect("slow", 0.5f, 2.0f));

		assertEquals(3, bloon.getSpeed()); // Math.round(5 * 0.5) = 3

		bloon.updateStatusEffects(1.0f);
		assertTrue(bloon.hasStatusEffect("slow"));
		assertEquals(3, bloon.getSpeed());

		bloon.updateStatusEffects(1.5f); // Total delta 2.5s > 2.0s duration
		assertFalse(bloon.hasStatusEffect("slow"));
		assertEquals(5, bloon.getSpeed()); // Restored to base speed
	}
}
