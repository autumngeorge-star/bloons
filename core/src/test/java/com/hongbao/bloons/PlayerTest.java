package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(1000, 100);
    }

    @Test
    public void testInitialValues() {
        assertEquals(1000, player.getMoney());
        assertEquals(100, player.getHealth());
    }

    @Test
    public void testDefaultConstructor() {
        Player defaultPlayer = new Player();
        assertEquals(200, defaultPlayer.getMoney());
        assertEquals(200, defaultPlayer.getHealth());
    }

    @Test
    public void testEarnMoney() {
        player.earnMoney(250);
        assertEquals(1250, player.getMoney());
    }

    @Test
    public void testSpendMoneySuccess() {
        assertTrue(player.spendMoney(300));
        assertEquals(700, player.getMoney());
    }

    @Test
    public void testSpendMoneyInsufficientFunds() {
        assertFalse(player.spendMoney(1500));
        assertEquals(1000, player.getMoney());
    }

    @Test
    public void testDecreaseHealth() {
        player.decreaseHealth(30);
        assertEquals(70, player.getHealth());

        player.decreaseHealth(100);
        assertEquals(0, player.getHealth());
    }

    @Test
    public void testCanPurchaseAndPurchaseGirl() {
        Girl reimu = GirlFactory.createReimu();
        assertTrue(player.canPurchaseGirl(reimu));

        int initialMoney = player.getMoney();
        player.purchaseGirl(reimu);
        assertEquals(initialMoney - reimu.getCost(), player.getMoney());
    }
}
