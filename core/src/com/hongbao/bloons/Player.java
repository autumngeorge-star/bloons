package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.hongbao.bloons.entities.Girl;


public class Player {
	
	private int money;
	private int health;
	private PreferencesManager preferencesManager;
	
	public Player() {
		this(200, 200, null);
	}
	
	public Player(int money, int health) {
		this(money, health, null);
	}

	public Player(int money, int health, PreferencesManager preferencesManager) {
		this.money = money;
		this.health = health;
		this.preferencesManager = preferencesManager;
		syncPreferences();
	}

	public PreferencesManager getPreferencesManager() {
		if (preferencesManager == null && Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			preferencesManager = ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getPreferencesManager();
		}
		return preferencesManager;
	}

	public void setPreferencesManager(PreferencesManager preferencesManager) {
		this.preferencesManager = preferencesManager;
		syncPreferences();
	}
	
	public int getMoney() {
		return money;
	}
	
	public void earnMoney(int money) {
		this.money += money;
		syncPreferences();
	}
	
	public boolean spendMoney(int money) {
		if (money > this.money) {
			return false;
		} else {
			this.money -= money;
			syncPreferences();
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
		syncPreferences();
	}
	
	public boolean canPurchaseGirl(Girl girl) {
		return money >= girl.getCost();
	}
	
	public void purchaseGirl(Girl girl) {
		// really regretting the class name choice now
		spendMoney(girl.getCost());
	}

	private void syncPreferences() {
		PreferencesManager prefs = getPreferencesManager();
		if (prefs != null) {
			prefs.saveMoney(this.money);
			prefs.saveHealth(this.health);
			prefs.flush();
		}
	}
	
}

