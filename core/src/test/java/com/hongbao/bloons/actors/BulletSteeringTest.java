package com.hongbao.bloons.actors;

import com.hongbao.bloons.helpers.Pair;

public class BulletSteeringTest {

    public static void main(String[] args) {
        testComputeAlpha();
        testBlendVectorsNormal();
        testBlendVectorsUnitLength();
        testBlendVectorsZeroLengthSafety();
        testBlendVectorsClamping();
        System.out.println("ALL BULLET STEERING TESTS PASSED SUCCESSFULLY!");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Test failed: " + message);
        }
    }

    private static void assertEquals(float expected, float actual, float epsilon, String message) {
        if (Math.abs(expected - actual) > epsilon) {
            throw new AssertionError("Test failed: " + message + " (expected: " + expected + ", actual: " + actual + ")");
        }
    }

    public static void testComputeAlpha() {
        // Far away and frame 0 => alpha should be base alpha (0.4)
        float alphaFar = BulletActor.computeAlpha(600.0f, 0);
        assertEquals(0.4f, alphaFar, 0.001f, "Alpha far away at frame 0 should be 0.4");

        // Distance 0 => alpha should be 1.0
        float alphaClose = BulletActor.computeAlpha(0.0f, 0);
        assertEquals(1.0f, alphaClose, 0.001f, "Alpha at distance 0 should be 1.0");

        // Frame 200 => alpha should be 1.0
        float alphaLateFrame = BulletActor.computeAlpha(500.0f, 200);
        assertEquals(1.0f, alphaLateFrame, 0.001f, "Alpha at frame 200 should be 1.0");

        // Distance 150 (halfway) => progress = 0.5 => alpha = 0.4 + 0.6*0.5 = 0.7
        float alphaMid = BulletActor.computeAlpha(150.0f, 0);
        assertEquals(0.7f, alphaMid, 0.001f, "Alpha at mid distance should be 0.7");
    }

    public static void testBlendVectorsNormal() {
        // Pattern = (1, 0), Homing = (0, 1), alpha = 0.5
        Pair<Float, Float> blended = BulletActor.blendVectors(1.0f, 0.0f, 0.0f, 1.0f, 0.5f);
        float expectedX = (float) (1.0 / Math.sqrt(2));
        float expectedY = (float) (1.0 / Math.sqrt(2));
        assertEquals(expectedX, blended.getFirst(), 0.001f, "Blended X component");
        assertEquals(expectedY, blended.getSecond(), 0.001f, "Blended Y component");
    }

    public static void testBlendVectorsUnitLength() {
        // Test various vector combinations and verify result is always unit length (length == 1.0)
        Pair<Float, Float> v1 = BulletActor.blendVectors(0.6f, 0.8f, -0.8f, 0.6f, 0.3f);
        float len1 = (float) Math.sqrt(v1.getFirst() * v1.getFirst() + v1.getSecond() * v1.getSecond());
        assertEquals(1.0f, len1, 0.001f, "Blended vector 1 must be unit vector");

        Pair<Float, Float> v2 = BulletActor.blendVectors(-0.5f, 0.866f, 1.0f, 0.0f, 0.8f);
        float len2 = (float) Math.sqrt(v2.getFirst() * v2.getFirst() + v2.getSecond() * v2.getSecond());
        assertEquals(1.0f, len2, 0.001f, "Blended vector 2 must be unit vector");
    }

    public static void testBlendVectorsZeroLengthSafety() {
        // Exact opposing vectors with alpha = 0.5
        // Pattern = (1, 0), Homing = (-1, 0), alpha = 0.5 => (0, 0)
        Pair<Float, Float> blended = BulletActor.blendVectors(1.0f, 0.0f, -1.0f, 0.0f, 0.5f);
        assertTrue(blended != null, "Blended result must not be null");
        float len = (float) Math.sqrt(blended.getFirst() * blended.getFirst() + blended.getSecond() * blended.getSecond());
        assertEquals(1.0f, len, 0.001f, "Zero-length fallback must produce unit length vector");

        // Zero pattern and zero homing
        Pair<Float, Float> zeroBoth = BulletActor.blendVectors(0.0f, 0.0f, 0.0f, 0.0f, 0.5f);
        assertTrue(zeroBoth != null, "Zero input result must not be null");
    }

    public static void testBlendVectorsClamping() {
        // Alpha < 0.0 should be clamped to 0.0 (100% pattern)
        Pair<Float, Float> under = BulletActor.blendVectors(1.0f, 0.0f, 0.0f, 1.0f, -0.5f);
        assertEquals(1.0f, under.getFirst(), 0.001f, "Alpha underflow clamped to 0.0");
        assertEquals(0.0f, under.getSecond(), 0.001f, "Alpha underflow clamped to 0.0");

        // Alpha > 1.0 should be clamped to 1.0 (100% homing)
        Pair<Float, Float> over = BulletActor.blendVectors(1.0f, 0.0f, 0.0f, 1.0f, 2.5f);
        assertEquals(0.0f, over.getFirst(), 0.001f, "Alpha overflow clamped to 1.0");
        assertEquals(1.0f, over.getSecond(), 0.001f, "Alpha overflow clamped to 1.0");
    }
}
