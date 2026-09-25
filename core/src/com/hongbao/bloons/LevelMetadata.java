package com.hongbao.bloons;

/**
 * Metadata associated with a wave level, including stage music track identifiers and boss flags.
 */
public class LevelMetadata {
	private String musicTrack;
	private boolean boss;

	public LevelMetadata() {
		this(null, false);
	}

	public LevelMetadata(String musicTrack) {
		this(musicTrack, false);
	}

	public LevelMetadata(String musicTrack, boolean boss) {
		this.musicTrack = musicTrack;
		this.boss = boss;
	}

	public String getMusicTrack() {
		return musicTrack;
	}

	public void setMusicTrack(String musicTrack) {
		this.musicTrack = musicTrack;
	}

	public boolean isBoss() {
		return boss;
	}

	public boolean getBoss() {
		return boss;
	}

	public void setBoss(boolean boss) {
		this.boss = boss;
	}

	@Override
	public String toString() {
		return "LevelMetadata{" +
				"musicTrack='" + musicTrack + '\'' +
				", boss=" + boss +
				'}';
	}
}
