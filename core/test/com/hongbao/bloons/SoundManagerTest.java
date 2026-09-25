package com.hongbao.bloons;

import com.hongbao.bloons.audio.AudioAssets;
import com.hongbao.bloons.audio.SoundManager;
import com.hongbao.bloons.event.BloonPoppedEvent;
import com.hongbao.bloons.event.DefaultEventBus;
import com.hongbao.bloons.event.EventBus;
import com.hongbao.bloons.event.LevelChangedEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundManagerTest {

    private EventBus eventBus;
    private SoundManager soundManager;

    @Before
    public void setUp() {
        eventBus = new DefaultEventBus();
        soundManager = new SoundManager(eventBus);
    }

    @Test
    public void testVolumeSettings() {
        soundManager.setSfxVolume(0.8f);
        soundManager.setMusicVolume(0.4f);

        assertEquals(0.8f, soundManager.getSfxVolume(), 0.001f);
        assertEquals(0.4f, soundManager.getMusicVolume(), 0.001f);
    }

    @Test
    public void testVolumeClamping() {
        soundManager.setSfxVolume(1.5f);
        assertEquals(1.0f, soundManager.getSfxVolume(), 0.001f);

        soundManager.setSfxVolume(-0.5f);
        assertEquals(0.0f, soundManager.getSfxVolume(), 0.001f);

        soundManager.setMusicVolume(2.0f);
        assertEquals(1.0f, soundManager.getMusicVolume(), 0.001f);

        soundManager.setMusicVolume(-1.0f);
        assertEquals(0.0f, soundManager.getMusicVolume(), 0.001f);
    }

    @Test
    public void testLevelChangedEventTriggering() {
        eventBus.publish(new LevelChangedEvent(1));
        assertEquals(AudioAssets.STAGE_MUSIC, soundManager.getCurrentMusicPath());

        eventBus.publish(new LevelChangedEvent(40));
        assertEquals(AudioAssets.FINAL_BOSS_MUSIC, soundManager.getCurrentMusicPath());
    }

    @Test
    public void testBloonPoppedEventSubscription() {
        // Should not throw NPE even when headless without Gdx.audio initialized
        eventBus.publish(new BloonPoppedEvent(null, 1));
    }
}
