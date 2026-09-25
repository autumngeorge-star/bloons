package com.hongbao.bloons.entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class BulletTest {

    @Test
    public void testDefaultBulletLifetimeBackwardsCompatibility() {
        Bullet bullet = new Bullet();
        assertEquals(Float.MAX_VALUE, bullet.getMaxLifetime(), 0.001f);
        assertEquals(Float.MAX_VALUE, bullet.getMaxLifetimeDuration(), 0.001f);
        assertEquals(0.0f, bullet.getElapsedTime(), 0.001f);
        assertFalse(bullet.isLifetimeExpired());
    }

    @Test
    public void testStaticBulletLifetimeExpiration() {
        // Static bullet with zero speed and 2.5 second max lifetime
        Bullet bullet = new Bullet(0.0f, 10, 5, 1000.0f, 2.5f, false, "red_spell_card.png");
        assertEquals(0.0f, bullet.getSpeed(), 0.001f);
        assertEquals(2.5f, bullet.getMaxLifetime(), 0.001f);

        // Accumulate 1.0 second
        bullet.incrementElapsedTime(1.0f);
        assertEquals(1.0f, bullet.getElapsedTime(), 0.001f);
        assertFalse(bullet.isLifetimeExpired());

        // Accumulate another 1.0 second (total 2.0s)
        bullet.incrementElapsedTime(1.0f);
        assertEquals(2.0f, bullet.getElapsedTime(), 0.001f);
        assertFalse(bullet.isLifetimeExpired());

        // Accumulate 0.5 seconds (total 2.5s == maxLifetime)
        bullet.incrementElapsedTime(0.5f);
        assertEquals(2.5f, bullet.getElapsedTime(), 0.001f);
        assertTrue(bullet.isLifetimeExpired());
    }

    @Test
    public void testVariableFrameRateDeltaAccumulation() {
        Bullet bullet = new Bullet(0.0f, 5, 2, 500.0f, 1.0f, false, "red_spell_card.png");

        // Simulate variable delta frame times (e.g. 60fps, 30fps, 120fps)
        bullet.incrementElapsedTime(0.016f); // frame 1
        bullet.incrementElapsedTime(0.033f); // frame 2
        bullet.incrementElapsedTime(0.008f); // frame 3
        assertEquals(0.057f, bullet.getElapsedTime(), 0.0001f);
        assertFalse(bullet.isLifetimeExpired());

        bullet.incrementElapsedTime(0.950f); // total 1.007s
        assertTrue(bullet.isLifetimeExpired());
    }

    @Test
    public void testDistanceTraveledStaticBulletStaysZero() {
        Bullet bullet = new Bullet(0.0f, 10, 5, 1000.0f, 2.0f, false, "red_spell_card.png");
        bullet.incrementDistanceTraveled();
        bullet.incrementDistanceTraveled();
        assertEquals(0.0f, bullet.getDistanceTraveled(), 0.001f);
        
        // Even though distance traveled is 0, accumulating time expires the bullet
        bullet.incrementElapsedTime(2.0f);
        assertTrue(bullet.isLifetimeExpired());
    }

    @Test
    public void testMovingBulletLifetimeExpirationBeforeMaxRange() {
        // Bullet with high max range (10000) but short max lifetime (0.5s)
        Bullet bullet = new Bullet(20.0f, 2, 2, 10000.0f, 0.5f, false, "red_spell_card.png");
        
        bullet.incrementDistanceTraveled(); // distanceTraveled = 4.0
        bullet.incrementElapsedTime(0.6f);  // elapsedTime = 0.6s
        
        assertTrue(bullet.getDistanceTraveled() < bullet.getMaxRange());
        assertTrue(bullet.isLifetimeExpired());
    }

    @Test
    public void testMovingBulletMaxRangeBeforeLifetimeExpiration() {
        // Bullet with max range 10 and default infinite lifetime
        Bullet bullet = new Bullet(50.0f, 2, 2, 10.0f, false, "red_spell_card.png");
        
        bullet.incrementDistanceTraveled(); // distanceTraveled = 10.0
        bullet.incrementElapsedTime(0.1f);  // elapsedTime = 0.1s
        
        assertTrue(bullet.getDistanceTraveled() >= bullet.getMaxRange());
        assertFalse(bullet.isLifetimeExpired());
    }

    @Test
    public void testSettersAndGetters() {
        Bullet bullet = new Bullet();
        bullet.setMaxLifetime(3.5f);
        assertEquals(3.5f, bullet.getMaxLifetime(), 0.001f);
        assertEquals(3.5f, bullet.getMaxLifetimeDuration(), 0.001f);

        bullet.setMaxLifetimeDuration(4.2f);
        assertEquals(4.2f, bullet.getMaxLifetime(), 0.001f);
        assertEquals(4.2f, bullet.getMaxLifetimeDuration(), 0.001f);

        bullet.setElapsedTime(1.2f);
        assertEquals(1.2f, bullet.getElapsedTime(), 0.001f);
    }
}
