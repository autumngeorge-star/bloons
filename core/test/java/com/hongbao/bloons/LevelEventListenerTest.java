package com.hongbao.bloons;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LevelEventListenerTest {

    @Test
    public void testLevelEventNotificationOnNextLevel() {
        BloonManager manager = new BloonManager(null, null);
        List<Integer> levelEvents = new ArrayList<>();

        LevelEventListener listener = levelEvents::add;
        manager.addLevelEventListener(listener);

        assertEquals(0, manager.getLevel());
        manager.nextLevel();

        assertEquals(1, manager.getLevel());
        assertEquals(1, levelEvents.size());
        assertEquals(Integer.valueOf(1), levelEvents.get(0));
    }

    @Test
    public void testMultipleListenersAndUnregistration() {
        BloonManager manager = new BloonManager(null, null);
        List<Integer> events1 = new ArrayList<>();
        List<Integer> events2 = new ArrayList<>();

        LevelEventListener listener1 = events1::add;
        LevelEventListener listener2 = events2::add;

        manager.registerLevelEventListener(listener1);
        manager.registerLevelEventListener(listener2);

        manager.nextLevel(); // Level 1
        assertEquals(1, events1.size());
        assertEquals(1, events2.size());

        manager.unregisterLevelEventListener(listener1);
        manager.nextLevel(); // Level 2

        assertEquals(1, events1.size()); // Did not receive second event
        assertEquals(2, events2.size()); // Received second event
        assertEquals(Integer.valueOf(2), events2.get(1));
    }

    @Test
    public void testMusicPlayerImplementsLevelEventListener() {
        MusicPlayer musicPlayer = new MusicPlayer();
        assertTrue(musicPlayer instanceof LevelEventListener);

        // Verify headless onLevelChanged calls without Gdx context initialized
        musicPlayer.onLevelChanged(1);
        musicPlayer.onLevelChanged(40);
        musicPlayer.onLevelChanged(10);
    }

    @Test
    public void testLevelMilestonesLevel1AndLevel40() {
        BloonManager manager = new BloonManager(null, null);
        List<Integer> receivedLevels = new ArrayList<>();

        manager.addLevelEventListener(receivedLevels::add);

        // Advance to level 1
        manager.nextLevel();
        assertEquals(1, manager.getLevel());
        assertEquals(1, receivedLevels.size());
        assertEquals(Integer.valueOf(1), receivedLevels.get(0));

        // Advance to level 40
        for (int i = 2; i <= 40; i++) {
            manager.nextLevel();
        }
        assertEquals(40, manager.getLevel());
        assertEquals(40, receivedLevels.size());
        assertEquals(Integer.valueOf(40), receivedLevels.get(39));
    }

    @Test
    public void testHeadlessBloonManagerNoGdxAppRequired() {
        // Ensure Gdx.app is null
        BloonManager manager = new BloonManager(null, null);

        // Verify multiple level progressions work headlessly
        for (int i = 0; i < 5; i++) {
            manager.nextLevel();
        }
        assertEquals(5, manager.getLevel());
    }
}
