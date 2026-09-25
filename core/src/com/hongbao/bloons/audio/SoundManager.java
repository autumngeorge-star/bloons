package com.hongbao.bloons.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.hongbao.bloons.event.BloonPoppedEvent;
import com.hongbao.bloons.event.DefaultEventBus;
import com.hongbao.bloons.event.EventBus;
import com.hongbao.bloons.event.LevelChangedEvent;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private final EventBus eventBus;
    private final Map<String, Sound> soundCache;
    private Music currentMusic;
    private String currentMusicPath;
    private float sfxVolume;
    private float musicVolume;

    public SoundManager() {
        this(DefaultEventBus.getDefault());
    }

    public SoundManager(EventBus eventBus) {
        this.eventBus = eventBus;
        this.soundCache = new HashMap<>();
        this.sfxVolume = 0.5f;
        this.musicVolume = 0.5f;

        registerEventListeners();
    }

    private void registerEventListeners() {
        if (eventBus != null) {
            eventBus.subscribe(BloonPoppedEvent.class, this::onBloonPopped);
            eventBus.subscribe(LevelChangedEvent.class, this::onLevelChanged);
        }
    }

    public void onBloonPopped(BloonPoppedEvent event) {
        playSound(AudioAssets.POP_SOUND);
    }

    public void onLevelChanged(LevelChangedEvent event) {
        if (event == null) {
            return;
        }
        if (event.getLevel() == 1) {
            playStageMusic();
        } else if (event.getLevel() == 40) {
            playFinalBossMusic();
        }
    }

    public void playSound(String fileName) {
        if (Gdx.audio == null || Gdx.files == null || fileName == null) {
            return;
        }
        try {
            Sound sound = soundCache.computeIfAbsent(fileName, path -> Gdx.audio.newSound(Gdx.files.internal(path)));
            sound.play(sfxVolume);
        } catch (Exception e) {
            // Log or ignore if audio hardware/files unavailable
        }
    }

    public void playMusic(String fileName) {
        if (fileName == null) {
            return;
        }
        boolean wasPlaying = currentMusic != null && isMusicPlaying();

        stopMusic();

        currentMusicPath = fileName;
        if (Gdx.audio != null && Gdx.files != null) {
            try {
                currentMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
                currentMusic.setVolume(musicVolume);
                currentMusic.setLooping(true);
                if (wasPlaying || currentMusicPath != null) {
                    currentMusic.play();
                }
            } catch (Exception e) {
                currentMusic = null;
            }
        }
    }

    public void playTitleMusic() {
        playMusic(AudioAssets.TITLE_MUSIC);
    }

    public void playStageMusic() {
        playMusic(AudioAssets.STAGE_MUSIC);
    }

    public void playFinalBossMusic() {
        playMusic(AudioAssets.FINAL_BOSS_MUSIC);
    }

    public void pauseMusic() {
        if (currentMusic != null) {
            try {
                currentMusic.pause();
            } catch (Exception ignored) {
            }
        }
    }

    public void resumeMusic() {
        if (currentMusic != null) {
            try {
                currentMusic.play();
            } catch (Exception ignored) {
            }
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            try {
                currentMusic.stop();
                currentMusic.dispose();
            } catch (Exception ignored) {
            }
            currentMusic = null;
        }
    }

    public void toggleMusic() {
        if (currentMusic != null) {
            if (isMusicPlaying()) {
                pauseMusic();
            } else {
                resumeMusic();
            }
        }
    }

    private boolean isMusicPlaying() {
        if (currentMusic == null) {
            return false;
        }
        try {
            return currentMusic.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = Math.max(0.0f, Math.min(1.0f, sfxVolume));
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, musicVolume));
        if (currentMusic != null) {
            try {
                currentMusic.setVolume(this.musicVolume);
            } catch (Exception ignored) {
            }
        }
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public String getCurrentMusicPath() {
        return currentMusicPath;
    }

    public void dispose() {
        stopMusic();
        for (Sound sound : soundCache.values()) {
            try {
                sound.dispose();
            } catch (Exception ignored) {
            }
        }
        soundCache.clear();
    }
}
