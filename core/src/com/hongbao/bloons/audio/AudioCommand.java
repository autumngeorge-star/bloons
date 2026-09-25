package com.hongbao.bloons.audio;

import com.badlogic.gdx.utils.Pool.Poolable;

public class AudioCommand implements Poolable {

    private AudioCommandType type;
    private String soundPath;
    private float volume;
    private float pitch;
    private float pan;
    private boolean looping;

    public AudioCommand() {
        reset();
    }

    public void initSoundPlay(String soundPath, float volume, float pitch, float pan) {
        this.type = AudioCommandType.SOUND_PLAY;
        this.soundPath = soundPath;
        this.volume = volume;
        this.pitch = pitch;
        this.pan = pan;
        this.looping = false;
    }

    public void initMusicPlay(String musicPath, float volume, boolean looping) {
        this.type = AudioCommandType.MUSIC_PLAY;
        this.soundPath = musicPath;
        this.volume = volume;
        this.pitch = 1.0f;
        this.pan = 0.0f;
        this.looping = looping;
    }

    public void initMusicControl(AudioCommandType type) {
        this.type = type;
        this.soundPath = null;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.pan = 0.0f;
        this.looping = false;
    }

    @Override
    public void reset() {
        this.type = null;
        this.soundPath = null;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.pan = 0.0f;
        this.looping = false;
    }

    public AudioCommandType getType() {
        return type;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public float getPan() {
        return pan;
    }

    public boolean isLooping() {
        return looping;
    }
}
