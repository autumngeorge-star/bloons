package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AudioManager implements Disposable {

	private final Map<String, Music> musicMap;
	private final Map<String, Sound> soundMap;

	public AudioManager() {
		this.musicMap = new HashMap<>();
		this.soundMap = new HashMap<>();
	}

	public Music getMusic(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (musicMap.containsKey(fileName)) {
			return musicMap.get(fileName);
		}
		try {
			FileHandle handle = Gdx.files != null ? Gdx.files.internal(fileName) : null;
			if (handle != null && handle.exists()) {
				Music music = Gdx.audio.newMusic(handle);
				musicMap.put(fileName, music);
				return music;
			} else if (Gdx.app != null) {
				Gdx.app.error("AudioManager", "Music file not found: " + fileName);
			}
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.error("AudioManager", "Error loading music file: " + fileName, e);
			}
		}
		return null;
	}

	public Sound getSound(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (soundMap.containsKey(fileName)) {
			return soundMap.get(fileName);
		}
		try {
			FileHandle handle = Gdx.files != null ? Gdx.files.internal(fileName) : null;
			if (handle != null && handle.exists()) {
				Sound sound = Gdx.audio.newSound(handle);
				soundMap.put(fileName, sound);
				return sound;
			} else if (Gdx.app != null) {
				Gdx.app.error("AudioManager", "Sound file not found: " + fileName);
			}
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.error("AudioManager", "Error loading sound file: " + fileName, e);
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		for (Music music : musicMap.values()) {
			if (music != null) {
				try {
					if (music.isPlaying()) {
						music.stop();
					}
					music.dispose();
				} catch (Exception e) {
					if (Gdx.app != null) {
						Gdx.app.error("AudioManager", "Error disposing music resource", e);
					}
				}
			}
		}
		musicMap.clear();

		for (Sound sound : soundMap.values()) {
			if (sound != null) {
				try {
					sound.stop();
					sound.dispose();
				} catch (Exception e) {
					if (Gdx.app != null) {
						Gdx.app.error("AudioManager", "Error disposing sound resource", e);
					}
				}
			}
		}
		soundMap.clear();
	}
}
