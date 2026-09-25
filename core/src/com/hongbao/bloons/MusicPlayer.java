package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.hongbao.bloons.events.EventBus;
import com.hongbao.bloons.events.EventHandler;
import com.hongbao.bloons.events.LevelStartedEvent;


public class MusicPlayer {
	
	private Music backgroundMusic;
	private EventHandler<LevelStartedEvent> levelStartedHandler;
	
	public MusicPlayer() {
		this(EventBus.getInstance());
	}

	public MusicPlayer(EventBus eventBus) {
		backgroundMusic = null;
		if (eventBus != null) {
			this.levelStartedHandler = this::onLevelStarted;
			eventBus.subscribe(LevelStartedEvent.class, levelStartedHandler);
		}
	}

	public void onLevelStarted(LevelStartedEvent event) {
		if (event != null) {
			if (event.getLevel() == 1) {
				playStageMusic();
			} else if (event.getLevel() == 40) {
				playFinalBossMusic();
			}
		}
	}

	public void unregister(EventBus eventBus) {
		if (eventBus != null && levelStartedHandler != null) {
			eventBus.unsubscribe(LevelStartedEvent.class, levelStartedHandler);
		}
	}

	public void dispose() {
		unregister(EventBus.getInstance());
	}

	private void playMusic(String fileName) {
		if (Gdx.audio == null || Gdx.files == null) {
			return;
		}
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
