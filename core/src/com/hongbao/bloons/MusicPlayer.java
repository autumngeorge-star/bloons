package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class MusicPlayer {
	
	private Music backgroundMusic;
	
	public MusicPlayer() {
		backgroundMusic = null;
	}

	private PreferencesManager getPreferencesManager() {
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			return ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getPreferencesManager();
		}
		return null;
	}

	private void playMusic(String fileName) {
		PreferencesManager prefs = getPreferencesManager();
		boolean musicEnabled = prefs == null || prefs.isMusicEnabled(true);
		float volume = prefs != null ? prefs.getMusicVolume(0.5f) : 0.5f;

		boolean wasPlaying = musicEnabled;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
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
		PreferencesManager prefs = getPreferencesManager();
		boolean musicEnabled = prefs == null || prefs.isMusicEnabled(true);
		if (backgroundMusic != null && musicEnabled) {
			backgroundMusic.play();
		}
	}
	
	public void stopMusic() {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
	}

	public void setVolume(float volume) {
		if (backgroundMusic != null) {
			backgroundMusic.setVolume(volume);
		}
		PreferencesManager prefs = getPreferencesManager();
		if (prefs != null) {
			prefs.saveMusicVolume(volume);
			prefs.flush();
		}
	}

	public void toggleMusic() {
		PreferencesManager prefs = getPreferencesManager();
		if (backgroundMusic != null) {
			if (backgroundMusic.isPlaying()) {
				pause();
				if (prefs != null) {
					prefs.saveMusicEnabled(false);
					prefs.flush();
				}
			} else {
				if (prefs != null) {
					prefs.saveMusicEnabled(true);
					prefs.flush();
				}
				resume();
			}
		} else {
			if (prefs != null) {
				boolean currentState = prefs.isMusicEnabled(true);
				prefs.saveMusicEnabled(!currentState);
				prefs.flush();
			}
		}
	}

}

