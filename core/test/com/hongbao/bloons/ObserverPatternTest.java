package com.hongbao.bloons;

import com.hongbao.bloons.listeners.BloonEventListener;
import com.hongbao.bloons.listeners.GameLifecycleListener;
import com.hongbao.bloons.listeners.LevelEventListener;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ObserverPatternTest {

    static class MockBloonEventListener implements BloonEventListener {
        int popCount = 0;

        @Override
        public void onBloonPopped() {
            popCount++;
        }
    }

    static class MockLevelEventListener implements LevelEventListener {
        int lastLevel = -1;
        int changeCount = 0;

        @Override
        public void onLevelChanged(int newLevel) {
            lastLevel = newLevel;
            changeCount++;
        }
    }

    static class MockGameLifecycleListener implements GameLifecycleListener {
        boolean paused = false;
        boolean resumed = false;
        boolean disposed = false;

        @Override
        public void onGamePaused() {
            paused = true;
        }

        @Override
        public void onGameResumed() {
            resumed = true;
        }

        @Override
        public void onGameDisposed() {
            disposed = true;
        }
    }

    static class MultiEventListener implements BloonEventListener, LevelEventListener {
        int popCount = 0;
        int levelCount = 0;

        @Override
        public void onBloonPopped() {
            popCount++;
        }

        @Override
        public void onLevelChanged(int newLevel) {
            levelCount++;
        }
    }

    @Test
    public void testBloonManagerEventListenerRegistrationAndNotification() {
        BloonManager bloonManager = new BloonManager(null, null);
        MockBloonEventListener bloonListener = new MockBloonEventListener();
        MockLevelEventListener levelListener = new MockLevelEventListener();

        bloonManager.addBloonEventListener(bloonListener);
        bloonManager.addLevelEventListener(levelListener);

        // Advance level
        bloonManager.nextLevel();
        Assert.assertEquals(1, levelListener.changeCount);
        Assert.assertEquals(1, levelListener.lastLevel);

        // Remove listener
        bloonManager.removeLevelEventListener(levelListener);
        bloonManager.nextLevel();
        Assert.assertEquals(1, levelListener.changeCount); // Should remain 1
    }

    @Test
    public void testGenericAddObserver() {
        BloonManager bloonManager = new BloonManager(null, null);
        MultiEventListener multiListener = new MultiEventListener();

        bloonManager.addObserver(multiListener);

        bloonManager.nextLevel();
        Assert.assertEquals(1, multiListener.levelCount);

        bloonManager.removeObserver(multiListener);
        bloonManager.nextLevel();
        Assert.assertEquals(1, multiListener.levelCount);
    }

    @Test
    public void testMapObserverDelegation() {
        Map map = new Map("heater.png", null);
        MockLevelEventListener levelListener = new MockLevelEventListener();

        map.addObserver(levelListener);

        map.getBloonManager().nextLevel();
        Assert.assertEquals(1, levelListener.changeCount);
        Assert.assertEquals(1, levelListener.lastLevel);

        map.removeObserver(levelListener);
        map.getBloonManager().nextLevel();
        Assert.assertEquals(1, levelListener.changeCount);
    }

    @Test
    public void testSoundManagerDependencyInjectionAndListeners() {
        AtomicBoolean popPlayed = new AtomicBoolean(false);
        AtomicInteger levelChanged = new AtomicInteger(-1);

        SoundManager soundManager = new SoundManager(null, null) {
            @Override
            public void onBloonPopped() {
                popPlayed.set(true);
            }

            @Override
            public void onLevelChanged(int newLevel) {
                levelChanged.set(newLevel);
            }
        };

        soundManager.onBloonPopped();
        Assert.assertTrue(popPlayed.get());

        soundManager.onLevelChanged(40);
        Assert.assertEquals(40, levelChanged.get());
    }

    @Test
    public void testGameLifecycleListenerNotifications() {
        BloonsTouhouDefense game = new BloonsTouhouDefense();
        MockGameLifecycleListener lifecycleListener = new MockGameLifecycleListener();

        game.addObserver(lifecycleListener);

        game.pause();
        Assert.assertTrue(lifecycleListener.paused);

        game.resume();
        Assert.assertTrue(lifecycleListener.resumed);

        game.dispose();
        Assert.assertTrue(lifecycleListener.disposed);
    }

    @Test
    public void testNoDirectGdxAudioInBloonManager() throws Exception {
        java.lang.reflect.Field[] fields = BloonManager.class.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            Assert.assertFalse("BloonManager should not contain Gdx Sound fields",
                field.getType().getName().contains("Sound"));
        }
    }
}
