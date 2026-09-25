package com.hongbao.bloons.components;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class AbilityCooldownManagerTest {

    private AbilityCooldownManager manager;
    private static final float COOLDOWN_DURATION = 15.0f;

    @Before
    public void setUp() {
        manager = new AbilityCooldownManager(COOLDOWN_DURATION);
    }

    @Test
    public void testInitialStateIsReady() {
        assertTrue("Ability should have ability enabled", manager.hasAbility());
        assertTrue("Ability should initially be ready", manager.isReady());
        assertEquals(0f, manager.getRemainingSeconds(), 0.0001f);
        assertEquals(1.0f, manager.getCooldownProgress(), 0.0001f);
        assertEquals(AbilityCooldownManager.AbilityState.READY, manager.getCurrentState());
    }

    @Test
    public void testTriggerStartsCooldown() {
        boolean triggered = manager.trigger();
        assertTrue("Trigger should return true when ready", triggered);
        assertFalse("Ability should not be ready after trigger", manager.isReady());
        assertEquals(COOLDOWN_DURATION, manager.getRemainingSeconds(), 0.0001f);
        assertEquals(0.0f, manager.getCooldownProgress(), 0.0001f);
        assertEquals(AbilityCooldownManager.AbilityState.ON_COOLDOWN, manager.getCurrentState());
    }

    @Test
    public void testTriggerRejectionDuringCooldown() {
        manager.trigger();
        float remainingBefore = manager.getRemainingSeconds();

        boolean secondTrigger = manager.trigger();
        assertFalse("Second trigger attempt during active cooldown should be rejected", secondTrigger);
        assertEquals("Remaining seconds should not change on rejected trigger", remainingBefore, manager.getRemainingSeconds(), 0.0001f);
    }

    @Test
    public void testDeltaTimeUpdates() {
        manager.trigger();

        // Advance 5 seconds
        manager.update(5.0f);
        assertEquals(10.0f, manager.getRemainingSeconds(), 0.0001f);
        assertEquals(5.0f / 15.0f, manager.getCooldownProgress(), 0.001f);
        assertFalse(manager.isReady());

        // Advance another 9.5 seconds
        manager.update(9.5f);
        assertEquals(0.5f, manager.getRemainingSeconds(), 0.0001f);
        assertFalse(manager.isReady());

        // Advance final 0.5 seconds
        manager.update(0.5f);
        assertEquals(0.0f, manager.getRemainingSeconds(), 0.0001f);
        assertTrue("Ability should be ready when cooldown completes", manager.isReady());
        assertEquals(1.0f, manager.getCooldownProgress(), 0.0001f);
    }

    @Test
    public void testSpeedUpModeSimulation() {
        manager.trigger();

        // Simulate 3x speed mode: 9 sub-ticks of 0.1s = 0.9s per frame
        float delta = 0.1f;
        for (int frame = 0; frame < 10; frame++) {
            for (int subTick = 0; subTick < 9; subTick++) {
                manager.update(delta);
            }
        }
        // Total elapsed time = 10 frames * 9 sub-ticks * 0.1s = 9.0s
        assertEquals(6.0f, manager.getRemainingSeconds(), 0.001f);
    }

    @Test
    public void testStateChangeListener() {
        final AtomicInteger readyToCooldownCount = new AtomicInteger(0);
        final AtomicInteger cooldownToReadyCount = new AtomicInteger(0);

        manager.addStateChangeListener(newState -> {
            if (newState == AbilityCooldownManager.AbilityState.ON_COOLDOWN) {
                readyToCooldownCount.incrementAndGet();
            } else if (newState == AbilityCooldownManager.AbilityState.READY) {
                cooldownToReadyCount.incrementAndGet();
            }
        });

        manager.trigger();
        assertEquals(1, readyToCooldownCount.get());

        manager.update(COOLDOWN_DURATION);
        assertEquals(1, cooldownToReadyCount.get());
    }

    @Test
    public void testNoAbilityTower() {
        AbilityCooldownManager noAbility = new AbilityCooldownManager(0f);
        assertFalse("Tower with 0 cooldown should report hasAbility = false", noAbility.hasAbility());
        assertFalse("Tower without ability should report isReady = false", noAbility.isReady());
        assertFalse("Triggering tower without ability should return false", noAbility.trigger());
        assertEquals(1.0f, noAbility.getCooldownProgress(), 0.0001f);
    }
}
