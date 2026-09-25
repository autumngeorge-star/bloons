package com.hongbao.bloons.events;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class GameEventBusTest {

    private GameEventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new GameEventBus();
    }

    @Test
    public void testSubscribeAndPublish() {
        AtomicBoolean received = new AtomicBoolean(false);
        eventBus.subscribe(ApplicationDisposeEvent.class, event -> received.set(true));

        eventBus.publish(new ApplicationDisposeEvent());
        assertTrue(received.get());
    }

    @Test
    public void testMultipleSubscribers() {
        AtomicInteger counter = new AtomicInteger(0);
        eventBus.subscribe(ApplicationDisposeEvent.class, event -> counter.incrementAndGet());
        eventBus.subscribe(ApplicationDisposeEvent.class, event -> counter.incrementAndGet());

        eventBus.publish(new ApplicationDisposeEvent());
        assertEquals(2, counter.get());
    }

    @Test
    public void testUnsubscribe() {
        AtomicInteger counter = new AtomicInteger(0);
        EventListener<ApplicationDisposeEvent> listener = event -> counter.incrementAndGet();

        eventBus.subscribe(ApplicationDisposeEvent.class, listener);
        eventBus.publish(new ApplicationDisposeEvent());
        assertEquals(1, counter.get());

        eventBus.unsubscribe(ApplicationDisposeEvent.class, listener);
        eventBus.publish(new ApplicationDisposeEvent());
        assertEquals(1, counter.get());
    }

    @Test
    public void testClearSubscribers() {
        AtomicInteger counter = new AtomicInteger(0);
        eventBus.subscribe(ApplicationDisposeEvent.class, event -> counter.incrementAndGet());

        eventBus.clear();
        eventBus.publish(new ApplicationDisposeEvent());
        assertEquals(0, counter.get());
    }

    @Test
    public void testExceptionSafetyInHandler() {
        AtomicBoolean secondListenerCalled = new AtomicBoolean(false);

        eventBus.subscribe(ApplicationDisposeEvent.class, event -> {
            throw new RuntimeException("Simulated exception in listener");
        });
        eventBus.subscribe(ApplicationDisposeEvent.class, event -> secondListenerCalled.set(true));

        eventBus.publish(new ApplicationDisposeEvent());
        assertTrue("Second listener should be called even if the first throws an exception", secondListenerCalled.get());
    }
}
