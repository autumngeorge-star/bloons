package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BloonQueueTest {

    @Test
    public void testSetLevelAndTotalLevels() {
        List<List<Bloon>> bloons = new ArrayList<>();
        List<List<Long>> intervals = new ArrayList<>();

        for (int i = 0; i <= 3; i++) {
            bloons.add(Collections.emptyList());
            intervals.add(Collections.emptyList());
        }

        BloonQueue queue = new BloonQueue(bloons, intervals);
        assertEquals(3, queue.getTotalLevels());
        assertEquals(0, queue.getLevel());

        queue.setLevel(2);
        assertEquals(2, queue.getLevel());

        queue.nextLevel();
        assertEquals(3, queue.getLevel());
    }
}
