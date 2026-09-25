package com.hongbao.bloons.helpers;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;

import static org.junit.Assert.*;

public class BloonPoppedResultTest {

    @Test
    public void testGetTotalHealthOfBloon() {
        Bloon redBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        assertEquals(1, BloonPoppedResult.getTotalHealthOfBloon(redBloon));

        Bloon blackBloon = new Bloon(Bloon.Color.BLACK, 6, false, false);
        assertEquals(11, BloonPoppedResult.getTotalHealthOfBloon(blackBloon));

        Bloon leadBloon = new Bloon(Bloon.Color.LEAD, 7, false, false);
        assertEquals(23, BloonPoppedResult.getTotalHealthOfBloon(leadBloon));

        Bloon rainbowBloon = new Bloon(Bloon.Color.RAINBOW, 8, false, false);
        assertEquals(47, BloonPoppedResult.getTotalHealthOfBloon(rainbowBloon));
    }
}
