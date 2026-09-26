package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.StatusEffectPayload;
import com.hongbao.bloons.factories.GirlFactory;

import java.util.Collections;
import java.util.List;

public class StatusEffectTest {

	public static void main(String[] args) {
		System.out.println("Running StatusEffectTest...");
		testStatusEffectPayloadImmutabilityAndAttributes();
		testGirlAndBulletStatusPayloadInheritanceAndUpgrades();
		testBloonStatusEffectApplicationAndSpeedCalculation();
		testBloonStackLimitAndDurationCap();
		testBloonManagerCheckCollisionAndNoRegressionOnEmptyPayloads();
		testTickIntervalDamage();
		System.out.println("All StatusEffectTest cases passed successfully!");
	}

	private static void assertEquals(Object expected, Object actual, String message) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
	}

	private static void assertEquals(float expected, float actual, float delta, String message) {
		if (Math.abs(expected - actual) <= delta) return;
		throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
	}

	private static void assertTrue(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError(message);
		}
	}

	private static void assertFalse(boolean condition, String message) {
		if (condition) {
			throw new AssertionError(message);
		}
	}

	public static void testStatusEffectPayloadImmutabilityAndAttributes() {
		StatusEffectPayload payload = new StatusEffectPayload(StatusEffectPayload.Type.SLOW, 3.5f, 0.5f, 1.0f);
		assertEquals(StatusEffectPayload.Type.SLOW, payload.getType(), "Payload type should match");
		assertEquals(StatusEffectPayload.Type.SLOW, payload.getEffectType(), "EffectType should match");
		assertEquals(3.5f, payload.getDuration(), 0.001f, "Duration should match");
		assertEquals(0.5f, payload.getPotency(), 0.001f, "Potency should match");
		assertEquals(1.0f, payload.getTickInterval(), 0.001f, "TickInterval should match");

		StatusEffectPayload stringPayload = new StatusEffectPayload("freeze", 2.0f, 1.0f);
		assertEquals(StatusEffectPayload.Type.FREEZE, stringPayload.getType(), "Parsed string type should match");
		assertEquals(2.0f, stringPayload.getDuration(), 0.001f, "Duration should match");
		assertEquals(1.0f, stringPayload.getPotency(), 0.001f, "Potency should match");
		assertEquals(0f, stringPayload.getTickInterval(), 0.001f, "Default tick interval should be 0");
	}

	public static void testGirlAndBulletStatusPayloadInheritanceAndUpgrades() {
		Girl sakuya = GirlFactory.createSakuya();
		assertEquals(0, sakuya.getLevel(), "Level 0 initial girl level");
		List<StatusEffectPayload> lvl0Payloads = sakuya.getStatusEffectPayloads();
		assertEquals(1, lvl0Payloads.size(), "Level 0 payload list size");
		assertEquals(StatusEffectPayload.Type.SLOW, lvl0Payloads.get(0).getType(), "Sakuya lvl 0 type is SLOW");
		assertEquals(0.5f, lvl0Payloads.get(0).getPotency(), 0.001f, "Sakuya lvl 0 potency");

		Bullet bulletLvl0 = sakuya.createBullet();
		assertEquals(1, bulletLvl0.getStatusEffectPayloads().size(), "Bullet inherits status payloads");
		assertEquals(StatusEffectPayload.Type.SLOW, bulletLvl0.getStatusEffectPayloads().get(0).getType(), "Bullet payload type");

		// Upgrade Sakuya to level 1
		sakuya.upgrade();
		assertEquals(1, sakuya.getLevel(), "Level 1 upgraded girl level");
		List<StatusEffectPayload> lvl1Payloads = sakuya.getStatusEffectPayloads();
		assertEquals(1, lvl1Payloads.size(), "Level 1 payload list size");
		assertEquals(0.6f, lvl1Payloads.get(0).getPotency(), 0.001f, "Sakuya lvl 1 potency");

		Bullet bulletLvl1 = sakuya.createBullet();
		assertEquals(0.6f, bulletLvl1.getStatusEffectPayloads().get(0).getPotency(), 0.001f, "Level 1 bullet potency");

		// Upgrade Sakuya to level 2
		Girl upgradedStats = sakuya.getUpgradedStats();
		assertEquals(2, upgradedStats.getLevel(), "Level 2 upgraded stats girl level");
		List<StatusEffectPayload> lvl2Payloads = upgradedStats.getStatusEffectPayloads();
		assertEquals(StatusEffectPayload.Type.FREEZE, lvl2Payloads.get(0).getType(), "Sakuya lvl 2 status is FREEZE");

		Bullet bulletLvl2 = upgradedStats.createBullet();
		assertEquals(StatusEffectPayload.Type.FREEZE, bulletLvl2.getStatusEffectPayloads().get(0).getType(), "Level 2 bullet type");
	}

	public static void testBloonStatusEffectApplicationAndSpeedCalculation() {
		Bloon bloon = new Bloon(Bloon.Color.YELLOW, 4, false, false);
		int baseSpeed = bloon.getBaseSpeed(); // YELLOW is speed 8
		assertEquals(8, baseSpeed, "Yellow bloon base speed is 8");
		assertEquals(8, bloon.getSpeed(), "Initial speed is base speed");

		// Apply SLOW effect (50% slow)
		StatusEffectPayload slowPayload = new StatusEffectPayload(StatusEffectPayload.Type.SLOW, 3.0f, 0.5f);
		bloon.applyStatusEffect(slowPayload);
		assertEquals(4, bloon.getSpeed(), "Speed with 50% slow is 4");

		// Apply FREEZE effect (100% stop)
		StatusEffectPayload freezePayload = new StatusEffectPayload(StatusEffectPayload.Type.FREEZE, 1.0f, 1.0f);
		bloon.applyStatusEffect(freezePayload);
		assertEquals(0, bloon.getSpeed(), "Frozen bloon speed is 0");

		// Advance time by 1.1s so freeze expires
		bloon.updateStatusEffects(1.1f);
		assertEquals(4, bloon.getSpeed(), "Speed after freeze expires returns to 4 (slow active)");

		// Advance time by 2.0s so slow expires
		bloon.updateStatusEffects(2.0f);
		assertEquals(8, bloon.getSpeed(), "Speed returns to 8 after all effects expire");
	}

	public static void testBloonStackLimitAndDurationCap() {
		Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
		StatusEffectPayload slow = new StatusEffectPayload(StatusEffectPayload.Type.SLOW, 2.0f, 0.5f);

		for (int i = 0; i < 10; i++) {
			bloon.applyStatusEffect(slow);
		}

		long slowCount = bloon.getActiveStatusEffects().stream()
				.filter(e -> e.getPayload().getType() == StatusEffectPayload.Type.SLOW)
				.count();
		assertTrue(slowCount <= Bloon.MAX_STACKS_PER_TYPE, "Max stacks limit enforced");
	}

	public static void testBloonManagerCheckCollisionAndNoRegressionOnEmptyPayloads() {
		BloonManager bloonManager = new BloonManager(null, null);
		Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
		BloonActor bloonActor = new BloonActor(bloon, 100, 100, null);

		// Bullet with no status payloads
		Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
		BulletActor bulletActor = new BulletActor(bullet, 100, 100, 1, 0);

		assertEquals(2, bloonActor.getBloon().getHealth(), "Initial health is 2");
		assertTrue(bloonActor.getBloon().getActiveStatusEffects().isEmpty(), "No initial status effects");

		// Bullet with SLOW payload
		Bullet slowBullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png",
				Collections.singletonList(new StatusEffectPayload(StatusEffectPayload.Type.SLOW, 2.0f, 0.5f)));
		BulletActor slowBulletActor = new BulletActor(slowBullet, 100, 100, 1, 0);

		slowBulletActor.getBullet().getStatusEffectPayloads().forEach(bloonActor::applyStatusEffect);
		assertFalse(bloonActor.getBloon().getActiveStatusEffects().isEmpty(), "Active status effects after slow bullet");
		assertEquals(3, bloonActor.getBloon().getSpeed(), "BLUE base speed 6 * (1 - 0.5) = 3");
	}

	public static void testTickIntervalDamage() {
		Bloon bloon = new Bloon(Bloon.Color.YELLOW, 4, false, false);
		StatusEffectPayload poison = new StatusEffectPayload(StatusEffectPayload.Type.POISON, 3.0f, 1.0f, 1.0f);
		bloon.applyStatusEffect(poison);

		int tickDmg1 = bloon.updateStatusEffects(0.5f);
		assertEquals(0, tickDmg1, "No tick damage at 0.5s");

		int tickDmg2 = bloon.updateStatusEffects(0.6f); // total 1.1s > 1.0s interval
		assertEquals(1, tickDmg2, "Tick damage of 1 at 1.1s");
	}
}
