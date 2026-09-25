package com.hongbao.bloons.modifiers;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class StatModifierTest {

    @Test
    public void testFlatModifiers() {
        Girl testGirl = new Girl(
                "TestGirl",
                100, 10f, 1, 1, 500f, 200f, false,
                "reimu.png", "red_spell_card.png", 100,
                Collections.singletonList(
                        UpgradeDefinition.builder()
                                .name("Flat Buffs")
                                .cost(50)
                                .addFlatModifier(StatType.ATTACK_DELAY, -20f)
                                .addFlatModifier(StatType.DAMAGE, 2f)
                                .addFlatModifier(StatType.PIERCE, 3f)
                                .addFlatModifier(StatType.RANGE, 100f)
                                .addFlatModifier(StatType.VISUAL_RANGE, 50f)
                                .addFlatModifier(StatType.BULLET_SPEED, 5f)
                                .addModifier(StatModifier.homing(true))
                                .build()
                )
        );

        assertEquals(100, testGirl.getAttackDelay());
        assertEquals(1, testGirl.getDamage());
        assertEquals(1, testGirl.getPierce());
        assertEquals(500f, testGirl.getRange(), 0.01f);
        assertEquals(200f, testGirl.getVisualRange(), 0.01f);
        assertEquals(10f, testGirl.getBulletSpeed(), 0.01f);
        assertFalse(testGirl.isHoming());

        // Apply Upgrade
        testGirl.upgrade();

        assertEquals(80, testGirl.getAttackDelay());
        assertEquals(3, testGirl.getDamage());
        assertEquals(4, testGirl.getPierce());
        assertEquals(600f, testGirl.getRange(), 0.01f);
        assertEquals(250f, testGirl.getVisualRange(), 0.01f);
        assertEquals(15f, testGirl.getBulletSpeed(), 0.01f);
        assertTrue(testGirl.isHoming());
    }

    @Test
    public void testDeterministicOrderAdditiveBeforeMultiplicative() {
        // Base range 500. Upgrade 1 adds 100 flat. Upgrade 2 multiplies by 1.2x.
        // Formula: (500 + 100) * 1.2 = 720 regardless of modifier insertion order.
        UpgradeDefinition upgradeMult = UpgradeDefinition.builder()
                .name("Mult")
                .cost(50)
                .addMultiplyModifier(StatType.RANGE, 1.2f)
                .build();

        UpgradeDefinition upgradeFlat = UpgradeDefinition.builder()
                .name("Flat")
                .cost(50)
                .addFlatModifier(StatType.RANGE, 100f)
                .build();

        Girl girl1 = new Girl(
                "Girl1", 100, 10f, 1, 1, 500f, 200f, false,
                "reimu.png", "red_spell_card.png", 100,
                Arrays.asList(upgradeMult, upgradeFlat)
        );

        girl1.upgrade(); // applies mult
        girl1.upgrade(); // applies flat

        assertEquals(720f, girl1.getRange(), 0.01f);
    }

    @Test
    public void testClampingBounds() {
        // Attack delay must not drop below 1ms
        Girl fastGirl = new Girl(
                "FastGirl",
                10, 10f, 1, 1, 500f, 200f, false,
                "reimu.png", "red_spell_card.png", 100,
                Collections.singletonList(
                        UpgradeDefinition.builder()
                                .name("Super Fast")
                                .cost(50)
                                .addFlatModifier(StatType.ATTACK_DELAY, -50f)
                                .build()
                )
        );

        fastGirl.upgrade();
        assertEquals(1, fastGirl.getAttackDelay());
    }

    @Test
    public void testPreviewUpgradedStats() {
        Girl reimu = GirlFactory.createReimu();
        assertEquals(0, reimu.getLevel());
        assertEquals(200, reimu.getUpgradeCost());

        Girl preview = reimu.getUpgradedStats();
        assertNotNull(preview);
        assertEquals(1, preview.getLevel());
        assertEquals(8, preview.getPierce());
        assertEquals(220f, preview.getVisualRange(), 0.01f);

        // Original reimu is unchanged
        assertEquals(0, reimu.getLevel());
        assertEquals(4, reimu.getPierce());
    }

    @Test
    public void testGirlFactoryCharactersTierProgression() {
        // Test Reimu
        Girl reimu = GirlFactory.createReimu();
        assertEquals(86, reimu.getAttackDelay());
        assertEquals(4, reimu.getPierce());
        assertEquals(200f, reimu.getVisualRange(), 0.01f);

        reimu.upgrade(); // Tier 1
        assertEquals(86, reimu.getAttackDelay());
        assertEquals(8, reimu.getPierce());
        assertEquals(220f, reimu.getVisualRange(), 0.01f);

        reimu.upgrade(); // Tier 2
        assertEquals(75, reimu.getAttackDelay());
        assertEquals(13, reimu.getPierce());
        assertEquals(250f, reimu.getVisualRange(), 0.01f);
        assertEquals(Girl.NO_UPGRADES_AVAILABLE, reimu.getUpgradeCost());
        assertNull(reimu.getUpgradedStats());

        // Test Sakuya
        Girl sakuya = GirlFactory.createSakuya();
        assertEquals(30, sakuya.getAttackDelay());
        assertEquals(2, sakuya.getPierce());
        sakuya.upgrade();
        assertEquals(20, sakuya.getAttackDelay());
        assertEquals(2, sakuya.getPierce());
        sakuya.upgrade();
        assertEquals(20, sakuya.getAttackDelay());
        assertEquals(4, sakuya.getPierce());

        // Test Yukari
        Girl yukari = GirlFactory.createYukari();
        assertEquals(1, yukari.getDamage());
        assertEquals(600f, yukari.getRange(), 0.01f);
        yukari.upgrade();
        assertEquals(700f, yukari.getRange(), 0.01f);
        yukari.upgrade();
        assertEquals(2, yukari.getDamage());
        assertEquals(1000f, yukari.getRange(), 0.01f);
    }
}
