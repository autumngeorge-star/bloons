package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class MusicPlayer {
	
	private Music backgroundMusic;
	
	public MusicPlayer() {
		backgroundMusic = null;
	}

	public void playMusic(String fileName) {
		if (fileName == null || fileName.trim().isEmpty()) {
			return;
		}
		String path = fileName.trim();
		if ("stage".equalsIgnoreCase(path)) {
			playStageMusic();
			return;
		}
		if ("boss".equalsIgnoreCase(path) || "final_boss".equalsIgnoreCase(path)) {
			playFinalBossMusic();
			return;
		}
		if ("title".equalsIgnoreCase(path)) {
			playTitleMusic();
			return;
		}
		if (!path.contains("/") && !path.contains("\\")) {
			path = "music/" + path;
		}

		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		if (Gdx.audio != null && Gdx.files != null) {
			backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
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
