package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import static org.junit.Assert.*;

public class BloonInheritanceTest {

    @Test
    public void testCreateChildFromParentInheritsTraits() {
        Bloon parentCamoRegen = new Bloon(Bloon.Color.CERAMIC, 18, true, true);
        Bloon child = BloonFactory.createChildFromParent(parentCamoRegen, Bloon.Color.RAINBOW, 8);

        assertEquals(Bloon.Color.RAINBOW, child.getColor());
        assertEquals(8, child.getHealth());
        assertTrue(child.isCamo());
        assertTrue(child.isRegen());
        assertEquals("img/bloons/rainbow_camo_regrowth_bloon.png", child.getImageFileName());
    }

    @Test
    public void testCreateChildFromParentNullParent() {
        Bloon child = BloonFactory.createChildFromParent(null, Bloon.Color.BLUE, 2);

        assertEquals(Bloon.Color.BLUE, child.getColor());
        assertEquals(2, child.getHealth());
        assertFalse(child.isCamo());
        assertFalse(child.isRegen());
        assertEquals("img/bloons/blue_bloon.png", child.getImageFileName());
    }

    @Test
    public void testBloonPoppedResultPreservesTraits() {
        // Ceramic camo regrowth popped down 1 layer (1 damage)
        Bloon ceramicCamoRegen = new Bloon(Bloon.Color.CERAMIC, 18, true, true);
        BloonPoppedResult result = ceramicCamoRegen.pop(1);

        assertNotNull(result.getBloonsGenerated());
        assertFalse(result.getBloonsGenerated().isEmpty());

        for (Bloon child : result.getBloonsGenerated()) {
            assertTrue("Child bloon must inherit camo flag", child.isCamo());
            assertTrue("Child bloon must inherit regen flag", child.isRegen());
            assertTrue("Image path must contain camo and regrowth denotation",
                    child.getImageFileName().contains("_camo_regrowth"));
        }
    }

    @Test
    public void testCreateBloonOfTypeParsingAcrossAllColors() {
        for (Bloon.Color color : Bloon.Color.values()) {
            String colorName = color.getValue();

            // Test plain
            Bloon plain = BloonFactory.createBloonOfType(colorName);
            assertEquals(color, plain.getColor());
            assertFalse(plain.isCamo());
            assertFalse(plain.isRegen());

            // Test camo suffix
            Bloon camo = BloonFactory.createBloonOfType(colorName + "_camo");
            assertEquals(color, camo.getColor());
            assertTrue(camo.isCamo());
            assertFalse(camo.isRegen());

            // Test regen suffix
            Bloon regen = BloonFactory.createBloonOfType(colorName + "_regen");
            assertEquals(color, regen.getColor());
            assertFalse(regen.isCamo());
            assertTrue(regen.isRegen());

            // Test regrowth suffix
            Bloon regrowth = BloonFactory.createBloonOfType(colorName + "_regrowth");
            assertEquals(color, regrowth.getColor());
            assertFalse(regrowth.isCamo());
            assertTrue(regrowth.isRegen());

            // Test camo_regen suffix
            Bloon camoRegen = BloonFactory.createBloonOfType(colorName + "_camo_regrowth");
            assertEquals(color, camoRegen.getColor());
            assertTrue(camoRegen.isCamo());
            assertTrue(camoRegen.isRegen());
        }
    }

    @Test
    public void testCopyModifiersFrom() {
        Bloon parent = new Bloon(Bloon.Color.LEAD, 7, true, true);
        Bloon child = new Bloon(Bloon.Color.BLACK, 6, false, false);

        child.copyModifiersFrom(parent);

        assertTrue(child.isCamo());
        assertTrue(child.isRegen());
        assertEquals("img/bloons/black_camo_regrowth_bloon.png", child.getImageFileName());
    }
}
