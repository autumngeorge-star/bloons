package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    private Player defaultPlayer;
    private Player customPlayer;

    @Before
    public void setUp() {
        defaultPlayer = new Player();
        customPlayer = new Player(500, 150);
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(200, defaultPlayer.getMoney());
        assertEquals(200, defaultPlayer.getHealth());
    }

    @Test
    public void testCustomConstructor() {
        assertEquals(500, customPlayer.getMoney());
        assertEquals(150, customPlayer.getHealth());
    }

    @Test
    public void testEarnMoney() {
        defaultPlayer.earnMoney(150);
        assertEquals(350, defaultPlayer.getMoney());
    }

    @Test
    public void testSpendMoneySuccess() {
        boolean success = defaultPlayer.spendMoney(100);
        assertTrue(success);
        assertEquals(100, defaultPlayer.getMoney());
    }

    @Test
    public void testSpendMoneyInsufficientFunds() {
        boolean success = defaultPlayer.spendMoney(300);
        assertFalse(success);
        assertEquals(200, defaultPlayer.getMoney());
    }

    @Test
    public void testDecreaseHealthNormal() {
        defaultPlayer.decreaseHealth(50);
        assertEquals(150, defaultPlayer.getHealth());
    }

    @Test
    public void testDecreaseHealthBelowZeroFloorsAtZero() {
        defaultPlayer.decreaseHealth(300);
        assertEquals(0, defaultPlayer.getHealth());
    }

    @Test
    public void testCanPurchaseGirlAndPurchaseGirl() {
        Girl marisa = GirlFactory.createMarisa(); // cost 200
        Girl yukari = GirlFactory.createYukari(); // cost 2500

        assertTrue(defaultPlayer.canPurchaseGirl(marisa));
        assertFalse(defaultPlayer.canPurchaseGirl(yukari));

        defaultPlayer.purchaseGirl(marisa);
        assertEquals(0, defaultPlayer.getMoney());
    }
}
