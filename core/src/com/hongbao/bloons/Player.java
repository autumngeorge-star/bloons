package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;


public class Player {
	
	private int money;
	private int health;
	private int score;
	private int highScore;
	private PreferenceManager preferenceManager;
	
	public Player() {
		this(200, 200);
	}
	
	public Player(int money, int health) {
		this.money = money;
		this.health = health;
		this.score = 0;
		this.highScore = 0;
	}

	public void setPreferenceManager(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
		if (preferenceManager != null) {
			this.highScore = preferenceManager.getHighScore();
		}
	}
	
	public int getMoney() {
		return money;
	}
	
	public void earnMoney(int money) {
		this.money += money;
		addScore(money);
	}

	public int getScore() {
		return score;
	}

	public void addScore(int points) {
		this.score += points;
		if (this.score > this.highScore) {
			this.highScore = this.score;
			if (preferenceManager != null) {
				preferenceManager.updateHighScore(this.highScore);
			}
		}
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
		if (preferenceManager != null) {
			preferenceManager.setHighScore(highScore);
		}
	}
	
	public boolean spendMoney(int money) {
		if (money > this.money) {
			return false;
		} else {
			this.money -= money;
			return true;
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public void decreaseHealth(int health) {
		this.health -= health;
		if (this.health < 0) {
			this.health = 0;
		}
	}
	
	public boolean canPurchaseGirl(Girl girl) {
		return money >= girl.getCost();
	}
	
	public void purchaseGirl(Girl girl) {
		// really regretting the class name choice now
		spendMoney(girl.getCost());
	}
	
}
