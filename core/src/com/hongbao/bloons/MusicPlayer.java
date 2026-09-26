package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.hongbao.bloons.audio.MusicService;


public class MusicPlayer implements MusicService {
	
	private Music backgroundMusic;
	
	public MusicPlayer() {
		backgroundMusic = null;
	}

	private void playMusic(String fileName) {
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		if (Gdx.audio != null && Gdx.files != null) {
			backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
			if (backgroundMusic != null) {
				backgroundMusic.setVolume(0.5f);
				backgroundMusic.setLooping(true);
				if (wasPlaying) {
					backgroundMusic.play();
				}
			}
		}
	}

	@Override
	public void playTitleMusic() {
		playMusic("music/title.mp3");
	}

	@Override
	public void playStageMusic() {
		playMusic("music/demystify_feast.mp3");
	}

	@Override
	public void playFinalBossMusic() {
		playMusic("music/night_falls.mp3");
	}
	
	@Override
	public void pause() {
		if (backgroundMusic != null) {
			backgroundMusic.pause();
		}
	}
	
	@Override
	public void resume() {
		if (backgroundMusic != null) {
			backgroundMusic.play();
		}
	}
	
	@Override
	public void stopMusic() {
		if (backgroundMusic != null) {
			backgroundMusic.stop();
		}
	}

	@Override
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
