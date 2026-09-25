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

	public void playMusicTrack(String trackOrCue) {
		if (trackOrCue == null || trackOrCue.trim().isEmpty()) {
			return;
		}
		String cue = trackOrCue.trim();
		if ("stage".equalsIgnoreCase(cue) || "demystify_feast".equalsIgnoreCase(cue)) {
			playStageMusic();
		} else if ("final_boss".equalsIgnoreCase(cue) || "boss".equalsIgnoreCase(cue) || "night_falls".equalsIgnoreCase(cue)) {
			playFinalBossMusic();
		} else if ("title".equalsIgnoreCase(cue)) {
			playTitleMusic();
		} else {
			if (!cue.startsWith("music/")) {
				cue = "music/" + cue;
			}
			playMusic(cue);
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
