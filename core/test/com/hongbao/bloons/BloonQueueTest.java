package com.hongbao.bloons;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class BloonQueueTest {

    @Test
    public void testInitialLevel() {
        BloonQueue queue = new BloonQueue(new ArrayList<>(), new ArrayList<>());
        Assert.assertEquals(0, queue.getLevel());
    }

    @Test
    public void testNextLevel() {
        BloonQueue queue = new BloonQueue(new ArrayList<>(), new ArrayList<>());
        queue.nextLevel();
        Assert.assertEquals(1, queue.getLevel());
    }
}
