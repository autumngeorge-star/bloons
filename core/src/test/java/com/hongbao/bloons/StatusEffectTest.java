package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.StatusEffect;
import com.hongbao.bloons.helpers.Pair;

public class StatusEffectTest {

	public static void main(String[] args) {
		System.out.println("Running StatusEffect tests...");
		testStatusEffectCreationAndTimers();
		testBloonStatusEffectManagement();
		testBloonManagerUpdateStatusEffectsAndDOT();
		testStatusEffectInheritanceOnPop();
		testMovementDeltaScaling();
		System.out.println("All StatusEffect tests passed successfully!");
	}

	public static void testStatusEffectCreationAndTimers() {
		StatusEffect burn = new StatusEffect("BURN", 2.0f, 0.5f, 1, 1.0f, true);
		assertEquals("BURN", burn.getName());
		assertEquals(2.0f, burn.getDuration(), 0.001f);
		assertEquals(0.5f, burn.getTickInterval(), 0.001f);
		assertEquals(1, burn.getDamageAmount());
		assertEquals(1.0f, burn.getSpeedModification(), 0.001f);
		assertTrue(burn.isInheritable());

		// Advance 0.3s (less than tickInterval 0.5s)
		boolean tick = burn.update(0.3f);
		assertFalse(tick);
		assertEquals(1.7f, burn.getDuration(), 0.001f);
		assertFalse(burn.isExpired());

		// Advance 0.3s (total 0.6s >= tickInterval 0.5s)
		tick = burn.update(0.3f);
		assertTrue(tick);
		assertEquals(1.4f, burn.getDuration(), 0.001f);

		// Advance remaining duration to expire
		burn.update(1.4f);
		assertTrue(burn.isExpired());

		// Test copy
		StatusEffect slow = new StatusEffect("SLOW", 3.0f, 0f, 0, 0.5f, true);
		StatusEffect slowCopy = slow.copy();
		assertEquals(slow.getName(), slowCopy.getName());
		assertEquals(slow.getSpeedModification(), slowCopy.getSpeedModification());
		assertNotSame(slow, slowCopy);
	}

	public static void testBloonStatusEffectManagement() {
		Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
		assertEquals(6, bloon.getSpeed());
		assertEquals(1.0f, bloon.getSpeedMultiplier());
		assertEquals(6.0f, bloon.getEffectiveSpeed());

		// Add slow effect
		StatusEffect slow = new StatusEffect("SLOW", 3.0f, 0f, 0, 0.5f);
		bloon.addStatusEffect(slow);
		assertTrue(bloon.hasStatusEffect("SLOW"));
		assertEquals(0.5f, bloon.getSpeedMultiplier());
		assertEquals(3.0f, bloon.getEffectiveSpeed());

		// Add freeze effect
		StatusEffect freeze = new StatusEffect("FREEZE", 1.0f, 0f, 0, 0.0f);
		bloon.addStatusEffect(freeze);
		assertTrue(bloon.hasStatusEffect("FREEZE"));
		assertEquals(0.0f, bloon.getSpeedMultiplier());
		assertEquals(0.0f, bloon.getEffectiveSpeed());

		// Remove freeze
		bloon.removeStatusEffect("FREEZE");
		assertFalse(bloon.hasStatusEffect("FREEZE"));
		assertEquals(0.5f, bloon.getSpeedMultiplier());

		// Remove slow
		bloon.removeStatusEffect("SLOW");
		assertEquals(1.0f, bloon.getSpeedMultiplier());
	}

	public static void testBloonManagerUpdateStatusEffectsAndDOT() {
		BloonManager bloonManager = new BloonManager(null, null);
		Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false); // Blue Bloon pops into 1 Red Bloon
		BloonActor actor = new BloonActor(bloon, 100, 100, null);

		StatusEffect dot = new StatusEffect("BURN", 2.0f, 0.5f, 1, 1.0f);
		bloon.addStatusEffect(dot);

		// Manually add actor to onstage bloons for testing
		try {
			java.lang.reflect.Field field = BloonManager.class.getDeclaredField("onstageBloons");
			field.setAccessible(true);
			@SuppressWarnings("unchecked")
			java.util.Set<BloonActor> onstage = (java.util.Set<BloonActor>) field.get(bloonManager);
			onstage.add(actor);

			assertEquals(1, onstage.size());

			// Advance 0.5s - triggers 1 damage tick, popping Blue (health 2) -> Red (health 1)
			bloonManager.updateStatusEffects(0.5f);

			// Original actor popped; child Red bloon created and placed onstage
			assertEquals(1, onstage.size());
			BloonActor childActor = onstage.iterator().next();
			assertNotSame(actor, childActor);
			assertEquals(Bloon.Color.RED, childActor.getBloon().getColor());

			// Child bloon inherited "BURN" effect
			assertTrue(childActor.getBloon().hasStatusEffect("BURN"));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void testStatusEffectInheritanceOnPop() {
		Bloon parentBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
		StatusEffect inheritableEffect = new StatusEffect("POISON", 4.0f, 1.0f, 1, 0.8f, true);
		StatusEffect nonInheritableEffect = new StatusEffect("TEMP", 4.0f, 0f, 0, 1.0f, false);
		parentBloon.addStatusEffect(inheritableEffect);
		parentBloon.addStatusEffect(nonInheritableEffect);

		BloonActor parentActor = new BloonActor(parentBloon, 200, 200, null);
		BloonManager bloonManager = new BloonManager(null, null);

		bloonManager.popBloon(parentActor, 1); // 1 damage pops Blue to Red

		try {
			java.lang.reflect.Field field = BloonManager.class.getDeclaredField("onstageBloons");
			field.setAccessible(true);
			@SuppressWarnings("unchecked")
			java.util.Set<BloonActor> onstage = (java.util.Set<BloonActor>) field.get(bloonManager);

			assertEquals(1, onstage.size());
			BloonActor childActor = onstage.iterator().next();
			Bloon childBloon = childActor.getBloon();

			assertTrue(childBloon.hasStatusEffect("POISON"));
			assertFalse(childBloon.hasStatusEffect("TEMP"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void testMovementDeltaScaling() {
		Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false); // speed = 5
		BloonActor actor = new BloonActor(bloon, 100, 100, null);

		Pair<Float, Float> direction = new Pair<>(1.0f, 0.0f);

		// Baseline move with 1/60s delta: stepFactor = 1.0
		float initialX = actor.getX();
		actor.move(direction, 1f / 60f);
		float deltaX1 = actor.getX() - initialX;
		assertEquals(1.0f, deltaX1, 0.01f); // 5 / 5 * 1.0 = 1.0

		// Slow bloon to 50%
		StatusEffect slow = new StatusEffect("SLOW", 2.0f, 0f, 0, 0.5f);
		bloon.addStatusEffect(slow);

		float beforeSlowX = actor.getX();
		actor.move(direction, 1f / 60f);
		float deltaX2 = actor.getX() - beforeSlowX;
		assertEquals(0.5f, deltaX2, 0.01f); // 2.5 / 5 * 1.0 = 0.5
	}

	private static void assertEquals(Object expected, Object actual) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new AssertionError("Expected " + expected + " but got " + actual);
	}

	private static void assertEquals(float expected, float actual, float delta) {
		if (Math.abs(expected - actual) <= delta) return;
		throw new AssertionError("Expected " + expected + " (+/- " + delta + ") but got " + actual);
	}

	private static void assertEquals(int expected, int actual) {
		if (expected == actual) return;
		throw new AssertionError("Expected " + expected + " but got " + actual);
	}

	private static void assertTrue(boolean condition) {
		if (!condition) throw new AssertionError("Expected true but got false");
	}

	private static void assertFalse(boolean condition) {
		if (condition) throw new AssertionError("Expected false but got true");
	}

	private static void assertNotSame(Object o1, Object o2) {
		if (o1 == o2) throw new AssertionError("Expected different instances but got same instance");
	}
}
