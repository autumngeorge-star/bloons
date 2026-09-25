package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundManager implements Disposable {

	public static final String POP_SOUND_PATH = "music/pop.mp3";
	public static final String TITLE_MUSIC_PATH = "music/title.mp3";
	public static final String STAGE_MUSIC_PATH = "music/demystify_feast.mp3";
	public static final String FINAL_BOSS_MUSIC_PATH = "music/night_falls.mp3";

	private Sound popSound;
	private Music backgroundMusic;
	private String currentMusicPath;

	private float masterVolume = 1.0f;
	private float sfxVolume = 0.5f;
	private float musicVolume = 0.5f;

	public SoundManager() {
		initAudioAssets();
	}

	private void initAudioAssets() {
		if (Gdx.audio != null && Gdx.files != null) {
			try {
				popSound = Gdx.audio.newSound(Gdx.files.internal(POP_SOUND_PATH));
			} catch (Exception ignored) {
			}
		}
	}

	public void setMasterVolume(float masterVolume) {
		this.masterVolume = Math.max(0.0f, Math.min(1.0f, masterVolume));
		updateMusicVolume();
	}

	public float getMasterVolume() {
		return masterVolume;
	}

	public void setSfxVolume(float sfxVolume) {
		this.sfxVolume = Math.max(0.0f, Math.min(1.0f, sfxVolume));
	}

	public float getSfxVolume() {
		return sfxVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = Math.max(0.0f, Math.min(1.0f, musicVolume));
		updateMusicVolume();
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public float getEffectiveSfxVolume() {
		return masterVolume * sfxVolume;
	}

	public float getEffectiveMusicVolume() {
		return masterVolume * musicVolume;
	}

	private void updateMusicVolume() {
		if (backgroundMusic != null) {
			backgroundMusic.setVolume(getEffectiveMusicVolume());
		}
	}

	public long playPopSound() {
		if (popSound == null && Gdx.audio != null && Gdx.files != null) {
			try {
				popSound = Gdx.audio.newSound(Gdx.files.internal(POP_SOUND_PATH));
			} catch (Exception ignored) {
			}
		}
		if (popSound != null) {
			return popSound.play(getEffectiveSfxVolume());
		}
		return -1;
	}

	private void playMusic(String fileName) {
		currentMusicPath = fileName;
		if (Gdx.audio == null || Gdx.files == null) {
			return;
		}
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		currentMusicPath = fileName;
		try {
			backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
			backgroundMusic.setVolume(getEffectiveMusicVolume());
			backgroundMusic.setLooping(true);
			if (wasPlaying) {
				backgroundMusic.play();
			}
		} catch (Exception ignored) {
		}
	}

	public void playTitleMusic() {
		playMusic(TITLE_MUSIC_PATH);
	}

	public void playStageMusic() {
		playMusic(STAGE_MUSIC_PATH);
	}

	public void playFinalBossMusic() {
		playMusic(FINAL_BOSS_MUSIC_PATH);
	}

	public void playLevelMusic(int level) {
		if (level == 1) {
			playStageMusic();
		} else if (level == 40) {
			playFinalBossMusic();
		}
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
			backgroundMusic.dispose();
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

	public String getCurrentMusicPath() {
		return currentMusicPath;
	}

	@Override
	public void dispose() {
		if (popSound != null) {
			popSound.dispose();
			popSound = null;
		}
		if (backgroundMusic != null) {
			backgroundMusic.dispose();
			backgroundMusic = null;
		}
	}
}
