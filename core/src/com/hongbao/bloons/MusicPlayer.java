package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class MusicPlayer implements LevelEventListener {
	
	private Music backgroundMusic;
	
	public MusicPlayer() {
		backgroundMusic = null;
	}

	@Override
	public void onLevelChanged(int level) {
		if (level == 1) {
			playStageMusic();
		} else if (level == 40) {
			playFinalBossMusic();
		}
	}

	private void playMusic(String fileName) {
		if (Gdx.audio == null || Gdx.files == null) {
			return;
		}
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
		backgroundMusic.setVolume(0.5f);
		backgroundMusic.setLooping(true);
		if (wasPlaying) {
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
