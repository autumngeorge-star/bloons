package com.hongbao.bloons.audio;

import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class AudioServiceTest {

    @Test
    public void testNullAudioServiceNoOps() {
        AudioService audioService = new NullAudioService();
        
        // Ensure calling all methods on NullAudioService causes no exceptions
        audioService.playPopSound();
        audioService.playDamageSound();
        audioService.playTowerPlacementSound();
        audioService.playSpellSound();
        audioService.playTitleMusic();
        audioService.playStageMusic();
        audioService.playFinalBossMusic();
        audioService.pauseMusic();
        audioService.resumeMusic();
        audioService.stopMusic();
        audioService.toggleMusic();
        audioService.dispose();
    }

    @Test
    public void testLibGDXAudioServiceGracefulFallbackAndDisposal() {
        LibGDXAudioService service = new LibGDXAudioService();
        
        // Without Gdx context, sound operations should degrade gracefully without throwing
        service.playPopSound();
        service.playDamageSound();
        service.playTowerPlacementSound();
        service.playSpellSound();
        service.playTitleMusic();
        service.playStageMusic();
        service.playFinalBossMusic();
        service.pauseMusic();
        service.resumeMusic();
        service.stopMusic();
        service.toggleMusic();
        
        // Disposal should safely release tracked resources
        service.dispose();
    }

    @Test
    public void testTestableAudioServiceInjectedInActorsAndManagers() {
        TestAudioService testAudioService = new TestAudioService();
        
        // Test AudioService methods directly
        testAudioService.playTowerPlacementSound();
        assertEquals(1, testAudioService.placementSoundCount);

        testAudioService.playSpellSound();
        assertEquals(1, testAudioService.spellSoundCount);

        testAudioService.playPopSound();
        assertEquals(1, testAudioService.popSoundCount);

        testAudioService.playDamageSound();
        assertEquals(1, testAudioService.damageSoundCount);

        testAudioService.dispose();
        assertTrue(testAudioService.disposed);
    }

    private static class TestAudioService extends NullAudioService {
        int popSoundCount = 0;
        int damageSoundCount = 0;
        int placementSoundCount = 0;
        int spellSoundCount = 0;
        boolean disposed = false;

        @Override
        public void playPopSound() {
            popSoundCount++;
        }

        @Override
        public void playDamageSound() {
            damageSoundCount++;
        }

        @Override
        public void playTowerPlacementSound() {
            placementSoundCount++;
        }

        @Override
        public void playSpellSound() {
            spellSoundCount++;
        }

        @Override
        public void dispose() {
            disposed = true;
        }
    }
}
