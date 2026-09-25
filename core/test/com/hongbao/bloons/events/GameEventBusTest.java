package com.hongbao.bloons.events;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameEventBusTest {

    private GameEventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new GameEventBus();
    }

    private static class TestEvent implements GameEvent {
        private final String message;
        public TestEvent(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    private static class AnotherEvent implements GameEvent {
    }

    @Test
    public void testSubscribeAndPublish() {
        List<String> receivedMessages = new ArrayList<>();
        eventBus.subscribe(TestEvent.class, event -> receivedMessages.add(event.getMessage()));

        eventBus.publish(new TestEvent("Hello"));
        eventBus.publish(new TestEvent("World"));

        assertEquals(2, receivedMessages.size());
        assertEquals("Hello", receivedMessages.get(0));
        assertEquals("World", receivedMessages.get(1));
    }

    @Test
    public void testTypedEventFiltering() {
        List<String> testEventLogs = new ArrayList<>();
        List<String> anotherEventLogs = new ArrayList<>();

        eventBus.subscribe(TestEvent.class, event -> testEventLogs.add(event.getMessage()));
        eventBus.subscribe(AnotherEvent.class, event -> anotherEventLogs.add("another"));

        eventBus.publish(new TestEvent("Test1"));
        assertEquals(1, testEventLogs.size());
        assertEquals(0, anotherEventLogs.size());

        eventBus.publish(new AnotherEvent());
        assertEquals(1, testEventLogs.size());
        assertEquals(1, anotherEventLogs.size());
    }

    @Test
    public void testMultipleSubscribers() {
        List<String> subscriber1 = new ArrayList<>();
        List<String> subscriber2 = new ArrayList<>();

        eventBus.subscribe(TestEvent.class, event -> subscriber1.add(event.getMessage()));
        eventBus.subscribe(TestEvent.class, event -> subscriber2.add(event.getMessage()));

        eventBus.publish(new TestEvent("Broadcast"));

        assertEquals(1, subscriber1.size());
        assertEquals(1, subscriber2.size());
        assertEquals("Broadcast", subscriber1.get(0));
        assertEquals("Broadcast", subscriber2.get(0));
    }

    @Test
    public void testUnsubscribe() {
        List<String> logs = new ArrayList<>();
        GameEventListener<TestEvent> listener = event -> logs.add(event.getMessage());

        eventBus.subscribe(TestEvent.class, listener);
        eventBus.publish(new TestEvent("Event 1"));
        assertEquals(1, logs.size());

        eventBus.unsubscribe(TestEvent.class, listener);
        eventBus.publish(new TestEvent("Event 2"));
        assertEquals(1, logs.size());
    }

    @Test
    public void testResetSubscriptions() {
        List<String> logs = new ArrayList<>();
        eventBus.subscribe(TestEvent.class, event -> logs.add(event.getMessage()));
        assertTrue(eventBus.hasSubscribers(TestEvent.class));

        eventBus.resetSubscriptions();

        assertFalse(eventBus.hasSubscribers(TestEvent.class));
        eventBus.publish(new TestEvent("After Reset"));
        assertTrue(logs.isEmpty());
    }
}
