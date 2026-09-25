package com.hongbao.bloons.dto;

import java.util.ArrayList;
import java.util.List;

public class SaveStateData {

	private int money;
	private int health;
	private int level;
	private List<GirlSaveData> girls;

	public SaveStateData() {
		girls = new ArrayList<>();
	}

	public SaveStateData(int money, int health, int level, List<GirlSaveData> girls) {
		this.money = money;
		this.health = health;
		this.level = level;
		this.girls = girls != null ? girls : new ArrayList<>();
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<GirlSaveData> getGirls() {
		return girls;
	}

	public void setGirls(List<GirlSaveData> girls) {
		this.girls = girls;
	}

}
