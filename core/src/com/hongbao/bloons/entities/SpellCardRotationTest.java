package com.hongbao.bloons.entities;

import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.factories.GirlFactory;

import java.util.List;

public class SpellCardRotationTest {

    public static void main(String[] args) {
        System.out.println("Running SpellCardRotationTest...");

        com.badlogic.gdx.Gdx.files = new com.badlogic.gdx.backends.headless.HeadlessFiles();

        testReimuRotation();
        testYuyukoRotation();
        testGirlCreateSpellCard();
        testGirlActorAndSpellCardActorIntegration();

        System.out.println("ALL TESTS PASSED!");
        System.exit(0);
    }

    private static void assertEquals(float expected, float actual, float delta, String message) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void testReimuRotation() {
        // Unrotated (0 degrees)
        SpellCard reimu0 = SpellCard.createReimuSpellCard(0f);
        List<Bullet> frame0Bullets0 = reimu0.getBulletsToCreateAndIncrementFrame();
        Bullet b1_0 = frame0Bullets0.get(0); // Originally dx=0, dy=1
        assertEquals(0f, b1_0.getInitialDXOverride(), 0.001f, "Reimu 0 deg bullet1 dx");
        assertEquals(1f, b1_0.getInitialDYOverride(), 0.001f, "Reimu 0 deg bullet1 dy");

        Bullet b4_0 = frame0Bullets0.get(3); // Originally dx=0, dy=-1
        assertEquals(0f, b4_0.getInitialDXOverride(), 0.001f, "Reimu 0 deg bullet4 dx");
        assertEquals(-1f, b4_0.getInitialDYOverride(), 0.001f, "Reimu 0 deg bullet4 dy");

        // Rotated 90 degrees clockwise (facing right)
        SpellCard reimu90 = SpellCard.createReimuSpellCard(90f);
        List<Bullet> frame0Bullets90 = reimu90.getBulletsToCreateAndIncrementFrame();
        Bullet b1_90 = frame0Bullets90.get(0); // Was dx=0, dy=1 -> Rotated 90 deg -> dx=1, dy=0
        assertEquals(1f, b1_90.getInitialDXOverride(), 0.001f, "Reimu 90 deg bullet1 dx");
        assertEquals(0f, b1_90.getInitialDYOverride(), 0.001f, "Reimu 90 deg bullet1 dy");

        Bullet b4_90 = frame0Bullets90.get(3); // Was dx=0, dy=-1 -> Rotated 90 deg -> dx=-1, dy=0
        assertEquals(-1f, b4_90.getInitialDXOverride(), 0.001f, "Reimu 90 deg bullet4 dx");
        assertEquals(0f, b4_90.getInitialDYOverride(), 0.001f, "Reimu 90 deg bullet4 dy");

        // Rotated 180 degrees clockwise (facing down)
        SpellCard reimu180 = SpellCard.createReimuSpellCard(180f);
        List<Bullet> frame0Bullets180 = reimu180.getBulletsToCreateAndIncrementFrame();
        Bullet b1_180 = frame0Bullets180.get(0); // Was dx=0, dy=1 -> Rotated 180 deg -> dx=0, dy=-1
        assertEquals(0f, b1_180.getInitialDXOverride(), 0.001f, "Reimu 180 deg bullet1 dx");
        assertEquals(-1f, b1_180.getInitialDYOverride(), 0.001f, "Reimu 180 deg bullet1 dy");
        
        System.out.println("testReimuRotation passed.");
    }

    public static void testYuyukoRotation() {
        // Unrotated (0 degrees)
        SpellCard yuyuko0 = SpellCard.createYuyukoSpellCard(0f);
        List<Bullet> frame0Bullets0 = yuyuko0.getBulletsToCreateAndIncrementFrame();
        // First group in frame 0 has initialXOffset = -125, initialYOffset = 0
        Bullet b0_left = frame0Bullets0.get(0);
        assertEquals(-125f, b0_left.getInitialXOffset(), 0.001f, "Yuyuko 0 deg offset left x");
        assertEquals(0f, b0_left.getInitialYOffset(), 0.001f, "Yuyuko 0 deg offset left y");

        // Second group in frame 0 has initialXOffset = 125, initialYOffset = 0
        Bullet b0_right = frame0Bullets0.get(4);
        assertEquals(125f, b0_right.getInitialXOffset(), 0.001f, "Yuyuko 0 deg offset right x");
        assertEquals(0f, b0_right.getInitialYOffset(), 0.001f, "Yuyuko 0 deg offset right y");

        // Rotated 90 degrees clockwise (facing right)
        SpellCard yuyuko90 = SpellCard.createYuyukoSpellCard(90f);
        List<Bullet> frame0Bullets90 = yuyuko90.getBulletsToCreateAndIncrementFrame();
        // Offset (-125, 0) rotated 90 deg -> (0, 125)
        Bullet b90_left = frame0Bullets90.get(0);
        assertEquals(0f, b90_left.getInitialXOffset(), 0.001f, "Yuyuko 90 deg offset left x");
        assertEquals(125f, b90_left.getInitialYOffset(), 0.001f, "Yuyuko 90 deg offset left y");

        // Offset (125, 0) rotated 90 deg -> (0, -125)
        Bullet b90_right = frame0Bullets90.get(4);
        assertEquals(0f, b90_right.getInitialXOffset(), 0.001f, "Yuyuko 90 deg offset right x");
        assertEquals(-125f, b90_right.getInitialYOffset(), 0.001f, "Yuyuko 90 deg offset right y");

        // Verify velocity vector rotation at 90 deg
        float unrotatedDX = b0_left.getInitialDXOverride();
        float unrotatedDY = b0_left.getInitialDYOverride();
        float expectedRotatedDX = unrotatedDY;
        float expectedRotatedDY = -unrotatedDX;
        assertEquals(expectedRotatedDX, b90_left.getInitialDXOverride(), 0.001f, "Yuyuko 90 deg rotated dx");
        assertEquals(expectedRotatedDY, b90_left.getInitialDYOverride(), 0.001f, "Yuyuko 90 deg rotated dy");

        System.out.println("testYuyukoRotation passed.");
    }

    public static void testGirlCreateSpellCard() {
        Girl reimu = GirlFactory.createReimu();
        SpellCard spellCardReimu = reimu.createSpellCard(90f);
        assertTrue(spellCardReimu != null, "Reimu spellcard created");
        assertEquals("Reimu", spellCardReimu.getOverrideName(), "Reimu name");

        Girl yuyuko = GirlFactory.createYuyuko();
        SpellCard spellCardYuyuko = yuyuko.createSpellCard(180f);
        assertTrue(spellCardYuyuko != null, "Yuyuko spellcard created");
        assertEquals("Yuyuko", spellCardYuyuko.getOverrideName(), "Yuyuko name");

        System.out.println("testGirlCreateSpellCard passed.");
    }

    public static void testGirlActorAndSpellCardActorIntegration() {
        try {
            Girl reimu = GirlFactory.createReimu();
            GirlActor girlActor = new GirlActor(reimu, 100f, 100f);
            girlActor.setRotationAngle(45f);

            SpellCardActor actor = girlActor.createSpellCardActor();
            assertTrue(actor != null, "SpellCardActor created");
            assertEquals(45f, actor.getRotationAngle(), 0.001f, "SpellCardActor rotationAngle");

            System.out.println("testGirlActorAndSpellCardActorIntegration passed.");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("testGirlActorAndSpellCardActorIntegration skipped texture loading in headless env.");
        }
    }
}
