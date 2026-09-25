package com.hongbao.bloons.test;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;

import java.util.Set;

public class BloonPoppedResultTest {

	public static void main(String[] args) {
		System.out.println("Running BloonPoppedResult test suite...");

		testYuyukoAttackingCeramic();
		testMoabTaking205Damage();
		testCashMatchesRbeDifference();
		testPreserveModifiersAndDistance();
		testZomgOverkillNoExceptions();
		testExactShellBreakNoOverkill();
		testCeramicOverkillOneDamage();
		testRedBloonOverkill();
		testBfbOverkill();

		System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
	}

	private static void testYuyukoAttackingCeramic() {
		// Yuyuko (20 damage) attacking a Ceramic bloon (18 health)
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonPoppedResult result = new BloonPoppedResult(ceramic, 20);

		Set<Bloon> generated = result.getBloonsGenerated();
		int cash = result.getCashGenerated();

		assert generated.isEmpty() : "Expected 0 surviving bloons, got " + generated.size();
		assert cash == 104 : "Expected 104 cash generated, got " + cash;

		System.out.println("testYuyukoAttackingCeramic passed: 0 surviving bloons, " + cash + " cash earned.");
	}

	private static void testMoabTaking205Damage() {
		// A MOAB (218 health) taking 205 damage leaves 4 Ceramic bloons with 13 health each on stage
		Bloon moab = BloonFactory.createMOAB();
		BloonPoppedResult result = new BloonPoppedResult(moab, 205);

		Set<Bloon> generated = result.getBloonsGenerated();
		int cash = result.getCashGenerated();

		assert generated.size() == 4 : "Expected 4 surviving Ceramics, got " + generated.size();
		for (Bloon bloon : generated) {
			assert bloon.getColor() == Bloon.Color.CERAMIC : "Expected CERAMIC, got " + bloon.getColor();
			assert bloon.getHealth() == 13 : "Expected health 13, got " + bloon.getHealth();
		}

		assert cash == 220 : "Expected 220 cash generated, got " + cash;

		System.out.println("testMoabTaking205Damage passed: 4 Ceramics with 13 health each, " + cash + " cash earned.");
	}

	private static void testCashMatchesRbeDifference() {
		// Test multiple damage scenarios and verify cash equals total parent health - total surviving health
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonPoppedResult result12 = new BloonPoppedResult(ceramic, 12);
		int parentTotalHealth = BloonPoppedResult.getTotalHealthOfBloon(ceramic);
		int survivingTotalHealth = 0;
		for (Bloon b : result12.getBloonsGenerated()) {
			survivingTotalHealth += BloonPoppedResult.getTotalHealthOfBloon(b);
		}
		assert result12.getCashGenerated() == (parentTotalHealth - survivingTotalHealth) :
				"Cash generated " + result12.getCashGenerated() + " does not match health difference " + (parentTotalHealth - survivingTotalHealth);

		System.out.println("testCashMatchesRbeDifference passed.");
	}

	private static void testPreserveModifiersAndDistance() {
		// Test camo, regen, and distance preservation
		Bloon ceramic = BloonFactory.createCeramicBloon();
		ceramic.setCamo(true);
		ceramic.setRegen(true);
		ceramic.setDistanceTravelled(350);

		BloonPoppedResult result = new BloonPoppedResult(ceramic, 12);
		Set<Bloon> generated = result.getBloonsGenerated();

		assert !generated.isEmpty() : "Expected spawned children";
		for (Bloon b : generated) {
			assert b.isCamo() : "Child bloon must preserve camo modifier";
			assert b.isRegen() : "Child bloon must preserve regen modifier";
			assert b.getDistanceTravelled() == 350 : "Child bloon must preserve distance travelled (expected 350, got " + b.getDistanceTravelled() + ")";
		}

		System.out.println("testPreserveModifiersAndDistance passed.");
	}

	private static void testZomgOverkillNoExceptions() {
		// Test massive overkill on ZOMG (e.g. 50,000 damage) without stack overflow or exceptions
		Bloon zomg = BloonFactory.createZOMG();
		BloonPoppedResult result = new BloonPoppedResult(zomg, 50000);

		assert result.getBloonsGenerated().isEmpty() : "Expected 0 surviving bloons for 50000 damage on ZOMG";
		int expectedCash = BloonPoppedResult.getTotalHealthOfBloon(zomg);
		assert result.getCashGenerated() == expectedCash : "Expected " + expectedCash + " cash, got " + result.getCashGenerated();

		System.out.println("testZomgOverkillNoExceptions passed.");
	}

	private static void testExactShellBreakNoOverkill() {
		// Ceramic (18 health) taking 10 damage: shell health is 10, overkill is 0 -> 2 Rainbows with 8 health
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonPoppedResult result = new BloonPoppedResult(ceramic, 10);

		Set<Bloon> generated = result.getBloonsGenerated();
		assert generated.size() == 2 : "Expected 2 Rainbow bloons, got " + generated.size();
		for (Bloon b : generated) {
			assert b.getColor() == Bloon.Color.RAINBOW : "Expected RAINBOW, got " + b.getColor();
			assert b.getHealth() == 8 : "Expected health 8, got " + b.getHealth();
		}
		assert result.getCashGenerated() == 10 : "Expected 10 cash, got " + result.getCashGenerated();

		System.out.println("testExactShellBreakNoOverkill passed.");
	}

	private static void testCeramicOverkillOneDamage() {
		// Ceramic (18 health) taking 11 damage: shell takes 10 damage -> 2 Rainbows (8 health) take 1 overkill damage -> 4 Zebras with 7 health
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonPoppedResult result = new BloonPoppedResult(ceramic, 11);

		Set<Bloon> generated = result.getBloonsGenerated();
		assert generated.size() == 4 : "Expected 4 Zebra bloons, got " + generated.size();
		for (Bloon b : generated) {
			assert b.getColor() == Bloon.Color.ZEBRA : "Expected ZEBRA, got " + b.getColor();
			assert b.getHealth() == 7 : "Expected health 7, got " + b.getHealth();
		}
		assert result.getCashGenerated() == 12 : "Expected 12 cash, got " + result.getCashGenerated();

		System.out.println("testCeramicOverkillOneDamage passed.");
	}

	private static void testRedBloonOverkill() {
		// Red bloon taking 100 damage: 0 surviving bloons, 1 cash generated
		Bloon red = BloonFactory.createRedBloon();
		BloonPoppedResult result = new BloonPoppedResult(red, 100);

		assert result.getBloonsGenerated().isEmpty() : "Expected 0 bloons";
		assert result.getCashGenerated() == 1 : "Expected 1 cash, got " + result.getCashGenerated();

		System.out.println("testRedBloonOverkill passed.");
	}

	private static void testBfbOverkill() {
		// BFB (918 health) taking 710 damage: BFB shell takes 700 damage (overkill 10) -> spawns 4 MOABs (218 health each) taking 10 overkill damage -> 4 MOABs with 208 health each
		Bloon bfb = BloonFactory.createBFB();
		BloonPoppedResult result = new BloonPoppedResult(bfb, 710);

		Set<Bloon> generated = result.getBloonsGenerated();
		assert generated.size() == 4 : "Expected 4 MOAB bloons, got " + generated.size();
		for (Bloon b : generated) {
			assert b.getColor() == Bloon.Color.MOAB : "Expected MOAB, got " + b.getColor();
			assert b.getHealth() == 208 : "Expected health 208, got " + b.getHealth();
		}
		// Cash = BFB total health (2464) - 4 * MOAB(208) health (416 + (208-18) = 606 * 4 = 2424) = 2464 - 2424 = 40 cash
		assert result.getCashGenerated() == 740 : "Expected 740 cash, got " + result.getCashGenerated();

		System.out.println("testBfbOverkill passed.");
	}
}
