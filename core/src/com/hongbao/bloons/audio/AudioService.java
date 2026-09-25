package com.hongbao.bloons.audio;

import com.badlogic.gdx.utils.Disposable;

public interface AudioService extends Disposable {
    void playPopSound();
    void playDamageSound();
    void playTowerPlacementSound();
    void playSpellSound();

    void playTitleMusic();
    void playStageMusic();
    void playFinalBossMusic();
    void pauseMusic();
    void resumeMusic();
    void stopMusic();
    void toggleMusic();

    @Override
    void dispose();
}
