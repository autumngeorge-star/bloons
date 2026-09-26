package com.hongbao.bloons.events;

import com.hongbao.bloons.SoundManager;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class EventBusTest {

    private EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new EventBus();
    }

    @Test
    public void testBloonPoppedEventPublishAndSubscribe() {
        AtomicReference<BloonPoppedEvent> received = new AtomicReference<>();
        EventListener<BloonPoppedEvent> listener = received::set;

        eventBus.subscribe(BloonPoppedEvent.class, listener);

        BloonPoppedEvent event = new BloonPoppedEvent("red", 100.0f, 200.0f, 1, true);
        eventBus.publish(event);

        assertNotNull(received.get());
        assertEquals("red", received.get().getBloonType());
        assertEquals(100.0f, received.get().getX(), 0.001f);
        assertEquals(200.0f, received.get().getY(), 0.001f);
        assertEquals(1, received.get().getDamage());
        assertTrue(received.get().isPopped());
    }

    @Test
    public void testLevelChangedEventPublishAndSubscribe() {
        AtomicInteger newLevelRef = new AtomicInteger(-1);
        EventListener<LevelChangedEvent> listener = e -> newLevelRef.set(e.getNewLevel());

        eventBus.subscribe(LevelChangedEvent.class, listener);

        eventBus.publish(new LevelChangedEvent(1, 2));

        assertEquals(2, newLevelRef.get());
    }

    @Test
    public void testGameStateChangedEventPublishAndSubscribe() {
        AtomicReference<GameStateChangedEvent.State> stateRef = new AtomicReference<>();
        EventListener<GameStateChangedEvent> listener = e -> stateRef.set(e.getNewState());

        eventBus.subscribe(GameStateChangedEvent.class, listener);

        eventBus.publish(new GameStateChangedEvent(GameStateChangedEvent.State.TITLE));

        assertEquals(GameStateChangedEvent.State.TITLE, stateRef.get());
    }

    @Test
    public void testUIInteractionEventPublishAndSubscribe() {
        AtomicReference<UIInteractionEvent.InteractionType> typeRef = new AtomicReference<>();
        EventListener<UIInteractionEvent> listener = e -> typeRef.set(e.getInteractionType());

        eventBus.subscribe(UIInteractionEvent.class, listener);

        eventBus.publish(new UIInteractionEvent(UIInteractionEvent.InteractionType.TOGGLE_MUSIC));

        assertEquals(UIInteractionEvent.InteractionType.TOGGLE_MUSIC, typeRef.get());
    }

    @Test
    public void testUnsubscribe() {
        AtomicBoolean called = new AtomicBoolean(false);
        EventListener<BloonPoppedEvent> listener = e -> called.set(true);

        eventBus.subscribe(BloonPoppedEvent.class, listener);
        eventBus.unsubscribe(BloonPoppedEvent.class, listener);

        eventBus.publish(new BloonPoppedEvent("red", 0, 0, 1, true));

        assertFalse(called.get());
    }

    @Test
    public void testSoundManagerInstantiatesAndSubscribesWithoutGdxAudioCrash() {
        // SoundManager should initialize without throwing NPE or crashing when Gdx.audio is null (headless mode)
        SoundManager soundManager = new SoundManager(eventBus);

        // Publish events to verify SoundManager event handlers don't crash when Gdx audio/files is null
        eventBus.publish(new BloonPoppedEvent("red", 50, 50, 1, true));
        eventBus.publish(new LevelChangedEvent(0, 1));
        eventBus.publish(new GameStateChangedEvent(GameStateChangedEvent.State.TITLE));
        eventBus.publish(new UIInteractionEvent(UIInteractionEvent.InteractionType.TOGGLE_MUSIC));

        soundManager.dispose();
    }
}
