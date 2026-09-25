package com.hongbao.bloons;

public interface AudioController {
	void playTitleMusic();
	void playStageMusic();
	void playFinalBossMusic();
	void pause();
	void resume();
	void stopMusic();
	void toggleMusic();
}
