package com.hongbao.bloons;

@FunctionalInterface
public interface WaveEventListener {
	void onLevelStarted(int level);
}
