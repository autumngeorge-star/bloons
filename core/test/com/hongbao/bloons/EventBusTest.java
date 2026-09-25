package com.hongbao.bloons;

import com.hongbao.bloons.event.DefaultEventBus;
import com.hongbao.bloons.event.EventBus;
import com.hongbao.bloons.event.EventListener;
import com.hongbao.bloons.event.LevelChangedEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class EventBusTest {

    private EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new DefaultEventBus();
    }

    @Test
    public void testSubscribeAndPublish() {
        AtomicInteger levelReceived = new AtomicInteger(-1);
        EventListener<LevelChangedEvent> listener = event -> levelReceived.set(event.getLevel());

        eventBus.subscribe(LevelChangedEvent.class, listener);
        eventBus.publish(new LevelChangedEvent(5));

        assertEquals(5, levelReceived.get());
    }

    @Test
    public void testUnsubscribe() {
        AtomicInteger count = new AtomicInteger(0);
        EventListener<LevelChangedEvent> listener = event -> count.incrementAndGet();

        eventBus.subscribe(LevelChangedEvent.class, listener);
        eventBus.publish(new LevelChangedEvent(1));
        assertEquals(1, count.get());

        eventBus.unsubscribe(LevelChangedEvent.class, listener);
        eventBus.publish(new LevelChangedEvent(2));
        assertEquals(1, count.get());
    }

    @Test
    public void testMultipleListeners() {
        AtomicInteger count1 = new AtomicInteger(0);
        AtomicInteger count2 = new AtomicInteger(0);

        eventBus.subscribe(LevelChangedEvent.class, e -> count1.incrementAndGet());
        eventBus.subscribe(LevelChangedEvent.class, e -> count2.incrementAndGet());

        eventBus.publish(new LevelChangedEvent(10));

        assertEquals(1, count1.get());
        assertEquals(1, count2.get());
    }

    @Test
    public void testNullPublishDoesNotThrow() {
        eventBus.publish(null);
    }
}
