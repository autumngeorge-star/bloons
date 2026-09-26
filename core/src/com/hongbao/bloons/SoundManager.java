package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.EventBus;
import com.hongbao.bloons.events.GameStateChangedEvent;
import com.hongbao.bloons.events.LevelChangedEvent;
import com.hongbao.bloons.events.UIInteractionEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Unified Audio Listener and Manager.
 * Subscribes to central EventBus domain events to reactively play sound effects and music.
 * Preloads and caches audio resources, and isolates all direct calls to Gdx.audio.
 */
public class SoundManager {

    private Sound popSound;
    private Music currentMusic;
    private String currentMusicPath;
    private final Map<String, Music> musicCache = new HashMap<>();
    private final EventBus eventBus;

    public SoundManager() {
        this(EventBus.getInstance());
    }

    public SoundManager(EventBus eventBus) {
        this.eventBus = eventBus;
        preloadAudio();
        registerEventListeners();
    }

    private void preloadAudio() {
        if (Gdx.audio == null || Gdx.files == null) {
            return;
        }

        try {
            popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));

            preloadMusicTrack("music/title.mp3");
            preloadMusicTrack("music/demystify_feast.mp3");
            preloadMusicTrack("music/night_falls.mp3");
        } catch (Exception e) {
            System.err.println("Warning: Unable to preload audio: " + e.getMessage());
        }
    }

    private void preloadMusicTrack(String path) {
        if (Gdx.audio == null || Gdx.files == null) {
            return;
        }
        try {
            if (!musicCache.containsKey(path)) {
                Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
                music.setVolume(0.5f);
                music.setLooping(true);
                musicCache.put(path, music);
            }
        } catch (Exception e) {
            System.err.println("Warning: Unable to preload music track " + path + ": " + e.getMessage());
        }
    }

    private void registerEventListeners() {
        if (eventBus == null) {
            return;
        }

        eventBus.subscribe(BloonPoppedEvent.class, this::onBloonPopped);
        eventBus.subscribe(LevelChangedEvent.class, this::onLevelChanged);
        eventBus.subscribe(GameStateChangedEvent.class, this::onGameStateChanged);
        eventBus.subscribe(UIInteractionEvent.class, this::onUIInteraction);
    }

    private void onBloonPopped(BloonPoppedEvent event) {
        if (event.isPopped() && popSound != null) {
            popSound.play(0.5f);
        }
    }

    private void onLevelChanged(LevelChangedEvent event) {
        if (event.getNewLevel() == 1) {
            playStageMusic();
        } else if (event.getNewLevel() == 40) {
            playFinalBossMusic();
        }
    }

    private void onGameStateChanged(GameStateChangedEvent event) {
        if (event.getNewState() == null) {
            return;
        }

        switch (event.getNewState()) {
            case TITLE:
                playTitleMusic();
                break;
            case PAUSED:
                pause();
                break;
            case RESUMED:
                resume();
                break;
            default:
                break;
        }
    }

    private void onUIInteraction(UIInteractionEvent event) {
        if (event.getInteractionType() == UIInteractionEvent.InteractionType.TOGGLE_MUSIC) {
            toggleMusic();
        }
    }

    public void playMusic(String fileName) {
        boolean wasPlaying = true;
        if (currentMusic != null) {
            wasPlaying = currentMusic.isPlaying();
            currentMusic.stop();
        }

        currentMusic = musicCache.get(fileName);
        if (currentMusic == null && Gdx.audio != null && Gdx.files != null) {
            preloadMusicTrack(fileName);
            currentMusic = musicCache.get(fileName);
        }

        currentMusicPath = fileName;
        if (currentMusic != null && wasPlaying) {
            currentMusic.play();
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
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }

    public void resume() {
        if (currentMusic != null) {
            currentMusic.play();
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void toggleMusic() {
        if (currentMusic != null) {
            if (currentMusic.isPlaying()) {
                pause();
            } else {
                resume();
            }
        }
    }

    public void dispose() {
        if (popSound != null) {
            popSound.dispose();
            popSound = null;
        }
        for (Music music : musicCache.values()) {
            if (music != null) {
                music.dispose();
            }
        }
        musicCache.clear();
        currentMusic = null;
    }
}
