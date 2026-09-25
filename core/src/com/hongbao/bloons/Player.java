package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.entities.Girl;


public class Player {
	
	public static final String PREFS_NAME = "bloons_game_prefs";
	public static final String HIGH_SCORE_KEY = "highScore";

	private int money;
	private int health;
	private int score;
	private int highScore;
	
	public Player() {
		this(200, 200);
	}
	
	public Player(int money, int health) {
		this.money = money;
		this.health = health;
		this.score = 0;
		this.highScore = 0;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void earnMoney(int money) {
		this.money += money;
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

	public int getScore() {
		return score;
	}

	public void addScore(int amount) {
		if (amount > 0) {
			this.score += amount;
			if (this.score > this.highScore) {
				this.highScore = this.score;
			}
		}
	}

	public void resetScore() {
		this.score = 0;
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
		if (this.score > this.highScore) {
			this.highScore = this.score;
		}
	}

	public void loadHighScore() {
		if (Gdx.app != null) {
			Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
			int storedHighScore = prefs.getInteger(HIGH_SCORE_KEY, 0);
			this.highScore = Math.max(storedHighScore, this.score);
		}
	}

	public void saveHighScore() {
		if (this.score > this.highScore) {
			this.highScore = this.score;
		}
		if (Gdx.app != null) {
			Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
			prefs.putInteger(HIGH_SCORE_KEY, this.highScore);
			prefs.flush();
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
