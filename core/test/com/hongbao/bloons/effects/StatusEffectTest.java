package com.hongbao.bloons.effects;

import com.hongbao.bloons.effects.strategies.DamageOverTimeStrategy;
import com.hongbao.bloons.effects.strategies.FreezeStatusStrategy;
import com.hongbao.bloons.effects.strategies.SlowStatusStrategy;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StatusEffectTest {

    private Bloon redBloon;
    private Bloon blueBloon;

    @Before
    public void setUp() {
        // Red Bloon: base speed = 5, health = 1
        redBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        // Blue Bloon: base speed = 6, health = 2
        blueBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
    }

    @Test
    public void testStatusEffectBuilderAndFactory() {
        StatusEffectFactory factory = new StatusEffectBuilder()
                .effectType(StatusEffectType.SLOW)
                .duration(4.0f)
                .intensity(0.4f)
                .stackable(true)
                .maxStacks(3)
                .tickInterval(1.0f)
                .tickDamage(0)
                .build();

        assertNotNull(factory);
        assertEquals(StatusEffectType.SLOW, factory.getEffectType());
        assertEquals(4.0f, factory.getDuration(), 0.001f);
        assertEquals(0.4f, factory.getIntensity(), 0.001f);
        assertTrue(factory.isStackable());
        assertEquals(3, factory.getMaxStacks());

        StatusEffectStrategy strategy = factory.createEffect();
        assertNotNull(strategy);
        assertTrue(strategy instanceof SlowStatusStrategy);
        assertEquals(StatusEffectType.SLOW, strategy.getType());
        assertEquals(4.0f, strategy.getDuration(), 0.001f);
        assertEquals(4.0f, strategy.getRemainingDuration(), 0.001f);
    }

    @Test
    public void testSlowStatusStrategyLifecycleAndSpeedRecalculation() {
        StatusEffectFactory slowFactory = StatusEffectFactory.builder()
                .effectType(StatusEffectType.SLOW)
                .duration(2.0f)
                .intensity(0.4f)
                .stackable(true)
                .maxStacks(2)
                .build();

        assertEquals(5, redBloon.getSpeed());

        // Apply first slow stack (40% slow: 5 * (1 - 0.4) = 3)
        slowFactory.applyEffect(redBloon);
        assertEquals(1, redBloon.getActiveStatusEffects().size());
        assertEquals(3, redBloon.getSpeed());

        // Apply second slow stack (40% * 2 = 80% slow: 5 * (1 - 0.8) = 1)
        slowFactory.applyEffect(redBloon);
        assertEquals(1, redBloon.getActiveStatusEffects().size()); // Stacked inside same strategy
        assertEquals(2, redBloon.getActiveStatusEffects().get(0).getStacks());
        assertEquals(1, redBloon.getSpeed());

        // Update bloon frame ticks for 1.0s (not yet expired)
        redBloon.update(1.0f);
        assertFalse(redBloon.getActiveStatusEffects().isEmpty());
        assertEquals(1, redBloon.getSpeed());

        // Update bloon frame ticks for another 1.1s (total 2.1s -> expired)
        redBloon.update(1.1f);
        assertTrue(redBloon.getActiveStatusEffects().isEmpty());
        // Speed restored to base speed 5
        assertEquals(5, redBloon.getSpeed());
    }

    @Test
    public void testFreezeStatusStrategyLifecycleAndTickDamage() {
        StatusEffectFactory freezeFactory = StatusEffectFactory.builder()
                .effectType(StatusEffectType.FREEZE)
                .duration(1.0f)
                .intensity(1.0f)
                .tickInterval(0.4f)
                .tickDamage(1)
                .build();

        assertEquals(6, blueBloon.getSpeed());
        assertEquals(2, blueBloon.getHealth());

        freezeFactory.applyEffect(blueBloon);

        // Movement stopped during freeze
        assertEquals(0, blueBloon.getSpeed());

        // Tick 0.4s -> 1 tick damage dealt (health 2 -> 1)
        blueBloon.update(0.4f);
        assertEquals(1, blueBloon.getHealth());
        assertEquals(0, blueBloon.getSpeed());

        // Tick 0.4s -> 2nd tick damage dealt (health 1 -> 0)
        blueBloon.update(0.4f);
        assertEquals(0, blueBloon.getHealth());

        // Tick 0.3s -> freeze expires, speed restored
        blueBloon.update(0.3f);
        assertTrue(blueBloon.getActiveStatusEffects().isEmpty());
        assertEquals(6, blueBloon.getSpeed());
    }

    @Test
    public void testDamageOverTimeStrategy() {
        StatusEffectFactory dotFactory = StatusEffectFactory.builder()
                .effectType(StatusEffectType.DOT)
                .duration(1.5f)
                .tickInterval(0.5f)
                .tickDamage(1)
                .build();

        dotFactory.applyEffect(blueBloon);
        assertEquals(1, blueBloon.getActiveStatusEffects().size());
        assertTrue(blueBloon.getActiveStatusEffects().get(0) instanceof DamageOverTimeStrategy);

        // Initial health = 2
        blueBloon.update(0.5f); // 1st tick -> damage 1
        assertEquals(1, blueBloon.getHealth());

        blueBloon.update(0.5f); // 2nd tick -> damage 1
        assertEquals(0, blueBloon.getHealth());

        blueBloon.update(0.6f); // Duration expired
        assertTrue(blueBloon.getActiveStatusEffects().isEmpty());
    }

    @Test
    public void testBulletAndGirlStatusEffectFactoryIntegration() {
        Girl sakuya = GirlFactory.createSakuya();
        assertNotNull(sakuya.getStatusEffectFactory());
        assertEquals(StatusEffectType.SLOW, sakuya.getStatusEffectFactory().getEffectType());

        Bullet bullet = sakuya.createBullet();
        assertNotNull(bullet);
        assertNotNull(bullet.getStatusEffectFactory());
        assertEquals(StatusEffectType.SLOW, bullet.getStatusEffectFactory().getEffectType());

        Girl alice = GirlFactory.createAlice();
        assertNotNull(alice.getStatusEffectFactory());
        assertEquals(StatusEffectType.FREEZE, alice.getStatusEffectFactory().getEffectType());

        Bullet aliceBullet = alice.createBullet();
        assertNotNull(aliceBullet.getStatusEffectFactory());
        assertEquals(StatusEffectType.FREEZE, aliceBullet.getStatusEffectFactory().getEffectType());

        Girl yuyuko = GirlFactory.createYuyuko();
        assertNotNull(yuyuko.getStatusEffectFactory());
        assertEquals(StatusEffectType.DOT, yuyuko.getStatusEffectFactory().getEffectType());

        Girl reimu = GirlFactory.createReimu();
        assertNull(reimu.getStatusEffectFactory());
        Bullet reimuBullet = reimu.createBullet();
        assertNull(reimuBullet.getStatusEffectFactory());
    }
}
