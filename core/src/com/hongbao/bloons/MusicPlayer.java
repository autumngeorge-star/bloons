package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;

public class MusicPlayer {

	private AudioManager audioManager;

	public MusicPlayer() {
		this.audioManager = null;
	}

	public MusicPlayer(AudioManager audioManager) {
		this.audioManager = audioManager;
	}

	private AudioManager getAudioManager() {
		if (audioManager != null) {
			return audioManager;
		}
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			return ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getAudioManager();
		}
		return null;
	}

	private void playMusic(String fileName) {
		AudioManager mgr = getAudioManager();
		if (mgr != null) {
			mgr.playMusic(fileName, 0.5f, true);
		}
	}

	public void playTitleMusic() {
		playMusic("music/title.mp3");
	}

	public void playStageMusic() {
		playMusic("music/demystify_feast.mp3");
	}

	public void playFinalBossMusic() {
		playMusic("music/night_falls.mp3");
	}

	public void pause() {
		AudioManager mgr = getAudioManager();
		if (mgr != null) {
			mgr.pauseMusic();
		}
	}

	public void resume() {
		AudioManager mgr = getAudioManager();
		if (mgr != null) {
			mgr.resumeMusic();
		}
	}

	public void stopMusic() {
		AudioManager mgr = getAudioManager();
		if (mgr != null) {
			mgr.stopMusic();
		}
	}

	public void toggleMusic() {
		AudioManager mgr = getAudioManager();
		if (mgr != null) {
			mgr.toggleMusic();
		}
	}

}
