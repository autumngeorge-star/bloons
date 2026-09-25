package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player(500, 100);
    }

    @Test
    public void testInitialState() {
        Player defaultPlayer = new Player();
        assertEquals(200, defaultPlayer.getMoney());
        assertEquals(200, defaultPlayer.getHealth());
    }

    @Test
    public void testEarnMoney() {
        player.earnMoney(150);
        assertEquals(650, player.getMoney());
    }

    @Test
    public void testSpendMoneySuccess() {
        boolean result = player.spendMoney(300);
        assertTrue(result);
        assertEquals(200, player.getMoney());
    }

    @Test
    public void testSpendMoneyFailure() {
        boolean result = player.spendMoney(600);
        assertFalse(result);
        assertEquals(500, player.getMoney());
    }

    @Test
    public void testDecreaseHealth() {
        player.decreaseHealth(30);
        assertEquals(70, player.getHealth());

        player.decreaseHealth(100);
        assertEquals(0, player.getHealth());
    }

    @Test
    public void testCanPurchaseGirlWithMockito() {
        Girl mockGirl = Mockito.mock(Girl.class);
        Mockito.when(mockGirl.getCost()).thenReturn(400);

        assertTrue(player.canPurchaseGirl(mockGirl));

        Girl expensiveGirl = Mockito.mock(Girl.class);
        Mockito.when(expensiveGirl.getCost()).thenReturn(600);

        assertFalse(player.canPurchaseGirl(expensiveGirl));
    }
}
