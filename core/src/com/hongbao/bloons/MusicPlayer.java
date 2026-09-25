package com.hongbao.bloons;

/**
 * @deprecated Use {@link SoundManager} instead.
 */
@Deprecated
public class MusicPlayer {
	
	private final SoundManager soundManager;
	
	public MusicPlayer() {
		this(new SoundManager());
	}

	public MusicPlayer(SoundManager soundManager) {
		this.soundManager = soundManager;
	}

	public void playTitleMusic() {
		soundManager.playTitleMusic();
	}

	public void playStageMusic() {
		soundManager.playStageMusic();
	}

	public void playFinalBossMusic() {
		soundManager.playFinalBossMusic();
	}
	
	public void pause() {
		soundManager.pause();
	}
	
	public void resume() {
		soundManager.resume();
	}
	
	public void stopMusic() {
		soundManager.stopMusic();
	}

	public void toggleMusic() {
		soundManager.toggleMusic();
	}
}
