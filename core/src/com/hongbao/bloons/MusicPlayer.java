package com.hongbao.bloons;

import com.badlogic.gdx.audio.Music;


public class MusicPlayer {
	
	private Music backgroundMusic;
	private AudioManager audioManager;
	
	public MusicPlayer(AudioManager audioManager) {
		this.audioManager = audioManager;
		this.backgroundMusic = null;
	}

	public MusicPlayer() {
		this(null);
	}

	public void setAudioManager(AudioManager audioManager) {
		this.audioManager = audioManager;
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	private void playMusic(String fileName) {
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		if (audioManager != null) {
			backgroundMusic = audioManager.getMusic(fileName);
		} else {
			backgroundMusic = null;
		}
		if (backgroundMusic != null) {
			backgroundMusic.setVolume(0.5f);
			backgroundMusic.setLooping(true);
			if (wasPlaying) {
				backgroundMusic.play();
			}
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
		if (backgroundMusic != null) {
			backgroundMusic.pause();
		}
	}
	
	public void resume() {
		if (backgroundMusic != null) {
			backgroundMusic.play();
		}
	}
	
	public void stopMusic() {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
	}

	public void toggleMusic() {
		if (backgroundMusic != null) {
			if (backgroundMusic.isPlaying()) {
				pause();
			} else {
				resume();
			}
		}
	}

}
