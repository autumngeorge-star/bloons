package com.hongbao.bloons.events;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LevelStartedEventTest {

    @Test
    public void testLevelStartedEventPayload() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("mapName", "HeaterMap");
        metadata.put("difficulty", "Hard");

        LevelStartedEvent event = new LevelStartedEvent(40, metadata);

        assertEquals(40, event.getLevel());
        assertEquals("HeaterMap", event.getMetadata("mapName"));
        assertEquals("Hard", event.getMetadata("difficulty"));
        assertEquals(40, event.getMetadata("level"));
    }

    @Test
    public void testLevelStartedEventNullMetadata() {
        LevelStartedEvent event = new LevelStartedEvent(1, null);
        assertEquals(1, event.getLevel());
        assertNotNull(event.getMetadata());
        assertEquals(1, event.getMetadata("level"));
    }
}
