package com.hongbao.bloons.audio;

public class AudioSystem {

    private static AudioSystem instance;

    private final AudioCommandQueue queue;
    private final AudioProcessor processor;

    public static synchronized AudioSystem getInstance() {
        if (instance == null) {
            instance = new AudioSystem();
        }
        return instance;
    }

    public static synchronized void setInstance(AudioSystem customInstance) {
        instance = customInstance;
    }

    public AudioSystem() {
        this.queue = new AudioCommandQueue();
        this.processor = new AudioProcessor(queue, new GdxAudioDriver());
    }

    public AudioSystem(AudioCommandQueue queue, AudioProcessor processor) {
        this.queue = queue;
        this.processor = processor;
    }

    public boolean playSound(String soundPath, float volume) {
        return queue.enqueueSound(soundPath, volume);
    }

    public boolean playSound(String soundPath, float volume, float pitch, float pan) {
        return queue.enqueueSound(soundPath, volume, pitch, pan);
    }

    public boolean playMusic(String musicPath, float volume, boolean looping) {
        return queue.enqueueMusicPlay(musicPath, volume, looping);
    }

    public boolean pauseMusic() {
        return queue.enqueueMusicControl(AudioCommandType.MUSIC_PAUSE);
    }

    public boolean resumeMusic() {
        return queue.enqueueMusicControl(AudioCommandType.MUSIC_RESUME);
    }

    public boolean stopMusic() {
        return queue.enqueueMusicControl(AudioCommandType.MUSIC_STOP);
    }

    public boolean toggleMusic() {
        return queue.enqueueMusicControl(AudioCommandType.MUSIC_TOGGLE);
    }

    public void processFrame() {
        processor.processFrame();
    }

    public AudioCommandQueue getQueue() {
        return queue;
    }

    public AudioProcessor getProcessor() {
        return processor;
    }

    public void dispose() {
        processor.dispose();
    }
}
