package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicPlayer {

    private Music backgroundMusic;

    public MusicPlayer() {
        backgroundMusic = null;
    }

    private void playMusic(String fileName) {
        if (Gdx.audio == null) {
            return;
        }
        try {
            boolean wasPlaying = true;
            if (backgroundMusic != null) {
                wasPlaying = backgroundMusic.isPlaying();
            }
            stopMusic();
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
            if (backgroundMusic != null) {
                backgroundMusic.setVolume(0.5f);
                backgroundMusic.setLooping(true);
                if (wasPlaying) {
                    backgroundMusic.play();
                }
            }
        } catch (Exception e) {
            backgroundMusic = null;
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
            try {
                backgroundMusic.pause();
            } catch (Exception ignored) {
            }
        }
    }

    public void resume() {
        if (backgroundMusic != null) {
            try {
                backgroundMusic.play();
            } catch (Exception ignored) {
            }
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            try {
                backgroundMusic.stop();
            } catch (Exception ignored) {
            }
        }
    }

    public void toggleMusic() {
        if (backgroundMusic != null) {
            try {
                if (backgroundMusic.isPlaying()) {
                    pause();
                } else {
                    resume();
                }
            } catch (Exception ignored) {
            }
        }
    }
}
