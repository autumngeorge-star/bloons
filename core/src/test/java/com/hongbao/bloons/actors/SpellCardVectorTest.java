package com.hongbao.bloons.actors;

import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;

import java.util.List;

public class SpellCardVectorTest {

    public static void main(String[] args) {
        System.out.println("Running SpellCardVectorTest...");
        testReimuSpellCardVectors();
        testYuyukoSpellCardVectors();
        testRotationMatrixMath();
        System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
    }

    public static void testReimuSpellCardVectors() {
        SpellCard reimuCard = SpellCard.createReimuSpellCard();
        List<Bullet> frame0Bullets = reimuCard.getBulletsToCreateAndIncrementFrame();
        if (frame0Bullets == null || frame0Bullets.size() != 6) {
            throw new AssertionError("Reimu spell card frame 0 should spawn 6 bullets, found: " +
                    (frame0Bullets == null ? "null" : frame0Bullets.size()));
        }

        // Bullet 1: Straight forward local (0, 1)
        Bullet b1 = frame0Bullets.get(0);
        assertClose(0f, b1.getInitialDXOverride(), "Reimu b1 local DX");
        assertClose(1f, b1.getInitialDYOverride(), "Reimu b1 local DY");

        // Bullet 4: Straight backward local (0, -1)
        Bullet b4 = frame0Bullets.get(3);
        assertClose(0f, b4.getInitialDXOverride(), "Reimu b4 local DX");
        assertClose(-1f, b4.getInitialDYOverride(), "Reimu b4 local DY");

        System.out.println("PASSED: testReimuSpellCardVectors");
    }

    public static void testYuyukoSpellCardVectors() {
        SpellCard yuyukoCard = SpellCard.createYuyukoSpellCard();
        List<Bullet> frame0Bullets = yuyukoCard.getBulletsToCreateAndIncrementFrame();
        if (frame0Bullets == null || frame0Bullets.isEmpty()) {
            throw new AssertionError("Yuyuko spell card frame 0 should spawn bullets");
        }

        // First bullet (i=0, left fan offset -125)
        Bullet b0 = frame0Bullets.get(0);
        if (b0.getInitialXOffset() != -125f) {
            throw new AssertionError("Expected Yuyuko left fan local X offset -125, got: " + b0.getInitialXOffset());
        }
        // At i=0, desiredAngle = Math.PI/2, so (cos(PI/2), sin(PI/2)) -> (0, 1) local
        assertClose(0f, b0.getInitialDXOverride(), "Yuyuko b0 local DX");
        assertClose(1f, b0.getInitialDYOverride(), "Yuyuko b0 local DY");

        System.out.println("PASSED: testYuyukoSpellCardVectors");
    }

    public static void testRotationMatrixMath() {
        // Test North (0 deg)
        verifyTransformation(0f, 0f, 1f, 0f, 1f); // (0,1) -> (0,1)
        verifyTransformation(0f, -125f, 0f, -125f, 0f); // offset (-125,0) -> (-125,0)

        // Test East (90 deg)
        verifyTransformation(90f, 0f, 1f, 1f, 0f); // (0,1) forward -> (1,0) East
        verifyTransformation(90f, -125f, 0f, 0f, 125f); // offset (-125,0) -> (0, 125) North/Up relative to world

        // Test South (180 deg)
        verifyTransformation(180f, 0f, 1f, 0f, -1f); // (0,1) forward -> (0,-1) South
        verifyTransformation(180f, -125f, 0f, 125f, 0f); // offset (-125,0) -> (125, 0) East relative to world

        // Test West (-90 or 270 deg)
        verifyTransformation(270f, 0f, 1f, -1f, 0f); // (0,1) forward -> (-1,0) West
        verifyTransformation(270f, -125f, 0f, 0f, -125f); // offset (-125,0) -> (0,-125) South relative to world

        // Test Diagonal (45 deg)
        float sqrt2_2 = (float) (Math.sqrt(2) / 2.0);
        verifyTransformation(45f, 0f, 1f, sqrt2_2, sqrt2_2);

        System.out.println("PASSED: testRotationMatrixMath");
    }

    private static void verifyTransformation(float rotationAngle, float localX, float localY, float expectedWorldX, float expectedWorldY) {
        double rad = Math.toRadians(rotationAngle);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float worldX = localX * cos + localY * sin;
        float worldY = -localX * sin + localY * cos;

        assertClose(expectedWorldX, worldX, "worldX for angle " + rotationAngle);
        assertClose(expectedWorldY, worldY, "worldY for angle " + rotationAngle);
    }

    private static void assertClose(float expected, float actual, String label) {
        if (Math.abs(expected - actual) > 1e-4) {
            throw new AssertionError(label + " failed: expected " + expected + ", but got " + actual);
        }
    }
}
