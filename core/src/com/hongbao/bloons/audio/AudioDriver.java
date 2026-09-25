package com.hongbao.bloons.audio;

public interface AudioDriver {
    void playSound(String soundPath, float volume, float pitch, float pan);
    void playMusic(String musicPath, float volume, boolean looping);
    void pauseMusic();
    void resumeMusic();
    void stopMusic();
    void toggleMusic();
    void dispose();
}
