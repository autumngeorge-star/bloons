package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.entities.Girl;


public class Player {
	
	private int money;
	private int health;
	private int score;
	private int highScore;
	private Preferences preferences;
	
	public Player() {
		this(200, 200);
	}
	
	public Player(int money, int health) {
		this.money = money;
		this.health = health;
		this.score = 0;
		if (Gdx.app != null) {
			this.preferences = Gdx.app.getPreferences("bloons");
			this.highScore = preferences.getInteger("highScore", 0);
		} else {
			this.highScore = 0;
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
	
	public void addScore(int amount) {
		this.score += amount;
		if (this.score > this.highScore) {
			this.highScore = this.score;
			if (this.preferences != null) {
				this.preferences.putInteger("highScore", this.highScore);
				this.preferences.flush();
			}
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
