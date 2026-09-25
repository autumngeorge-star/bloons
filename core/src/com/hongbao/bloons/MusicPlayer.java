package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.hongbao.bloons.events.AudioControlEvent;
import com.hongbao.bloons.events.AudioToggleEvent;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameEventManager;
import com.hongbao.bloons.events.LevelChangedEvent;

/**
 * Audio subscriber and manager for sound effect and background music playback.
 */
public class MusicPlayer {

    private Sound popSound;
    private Music backgroundMusic;

    public MusicPlayer() {
        backgroundMusic = null;
        registerEventListeners();
    }

    private void registerEventListeners() {
        GameEventManager manager = GameEventManager.getInstance();

        manager.subscribe(BloonPoppedEvent.class, event -> onBloonPopped());
        manager.subscribe(LevelChangedEvent.class, this::onLevelChanged);
        manager.subscribe(AudioToggleEvent.class, event -> toggleMusic());
        manager.subscribe(AudioControlEvent.class, this::onAudioControl);
    }

    private void onBloonPopped() {
        if (Gdx.audio != null) {
            if (popSound == null && Gdx.files != null) {
                popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
            }
            if (popSound != null) {
                popSound.play(0.5f);
            }
        }
    }

    private void onLevelChanged(LevelChangedEvent event) {
        if (event.getLevel() == 1) {
            playStageMusic();
        } else if (event.getLevel() == 40) {
            playFinalBossMusic();
        }
    }

    private void onAudioControl(AudioControlEvent event) {
        switch (event) {
            case PLAY_TITLE:
                playTitleMusic();
                break;
            case PAUSE:
                pause();
                break;
            case RESUME:
                resume();
                break;
            case STOP:
                stopMusic();
                break;
        }
    }

    private void playMusic(String fileName) {
        if (Gdx.audio == null || Gdx.files == null) {
            return;
        }
        boolean wasPlaying = true;
        if (backgroundMusic != null) {
            wasPlaying = backgroundMusic.isPlaying();
        }
        stopMusic();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.setLooping(true);
        if (wasPlaying) {
            backgroundMusic.play();
        }
    }

    public void playTitleMusic() {
        playMusic("music/title.mp3");
    }

    public void playStageMusic() {
        playMusic("music/demystify_feast.mp3");
    }

    public void playFinalBossMusic() {
        playMusic("music/night_falls.mp3");
    }

    public void pause() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    public void resume() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void toggleMusic() {
        if (backgroundMusic != null) {
            if (backgroundMusic.isPlaying()) {
                pause();
            } else {
                resume();
            }
        }
    }
}
