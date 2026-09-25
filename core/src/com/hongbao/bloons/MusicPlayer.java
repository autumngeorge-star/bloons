package com.hongbao.bloons;

import com.hongbao.bloons.audio.AudioAssets;
import com.hongbao.bloons.audio.SoundManager;

public class MusicPlayer {
	
	private final SoundManager soundManager;

	public MusicPlayer() {
		this(new SoundManager());
	}

	public MusicPlayer(SoundManager soundManager) {
		this.soundManager = soundManager;
	}

	public void playTitleMusic() {
		if (soundManager != null) {
			soundManager.playTitleMusic();
		}
	}

	public void playStageMusic() {
		if (soundManager != null) {
			soundManager.playStageMusic();
		}
	}

	public void playFinalBossMusic() {
		if (soundManager != null) {
			soundManager.playFinalBossMusic();
		}
	}
	
	public void pause() {
		if (soundManager != null) {
			soundManager.pauseMusic();
		}
	}
	
	public void resume() {
		if (soundManager != null) {
			soundManager.resumeMusic();
		}
	}
	
	public void stopMusic() {
		if (soundManager != null) {
			soundManager.stopMusic();
		}
	}

	public void toggleMusic() {
		if (soundManager != null) {
			soundManager.toggleMusic();
		}
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}
}
