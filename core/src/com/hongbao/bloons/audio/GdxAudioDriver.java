package com.hongbao.bloons.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class GdxAudioDriver implements AudioDriver {

    private final Map<String, Sound> soundCache = new HashMap<>();
    private Music currentMusic;

    @Override
    public void playSound(String soundPath, float volume, float pitch, float pan) {
        if (Gdx.audio == null || Gdx.files == null) {
            return;
        }

        Sound sound = soundCache.get(soundPath);
        if (sound == null) {
            try {
                sound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
                soundCache.put(soundPath, sound);
            } catch (Exception e) {
                if (Gdx.app != null) {
                    Gdx.app.error("GdxAudioDriver", "Error loading sound: " + soundPath, e);
                }
                return;
            }
        }

        sound.play(volume, pitch, pan);
    }

    @Override
    public void playMusic(String musicPath, float volume, boolean looping) {
        if (Gdx.audio == null || Gdx.files == null) {
            return;
        }

        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }

        try {
            currentMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
            currentMusic.setVolume(volume);
            currentMusic.setLooping(looping);
            currentMusic.play();
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("GdxAudioDriver", "Error loading music: " + musicPath, e);
            }
        }
    }

    @Override
    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    @Override
    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    @Override
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    @Override
    public void toggleMusic() {
        if (currentMusic != null) {
            if (currentMusic.isPlaying()) {
                currentMusic.pause();
            } else {
                currentMusic.play();
            }
        }
    }

    @Override
    public void dispose() {
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();

        if (currentMusic != null) {
            currentMusic.dispose();
            currentMusic = null;
        }
    }
}
