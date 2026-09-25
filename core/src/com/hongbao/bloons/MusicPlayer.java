package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class MusicPlayer {

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
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
		backgroundMusic.setVolume(0.5f);
		backgroundMusic.setLooping(true);
		if (wasPlaying) {
			backgroundMusic.play();
		}
	}

	public void playMusicTrack(String trackName) {
		if (trackName == null || trackName.trim().isEmpty()) {
			return;
		}
		String fileName = trackName.trim();
		if (!fileName.startsWith("music/")) {
			fileName = "music/" + fileName;
		}
		if (!fileName.endsWith(".mp3")) {
			fileName = fileName + ".mp3";
		}
		playMusic(fileName);
	}

	public void playTitleMusic() {
		playMusicTrack("title");
	}

	public void playStageMusic() {
		playMusicTrack("demystify_feast");
	}

	public void playFinalBossMusic() {
		playMusicTrack("night_falls");
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
