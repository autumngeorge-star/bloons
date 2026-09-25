package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.hongbao.bloons.event.GameEventBus;
import com.hongbao.bloons.event.GameEventListener;
import com.hongbao.bloons.event.LevelStartedEvent;


public class MusicPlayer implements GameEventListener<LevelStartedEvent> {
	
	private Music backgroundMusic;
	
	public MusicPlayer() {
		backgroundMusic = null;
	}

	public void subscribeTo(GameEventBus eventBus) {
		if (eventBus != null) {
			eventBus.subscribe(LevelStartedEvent.class, this);
		}
	}

	public void unsubscribeFrom(GameEventBus eventBus) {
		if (eventBus != null) {
			eventBus.unsubscribe(LevelStartedEvent.class, this);
		}
	}

	@Override
	public void onEvent(LevelStartedEvent event) {
		if (event == null) {
			return;
		}
		if (event.getLevel() == 1) {
			playStageMusic();
		} else if (event.getLevel() == 40) {
			playFinalBossMusic();
		}
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
