package com.hongbao.bloons.events;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EventBusTest {

    private EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        eventBus.clear();
    }

    @Test
    public void testSubscribeAndPublish() {
        List<LevelStartedEvent> receivedEvents = new ArrayList<>();
        EventHandler<LevelStartedEvent> handler = receivedEvents::add;

        eventBus.subscribe(LevelStartedEvent.class, handler);
        LevelStartedEvent event = new LevelStartedEvent(1);
        eventBus.publish(event);

        assertEquals(1, receivedEvents.size());
        assertEquals(1, receivedEvents.get(0).getLevel());
    }

    @Test
    public void testUnsubscribe() {
        List<LevelStartedEvent> receivedEvents = new ArrayList<>();
        EventHandler<LevelStartedEvent> handler = receivedEvents::add;

        eventBus.subscribe(LevelStartedEvent.class, handler);
        eventBus.unsubscribe(LevelStartedEvent.class, handler);

        eventBus.publish(new LevelStartedEvent(1));
        assertTrue(receivedEvents.isEmpty());
    }

    @Test
    public void testMultipleSubscribers() {
        List<String> log = new ArrayList<>();

        eventBus.subscribe(LevelStartedEvent.class, e -> log.add("Handler1:" + e.getLevel()));
        eventBus.subscribe(LevelStartedEvent.class, e -> log.add("Handler2:" + e.getLevel()));

        eventBus.publish(new LevelStartedEvent(5));

        assertEquals(2, log.size());
        assertEquals("Handler1:5", log.get(0));
        assertEquals("Handler2:5", log.get(1));
    }

    @Test
    public void testClear() {
        List<LevelStartedEvent> receivedEvents = new ArrayList<>();
        eventBus.subscribe(LevelStartedEvent.class, receivedEvents::add);
        eventBus.clear();

        eventBus.publish(new LevelStartedEvent(10));
        assertTrue(receivedEvents.isEmpty());
    }
}
