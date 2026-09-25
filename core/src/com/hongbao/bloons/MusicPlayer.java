package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;


public class MusicPlayer implements Disposable {
	
	private Music backgroundMusic;
	private final Map<String, Music> musicCache;
	
	public MusicPlayer() {
		backgroundMusic = null;
		musicCache = new HashMap<>();
	}

	private void playMusic(String fileName) {
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		if (!musicCache.containsKey(fileName)) {
			musicCache.put(fileName, Gdx.audio.newMusic(Gdx.files.internal(fileName)));
		}
		backgroundMusic = musicCache.get(fileName);
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

	@Override
	public void dispose() {
		stopMusic();
		for (Music music : musicCache.values()) {
			if (music != null) {
				music.dispose();
			}
		}
		musicCache.clear();
		backgroundMusic = null;
	}

}
