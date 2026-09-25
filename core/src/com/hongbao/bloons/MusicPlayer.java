package com.hongbao.bloons;

import com.hongbao.bloons.audio.AudioSystem;


public class MusicPlayer {
	
	public MusicPlayer() {
	}

	private void playMusic(String fileName) {
		AudioSystem.getInstance().playMusic(fileName, 0.5f, true);
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
		AudioSystem.getInstance().pauseMusic();
	}
	
	public void resume() {
		AudioSystem.getInstance().resumeMusic();
	}
	
	public void stopMusic() {
		AudioSystem.getInstance().stopMusic();
	}

	public void toggleMusic() {
		AudioSystem.getInstance().toggleMusic();
	}

}
