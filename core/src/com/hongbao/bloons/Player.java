package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.entities.Girl;


public class Player {
	
	public static final String PREFERENCE_NAME = "BloonsPreferences";
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
		this.highScore = loadHighScore();
	}

	private int loadHighScore() {
		if (Gdx.app != null) {
			Preferences prefs = Gdx.app.getPreferences(PREFERENCE_NAME);
			return prefs.getInteger(HIGH_SCORE_KEY, 0);
		}
		return 0;
	}

	public void saveHighScore() {
		if (Gdx.app != null) {
			Preferences prefs = Gdx.app.getPreferences(PREFERENCE_NAME);
			prefs.putInteger(HIGH_SCORE_KEY, highScore);
			prefs.flush();
		}
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

	public int getHighScore() {
		return highScore;
	}

	public void addScore(int points) {
		if (points <= 0) {
			return;
		}
		this.score += points;
		if (this.score < 0) {
			this.score = 0;
		}
		if (this.score > this.highScore) {
			this.highScore = this.score;
			saveHighScore();
		}
	}

	public void resetScore() {
		this.score = 0;
	}
	
	public boolean canPurchaseGirl(Girl girl) {
		return money >= girl.getCost();
	}
	
	public void purchaseGirl(Girl girl) {
		// really regretting the class name choice now
		spendMoney(girl.getCost());
	}
	
}
