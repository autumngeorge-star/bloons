package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized Audio Manager service that controls audio resource loading,
 * caching sound effects, and managing music stream lifecycles with single-point disposal.
 */
public class AudioManager implements Disposable {

	private final Map<String, Sound> soundCache;
	private Music currentMusic;
	private String currentMusicPath;

	public AudioManager() {
		this.soundCache = new HashMap<>();
		this.currentMusic = null;
		this.currentMusicPath = null;
	}

	/**
	 * Retrieves a cached Sound instance or loads and caches it if not already present.
	 * Gracefully handles missing or invalid file paths.
	 *
	 * @param filePath Internal path to the sound file
	 * @return Sound instance, or null if file is missing/invalid
	 */
	public Sound getSound(String filePath) {
		if (filePath == null || filePath.isEmpty()) {
			return null;
		}

		if (soundCache.containsKey(filePath)) {
			return soundCache.get(filePath);
		}

		try {
			if (Gdx.files != null && Gdx.audio != null && Gdx.files.internal(filePath).exists()) {
				Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
				soundCache.put(filePath, sound);
				return sound;
			} else {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Sound file does not exist: " + filePath);
				}
			}
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.error("AudioManager", "Failed to load sound asset: " + filePath, e);
			}
		}

		return null;
	}

	/**
	 * Plays a sound effect from cached memory at default volume (1.0f).
	 *
	 * @param filePath Internal path to the sound file
	 * @return sound instance ID, or -1 if play failed
	 */
	public long playSound(String filePath) {
		return playSound(filePath, 1.0f);
	}

	/**
	 * Plays a sound effect from cached memory at specified volume.
	 *
	 * @param filePath Internal path to the sound file
	 * @param volume   Playback volume (0.0 to 1.0)
	 * @return sound instance ID, or -1 if play failed
	 */
	public long playSound(String filePath, float volume) {
		Sound sound = getSound(filePath);
		if (sound != null) {
			try {
				return sound.play(volume);
			} catch (Exception e) {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Error playing sound: " + filePath, e);
				}
			}
		}
		return -1;
	}

	/**
	 * Plays background music looping at 0.5f volume.
	 * Disposes any existing background music stream first.
	 *
	 * @param filePath Internal path to the music file
	 */
	public void playMusic(String filePath) {
		playMusic(filePath, 0.5f, true);
	}

	/**
	 * Plays background music with specified volume and looping setting.
	 * Automatically disposes any existing music stream before creating the new one.
	 *
	 * @param filePath Internal path to the music file
	 * @param volume   Playback volume (0.0 to 1.0)
	 * @param looping  Whether music should loop
	 */
	public void playMusic(String filePath, float volume, boolean looping) {
		boolean wasPlaying = true;
		if (currentMusic != null) {
			try {
				wasPlaying = currentMusic.isPlaying();
				currentMusic.stop();
				currentMusic.dispose();
			} catch (Exception e) {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Error disposing previous music track", e);
				}
			} finally {
				currentMusic = null;
				currentMusicPath = null;
			}
		}

		if (filePath == null || filePath.isEmpty()) {
			return;
		}

		try {
			if (Gdx.files != null && Gdx.audio != null && Gdx.files.internal(filePath).exists()) {
				Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
				music.setVolume(volume);
				music.setLooping(looping);
				currentMusic = music;
				currentMusicPath = filePath;
				if (wasPlaying) {
					music.play();
				}
			} else {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Music file does not exist: " + filePath);
				}
			}
		} catch (Exception e) {
			if (Gdx.app != null) {
				Gdx.app.error("AudioManager", "Failed to load music asset: " + filePath, e);
			}
		}
	}

	/**
	 * Pauses current background music if playing.
	 */
	public void pauseMusic() {
		if (currentMusic != null && currentMusic.isPlaying()) {
			try {
				currentMusic.pause();
			} catch (Exception e) {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Error pausing music", e);
				}
			}
		}
	}

	/**
	 * Resumes background music playback.
	 */
	public void resumeMusic() {
		if (currentMusic != null) {
			try {
				currentMusic.play();
			} catch (Exception e) {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Error resuming music", e);
				}
			}
		}
	}

	/**
	 * Stops background music.
	 */
	public void stopMusic() {
		if (currentMusic != null) {
			try {
				currentMusic.stop();
			} catch (Exception e) {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Error stopping music", e);
				}
			}
		}
	}

	/**
	 * Toggles background music between paused and playing.
	 */
	public void toggleMusic() {
		if (currentMusic != null) {
			if (currentMusic.isPlaying()) {
				pauseMusic();
			} else {
				resumeMusic();
			}
		}
	}

	/**
	 * Returns whether background music is currently playing.
	 */
	public boolean isMusicPlaying() {
		return currentMusic != null && currentMusic.isPlaying();
	}

	/**
	 * Returns the current Music stream, or null if none loaded.
	 */
	public Music getCurrentMusic() {
		return currentMusic;
	}

	/**
	 * Returns path to current music stream.
	 */
	public String getCurrentMusicPath() {
		return currentMusicPath;
	}

	/**
	 * Returns the number of cached Sound instances.
	 */
	public int getCachedSoundCount() {
		return soundCache.size();
	}

	/**
	 * Disposes all managed audio resources (Music stream and cached Sound instances).
	 */
	@Override
	public void dispose() {
		if (currentMusic != null) {
			try {
				currentMusic.stop();
				currentMusic.dispose();
			} catch (Exception e) {
				if (Gdx.app != null) {
					Gdx.app.error("AudioManager", "Error disposing music during audio teardown", e);
				}
			} finally {
				currentMusic = null;
				currentMusicPath = null;
			}
		}

		for (Sound sound : soundCache.values()) {
			if (sound != null) {
				try {
					sound.dispose();
				} catch (Exception e) {
					if (Gdx.app != null) {
						Gdx.app.error("AudioManager", "Error disposing cached sound asset", e);
					}
				}
			}
		}
		soundCache.clear();
	}
}
