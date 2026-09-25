package com.hongbao.bloons.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.hongbao.bloons.MusicPlayer;

public class LibGDXAudioService implements AudioService {

    private Sound popSound;
    private Sound damageSound;
    private Sound towerPlacementSound;
    private Sound spellSound;
    private MusicPlayer musicPlayer;

    public LibGDXAudioService() {
        popSound = loadSound("music/pop.mp3");
        damageSound = loadSound("music/damage.mp3");
        towerPlacementSound = loadSound("music/place.mp3");
        spellSound = loadSound("music/spell.mp3");
        musicPlayer = new MusicPlayer();
    }

    private Sound loadSound(String fileName) {
        try {
            if (Gdx.files != null && Gdx.files.internal(fileName).exists()) {
                return Gdx.audio.newSound(Gdx.files.internal(fileName));
            }
        } catch (Exception e) {
            // Fail gracefully if sound file is missing or audio system unavailable
        }
        return null;
    }

    private void playSound(Sound sound) {
        if (sound != null) {
            try {
                sound.play(0.5f);
            } catch (Exception e) {
                // Fail gracefully
            }
        }
    }

    @Override
    public void playPopSound() {
        playSound(popSound);
    }

    @Override
    public void playDamageSound() {
        playSound(damageSound);
    }

    @Override
    public void playTowerPlacementSound() {
        playSound(towerPlacementSound);
    }

    @Override
    public void playSpellSound() {
        playSound(spellSound);
    }

    @Override
    public void playTitleMusic() {
        if (musicPlayer != null) {
            musicPlayer.playTitleMusic();
        }
    }

    @Override
    public void playStageMusic() {
        if (musicPlayer != null) {
            musicPlayer.playStageMusic();
        }
    }

    @Override
    public void playFinalBossMusic() {
        if (musicPlayer != null) {
            musicPlayer.playFinalBossMusic();
        }
    }

    @Override
    public void pauseMusic() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    @Override
    public void resumeMusic() {
        if (musicPlayer != null) {
            musicPlayer.resume();
        }
    }

    @Override
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
    }

    @Override
    public void toggleMusic() {
        if (musicPlayer != null) {
            musicPlayer.toggleMusic();
        }
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    @Override
    public void dispose() {
        if (popSound != null) {
            popSound.dispose();
            popSound = null;
        }
        if (damageSound != null) {
            damageSound.dispose();
            damageSound = null;
        }
        if (towerPlacementSound != null) {
            towerPlacementSound.dispose();
            towerPlacementSound = null;
        }
        if (spellSound != null) {
            spellSound.dispose();
            spellSound = null;
        }
        if (musicPlayer != null) {
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }
}
