package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    private Girl createGirlWithCost(int cost) {
        return new Girl(
                "TestGirl",
                Collections.singletonList(10),
                Collections.singletonList(5.0f),
                Collections.singletonList(1),
                Collections.singletonList(1),
                Collections.singletonList(100.0f),
                Collections.singletonList(100.0f),
                Collections.singletonList(false),
                "test.png",
                "bullet.png",
                cost,
                Collections.singletonList(50)
        );
    }

    @Test
    public void testDefaultConstructor() {
        Player player = new Player();
        assertEquals(200, player.getMoney());
        assertEquals(200, player.getHealth());
    }

    @Test
    public void testParameterizedConstructor() {
        Player player = new Player(500, 150);
        assertEquals(500, player.getMoney());
        assertEquals(150, player.getHealth());

        Player zeroPlayer = new Player(0, 0);
        assertEquals(0, zeroPlayer.getMoney());
        assertEquals(0, zeroPlayer.getHealth());
    }

    @Test
    public void testEarnMoney() {
        Player player = new Player(200, 200);
        player.earnMoney(50);
        assertEquals(250, player.getMoney());

        player.earnMoney(0);
        assertEquals(250, player.getMoney());

        player.earnMoney(100);
        assertEquals(350, player.getMoney());
    }

    @Test
    public void testSpendMoneySuccess() {
        Player player = new Player(200, 200);
        boolean success = player.spendMoney(50);
        assertTrue(success);
        assertEquals(150, player.getMoney());

        boolean exactSuccess = player.spendMoney(150);
        assertTrue(exactSuccess);
        assertEquals(0, player.getMoney());
    }

    @Test
    public void testSpendMoneyOverdraft() {
        Player player = new Player(200, 200);
        boolean success = player.spendMoney(201);
        assertFalse(success);
        assertEquals(200, player.getMoney());

        boolean largeSuccess = player.spendMoney(1000);
        assertFalse(largeSuccess);
        assertEquals(200, player.getMoney());
    }

    @Test
    public void testDecreaseHealthStandard() {
        Player player = new Player(200, 200);
        player.decreaseHealth(50);
        assertEquals(150, player.getHealth());

        player.decreaseHealth(50);
        assertEquals(100, player.getHealth());
    }

    @Test
    public void testDecreaseHealthExact() {
        Player player = new Player(200, 200);
        player.decreaseHealth(200);
        assertEquals(0, player.getHealth());
    }

    @Test
    public void testDecreaseHealthZeroClamping() {
        Player player = new Player(200, 200);
        player.decreaseHealth(250);
        assertEquals(0, player.getHealth());

        player.decreaseHealth(100);
        assertEquals(0, player.getHealth());
    }

    @Test
    public void testCanPurchaseGirl() {
        Player player = new Player(200, 200);

        Girl affordableGirl = createGirlWithCost(150);
        assertTrue(player.canPurchaseGirl(affordableGirl));

        Girl exactCostGirl = createGirlWithCost(200);
        assertTrue(player.canPurchaseGirl(exactCostGirl));

        Girl expensiveGirl = createGirlWithCost(201);
        assertFalse(player.canPurchaseGirl(expensiveGirl));
    }

    @Test
    public void testPurchaseGirlAffordable() {
        Player player = new Player(200, 200);
        Girl affordableGirl = createGirlWithCost(150);

        player.purchaseGirl(affordableGirl);
        assertEquals(50, player.getMoney());
    }

    @Test
    public void testPurchaseGirlUnaffordable() {
        Player player = new Player(200, 200);
        Girl expensiveGirl = createGirlWithCost(250);

        player.purchaseGirl(expensiveGirl);
        assertEquals(200, player.getMoney());
    }
}
