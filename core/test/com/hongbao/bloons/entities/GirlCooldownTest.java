package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class GirlCooldownTest {

    @Test
    public void testReimuAbilityCooldown() {
        Girl reimu = GirlFactory.createReimu();
        assertEquals(15.0f, reimu.getAbilityCooldown(), 0.001f);
    }

    @Test
    public void testYuyukoAbilityCooldown() {
        Girl yuyuko = GirlFactory.createYuyuko();
        assertEquals(20.0f, yuyuko.getAbilityCooldown(), 0.001f);
    }

    @Test
    public void testMarisaNoAbilityCooldown() {
        Girl marisa = GirlFactory.createMarisa();
        assertEquals(0.0f, marisa.getAbilityCooldown(), 0.001f);
    }

    @Test
    public void testUpgradedGirlPreservesAbilityCooldown() {
        Girl reimu = GirlFactory.createReimu();
        Girl upgradedReimu = reimu.getUpgradedStats();
        assertNotNull(upgradedReimu);
        assertEquals(15.0f, upgradedReimu.getAbilityCooldown(), 0.001f);
    }
}
