package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.hongbao.bloons.listeners.BloonEventListener;
import com.hongbao.bloons.listeners.GameLifecycleListener;
import com.hongbao.bloons.listeners.LevelEventListener;

public class SoundManager implements BloonEventListener, LevelEventListener, GameLifecycleListener {

    private Sound popSound;
    private MusicPlayer musicPlayer;

    public SoundManager() {
        this(
            Gdx.audio != null ? Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3")) : null,
            new MusicPlayer()
        );
    }

    public SoundManager(Sound popSound, MusicPlayer musicPlayer) {
        this.popSound = popSound;
        this.musicPlayer = musicPlayer;
    }

    @Override
    public void onBloonPopped() {
        if (popSound != null) {
            popSound.play(0.5f);
        }
    }

    @Override
    public void onLevelChanged(int newLevel) {
        if (newLevel == 1) {
            playStageMusic();
        } else if (newLevel == 40) {
            playFinalBossMusic();
        }
    }

    @Override
    public void onGamePaused() {
        pause();
    }

    @Override
    public void onGameResumed() {
        resume();
    }

    @Override
    public void onGameDisposed() {
        dispose();
    }

    public void playTitleMusic() {
        if (musicPlayer != null) {
            musicPlayer.playTitleMusic();
        }
    }

    public void playStageMusic() {
        if (musicPlayer != null) {
            musicPlayer.playStageMusic();
        }
    }

    public void playFinalBossMusic() {
        if (musicPlayer != null) {
            musicPlayer.playFinalBossMusic();
        }
    }

    public void pause() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    public void resume() {
        if (musicPlayer != null) {
            musicPlayer.resume();
        }
    }

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
    }

    public void toggleMusic() {
        if (musicPlayer != null) {
            musicPlayer.toggleMusic();
        }
    }

    public void dispose() {
        if (popSound != null) {
            popSound.dispose();
            popSound = null;
        }
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
    }

    public Sound getPopSound() {
        return popSound;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }
}
