package com.hongbao.bloons.registry;

import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WaveRegistryTest {

    private WaveRegistry registry;

    @Before
    public void setUp() {
        registry = new WaveRegistry();
        registry.clear();
    }

    private BloonQueue createMockQueue() {
        List<List<Bloon>> bloons = new ArrayList<>();
        bloons.add(Collections.singletonList(BloonFactory.createRedBloon()));
        bloons.add(Collections.singletonList(BloonFactory.createBlueBloon()));

        List<List<Long>> intervals = new ArrayList<>();
        intervals.add(Collections.singletonList(0L));
        intervals.add(Collections.singletonList(0L));

        return new BloonQueue(bloons, intervals);
    }

    @Test
    public void testRegisterAndRetrieveWaveQueue() {
        registry.registerWaveQueue("custom_key", this::createMockQueue);
        assertTrue(registry.hasWaveQueue("custom_key"));

        BloonQueue queue = registry.getWaveQueue("custom_key");
        assertNotNull(queue);
        assertEquals(0, queue.getLevel());
    }

    @Test
    public void testActiveProfileKey() {
        registry.registerWaveQueue("campaign_profile", this::createMockQueue);
        registry.setActiveProfileKey("campaign_profile");

        assertEquals("campaign_profile", registry.getActiveProfileKey());
        BloonQueue activeQueue = registry.getActiveWaveQueue();
        assertNotNull(activeQueue);
    }

    @Test
    public void testFallbackToDefault() {
        registry.registerWaveQueue(WaveRegistry.DEFAULT_KEY, this::createMockQueue);
        BloonQueue fallbackQueue = registry.getWaveQueue("unknown_key");
        assertNotNull(fallbackQueue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnregisteredKeyThrowsExceptionWhenNoDefaultSet() {
        registry.getWaveQueue("nonexistent_key");
    }
}
