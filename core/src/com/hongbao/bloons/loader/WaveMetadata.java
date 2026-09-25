package com.hongbao.bloons.loader;

public class WaveMetadata {

	private String title;
	private String musicTrack;
	private int rewardMoney;

	public WaveMetadata() {
		this("Untitled Wave", null, 0);
	}

	public WaveMetadata(String title, String musicTrack, int rewardMoney) {
		this.title = title;
		this.musicTrack = musicTrack;
		this.rewardMoney = rewardMoney;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMusicTrack() {
		return musicTrack;
	}

	public void setMusicTrack(String musicTrack) {
		this.musicTrack = musicTrack;
	}

	public int getRewardMoney() {
		return rewardMoney;
	}

	public void setRewardMoney(int rewardMoney) {
		this.rewardMoney = rewardMoney;
	}

}
