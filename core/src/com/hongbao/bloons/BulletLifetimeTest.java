package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;

import java.util.List;

public class BulletLifetimeTest {

    public static void main(String[] args) {
        System.out.println("Running BulletLifetimeTest...");

        testBulletStateVariables();
        testGirlBulletLifetimeCalculation();
        testGirlRangePreserved();
        testSpellCardExplicitLifetime();
        testFrameRateIndependence();

        System.out.println("ALL BULLET LIFETIME TESTS PASSED SUCCESSFULLY!");
    }

    private static void testBulletStateVariables() {
        Bullet bullet = new Bullet(20f, 2, 2, 3.5f, false, "red_spell_card.png");

        assertEquals(0f, bullet.getElapsedTimeSeconds(), "Initial elapsedTimeSeconds should be 0");
        assertEquals(3.5f, bullet.getMaxLifetimeSeconds(), "maxLifetimeSeconds should be 3.5");
        assertFalse(bullet.isExpired(), "Bullet should not be expired initially");

        bullet.updateElapsedTime(1.5f);
        assertEquals(1.5f, bullet.getElapsedTimeSeconds(), "elapsedTimeSeconds should be 1.5 after update");
        assertFalse(bullet.isExpired(), "Bullet should not be expired after 1.5 seconds");

        bullet.updateElapsedTime(2.0f);
        assertEquals(3.5f, bullet.getElapsedTimeSeconds(), "elapsedTimeSeconds should be 3.5");
        assertTrue(bullet.isExpired(), "Bullet should be expired when elapsedTimeSeconds >= maxLifetimeSeconds");
    }

    private static void testGirlBulletLifetimeCalculation() {
        Girl reimu = GirlFactory.createReimu();
        Bullet bullet = reimu.createBullet();

        float expectedLifetime = reimu.getRange() / (12f * 20f); // 500 / 240 = 2.0833333s
        assertEquals(expectedLifetime, bullet.getMaxLifetimeSeconds(), "Reimu bullet lifetime should match range / (12 * speed)");

        Girl yukari = GirlFactory.createYukari();
        Bullet yukariBullet = yukari.createBullet();
        float expectedYukariLifetime = yukari.getRange() / (12f * 100f); // 600 / 1200 = 0.5s
        assertEquals(expectedYukariLifetime, yukariBullet.getMaxLifetimeSeconds(), "Yukari bullet lifetime should match range / (12 * speed)");
    }

    private static void testGirlRangePreserved() {
        Girl reimu = GirlFactory.createReimu();
        assertEquals(500f, reimu.getRange(), "Girl.getRange() must be preserved for UI/targeting");

        Girl yukari = GirlFactory.createYukari();
        assertEquals(600f, yukari.getRange(), "Girl.getRange() must be preserved for UI/targeting");
    }

    private static void testSpellCardExplicitLifetime() {
        SpellCard reimuSpellCard = SpellCard.createReimuSpellCard();
        List<Bullet> reimuBullets = reimuSpellCard.getBulletsToCreateAndIncrementFrame();
        assertNotNull(reimuBullets, "Reimu spell card frame 0 bullets should not be null");
        assertFalse(reimuBullets.isEmpty(), "Reimu spell card frame 0 bullets should not be empty");

        for (Bullet b : reimuBullets) {
            assertEquals(6.0f, b.getMaxLifetimeSeconds(), "Reimu spell card bullet should have explicit maxLifetimeSeconds = 6.0");
        }

        SpellCard yuyukoSpellCard = SpellCard.createYuyukoSpellCard();
        List<Bullet> yuyukoBullets = yuyukoSpellCard.getBulletsToCreateAndIncrementFrame();
        assertNotNull(yuyukoBullets, "Yuyuko spell card frame 0 bullets should not be null");
        assertFalse(yuyukoBullets.isEmpty(), "Yuyuko spell card frame 0 bullets should not be empty");

        for (Bullet b : yuyukoBullets) {
            assertEquals(8.0f, b.getMaxLifetimeSeconds(), "Yuyuko spell card bullet should have explicit maxLifetimeSeconds = 8.0");
        }
    }

    private static void testFrameRateIndependence() {
        float maxLifetime = 2.0f;

        // 60 FPS: 120 steps = ~1.999999f (not expired), step 121 = 2.016s (expired)
        Bullet bullet60 = new Bullet(20f, 1, 1, maxLifetime, false, "red_spell_card.png");
        for (int i = 0; i < 120; i++) {
            bullet60.updateElapsedTime(1f / 60f);
        }
        assertFalse(bullet60.isExpired(), "Bullet before reaching 2.0s should not expire");
        bullet60.updateElapsedTime(1f / 60f);
        assertTrue(bullet60.isExpired(), "Bullet after 2.0s should expire");

        // 30 FPS: 60 steps = ~1.999999f (not expired), step 61 = 2.033s (expired)
        Bullet bullet30 = new Bullet(20f, 1, 1, maxLifetime, false, "red_spell_card.png");
        for (int i = 0; i < 60; i++) {
            bullet30.updateElapsedTime(1f / 30f);
        }
        assertFalse(bullet30.isExpired(), "Bullet before reaching 2.0s should not expire");
        bullet30.updateElapsedTime(1f / 30f);
        assertTrue(bullet30.isExpired(), "Bullet after 2.0s should expire");

        // Variable frame rates (e.g. frame drop spikes)
        Bullet bulletVar = new Bullet(20f, 1, 1, maxLifetime, false, "red_spell_card.png");
        bulletVar.updateElapsedTime(0.016f);
        bulletVar.updateElapsedTime(0.033f);
        bulletVar.updateElapsedTime(0.050f);
        bulletVar.updateElapsedTime(0.100f);
        assertFalse(bulletVar.isExpired(), "Bullet should not expire until total elapsed time >= 2.0s");

        bulletVar.updateElapsedTime(1.801f);
        assertTrue(bulletVar.isExpired(), "Bullet should expire once total elapsed time >= 2.0s");
    }

    private static void assertEquals(float expected, float actual, String message) {
        if (Math.abs(expected - actual) > 0.001f) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }
}
