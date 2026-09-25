package com.hongbao.bloons.events;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class GameEventManagerTest {

    private GameEventManager eventManager;

    @Before
    public void setUp() {
        eventManager = GameEventManager.getInstance();
        eventManager.clearListeners();
    }

    @Test
    public void testSubscribeAndPublishBloonPoppedEvent() {
        AtomicInteger popCount = new AtomicInteger(0);

        eventManager.subscribe(BloonPoppedEvent.class, event -> popCount.incrementAndGet());

        eventManager.publish(BloonPoppedEvent.INSTANCE);
        eventManager.publish(BloonPoppedEvent.INSTANCE);

        assertEquals("Listener should be invoked twice for published events", 2, popCount.get());
    }

    @Test
    public void testLevelChangedEventData() {
        AtomicInteger receivedLevel = new AtomicInteger(-1);

        eventManager.subscribe(LevelChangedEvent.class, event -> receivedLevel.set(event.getLevel()));

        eventManager.publish(new LevelChangedEvent(40));

        assertEquals("Listener should receive correct level from event data", 40, receivedLevel.get());
    }

    @Test
    public void testEventFilteringByType() {
        AtomicBoolean poppedFired = new AtomicBoolean(false);
        AtomicBoolean levelFired = new AtomicBoolean(false);

        eventManager.subscribe(BloonPoppedEvent.class, event -> poppedFired.set(true));
        eventManager.subscribe(LevelChangedEvent.class, event -> levelFired.set(true));

        eventManager.publish(new LevelChangedEvent(1));

        assertFalse("BloonPoppedEvent listener should not trigger on LevelChangedEvent", poppedFired.get());
        assertTrue("LevelChangedEvent listener should trigger on LevelChangedEvent", levelFired.get());
    }

    @Test
    public void testUnsubscribeListener() {
        AtomicInteger count = new AtomicInteger(0);
        GameEventListener<AudioToggleEvent> listener = event -> count.incrementAndGet();

        eventManager.subscribe(AudioToggleEvent.class, listener);
        eventManager.publish(AudioToggleEvent.INSTANCE);
        assertEquals(1, count.get());

        eventManager.unsubscribe(AudioToggleEvent.class, listener);
        eventManager.publish(AudioToggleEvent.INSTANCE);
        assertEquals("Unsubscribed listener should not receive subsequent events", 1, count.get());
    }
}
