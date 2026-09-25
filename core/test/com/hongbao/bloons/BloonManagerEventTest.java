package com.hongbao.bloons;

import com.hongbao.bloons.event.BloonPoppedEvent;
import com.hongbao.bloons.event.DefaultEventBus;
import com.hongbao.bloons.event.EventBus;
import com.hongbao.bloons.event.LevelChangedEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class BloonManagerEventTest {

    private EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new DefaultEventBus();
    }

    @Test
    public void testBloonManagerLevelChangedEvent() {
        AtomicInteger levelReceived = new AtomicInteger(-1);
        eventBus.subscribe(LevelChangedEvent.class, event -> levelReceived.set(event.getLevel()));

        // Instantiate BloonManager with custom event bus
        BloonManager bloonManager = new BloonManager(null, null, eventBus);
        assertEquals(0, bloonManager.getLevel());

        // Publish level change event
        eventBus.publish(new LevelChangedEvent(1));
        assertEquals(1, levelReceived.get());
    }

    @Test
    public void testBloonPoppedEventPublication() {
        AtomicInteger popCount = new AtomicInteger(0);
        eventBus.subscribe(BloonPoppedEvent.class, event -> popCount.incrementAndGet());

        eventBus.publish(new BloonPoppedEvent(null, 1));
        assertEquals(1, popCount.get());
    }
}
