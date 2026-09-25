package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;

import java.util.Set;

public class BloonAttributeTest {

    public static void main(String[] args) {
        System.out.println("Starting Bloon Attribute Tests...");

        testBloonFactoryExplicitParameters();
        testBloonFactorySuffixParsingAllColors();
        testBloonPoppedResultCamoInheritance();
        testBloonPoppedResultRegenInheritance();
        testBloonPoppedResultCamoAndRegenInheritance();
        testBloonPoppedResultBaseBloonNoFlags();
        testCashGenerationAndHealthDifference();

        System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Test failed: " + message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError("Test failed: " + message + " (expected: " + expected + ", actual: " + actual + ")");
    }

    private static void testBloonFactoryExplicitParameters() {
        System.out.println("Testing BloonFactory explicit parameter methods...");

        Bloon camoRegenGreen = BloonFactory.createBloonOfType("green", 3, true, true);
        assertEquals(Bloon.Color.GREEN, camoRegenGreen.getColor(), "Color should be GREEN");
        assertEquals(3, camoRegenGreen.getHealth(), "Health should be 3");
        assertTrue(camoRegenGreen.isCamo(), "Bloon should be camo");
        assertTrue(camoRegenGreen.isRegen(), "Bloon should be regen");

        Bloon normalYellow = BloonFactory.createBloonOfType("yellow", 4, false, false);
        assertEquals(Bloon.Color.YELLOW, normalYellow.getColor(), "Color should be YELLOW");
        assertEquals(4, normalYellow.getHealth(), "Health should be 4");
        assertTrue(!normalYellow.isCamo(), "Bloon should not be camo");
        assertTrue(!normalYellow.isRegen(), "Bloon should not be regen");

        Bloon camoLead = BloonFactory.createBloonOfType("lead", true, false);
        assertEquals(Bloon.Color.LEAD, camoLead.getColor(), "Color should be LEAD");
        assertTrue(camoLead.isCamo(), "Bloon should be camo");
        assertTrue(!camoLead.isRegen(), "Bloon should not be regen");
    }

    private static void testBloonFactorySuffixParsingAllColors() {
        System.out.println("Testing BloonFactory suffix parsing for all colors...");

        String[] colors = {
            "red", "blue", "green", "yellow", "pink",
            "black", "lead", "zebra", "rainbow", "ceramic",
            "moab", "bfb", "zomg"
        };

        for (String color : colors) {
            // Test _camo suffix
            Bloon camoBloon = BloonFactory.createBloonOfType(color + "_camo");
            assertTrue(camoBloon.isCamo(), color + "_camo should be camo");
            assertTrue(!camoBloon.isRegen(), color + "_camo should not be regen");

            // Test _regen suffix
            Bloon regenBloon = BloonFactory.createBloonOfType(color + "_regen");
            assertTrue(!regenBloon.isCamo(), color + "_regen should not be camo");
            assertTrue(regenBloon.isRegen(), color + "_regen should be regen");

            // Test _regrowth suffix
            Bloon regrowthBloon = BloonFactory.createBloonOfType(color + "_regrowth");
            assertTrue(!regrowthBloon.isCamo(), color + "_regrowth should not be camo");
            assertTrue(regrowthBloon.isRegen(), color + "_regrowth should be regen");

            // Test _camo_regen suffix
            Bloon camoRegenBloon = BloonFactory.createBloonOfType(color + "_camo_regen");
            assertTrue(camoRegenBloon.isCamo(), color + "_camo_regen should be camo");
            assertTrue(camoRegenBloon.isRegen(), color + "_camo_regen should be regen");

            // Test _regen_camo suffix
            Bloon regenCamoBloon = BloonFactory.createBloonOfType(color + "_regen_camo");
            assertTrue(regenCamoBloon.isCamo(), color + "_regen_camo should be camo");
            assertTrue(regenCamoBloon.isRegen(), color + "_regen_camo should be regen");
        }
    }

    private static void testBloonPoppedResultCamoInheritance() {
        System.out.println("Testing BloonPoppedResult camo inheritance...");

        Bloon greenCamo = BloonFactory.createBloonOfType("green", 3, true, false);
        BloonPoppedResult result = greenCamo.pop(1); // pops to blue

        Set<Bloon> children = result.getBloonsGenerated();
        assertTrue(!children.isEmpty(), "Should produce child bloons");

        for (Bloon child : children) {
            assertEquals(Bloon.Color.BLUE, child.getColor(), "Child should be blue");
            assertTrue(child.isCamo(), "Child bloon must inherit camo status");
            assertTrue(!child.isRegen(), "Child bloon must not inherit regen status");
        }
    }

    private static void testBloonPoppedResultRegenInheritance() {
        System.out.println("Testing BloonPoppedResult regen inheritance...");

        Bloon pinkRegen = BloonFactory.createBloonOfType("pink", 5, false, true);
        BloonPoppedResult result = pinkRegen.pop(1); // pops to yellow

        Set<Bloon> children = result.getBloonsGenerated();
        assertTrue(!children.isEmpty(), "Should produce child bloons");

        for (Bloon child : children) {
            assertEquals(Bloon.Color.YELLOW, child.getColor(), "Child should be yellow");
            assertTrue(!child.isCamo(), "Child bloon must not inherit camo status");
            assertTrue(child.isRegen(), "Child bloon must inherit regen status");
        }
    }

    private static void testBloonPoppedResultCamoAndRegenInheritance() {
        System.out.println("Testing BloonPoppedResult camo and regen inheritance...");

        Bloon ceramicCamoRegen = BloonFactory.createBloonOfType("ceramic", 18, true, true);
        BloonPoppedResult result = ceramicCamoRegen.pop(10); // pops to rainbow

        Set<Bloon> children = result.getBloonsGenerated();
        assertTrue(!children.isEmpty(), "Should produce child bloons");

        for (Bloon child : children) {
            assertTrue(child.isCamo(), "Child bloon must inherit camo status");
            assertTrue(child.isRegen(), "Child bloon must inherit regen status");
        }
    }

    private static void testBloonPoppedResultBaseBloonNoFlags() {
        System.out.println("Testing BloonPoppedResult base bloon without modifier flags...");

        Bloon greenNormal = BloonFactory.createBloonOfType("green", 3, false, false);
        BloonPoppedResult result = greenNormal.pop(1);

        Set<Bloon> children = result.getBloonsGenerated();
        assertTrue(!children.isEmpty(), "Should produce child bloons");

        for (Bloon child : children) {
            assertTrue(!child.isCamo(), "Normal parent should yield non-camo child");
            assertTrue(!child.isRegen(), "Normal parent should yield non-regen child");
        }
    }

    private static void testCashGenerationAndHealthDifference() {
        System.out.println("Testing cash generation formulas...");

        Bloon ceramic = BloonFactory.createBloonOfType("ceramic", 18, true, true);
        BloonPoppedResult result = ceramic.pop(1);

        // Health difference should match 1 damage
        assertEquals(1, result.getCashGenerated(), "Cash generated should match 1");
    }
}
