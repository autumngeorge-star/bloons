package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;

import java.util.HashMap;
import java.util.Map;

public class SoundManager implements Disposable, SoundEventListener {

    private final Map<String, Sound> soundCache;
    private final Map<String, Music> musicCache;
    private boolean enabled;

    public SoundManager() {
        this.soundCache = new HashMap<>();
        this.musicCache = new HashMap<>();
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Sound getSound(String fileName) {
        if (Gdx.audio == null) {
            return null;
        }
        Sound sound = soundCache.get(fileName);
        if (sound == null && Gdx.files != null) {
            try {
                if (Gdx.files.internal(fileName).exists()) {
                    sound = Gdx.audio.newSound(Gdx.files.internal(fileName));
                    soundCache.put(fileName, sound);
                }
            } catch (Exception e) {
                // Return null if audio device creation fails or in headless mode
            }
        }
        return sound;
    }

    public Music getMusic(String fileName) {
        if (Gdx.audio == null) {
            return null;
        }
        Music music = musicCache.get(fileName);
        if (music == null && Gdx.files != null) {
            try {
                if (Gdx.files.internal(fileName).exists()) {
                    music = Gdx.audio.newMusic(Gdx.files.internal(fileName));
                    musicCache.put(fileName, music);
                }
            } catch (Exception e) {
                // Return null if audio device creation fails or in headless mode
            }
        }
        return music;
    }

    public void playSound(String fileName, float volume) {
        playSound(fileName, volume, 1.0f);
    }

    public void playSound(String fileName, float volume, float pitch) {
        if (!enabled || Gdx.audio == null) {
            return;
        }
        try {
            Sound sound = getSound(fileName);
            if (sound != null) {
                sound.play(volume, pitch, 0.0f);
            } else if (!"music/pop.mp3".equals(fileName)) {
                // Fallback to pop sound if a specific sound effect file isn't present
                Sound fallback = getSound("music/pop.mp3");
                if (fallback != null) {
                    fallback.play(volume, pitch, 0.0f);
                }
            }
        } catch (Exception e) {
            // Safe fallback for headless or missing audio device
        }
    }

    public Map<String, Sound> getSoundCache() {
        return soundCache;
    }

    public Map<String, Music> getMusicCache() {
        return musicCache;
    }

    @Override
    public void onTowerPlaced(GirlActor girlActor) {
        playSound("music/place.mp3", 0.4f, 0.9f);
    }

    @Override
    public void onTowerUpgraded(GirlActor girlActor) {
        playSound("music/upgrade.mp3", 0.5f, 1.3f);
    }

    @Override
    public void onTowerSold(GirlActor girlActor) {
        playSound("music/sell.mp3", 0.4f, 0.7f);
    }

    @Override
    public void onSpellActivated(SpellCardActor spellCardActor) {
        playSound("music/spell.mp3", 0.5f, 1.2f);
    }

    @Override
    public void onBloonDamaged(BloonActor bloonActor, int damage) {
        playSound("music/damage.mp3", 0.2f, 1.1f);
    }

    @Override
    public void onBloonPopped(BloonActor bloonActor, int damage) {
        playSound("music/pop.mp3", 0.5f, 1.0f);
    }

    @Override
    public void onBulletFired(BulletActor bulletActor) {
        playSound("music/shoot.mp3", 0.1f, 1.8f);
    }

    @Override
    public void dispose() {
        for (Sound sound : soundCache.values()) {
            if (sound != null) {
                try {
                    sound.dispose();
                } catch (Exception e) {
                    // Ignore disposal errors in test/mock setups
                }
            }
        }
        soundCache.clear();

        for (Music music : musicCache.values()) {
            if (music != null) {
                try {
                    music.stop();
                    music.dispose();
                } catch (Exception e) {
                    // Ignore disposal errors in test/mock setups
                }
            }
        }
        musicCache.clear();
    }
}
