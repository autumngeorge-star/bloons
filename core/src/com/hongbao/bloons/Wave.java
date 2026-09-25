package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.List;

public class Wave {

	private List<Bloon> bloons;
	private List<Long> intervals;
	private String title;
	private String music;
	private int bonus;

	public Wave() {
		this.bloons = new ArrayList<>();
		this.intervals = new ArrayList<>();
		this.title = null;
		this.music = null;
		this.bonus = 0;
	}

	public Wave(List<Bloon> bloons, List<Long> intervals, String title, String music, int bonus) {
		this.bloons = bloons != null ? bloons : new ArrayList<>();
		this.intervals = intervals != null ? intervals : new ArrayList<>();
		this.title = title;
		this.music = music;
		this.bonus = bonus;
	}

	public List<Bloon> getBloons() {
		return bloons;
	}

	public void setBloons(List<Bloon> bloons) {
		this.bloons = bloons;
	}

	public List<Long> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<Long> intervals) {
		this.intervals = intervals;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

}
