package com.hongbao.bloons.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class PairTest {

    @Test
    public void testPairGetters() {
        Pair<String, Integer> pair = new Pair<>("Level", 1);
        assertEquals("Level", pair.getFirst());
        assertEquals(Integer.valueOf(1), pair.getSecond());
    }
}
