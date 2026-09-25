package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BloonTest {

    @Test
    @DisplayName("Bloon initialization sets correct properties")
    void testBloonInitialization() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertEquals(Bloon.Color.RED, bloon.getColor());
        assertEquals(1, bloon.getHealth());
        assertFalse(bloon.isCamo());
        assertFalse(bloon.isRegen());
        assertEquals("img/bloons/red_bloon.png", bloon.getImageFileName());
    }

    @Test
    @DisplayName("Bloon camo and regen filename generation")
    void testCamoAndRegenFileName() {
        Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, true, true);
        assertEquals("img/bloons/blue_camo_regrowth_bloon.png", bloon.getImageFileName());
    }

    @Nested
    @DisplayName("Health to Color Mapping")
    class HealthToColorTests {
        @Test
        void testGetColorFromHealth() {
            assertEquals(Bloon.Color.RED, Bloon.getColorFromHealth(1));
            assertEquals(Bloon.Color.BLUE, Bloon.getColorFromHealth(2));
            assertEquals(Bloon.Color.GREEN, Bloon.getColorFromHealth(3));
            assertEquals(Bloon.Color.CERAMIC, Bloon.getColorFromHealth(15));
            assertEquals(Bloon.Color.MOAB, Bloon.getColorFromHealth(100));
            assertEquals(Bloon.Color.BFB, Bloon.getColorFromHealth(500));
            assertEquals(Bloon.Color.ZOMG, Bloon.getColorFromHealth(1000));
        }
    }

    @Test
    @DisplayName("Bloon popping mechanics")
    void testPopBloon() {
        Bloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false);
        assertTrue(bloon.willPopBloon(3));

        BloonPoppedResult result = bloon.pop(1);
        assertNotNull(result);
    }
}
