package com.hongbao.bloons.audio;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioCommandQueue {

    public static final int DEFAULT_MAX_QUEUE_SIZE = 512;

    private final ConcurrentLinkedQueue<AudioCommand> queue;
    private final AtomicInteger currentSize;
    private final int maxQueueSize;
    private final AudioCommandPool pool;

    public AudioCommandQueue() {
        this(DEFAULT_MAX_QUEUE_SIZE, new AudioCommandPool());
    }

    public AudioCommandQueue(int maxQueueSize, AudioCommandPool pool) {
        this.maxQueueSize = maxQueueSize;
        this.queue = new ConcurrentLinkedQueue<>();
        this.currentSize = new AtomicInteger(0);
        this.pool = pool;
    }

    public boolean enqueueSound(String soundPath, float volume) {
        return enqueueSound(soundPath, volume, 1.0f, 0.0f);
    }

    public boolean enqueueSound(String soundPath, float volume, float pitch, float pan) {
        if (currentSize.get() >= maxQueueSize) {
            return false;
        }

        AudioCommand cmd = pool.obtain();
        cmd.initSoundPlay(soundPath, volume, pitch, pan);

        queue.add(cmd);
        currentSize.incrementAndGet();
        return true;
    }

    public boolean enqueueMusicPlay(String musicPath, float volume, boolean looping) {
        if (currentSize.get() >= maxQueueSize) {
            return false;
        }

        AudioCommand cmd = pool.obtain();
        cmd.initMusicPlay(musicPath, volume, looping);

        queue.add(cmd);
        currentSize.incrementAndGet();
        return true;
    }

    public boolean enqueueMusicControl(AudioCommandType type) {
        if (currentSize.get() >= maxQueueSize) {
            return false;
        }

        AudioCommand cmd = pool.obtain();
        cmd.initMusicControl(type);

        queue.add(cmd);
        currentSize.incrementAndGet();
        return true;
    }

    public void drainTo(List<AudioCommand> outList) {
        AudioCommand cmd;
        while ((cmd = queue.poll()) != null) {
            currentSize.decrementAndGet();
            outList.add(cmd);
        }
    }

    public void freeCommand(AudioCommand cmd) {
        pool.free(cmd);
    }

    public int size() {
        return currentSize.get();
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public AudioCommandPool getPool() {
        return pool;
    }
}
