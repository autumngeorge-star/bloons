package com.hongbao.bloons.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AudioProcessor {

    private final AudioCommandQueue queue;
    private AudioDriver driver;
    private int maxDuplicatesPerFrame;
    private float volumeScalePerDuplicate;

    private final List<AudioCommand> drainedCommands = new ArrayList<>();
    private final Map<String, List<AudioCommand>> soundGroupMap = new HashMap<>();

    public AudioProcessor(AudioCommandQueue queue, AudioDriver driver) {
        this(queue, driver, 1, 0.02f);
    }

    public AudioProcessor(AudioCommandQueue queue, AudioDriver driver, int maxDuplicatesPerFrame, float volumeScalePerDuplicate) {
        this.queue = queue;
        this.driver = driver;
        this.maxDuplicatesPerFrame = maxDuplicatesPerFrame;
        this.volumeScalePerDuplicate = volumeScalePerDuplicate;
    }

    public void processFrame() {
        if (driver == null) {
            return;
        }

        drainedCommands.clear();
        queue.drainTo(drainedCommands);

        if (drainedCommands.isEmpty()) {
            return;
        }

        soundGroupMap.clear();

        for (int i = 0; i < drainedCommands.size(); i++) {
            AudioCommand cmd = drainedCommands.get(i);
            AudioCommandType type = cmd.getType();

            if (type == AudioCommandType.SOUND_PLAY) {
                String soundPath = cmd.getSoundPath();
                List<AudioCommand> group = soundGroupMap.get(soundPath);
                if (group == null) {
                    group = new ArrayList<>();
                    soundGroupMap.put(soundPath, group);
                }
                group.add(cmd);
            } else if (type != null) {
                processMusicCommand(cmd);
            }
        }

        for (Map.Entry<String, List<AudioCommand>> entry : soundGroupMap.entrySet()) {
            String soundPath = entry.getKey();
            List<AudioCommand> group = entry.getValue();
            processSoundGroup(soundPath, group);
        }

        for (int i = 0; i < drainedCommands.size(); i++) {
            queue.freeCommand(drainedCommands.get(i));
        }

        drainedCommands.clear();
        soundGroupMap.clear();
    }

    private void processMusicCommand(AudioCommand cmd) {
        switch (cmd.getType()) {
            case MUSIC_PLAY:
                driver.playMusic(cmd.getSoundPath(), cmd.getVolume(), cmd.isLooping());
                break;
            case MUSIC_PAUSE:
                driver.pauseMusic();
                break;
            case MUSIC_RESUME:
                driver.resumeMusic();
                break;
            case MUSIC_STOP:
                driver.stopMusic();
                break;
            case MUSIC_TOGGLE:
                driver.toggleMusic();
                break;
            default:
                break;
        }
    }

    private void processSoundGroup(String soundPath, List<AudioCommand> group) {
        int count = group.size();
        if (count == 0) return;

        float maxVolume = 0.0f;
        float pitch = group.get(0).getPitch();
        float pan = group.get(0).getPan();

        for (int i = 0; i < count; i++) {
            AudioCommand cmd = group.get(i);
            if (cmd.getVolume() > maxVolume) {
                maxVolume = cmd.getVolume();
            }
        }

        float scaledVolume = Math.min(1.0f, maxVolume * (1.0f + volumeScalePerDuplicate * Math.min(count - 1, 10)));

        int playsToEmit = Math.min(count, maxDuplicatesPerFrame);
        for (int p = 0; p < playsToEmit; p++) {
            driver.playSound(soundPath, scaledVolume, pitch, pan);
        }
    }

    public void setAudioDriver(AudioDriver driver) {
        this.driver = driver;
    }

    public AudioDriver getAudioDriver() {
        return driver;
    }

    public int getMaxDuplicatesPerFrame() {
        return maxDuplicatesPerFrame;
    }

    public void setMaxDuplicatesPerFrame(int maxDuplicatesPerFrame) {
        this.maxDuplicatesPerFrame = maxDuplicatesPerFrame;
    }

    public float getVolumeScalePerDuplicate() {
        return volumeScalePerDuplicate;
    }

    public void setVolumeScalePerDuplicate(float volumeScalePerDuplicate) {
        this.volumeScalePerDuplicate = volumeScalePerDuplicate;
    }

    public void dispose() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
