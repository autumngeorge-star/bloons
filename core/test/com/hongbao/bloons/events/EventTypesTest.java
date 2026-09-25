package com.hongbao.bloons.events;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class EventTypesTest {

    @Before
    public void setUp() {
        GameEventBus.getInstance().clear();
    }

    @Test
    public void testEventsCanPublishWithoutSubscribers() {
        // Acceptance criterion: Unit tests can run without audio subscribers attached.
        GameEventBus bus = GameEventBus.getInstance();

        bus.publish(new TowerPlacedEvent(null));
        bus.publish(new TowerUpgradedEvent(null));
        bus.publish(new SpellCardActivatedEvent(null));
        bus.publish(new BloonDamagedEvent(null, 10));
        bus.publish(new BloonPoppedEvent(null, null));
        bus.publish(new ApplicationDisposeEvent());
    }

    @Test
    public void testEventPayloads() {
        AtomicBoolean damagedReceived = new AtomicBoolean(false);
        GameEventBus.getInstance().subscribe(BloonDamagedEvent.class, event -> {
            assertEquals(15, event.getDamage());
            damagedReceived.set(true);
        });

        GameEventBus.getInstance().publish(new BloonDamagedEvent(null, 15));
        assertTrue(damagedReceived.get());
    }
}
