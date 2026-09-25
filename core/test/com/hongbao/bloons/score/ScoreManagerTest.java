package com.hongbao.bloons.score;

import com.hongbao.bloons.storage.GameStorageService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ScoreManagerTest {

    private MemoryStorageService storageService;
    private ScoreManager scoreManager;

    private static class MemoryStorageService implements GameStorageService {
        private final Map<String, Integer> storage = new HashMap<>();

        @Override
        public int loadHighScore() {
            return loadScore("high_score", 0);
        }

        @Override
        public void saveHighScore(int highScore) {
            saveScore("high_score", highScore);
        }

        @Override
        public int loadScore(String key, int defaultValue) {
            return storage.getOrDefault(key, defaultValue);
        }

        @Override
        public void saveScore(String key, int value) {
            storage.put(key, value);
        }

        @Override
        public void clear() {
            storage.clear();
        }
    }

    @Before
    public void setUp() {
        storageService = new MemoryStorageService();
        storageService.saveHighScore(100);
        scoreManager = new ScoreManager(storageService);
    }

    @Test
    public void testInitialHighScoreLoadedFromStorage() {
        assertEquals(0, scoreManager.getCurrentScore());
        assertEquals(100, scoreManager.getHighScore());
    }

    @Test
    public void testScoreAccumulationBelowHighScore() {
        scoreManager.onScoreEvent(new ScoreEvent(50, ScoreEvent.Type.BLOON_POPPED));
        assertEquals(50, scoreManager.getCurrentScore());
        assertEquals(100, scoreManager.getHighScore());
        assertEquals(100, storageService.loadHighScore());
    }

    @Test
    public void testHighScoreUpdateAndPersistence() {
        scoreManager.onScoreEvent(new ScoreEvent(150, ScoreEvent.Type.BLOON_POPPED));
        assertEquals(150, scoreManager.getCurrentScore());
        assertEquals(150, scoreManager.getHighScore());
        assertEquals(150, storageService.loadHighScore());
    }

    @Test
    public void testScoreListenerNotification() {
        List<ScoreEvent> receivedEvents = new ArrayList<>();
        scoreManager.addScoreListener(receivedEvents::add);

        scoreManager.onScoreEvent(new ScoreEvent(30, ScoreEvent.Type.BLOON_DAMAGE));

        assertEquals(1, receivedEvents.size());
        ScoreEvent event = receivedEvents.get(0);
        assertEquals(30, event.getCurrentScore());
        assertEquals(100, event.getHighScore());
        assertEquals(ScoreEvent.Type.SCORE_UPDATED, event.getType());
    }

    @Test
    public void testNewHighScoreNotificationType() {
        List<ScoreEvent> receivedEvents = new ArrayList<>();
        scoreManager.addScoreListener(receivedEvents::add);

        scoreManager.onScoreEvent(new ScoreEvent(200, ScoreEvent.Type.BLOON_POPPED));

        assertEquals(1, receivedEvents.size());
        ScoreEvent event = receivedEvents.get(0);
        assertEquals(200, event.getCurrentScore());
        assertEquals(200, event.getHighScore());
        assertEquals(ScoreEvent.Type.HIGH_SCORE_UPDATED, event.getType());
    }

    @Test
    public void testResetScore() {
        scoreManager.onScoreEvent(new ScoreEvent(200, ScoreEvent.Type.BLOON_POPPED));
        assertEquals(200, scoreManager.getCurrentScore());

        scoreManager.resetScore();
        assertEquals(0, scoreManager.getCurrentScore());
        assertEquals(200, scoreManager.getHighScore());
    }
}
