package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;


public class Player {
	
	private int money;
	private int health;
	private int score;
	private int highScore;
	private ScorePersistenceService scorePersistenceService;
	
	public Player() {
		this(200, 200);
	}
	
	public Player(int money, int health) {
		this.money = money;
		this.health = health;
		this.score = 0;
		this.scorePersistenceService = new ScorePersistenceService();
		this.highScore = this.scorePersistenceService.getHighScore();
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

	public void setScore(int score) {
		this.score = score;
		checkAndUpdateHighScore();
	}

	public void addScore(int points) {
		if (points > 0) {
			this.score += points;
			checkAndUpdateHighScore();
		}
	}

	public void earnScore(int points) {
		addScore(points);
	}

	public void increaseScore(int points) {
		addScore(points);
	}

	public int getHighScore() {
		return highScore;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
		if (scorePersistenceService != null) {
			scorePersistenceService.saveHighScore(highScore);
		}
	}

	public ScorePersistenceService getScorePersistenceService() {
		return scorePersistenceService;
	}

	public void setScorePersistenceService(ScorePersistenceService service) {
		this.scorePersistenceService = service;
		if (service != null) {
			this.highScore = service.getHighScore();
			checkAndUpdateHighScore();
		}
	}

	private void checkAndUpdateHighScore() {
		if (this.score > this.highScore) {
			this.highScore = this.score;
			if (scorePersistenceService != null) {
				scorePersistenceService.saveHighScore(this.highScore);
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
