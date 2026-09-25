package com.hongbao.bloons;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testMapTypeIndicesAndNames() {
        assertEquals(0, MapType.BASIC_MAP.getIndex());
        assertEquals("Basic Map", MapType.BASIC_MAP.getDisplayName());

        assertEquals(1, MapType.MAP_WITH_TURN.getIndex());
        assertEquals("Map with Turn", MapType.MAP_WITH_TURN.getDisplayName());

        assertEquals(2, MapType.HEATER.getIndex());
        assertEquals("Heater Map", MapType.HEATER.getDisplayName());
    }

    @Test
    public void testGetByIndex() {
        assertEquals(MapType.BASIC_MAP, MapType.getByIndex(0));
        assertEquals(MapType.MAP_WITH_TURN, MapType.getByIndex(1));
        assertEquals(MapType.HEATER, MapType.getByIndex(2));
        assertEquals(MapType.BASIC_MAP, MapType.getByIndex(99));
    }
}
