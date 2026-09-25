package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GirlTest {

    @Test
    public void testInitializationSetsBaseInvestmentAndLevelZero() {
        // Test entity initialized directly via constructor with even cost
        Girl customGirlEven = new Girl(
                "TestGirlEven",
                Collections.singletonList(10),
                Collections.singletonList(10.0f),
                Collections.singletonList(1),
                Collections.singletonList(1),
                Collections.singletonList(100.0f),
                Collections.singletonList(100.0f),
                Collections.singletonList(false),
                "test.png",
                "bullet.png",
                300,
                Collections.singletonList(150)
        );

        assertEquals(300, customGirlEven.getCost());
        assertEquals(300, customGirlEven.getTotalInvestment());
        assertEquals(0, customGirlEven.getLevel());

        // Test entity initialized directly via constructor with odd cost
        Girl customGirlOdd = new Girl(
                "TestGirlOdd",
                Collections.singletonList(10),
                Collections.singletonList(10.0f),
                Collections.singletonList(1),
                Collections.singletonList(1),
                Collections.singletonList(100.0f),
                Collections.singletonList(100.0f),
                Collections.singletonList(false),
                "test.png",
                "bullet.png",
                325,
                Collections.singletonList(200)
        );

        assertEquals(325, customGirlOdd.getCost());
        assertEquals(325, customGirlOdd.getTotalInvestment());
        assertEquals(0, customGirlOdd.getLevel());

        // Test factory creation (Reimu)
        Girl reimu = GirlFactory.createReimu();
        assertEquals(325, reimu.getCost());
        assertEquals(325, reimu.getTotalInvestment());
        assertEquals(0, reimu.getLevel());
    }

    @Test
    public void testSingleAndMultiLevelUpgradeAccumulationAndLevelProgression() {
        Girl reimu = GirlFactory.createReimu();
        int initialBaseCost = reimu.getCost(); // 325
        assertEquals(325, initialBaseCost);
        assertEquals(325, reimu.getTotalInvestment());
        assertEquals(0, reimu.getLevel());

        // Single-level upgrade
        int firstUpgradeCost = reimu.getUpgradeCost(); // 200
        int returnedCost1 = reimu.upgrade();

        assertEquals(firstUpgradeCost, returnedCost1);
        assertEquals(1, reimu.getLevel());
        assertEquals(initialBaseCost + firstUpgradeCost, reimu.getTotalInvestment()); // 525

        // Multi-level upgrade
        int secondUpgradeCost = reimu.getUpgradeCost(); // 280
        int returnedCost2 = reimu.upgrade();

        assertEquals(secondUpgradeCost, returnedCost2);
        assertEquals(2, reimu.getLevel());
        assertEquals(initialBaseCost + firstUpgradeCost + secondUpgradeCost, reimu.getTotalInvestment()); // 805
    }

    @Test
    public void testGetSellPriceIntegerDivisionTruncationForEvenAndOddInvestment() {
        // Even total investment: 300 / 2 = 150
        Girl evenGirl = new Girl(
                "EvenGirl",
                Collections.singletonList(10),
                Collections.singletonList(10.0f),
                Collections.singletonList(1),
                Collections.singletonList(1),
                Collections.singletonList(100.0f),
                Collections.singletonList(100.0f),
                Collections.singletonList(false),
                "test.png",
                "bullet.png",
                300,
                Collections.singletonList(100)
        );
        assertEquals(300, evenGirl.getTotalInvestment());
        assertEquals(150, evenGirl.getSellPrice());

        // Odd total investment: 325 / 2 = 162 (162.5 truncated)
        Girl oddGirl = new Girl(
                "OddGirl",
                Collections.singletonList(10),
                Collections.singletonList(10.0f),
                Collections.singletonList(1),
                Collections.singletonList(1),
                Collections.singletonList(100.0f),
                Collections.singletonList(100.0f),
                Collections.singletonList(false),
                "test.png",
                "bullet.png",
                325,
                Collections.singletonList(200)
        );
        assertEquals(325, oddGirl.getTotalInvestment());
        assertEquals(162, oddGirl.getSellPrice());

        // Odd total investment after upgrade: 325 + 200 = 525 / 2 = 262 (262.5 truncated)
        oddGirl.upgrade();
        assertEquals(525, oddGirl.getTotalInvestment());
        assertEquals(262, oddGirl.getSellPrice());

        // Custom odd investment test case: 101 / 2 = 50
        Girl customOdd101 = new Girl(
                "Custom101",
                Collections.singletonList(10),
                Collections.singletonList(10.0f),
                Collections.singletonList(1),
                Collections.singletonList(1),
                Collections.singletonList(100.0f),
                Collections.singletonList(100.0f),
                Collections.singletonList(false),
                "test.png",
                "bullet.png",
                101,
                Collections.singletonList(50)
        );
        assertEquals(101, customOdd101.getTotalInvestment());
        assertEquals(50, customOdd101.getSellPrice());
    }

    @Test
    public void testCanUpgradeBoundaryConditionsAndMaxLevel() {
        Girl reimu = GirlFactory.createReimu();
        int firstUpgradeCost = reimu.getUpgradeCost(); // 200

        // Case 1: Insufficient cash
        assertFalse(reimu.canUpgrade(0));
        assertFalse(reimu.canUpgrade(firstUpgradeCost - 1)); // 199

        // Case 2: Exact cash matching upgrade cost
        assertTrue(reimu.canUpgrade(firstUpgradeCost)); // 200

        // Case 3: Excess cash
        assertTrue(reimu.canUpgrade(firstUpgradeCost + 100)); // 300

        // Perform first upgrade to reach level 1
        reimu.upgrade();
        int secondUpgradeCost = reimu.getUpgradeCost(); // 280

        assertFalse(reimu.canUpgrade(secondUpgradeCost - 1)); // 279
        assertTrue(reimu.canUpgrade(secondUpgradeCost)); // 280

        // Perform second upgrade to reach max level (level 2)
        reimu.upgrade();
        assertEquals(Girl.NO_UPGRADES_AVAILABLE, reimu.getUpgradeCost());

        // Case 4: Max-level scenario where cost is NO_UPGRADES_AVAILABLE (-1)
        assertFalse(reimu.canUpgrade(0));
        assertFalse(reimu.canUpgrade(1000));
        assertFalse(reimu.canUpgrade(Integer.MAX_VALUE));
    }
}
