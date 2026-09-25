package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;


public class MusicPlayer {
	
	private Music backgroundMusic;
	private AssetManager assetManager;
	
	public MusicPlayer() {
		backgroundMusic = null;
		assetManager = null;
	}

	public MusicPlayer(AssetManager assetManager) {
		backgroundMusic = null;
		this.assetManager = assetManager;
	}

	private AssetManager getAssetManager() {
		if (assetManager != null) {
			return assetManager;
		}
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			return ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getAssetManager();
		}
		return null;
	}

	private void playMusic(String fileName) {
		boolean wasPlaying = true;
		if (backgroundMusic != null) {
			wasPlaying = backgroundMusic.isPlaying();
		}
		stopMusic();
		AssetManager am = getAssetManager();
		if (am != null && am.isLoaded(fileName, Music.class)) {
			backgroundMusic = am.get(fileName, Music.class);
		} else {
			backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(fileName));
		}
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
