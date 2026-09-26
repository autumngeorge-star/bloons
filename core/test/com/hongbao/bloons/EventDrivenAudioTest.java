package com.hongbao.bloons;

import com.hongbao.bloons.audio.AudioManager;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.DefaultGameEventBus;
import com.hongbao.bloons.events.GameEvent;
import com.hongbao.bloons.events.GameEventBus;
import com.hongbao.bloons.events.GameEventListener;
import com.hongbao.bloons.events.GamePausedEvent;
import com.hongbao.bloons.events.GameResumedEvent;
import com.hongbao.bloons.events.LevelChangedEvent;
import com.hongbao.bloons.events.MusicToggleRequestedEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventDrivenAudioTest {

    private GameEventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new DefaultGameEventBus();
    }

    @Test
    public void testEventBusPublishAndSubscribe() {
        final List<GameEvent> receivedEvents = new ArrayList<>();
        GameEventListener listener = receivedEvents::add;

        eventBus.subscribe(listener);
        eventBus.publish(new BloonPoppedEvent());
        eventBus.publish(new LevelChangedEvent(5));

        assertEquals(2, receivedEvents.size());
        assertTrue(receivedEvents.get(0) instanceof BloonPoppedEvent);
        assertTrue(receivedEvents.get(1) instanceof LevelChangedEvent);
        assertEquals(5, ((LevelChangedEvent) receivedEvents.get(1)).getLevel());

        eventBus.unsubscribe(listener);
        eventBus.publish(new BloonPoppedEvent());
        assertEquals(2, receivedEvents.size());
    }

    @Test
    public void testAudioManagerHeadlessMode() {
        AudioManager audioManager = new AudioManager(eventBus);

        // Publish events to verify AudioManager receives them in headless mode without crashing
        eventBus.publish(new LevelChangedEvent(0));
        eventBus.publish(new LevelChangedEvent(1));
        eventBus.publish(new LevelChangedEvent(40));
        eventBus.publish(new BloonPoppedEvent());
        eventBus.publish(new MusicToggleRequestedEvent());
        eventBus.publish(new GamePausedEvent());
        eventBus.publish(new GameResumedEvent());

        // Test disposal
        audioManager.dispose();
    }

    @Test
    public void testBloonManagerEventPublishingInHeadlessMode() {
        final List<GameEvent> receivedEvents = new ArrayList<>();
        eventBus.subscribe(receivedEvents::add);

        Player player = new Player(100, 100);
        BloonManager bloonManager = new BloonManager(null, null, eventBus, player, () -> true);

        List<List<Bloon>> levels = new ArrayList<>();
        levels.add(new ArrayList<>());
        levels.add(new ArrayList<>());
        List<List<Long>> intervals = new ArrayList<>();
        intervals.add(new ArrayList<>());
        intervals.add(new ArrayList<>());
        bloonManager.setBloonQueue(new BloonQueue(levels, intervals));

        assertTrue(bloonManager.canGoToNextLevel());

        // Advancing level should publish LevelChangedEvent
        bloonManager.nextLevel();
        assertEquals(1, receivedEvents.size());
        assertTrue(receivedEvents.get(0) instanceof LevelChangedEvent);
        assertEquals(1, ((LevelChangedEvent) receivedEvents.get(0)).getLevel());

        // Popping a bloon should publish BloonPoppedEvent
        Bloon bloon = BloonFactory.createRedBloon();
        BloonActor bloonActor = new BloonActor(bloon, 0, 0, null);
        bloonManager.popBloon(bloonActor, 1);

        assertEquals(2, receivedEvents.size());
        assertTrue(receivedEvents.get(1) instanceof BloonPoppedEvent);
    }
}
