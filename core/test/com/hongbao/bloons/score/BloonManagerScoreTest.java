package com.hongbao.bloons.score;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BloonManagerScoreTest {

    @Test
    public void testScoreEventFieldsAndListeners() {
        ScoreEvent damageEvent = new ScoreEvent(10, ScoreEvent.Type.BLOON_DAMAGE);
        assertEquals(10, damageEvent.getPoints());
        assertEquals(ScoreEvent.Type.BLOON_DAMAGE, damageEvent.getType());

        ScoreEvent popEvent = new ScoreEvent(25, ScoreEvent.Type.BLOON_POPPED);
        assertEquals(25, popEvent.getPoints());
        assertEquals(ScoreEvent.Type.BLOON_POPPED, popEvent.getType());

        List<ScoreEvent> events = new ArrayList<>();
        ScoreListener listener = events::add;

        listener.onScoreEvent(damageEvent);
        listener.onScoreEvent(popEvent);

        assertEquals(2, events.size());
        assertEquals(10, events.get(0).getPoints());
        assertEquals(25, events.get(1).getPoints());
    }

    @Test
    public void testScoreManagerIntegrationWithScoreEvents() {
        ScoreManager manager = new ScoreManager(new GameStorageServiceTestImpl());
        List<ScoreEvent> uiEvents = new ArrayList<>();
        manager.addScoreListener(uiEvents::add);

        manager.onScoreEvent(new ScoreEvent(15, ScoreEvent.Type.BLOON_DAMAGE));
        manager.onScoreEvent(new ScoreEvent(40, ScoreEvent.Type.BLOON_POPPED));

        assertEquals(55, manager.getCurrentScore());
        assertEquals(2, uiEvents.size());
        assertEquals(55, uiEvents.get(1).getCurrentScore());
    }

    private static class GameStorageServiceTestImpl implements com.hongbao.bloons.storage.GameStorageService {
        private int highScore = 0;

        @Override
        public int loadHighScore() {
            return highScore;
        }

        @Override
        public void saveHighScore(int highScore) {
            this.highScore = highScore;
        }

        @Override
        public int loadScore(String key, int defaultValue) {
            return defaultValue;
        }

        @Override
        public void saveScore(String key, int value) {
        }

        @Override
        public void clear() {
            highScore = 0;
        }
    }
}
