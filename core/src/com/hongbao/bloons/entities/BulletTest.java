package com.hongbao.bloons.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BulletTest {

    public static void main(String[] args) {
        System.out.println("Running Bullet Virtual Distance Tests...");

        testStationaryBulletVirtualDistanceAccumulation();
        testStationaryBulletExceedsMaxRange();
        testMovingBulletSpeedBasedDistanceAccumulation();
        testEntitySchemaUnchanged();

        System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
    }

    public static void testStationaryBulletVirtualDistanceAccumulation() {
        Bullet staticBullet = new Bullet(0f, 10, 5, 10f, false, "red_spell_card.png");
        assertValue("Initial distanceTraveled for static bullet", staticBullet.getDistanceTraveled(), 0f);

        for (int i = 1; i <= 5; i++) {
            staticBullet.incrementDistanceTraveled();
            assertValue("distanceTraveled at step " + i, staticBullet.getDistanceTraveled(), (float) i);
        }

        boolean reachedMax = staticBullet.getDistanceTraveled() >= staticBullet.getMaxRange();
        if (reachedMax) {
            throw new RuntimeException("Static bullet should not have reached maxRange at 5 updates");
        }

        for (int i = 6; i <= 10; i++) {
            staticBullet.incrementDistanceTraveled();
        }
        assertValue("distanceTraveled at step 10", staticBullet.getDistanceTraveled(), 10f);

        boolean reachedMaxAt10 = staticBullet.getDistanceTraveled() >= staticBullet.getMaxRange();
        if (!reachedMaxAt10) {
            throw new RuntimeException("Static bullet should have reached maxRange at 10 updates");
        }
        System.out.println("PASSED: testStationaryBulletVirtualDistanceAccumulation");
    }

    public static void testStationaryBulletExceedsMaxRange() {
        Bullet staticBullet = new Bullet(0f, 5, 1, 5f, false, "red_spell_card.png");
        for (int i = 0; i < 6; i++) {
            staticBullet.incrementDistanceTraveled();
        }
        assertValue("distanceTraveled after 6 steps", staticBullet.getDistanceTraveled(), 6f);
        if (!(staticBullet.getDistanceTraveled() >= staticBullet.getMaxRange())) {
            throw new RuntimeException("Static bullet at 6f distance should exceed maxRange 5f");
        }
        System.out.println("PASSED: testStationaryBulletExceedsMaxRange");
    }

    public static void testMovingBulletSpeedBasedDistanceAccumulation() {
        Bullet movingBullet = new Bullet(20f, 2, 2, 100f, false, "red_spell_card.png");
        assertValue("Initial distanceTraveled for moving bullet", movingBullet.getDistanceTraveled(), 0f);

        movingBullet.incrementDistanceTraveled();
        assertValue("distanceTraveled after 1 update (20 speed / 5)", movingBullet.getDistanceTraveled(), 4f);

        for (int i = 0; i < 24; i++) {
            movingBullet.incrementDistanceTraveled();
        }
        assertValue("distanceTraveled after 25 updates", movingBullet.getDistanceTraveled(), 100f);
        if (!(movingBullet.getDistanceTraveled() >= movingBullet.getMaxRange())) {
            throw new RuntimeException("Moving bullet at 100f distance should reach maxRange 100f");
        }
        System.out.println("PASSED: testMovingBulletSpeedBasedDistanceAccumulation");
    }

    public static void testEntitySchemaUnchanged() {
        Field[] fields = Bullet.class.getDeclaredFields();
        int instanceFieldCount = 0;
        for (Field f : fields) {
            if (!Modifier.isStatic(f.getModifiers())) {
                instanceFieldCount++;
            }
        }
        if (instanceFieldCount != 11) {
            throw new RuntimeException("Expected 11 instance fields in Bullet, found: " + instanceFieldCount);
        }

        Constructor<?>[] constructors = Bullet.class.getDeclaredConstructors();
        if (constructors.length != 2) {
            throw new RuntimeException("Expected 2 constructors in Bullet, found: " + constructors.length);
        }
        System.out.println("PASSED: testEntitySchemaUnchanged");
    }

    private static void assertValue(String message, float actual, float expected) {
        if (Math.abs(actual - expected) > 1e-5f) {
            throw new RuntimeException(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }
}
