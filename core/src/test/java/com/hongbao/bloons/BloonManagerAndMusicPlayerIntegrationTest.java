package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.events.EventBus;
import com.hongbao.bloons.events.LevelStartedEvent;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.registry.WaveRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class BloonManagerAndMusicPlayerIntegrationTest {

    private EventBus eventBus;
    private WaveRegistry waveRegistry;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        waveRegistry = new WaveRegistry();
        waveRegistry.clear();
        waveRegistry.registerWaveQueue(WaveRegistry.DEFAULT_KEY, this::createTestQueue);
    }

    private BloonQueue createTestQueue() {
        List<List<Bloon>> bloons = new ArrayList<>();
        // Level 0: empty level
        bloons.add(Collections.emptyList());
        // Level 1
        bloons.add(Collections.singletonList(BloonFactory.createBlueBloon()));
        // Levels 2 to 40
        for (int i = 2; i <= 40; i++) {
            bloons.add(Collections.singletonList(BloonFactory.createGreenBloon()));
        }

        List<List<Long>> intervals = new ArrayList<>();
        intervals.add(Collections.emptyList());
        for (int i = 1; i <= 40; i++) {
            intervals.add(Collections.singletonList(0L));
        }

        return new BloonQueue(bloons, intervals);
    }

    private static class TestableMusicPlayer extends MusicPlayer {
        public String currentTrack = null;

        public TestableMusicPlayer(EventBus eventBus) {
            super(eventBus);
        }

        @Override
        public void playStageMusic() {
            this.currentTrack = "STAGE_MUSIC";
        }

        @Override
        public void playFinalBossMusic() {
            this.currentTrack = "FINAL_BOSS_MUSIC";
        }
    }

    @Test
    public void testLevelStartedEventPublishedOnNextLevel() {
        List<LevelStartedEvent> events = new ArrayList<>();
        eventBus.subscribe(LevelStartedEvent.class, events::add);

        BloonManager bloonManager = new BloonManager(null, null, waveRegistry, eventBus, WaveRegistry.DEFAULT_KEY);

        assertTrue(bloonManager.canGoToNextLevel());

        bloonManager.nextLevel();

        assertEquals(1, events.size());
        assertEquals(1, events.get(0).getLevel());
    }

    @Test
    public void testMusicPlayerReactsToLevelStartedEvent() {
        TestableMusicPlayer musicPlayer = new TestableMusicPlayer(eventBus);

        eventBus.publish(new LevelStartedEvent(1));
        assertEquals("STAGE_MUSIC", musicPlayer.currentTrack);

        eventBus.publish(new LevelStartedEvent(40));
        assertEquals("FINAL_BOSS_MUSIC", musicPlayer.currentTrack);

        musicPlayer.unregister(eventBus);
        eventBus.publish(new LevelStartedEvent(1));
        // Track shouldn't change after unregistering
        assertEquals("FINAL_BOSS_MUSIC", musicPlayer.currentTrack);
    }
}
