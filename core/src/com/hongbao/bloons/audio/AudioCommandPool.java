package com.hongbao.bloons.audio;

import com.badlogic.gdx.utils.Pool;

public class AudioCommandPool extends Pool<AudioCommand> {

    public AudioCommandPool() {
        super(32, 512);
    }

    public AudioCommandPool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    @Override
    protected AudioCommand newObject() {
        return new AudioCommand();
    }
}
