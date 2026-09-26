package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;


public class Player {
	
	private int money;
	private int health;
	private int score;
	
	public Player() {
		money = 200;
		health = 200;
		score = 0;
	}
	
	public Player(int money, int health) {
		this.money = money;
		this.health = health;
		this.score = 0;
	}

	public Player(int money, int health, int score) {
		this.money = money;
		this.health = health;
		this.score = score;
	}
	
	public int getMoney() {
		return money;
	}
	
	public void earnMoney(int money) {
		this.money += money;
		this.score += money;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
