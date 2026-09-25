package com.hongbao.bloons;

public class WaveMetadata {

	private String title;
	private String music;
	private int cashBonus;

	public WaveMetadata() {
		this.title = "";
		this.music = "";
		this.cashBonus = 0;
	}

	public WaveMetadata(String title, String music, int cashBonus) {
		this.title = title != null ? title : "";
		this.music = music != null ? music : "";
		this.cashBonus = cashBonus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title != null ? title : "";
	}

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music != null ? music : "";
	}

	public int getCashBonus() {
		return cashBonus;
	}

	public void setCashBonus(int cashBonus) {
		this.cashBonus = cashBonus;
	}

	@Override
	public String toString() {
		return "WaveMetadata{" +
				"title='" + title + '\'' +
				", music='" + music + '\'' +
				", cashBonus=" + cashBonus +
				'}';
	}
}
