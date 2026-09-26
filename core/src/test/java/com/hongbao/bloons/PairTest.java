package com.hongbao.bloons;

import com.hongbao.bloons.helpers.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PairTest {

    @Test
    public void testPairGetters() {
        Pair<String, Integer> pair = new Pair<>("score", 100);
        assertEquals("score", pair.getFirst());
        assertEquals(Integer.valueOf(100), pair.getSecond());
    }

    @Test
    public void testPairWithNullValues() {
        Pair<String, String> pair = new Pair<>(null, null);
        assertNull(pair.getFirst());
        assertNull(pair.getSecond());
    }
}
