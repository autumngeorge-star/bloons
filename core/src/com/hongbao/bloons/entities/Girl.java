package com.hongbao.bloons.entities;

import java.util.List;


public class Girl {
	
	public static final String IMAGE_FOLDER = "img/characters/";
	public static final int NO_UPGRADES_AVAILABLE = -1;
	public static final int TICKS_PER_SECOND = 180;
	
	private String name;
	private List<Integer> attackDelay;
	private int cooldown;
	private List<Float> bulletSpeed;
	private List<Integer> damage;
	private List<Integer> pierce;
	private List<Float> range;
	private List<Float> visualRange;
	private List<Boolean> homing;
	private String imageFileName;
	private String bulletFileName;
	private int cost;
	private List<Integer> upgradeCost;
	private int level;
	private int totalInvestment;
	private int spellCardCooldown;
	private int spellCardMaxCooldown;
	
	
	public Girl(String name, List<Integer> attackDelay, List<Float> bulletSpeed, List<Integer> damage, List<Integer> pierce, List<Float> range, List<Float> visualRange, List<Boolean> homing, String imageFileName, String bulletFileName, int cost, List<Integer> upgradeCost) {
		this(name, attackDelay, bulletSpeed, damage, pierce, range, visualRange, homing, imageFileName, bulletFileName, cost, upgradeCost, 0);
	}

	public Girl(String name, List<Integer> attackDelay, List<Float> bulletSpeed, List<Integer> damage, List<Integer> pierce, List<Float> range, List<Float> visualRange, List<Boolean> homing, String imageFileName, String bulletFileName, int cost, List<Integer> upgradeCost, int spellCardMaxCooldown) {
		this.name = name;
		this.attackDelay = attackDelay;
		this.cooldown = attackDelay.get(0);
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		this.pierce = pierce;
		this.range = range;
		this.visualRange = visualRange;
		this.homing = homing;
		this.imageFileName = IMAGE_FOLDER + imageFileName;
		this.bulletFileName = bulletFileName;
		this.cost = cost;
		this.upgradeCost = upgradeCost;
		this.spellCardMaxCooldown = spellCardMaxCooldown;
		this.spellCardCooldown = 0;
		level = 0;
		totalInvestment = cost;
	}

	public String getName() {
		return name;
	}
	
	public int getAttackDelay() {
		return attackDelay.get(level);
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public void decrementCooldown() {
		cooldown--;
	}
	
	public void resetCooldown() {
		cooldown = attackDelay.get(level);
	}
	
	public int getDamage() {
		return damage.get(level);
	}
	
	public int getPierce() {
		return pierce.get(level);
	}
	
	public float getRange() {
		return range.get(level);
	}
	
	public float getVisualRange() {
		return visualRange.get(level);
	}

	public boolean isHoming() {
		return homing.get(level);
	}

	public String getImageFileName() {
		return imageFileName;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getUpgradeCostString() {
		if (getUpgradeCost() == NO_UPGRADES_AVAILABLE) {
			return "N/A";
		} else {
			return "$" + getUpgradeCost();
		}
	}
	
	public int getUpgradeCost() {
		return upgradeCost.get(level);
	}
	
	public int getSellPrice() {
		return totalInvestment / 2;
	}
	
	public int getLevel() {
		return level;
	}

	public Bullet createBullet() {
		return new Bullet(bulletSpeed.get(level), getDamage(), getPierce(), getRange(), isHoming(), bulletFileName);
	}
	
	public SpellCard createSpellCard() {
		if (name.equals("Reimu")) {
			return SpellCard.createReimuSpellCard();
		}
		if (name.equals("Yuyuko")) {
			return SpellCard.createYuyukoSpellCard();
		}
		return null;
	}
	
	public int upgrade() {
		int upgradeCost = getUpgradeCost();
		level++;
		totalInvestment += upgradeCost;
		return upgradeCost;
	}
	
	public boolean canUpgrade(int currentCash) {
		if (getUpgradeCost() == NO_UPGRADES_AVAILABLE) {
			return false;
		}
		return currentCash >= getUpgradeCost();
	}
	
	public Girl getUpgradedStats() {
		if (getUpgradeCost() != NO_UPGRADES_AVAILABLE) {
			Girl upgradedGirl = new Girl(
			 name,
			 attackDelay,
			 bulletSpeed,
			 damage,
			 pierce,
			 range,
			 visualRange,
			 homing,
			 imageFileName,
			 bulletFileName,
			 cost,
			 upgradeCost,
			 spellCardMaxCooldown
			);
			upgradedGirl.level = level + 1;
			upgradedGirl.spellCardCooldown = spellCardCooldown;
			return upgradedGirl;
		} else {
			return null;
		}
	}

	public int getSpellCardCooldown() {
		return spellCardCooldown;
	}

	public void setSpellCardCooldown(int spellCardCooldown) {
		this.spellCardCooldown = Math.max(0, spellCardCooldown);
	}

	public int getSpellCardMaxCooldown() {
		return spellCardMaxCooldown;
	}

	public void setSpellCardMaxCooldown(int spellCardMaxCooldown) {
		this.spellCardMaxCooldown = Math.max(0, spellCardMaxCooldown);
	}

	public void decrementSpellCardCooldown() {
		if (spellCardCooldown > 0) {
			spellCardCooldown--;
		}
	}

	public void resetSpellCardCooldown() {
		spellCardCooldown = spellCardMaxCooldown;
	}

	public boolean hasSpellCard() {
		return spellCardMaxCooldown > 0;
	}

	public boolean isSpellCardReady() {
		return hasSpellCard() && spellCardCooldown == 0;
	}

	public int getSpellCardCooldownSeconds() {
		return (int) Math.ceil((double) spellCardCooldown / TICKS_PER_SECOND);
	}

	public String getSpellCardStatus() {
		if (!hasSpellCard()) {
			return "SPELL CARD: N/A";
		}
		if (isSpellCardReady()) {
			return "SPELL CARD: READY";
		}
		return "SPELL CARD: " + getSpellCardCooldownSeconds() + "s";
	}
	
}
