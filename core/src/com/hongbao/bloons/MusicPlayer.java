package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class MusicPlayer {
	
	private Music backgroundMusic;
	private PreferenceManager preferenceManager;
	private boolean musicEnabled = true;
	private float volume = 0.5f;
	
	public MusicPlayer() {
		this(null);
	}

	public MusicPlayer(PreferenceManager preferenceManager) {
		backgroundMusic = null;
		setPreferenceManager(preferenceManager);
	}

	public void setPreferenceManager(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
		if (preferenceManager != null) {
			this.musicEnabled = preferenceManager.isMusicEnabled();
			this.volume = preferenceManager.getVolume();
		}
	}

	private void playMusic(String fileName) {
		boolean wasPlaying = musicEnabled;
		if (backgroundMusic != null) {
			wasPlaying = wasPlaying && backgroundMusic.isPlaying();
		}
		stopMusic();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
		backgroundMusic.setVolume(volume);
		backgroundMusic.setLooping(true);
		if (wasPlaying && musicEnabled) {
			backgroundMusic.play();
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
		if (backgroundMusic != null && musicEnabled) {
			backgroundMusic.play();
		}
	}
	
	public void stopMusic() {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
	}

	public void toggleMusic() {
		setMusicEnabled(!musicEnabled);
	}

	public boolean isMusicEnabled() {
		return musicEnabled;
	}

	public void setMusicEnabled(boolean enabled) {
		this.musicEnabled = enabled;
		if (preferenceManager != null) {
			preferenceManager.setMusicEnabled(enabled);
		}
		if (backgroundMusic != null) {
			if (enabled) {
				backgroundMusic.play();
			} else {
				backgroundMusic.pause();
			}
		}
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		if (preferenceManager != null) {
			preferenceManager.setVolume(volume);
		}
		if (backgroundMusic != null) {
			backgroundMusic.setVolume(volume);
		}
	}

}
