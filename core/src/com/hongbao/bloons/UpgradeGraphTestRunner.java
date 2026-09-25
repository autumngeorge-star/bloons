package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.GirlTier;
import com.hongbao.bloons.entities.UpgradeNode;
import com.hongbao.bloons.factories.GirlFactory;

import java.util.List;

public class UpgradeGraphTestRunner {

	private static void assertEquals(Object expected, Object actual, String message) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new AssertionError("FAIL: " + message + " - Expected: " + expected + ", Actual: " + actual);
	}

	private static void assertEquals(int expected, int actual, String message) {
		if (expected != actual) {
			throw new AssertionError("FAIL: " + message + " - Expected: " + expected + ", Actual: " + actual);
		}
	}

	private static void assertEquals(float expected, float actual, float delta, String message) {
		if (Math.abs(expected - actual) > delta) {
			throw new AssertionError("FAIL: " + message + " - Expected: " + expected + ", Actual: " + actual);
		}
	}

	private static void assertTrue(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError("FAIL: " + message + " - Expected true, got false");
		}
	}

	private static void assertFalse(boolean condition, String message) {
		if (condition) {
			throw new AssertionError("FAIL: " + message + " - Expected false, got true");
		}
	}

	private static void assertNotNull(Object obj, String message) {
		if (obj == null) {
			throw new AssertionError("FAIL: " + message + " - Expected non-null object");
		}
	}

	public static void main(String[] args) {
		System.out.println("Running Upgrade Graph Test Suite...");
		int passed = 0;

		try {
			testUpgradeNodePropertiesAndChildren();
			System.out.println("  [PASS] testUpgradeNodePropertiesAndChildren");
			passed++;

			testAcyclicConstraintInUpgradeNode();
			System.out.println("  [PASS] testAcyclicConstraintInUpgradeNode");
			passed++;

			testGirlLinearGraphBackwardCompatibility();
			System.out.println("  [PASS] testGirlLinearGraphBackwardCompatibility");
			passed++;

			testGirlBranchingUpgradeTreeSelectionPath1();
			System.out.println("  [PASS] testGirlBranchingUpgradeTreeSelectionPath1");
			passed++;

			testGirlBranchingUpgradeTreeSelectionPath2();
			System.out.println("  [PASS] testGirlBranchingUpgradeTreeSelectionPath2");
			passed++;

			testGirlUpgradedStatsPreview();
			System.out.println("  [PASS] testGirlUpgradedStatsPreview");
			passed++;

			System.out.println("SUCCESS: All " + passed + " tests passed!");
		} catch (Throwable t) {
			System.err.println("TEST SUITE FAILED:");
			t.printStackTrace();
			System.exit(1);
		}
	}

	public static void testUpgradeNodePropertiesAndChildren() {
		GirlTier tier0 = new GirlTier(80, 20f, 1, 4, 500f, 200f, true);
		GirlTier tier1 = new GirlTier(70, 25f, 2, 8, 550f, 220f, true);

		UpgradeNode child = new UpgradeNode("Path A", 200, tier1, 1);
		UpgradeNode root = new UpgradeNode("Base", 0, tier0, 0);
		root.addChild(child);

		assertEquals("Base", root.getTitle(), "Root title");
		assertEquals(0, root.getUpgradeCost(), "Root cost");
		assertEquals(1, root.getChildren().size(), "Root children size");
		assertEquals("Path A", root.getChildren().get(0).getTitle(), "Child title");
		assertEquals(200, root.getChildren().get(0).getUpgradeCost(), "Child cost");
		assertEquals(2, root.getChildren().get(0).getStats().getDamage(), "Child damage");
	}

	public static void testAcyclicConstraintInUpgradeNode() {
		GirlTier tier = new GirlTier(80, 20f, 1, 4, 500f, 200f, true);
		UpgradeNode nodeA = new UpgradeNode("Node A", 100, tier);
		UpgradeNode nodeB = new UpgradeNode("Node B", 100, tier);

		nodeA.addChild(nodeB);
		boolean caught = false;
		try {
			nodeB.addChild(nodeA);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught, "Cycle detection should throw IllegalArgumentException");
	}

	public static void testGirlLinearGraphBackwardCompatibility() {
		Girl yukari = GirlFactory.createYukari();

		assertEquals("Yukari", yukari.getName(), "Girl name");
		assertEquals(0, yukari.getLevel(), "Initial level");
		assertEquals(1, yukari.getDamage(), "Initial damage");
		assertEquals(1, yukari.getPierce(), "Initial pierce");
		assertEquals(2500, yukari.getUpgradeCost(), "Initial upgrade cost");
		assertTrue(yukari.canUpgrade(3000), "Can upgrade with $3000");
		assertFalse(yukari.canUpgrade(1000), "Cannot upgrade with $1000");

		int cost1 = yukari.upgrade();
		assertEquals(2500, cost1, "First upgrade cost");
		assertEquals(1, yukari.getLevel(), "Level after upgrade 1");
		assertEquals(1, yukari.getDamage(), "Damage after upgrade 1");
		assertEquals(2, yukari.getPierce(), "Pierce after upgrade 1");
		assertEquals(4500, yukari.getUpgradeCost(), "Upgrade cost at level 1");

		int cost2 = yukari.upgrade();
		assertEquals(4500, cost2, "Second upgrade cost");
		assertEquals(2, yukari.getLevel(), "Level after upgrade 2");
		assertEquals(2, yukari.getDamage(), "Damage after upgrade 2");
		assertEquals(4, yukari.getPierce(), "Pierce after upgrade 2");

		assertEquals(Girl.NO_UPGRADES_AVAILABLE, yukari.getUpgradeCost(), "No upgrades cost");
		assertFalse(yukari.canUpgrade(10000), "Cannot upgrade fully upgraded tower");
		assertEquals(0, yukari.getAvailableUpgrades().size(), "No available upgrades");
	}

	public static void testGirlBranchingUpgradeTreeSelectionPath1() {
		Girl reimu = GirlFactory.createReimu();

		assertEquals("Reimu", reimu.getName(), "Girl name");
		assertEquals(0, reimu.getLevel(), "Initial level");
		assertEquals(1, reimu.getDamage(), "Initial damage");

		List<UpgradeNode> available = reimu.getAvailableUpgrades();
		assertEquals(2, available.size(), "Root available branches count");
		assertEquals("Homing Master", available.get(0).getTitle(), "Branch 1 title");
		assertEquals("Shrine Maiden Barrier", available.get(1).getTitle(), "Branch 2 title");

		UpgradeNode homingBranch = available.get(0);
		assertTrue(reimu.canUpgrade(homingBranch, 500), "Can upgrade to Homing Master with $500");
		int cost = reimu.upgrade(homingBranch);

		assertEquals(200, cost, "Homing Master upgrade cost");
		assertEquals("Homing Master", reimu.getCurrentNode().getTitle(), "Active node title");
		assertEquals(2, reimu.getDamage(), "Homing Master damage");
		assertEquals(8, reimu.getPierce(), "Homing Master pierce");
		assertEquals(75, reimu.getAttackDelay(), "Homing Master attack delay");

		List<UpgradeNode> nextAvailable = reimu.getAvailableUpgrades();
		assertEquals(1, nextAvailable.size(), "Next available upgrades count");
		assertEquals("Divine Spirit", nextAvailable.get(0).getTitle(), "Divine Spirit title");

		reimu.upgrade(nextAvailable.get(0));
		assertEquals("Divine Spirit", reimu.getCurrentNode().getTitle(), "Divine Spirit node title");
		assertEquals(3, reimu.getDamage(), "Divine Spirit damage");
		assertEquals(12, reimu.getPierce(), "Divine Spirit pierce");
		assertFalse(reimu.canUpgrade(10000), "Cannot upgrade past Divine Spirit");
	}

	public static void testGirlBranchingUpgradeTreeSelectionPath2() {
		Girl reimu = GirlFactory.createReimu();

		List<UpgradeNode> available = reimu.getAvailableUpgrades();
		UpgradeNode barrierBranch = available.get(1);
		assertEquals("Shrine Maiden Barrier", barrierBranch.getTitle(), "Branch 2 title");

		int cost = reimu.upgrade(barrierBranch);
		assertEquals(250, cost, "Shrine Maiden Barrier cost");
		assertEquals("Shrine Maiden Barrier", reimu.getCurrentNode().getTitle(), "Active node title");
		assertEquals(1, reimu.getDamage(), "Shrine Maiden Barrier damage");
		assertEquals(16, reimu.getPierce(), "Shrine Maiden Barrier pierce");
		assertEquals(550f, reimu.getRange(), 0.01f, "Shrine Maiden Barrier range");
	}

	public static void testGirlUpgradedStatsPreview() {
		Girl marisa = GirlFactory.createMarisa();
		List<UpgradeNode> options = marisa.getAvailableUpgrades();
		assertEquals(2, options.size(), "Marisa initial upgrade options count");

		UpgradeNode option1 = options.get(0);
		Girl preview = marisa.getUpgradedStats(option1);

		assertNotNull(preview, "Preview girl should not be null");
		assertEquals(2, preview.getDamage(), "Preview damage");
		assertEquals(50f, preview.getBulletSpeed(), 0.01f, "Preview bullet speed");

		assertEquals(1, marisa.getDamage(), "Original Marisa damage unchanged");
		assertEquals(35f, marisa.getBulletSpeed(), 0.01f, "Original Marisa bullet speed unchanged");
	}
}
