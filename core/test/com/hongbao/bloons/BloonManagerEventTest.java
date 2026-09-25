package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameEventBus;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BloonManagerEventTest {

    private GameEventBus testEventBus;

    @Before
    public void setUp() {
        testEventBus = new GameEventBus();
        GameEventBus.setInstance(testEventBus);
    }

    @Test
    public void testBloonManagerPublishesBloonPoppedEvent() {
        BloonManager bloonManager = new BloonManager(null, null, testEventBus);

        List<BloonPoppedEvent> poppedEvents = new ArrayList<>();
        testEventBus.subscribe(BloonPoppedEvent.class, poppedEvents::add);

        Bloon redBloon = BloonFactory.createRedBloon(); // Red bloon with 1 health
        BloonActor bloonActor = new BloonActor(redBloon, 100, 100, null);

        // Pop bloon with 1 damage
        bloonManager.popBloon(bloonActor, 1);

        assertEquals(1, poppedEvents.size());
        BloonPoppedEvent event = poppedEvents.get(0);
        assertSame(bloonActor, event.getBloonActor());
        assertSame(redBloon, event.getBloon());
        assertEquals(1, event.getDamage());
    }

    @Test
    public void testMultipleSubscribersReceivePoppedEventWithoutModifyingBloonManager() {
        BloonManager bloonManager = new BloonManager(null, null, testEventBus);

        List<String> analyticsLog = new ArrayList<>();
        List<String> particleLog = new ArrayList<>();

        // Analytics subsystem subscriber
        testEventBus.subscribe(BloonPoppedEvent.class, e -> analyticsLog.add("Track pop"));
        // Particle effects subsystem subscriber
        testEventBus.subscribe(BloonPoppedEvent.class, e -> particleLog.add("Spawn particle"));

        Bloon redBloon = BloonFactory.createRedBloon();
        BloonActor bloonActor = new BloonActor(redBloon, 200, 200, null);

        bloonManager.popBloon(bloonActor, 1);

        assertEquals(1, analyticsLog.size());
        assertEquals(1, particleLog.size());
        assertEquals("Track pop", analyticsLog.get(0));
        assertEquals("Spawn particle", particleLog.get(0));
    }

    @Test
    public void testExecutionWithoutAudioSubscribers() {
        // Run bloon manager without registering any audio subscriber on event bus
        BloonManager bloonManager = new BloonManager(null, null, testEventBus);

        assertFalse(testEventBus.hasSubscribers(BloonPoppedEvent.class));

        Bloon redBloon = BloonFactory.createRedBloon();
        BloonActor bloonActor = new BloonActor(redBloon, 50, 50, null);

        // Should complete without throwing exceptions or executing audio side effects
        bloonManager.popBloon(bloonActor, 1);
    }
}
