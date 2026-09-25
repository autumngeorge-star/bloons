package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class MusicPlayer implements Disposable {
	
	private Music backgroundMusic;
	private final Map<String, Music> musicCache;
	private final Map<String, Sound> soundCache;
	
	public MusicPlayer() {
		backgroundMusic = null;
		musicCache = new HashMap<>();
		soundCache = new HashMap<>();
	}

	public Music getMusic(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (musicCache.containsKey(fileName)) {
			return musicCache.get(fileName);
		}
		try {
			if (Gdx.files == null || Gdx.audio == null) {
				return null;
			}
			FileHandle file = Gdx.files.internal(fileName);
			if (file != null && file.exists()) {
				Music music = Gdx.audio.newMusic(file);
				if (music != null) {
					musicCache.put(fileName, music);
				}
				return music;
			}
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.log("MusicPlayer", "Failed to load music asset: " + fileName, e);
			}
		}
		return null;
	}

	public Sound getSound(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (soundCache.containsKey(fileName)) {
			return soundCache.get(fileName);
		}
		try {
			if (Gdx.files == null || Gdx.audio == null) {
				return null;
			}
			FileHandle file = Gdx.files.internal(fileName);
			if (file != null && file.exists()) {
				Sound sound = Gdx.audio.newSound(file);
				if (sound != null) {
					soundCache.put(fileName, sound);
				}
				return sound;
			}
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.log("MusicPlayer", "Failed to load sound asset: " + fileName, e);
			}
		}
		return null;
	}

	public void playSound(String fileName) {
		playSound(fileName, 1.0f);
	}

	public void playSound(String fileName, float volume) {
		Sound sound = getSound(fileName);
		if (sound != null) {
			sound.play(volume);
		}
	}

	private void playMusic(String fileName) {
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		backgroundMusic = getMusic(fileName);
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

	@Override
	public void dispose() {
		if (backgroundMusic != null) {
			stopMusic();
			backgroundMusic = null;
		}
		for (Music music : musicCache.values()) {
			if (music != null) {
				try {
					music.dispose();
				} catch (Exception e) {
					// Ignore exception if already disposed
				}
			}
		}
		musicCache.clear();

		for (Sound sound : soundCache.values()) {
			if (sound != null) {
				try {
					sound.dispose();
				} catch (Exception e) {
					// Ignore exception if already disposed
				}
			}
		}
		soundCache.clear();
	}

	public Map<String, Music> getMusicCache() {
		return musicCache;
	}

	public Map<String, Sound> getSoundCache() {
		return soundCache;
	}

}
