package com.hongbao.bloons.event;

public class LevelStartedEvent {

	private final int level;

	public LevelStartedEvent(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public int getLevelIndex() {
		return level;
	}
}
