package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;


public class MusicPlayer implements Disposable {
	
	private Music backgroundMusic;
	
	public MusicPlayer() {
		backgroundMusic = null;
	}

	public Music getBackgroundMusic() {
		return backgroundMusic;
	}

	private void playMusic(String fileName) {
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
			stopMusic();
		}
		if (Gdx.audio != null && Gdx.files != null) {
			try {
				if (Gdx.files.internal(fileName).exists()) {
					backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
					backgroundMusic.setVolume(0.5f);
					backgroundMusic.setLooping(true);
					if (wasPlaying) {
						backgroundMusic.play();
					}
				}
			} catch (Exception e) {
				// Return null safely if audio device is unavailable
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
			try {
				backgroundMusic.stop();
				backgroundMusic.dispose();
			} catch (Exception e) {
				// Safe cleanup
			}
			backgroundMusic = null;
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

	@Override
	public void dispose() {
		stopMusic();
	}

}
