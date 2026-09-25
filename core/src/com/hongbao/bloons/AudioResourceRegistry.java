package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized audio resource lifecycle registry.
 * Manages native Sound and Music allocations to prevent memory leaks.
 */
public class AudioResourceRegistry implements Disposable {

    private final Map<String, Sound> soundRegistry = new HashMap<>();
    private Music currentMusic;

    public AudioResourceRegistry() {
        this.currentMusic = null;
    }

    /**
     * Retrieves or loads a Sound asset for the given internal file path.
     * The sound is cached and tracked by the registry for teardown disposal.
     *
     * @param filePath internal asset path
     * @return Sound instance
     */
    public synchronized Sound getSound(String filePath) {
        if (!soundRegistry.containsKey(filePath)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            soundRegistry.put(filePath, sound);
        }
        return soundRegistry.get(filePath);
    }

    /**
     * Alias for getSound(String filePath).
     */
    public synchronized Sound loadSound(String filePath) {
        return getSound(filePath);
    }

    /**
     * Loads a Music asset for the given internal file path.
     * Automatically disposes the previously tracked background music instance.
     *
     * @param filePath internal asset path
     * @return Music instance
     */
    public synchronized Music loadMusic(String filePath) {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        return currentMusic;
    }

    /**
     * Alias for loadMusic(String filePath).
     */
    public synchronized Music getMusic(String filePath) {
        return loadMusic(filePath);
    }

    /**
     * Returns the currently active Music object, if any.
     */
    public synchronized Music getCurrentMusic() {
        return currentMusic;
    }

    /**
     * Disposes all tracked native audio instances (sounds and music).
     */
    @Override
    public synchronized void dispose() {
        for (Sound sound : soundRegistry.values()) {
            if (sound != null) {
                sound.dispose();
            }
        }
        soundRegistry.clear();

        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
    }
}
