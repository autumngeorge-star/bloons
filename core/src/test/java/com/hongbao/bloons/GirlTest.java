package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GirlTest {

    @Test
    public void testReimuCreationAndStats() {
        Girl reimu = GirlFactory.createReimu();
        assertEquals("Reimu", reimu.getName());
        assertEquals(0, reimu.getLevel());
        assertTrue(reimu.getCost() > 0);
        assertTrue(reimu.getDamage() > 0);
        assertTrue(reimu.getPierce() > 0);
        assertTrue(reimu.getRange() > 0);
        assertTrue(reimu.getVisualRange() > 0);
        assertNotNull(reimu.getImageFileName());
    }

    @Test
    public void testCooldownLogic() {
        Girl reimu = GirlFactory.createReimu();
        int initialCooldown = reimu.getCooldown();
        reimu.decrementCooldown();
        assertEquals(initialCooldown - 1, reimu.getCooldown());

        reimu.resetCooldown();
        assertEquals(reimu.getAttackDelay(), reimu.getCooldown());
    }

    @Test
    public void testUpgradeLogic() {
        Girl marisa = GirlFactory.createMarisa();
        assertEquals(0, marisa.getLevel());
        int initialCost = marisa.getCost();
        assertEquals(initialCost / 2, marisa.getSellPrice());

        int upgradeCost = marisa.getUpgradeCost();
        assertTrue(marisa.canUpgrade(upgradeCost + 100));
        assertFalse(marisa.canUpgrade(upgradeCost - 10));

        marisa.upgrade();
        assertEquals(1, marisa.getLevel());
        assertEquals((initialCost + upgradeCost) / 2, marisa.getSellPrice());
    }

    @Test
    public void testUpgradedStatsObject() {
        Girl sakuya = GirlFactory.createSakuya();
        Girl upgraded = sakuya.getUpgradedStats();
        assertNotNull(upgraded);
        assertEquals(1, upgraded.getLevel());
        assertEquals(sakuya.getName(), upgraded.getName());
    }
}
