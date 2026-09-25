package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.events.BloonEventListener;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import static org.junit.Assert.*;

public class BloonEventListenerTest {

    @Test
    public void testListenerRegistrationAndCallbacks() {
        // Test listener implementation tracking callbacks
        TestListener listener = new TestListener();
        ScoreManager scoreManager = new ScoreManager(new ScoreManagerTest.InMemoryPreferences());

        // Test ScoreManager listener integration
        scoreManager.onBloonDamaged(null, 5);
        assertEquals(50, scoreManager.getScore());

        scoreManager.onLevelCleared(2);
        assertEquals(250, scoreManager.getScore());
    }

    private static class TestListener implements BloonEventListener {
        int damagedCalls = 0;
        int poppedCalls = 0;
        int levelClearedCalls = 0;

        @Override
        public void onBloonDamaged(BloonActor bloonActor, int damage) {
            damagedCalls++;
        }

        @Override
        public void onBloonPopped(BloonActor bloonActor, BloonPoppedResult result) {
            poppedCalls++;
        }

        @Override
        public void onLevelCleared(int level) {
            levelClearedCalls++;
        }
    }
}
