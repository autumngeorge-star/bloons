package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.audio.NoOpMusicService;
import com.hongbao.bloons.audio.NoOpSoundService;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonManagerTest {

    @Test
    public void testBloonManagerInitializationWithNoOpServices() {
        NoOpSoundService soundService = new NoOpSoundService();
        NoOpMusicService musicService = new NoOpMusicService();

        BloonManager bloonManager = new BloonManager(null, null, soundService, musicService);
        assertNotNull("BloonManager should be successfully initialized with no-op audio services", bloonManager);
        assertEquals("Initial level should be 0 before starting next level", 0, bloonManager.getLevel());
    }

    @Test
    public void testNextLevelWithAudioServiceStubs() {
        NoOpSoundService soundService = new NoOpSoundService();
        NoOpMusicService musicService = new NoOpMusicService();

        BloonManager bloonManager = new BloonManager(null, null, soundService, musicService);
        assertTrue("BloonManager should be able to advance to level 1", bloonManager.canGoToNextLevel());

        bloonManager.nextLevel();
        assertEquals("BloonManager level should advance to level 1", 1, bloonManager.getLevel());
    }

    @Test
    public void testPopBloonWithNoOpSoundService() {
        class TrackingSoundService extends NoOpSoundService {
            boolean popSoundPlayed = false;

            @Override
            public void playPopSound() {
                popSoundPlayed = true;
            }
        }

        TrackingSoundService soundService = new TrackingSoundService();
        NoOpMusicService musicService = new NoOpMusicService();

        BloonManager bloonManager = new BloonManager(null, null, soundService, musicService);
        Bloon redBloon = BloonFactory.createRedBloon();
        BloonActor bloonActor = new BloonActor(redBloon, 0, 0, null);

        bloonManager.popBloon(bloonActor, 1);
        assertTrue("Pop sound should be triggered on injected SoundService when popping a bloon", soundService.popSoundPlayed);
    }
}
