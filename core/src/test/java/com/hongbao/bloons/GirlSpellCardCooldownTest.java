package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Assert;
import org.junit.Test;

public class GirlSpellCardCooldownTest {

    @Test
    public void testInitialStateAndReadiness() {
        Girl reimu = GirlFactory.createReimu();
        Assert.assertEquals(1800, reimu.getSpellCardDelay());
        Assert.assertEquals(0, reimu.getSpellCardCooldown());
        Assert.assertTrue(reimu.canActivateSpellCard());

        Girl marisa = GirlFactory.createMarisa();
        Assert.assertNull(marisa.createSpellCard());
        Assert.assertFalse(marisa.canActivateSpellCard());
    }

    @Test
    public void testTriggerCooldownAndDecrement() {
        Girl reimu = GirlFactory.createReimu();
        Assert.assertTrue(reimu.canActivateSpellCard());

        reimu.triggerSpellCardCooldown();
        Assert.assertEquals(1800, reimu.getSpellCardCooldown());
        Assert.assertFalse(reimu.canActivateSpellCard());

        reimu.decrementSpellCardCooldown();
        Assert.assertEquals(1799, reimu.getSpellCardCooldown());
        Assert.assertFalse(reimu.canActivateSpellCard());

        for (int i = 0; i < 1799; i++) {
            reimu.decrementSpellCardCooldown();
        }
        Assert.assertEquals(0, reimu.getSpellCardCooldown());
        Assert.assertTrue(reimu.canActivateSpellCard());
    }

    @Test
    public void testUpgradedStatsPreservesCooldownState() {
        Girl reimu = GirlFactory.createReimu();
        reimu.triggerSpellCardCooldown();
        reimu.decrementSpellCardCooldown(); // remaining 1799

        Girl upgraded = reimu.getUpgradedStats();
        Assert.assertNotNull(upgraded);
        Assert.assertEquals(1800, upgraded.getSpellCardDelay());
        Assert.assertEquals(1799, upgraded.getSpellCardCooldown());
        Assert.assertFalse(upgraded.canActivateSpellCard());
    }

    @Test
    public void testYuyukoSpellCardCooldown() {
        Girl yuyuko = GirlFactory.createYuyuko();
        Assert.assertEquals(2400, yuyuko.getSpellCardDelay());
        Assert.assertEquals(0, yuyuko.getSpellCardCooldown());
        Assert.assertTrue(yuyuko.canActivateSpellCard());

        yuyuko.triggerSpellCardCooldown();
        Assert.assertEquals(2400, yuyuko.getSpellCardCooldown());
        Assert.assertFalse(yuyuko.canActivateSpellCard());
    }

    @Test
    public void testTowersWithoutSpellCardCannotActivate() {
        Girl yukari = GirlFactory.createYukari();
        Assert.assertFalse(yukari.canActivateSpellCard());
        yukari.triggerSpellCardCooldown();
        Assert.assertFalse(yukari.canActivateSpellCard());

        Girl alice = GirlFactory.createAlice();
        Assert.assertFalse(alice.canActivateSpellCard());
    }
}
