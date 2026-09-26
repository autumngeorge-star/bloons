package com.hongbao.bloons.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Concrete {@link SoundService} implementation backed by LibGDX audio APIs.
 */
public class LibGdxSoundService implements SoundService {
    private Sound popSound;

    public LibGdxSoundService() {
    }

    @Override
    public void playPopSound() {
        if (popSound == null && Gdx.audio != null && Gdx.files != null) {
            popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
        }
        if (popSound != null) {
            popSound.play(0.5f);
        }
    }

    @Override
    public void playSound(String soundFilePath, float volume) {
        if (Gdx.audio != null && Gdx.files != null) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(soundFilePath));
            if (sound != null) {
                sound.play(volume);
            }
        }
    }

    @Override
    public void dispose() {
        if (popSound != null) {
            popSound.dispose();
            popSound = null;
        }
    }
}
