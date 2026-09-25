package com.hongbao.bloons.entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class BulletTest {

    @Test
    public void testDistanceExpiration() {
        // Bullet with maxRange 100 and maxDuration 10 seconds
        Bullet bullet = new Bullet(20f, 1, 1, 100f, 10.0f, false, "red_spell_card.png");
        assertFalse(bullet.isExpired());

        // Delta 1/60s advances distance by speed / 5 = 4
        boolean expired = bullet.updateAndCheckExpired(1f / 60f);
        assertFalse(expired);
        assertEquals(4.0f, bullet.getDistanceTraveled(), 0.001f);

        // Advance 25 frames (total distance = 25 * 4 = 100)
        for (int i = 0; i < 24; i++) {
            expired = bullet.updateAndCheckExpired(1f / 60f);
        }
        assertTrue(expired);
        assertTrue(bullet.isExpired());
    }

    @Test
    public void testDurationExpirationStationary() {
        // Stationary bullet (speed 0) with maxDuration 2.0 seconds and maxRange 500
        Bullet bullet = new Bullet(0f, 1, 1, 500f, 2.0f, false, "red_spell_card.png");
        assertFalse(bullet.isExpired());

        // Advance by 1 second
        boolean expired = bullet.updateAndCheckExpired(1.0f);
        assertFalse(expired);
        assertEquals(1.0f, bullet.getDuration(), 0.001f);

        // Advance by another 1 second (total 2.0 seconds)
        expired = bullet.updateAndCheckExpired(1.0f);
        assertTrue(expired);
        assertTrue(bullet.isExpired());
    }

    @Test
    public void testDurationExpirationFirst() {
        // Bullet with slow speed, high max range (5000) but short duration (1.0 second)
        Bullet bullet = new Bullet(5f, 1, 1, 5000f, 1.0f, false, "pink_butterfly.png");
        assertFalse(bullet.isExpired());

        // Advance by 0.5s
        assertFalse(bullet.updateAndCheckExpired(0.5f));

        // Advance by 0.5s (total 1.0s) -> duration limit reached
        assertTrue(bullet.updateAndCheckExpired(0.5f));
        assertTrue(bullet.getDistanceTraveled() < bullet.getMaxRange());
    }

    @Test
    public void testGirlBulletDuration() {
        Girl girl = new Girl(
                "TestGirl",
                java.util.Arrays.asList(10),
                java.util.Arrays.asList(20f),
                java.util.Arrays.asList(1),
                java.util.Arrays.asList(1),
                java.util.Arrays.asList(500f),
                java.util.Arrays.asList(200f),
                java.util.Arrays.asList(false),
                java.util.Arrays.asList(5.0f),
                "reimu.png",
                "red_spell_card.png",
                100,
                java.util.Arrays.asList(-1)
        );

        Bullet bullet = girl.createBullet();
        assertEquals(5.0f, bullet.getMaxDuration(), 0.001f);
    }

    @Test
    public void testSpellCardBulletsDuration() {
        SpellCard reimuCard = SpellCard.createReimuSpellCard();
        java.util.List<Bullet> bullets = reimuCard.getBulletsToCreateAndIncrementFrame();
        assertNotNull(bullets);
        assertFalse(bullets.isEmpty());
        for (Bullet bullet : bullets) {
            assertEquals(10.0f, bullet.getMaxDuration(), 0.001f);
        }

        SpellCard yuyukoCard = SpellCard.createYuyukoSpellCard();
        bullets = yuyukoCard.getBulletsToCreateAndIncrementFrame();
        assertNotNull(bullets);
        assertFalse(bullets.isEmpty());
        for (Bullet bullet : bullets) {
            assertEquals(10.0f, bullet.getMaxDuration(), 0.001f);
        }
    }
}
