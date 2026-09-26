package com.hongbao.bloons.entities;

import java.util.List;
import java.util.Locale;


public class Girl {
	
	public static final String IMAGE_FOLDER = "img/characters/";
	public static final int NO_UPGRADES_AVAILABLE = -1;
	
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
	private float spellCardCooldownTimer;
	private float spellCardMaxCooldown;
	
	
	public Girl(String name, List<Integer> attackDelay, List<Float> bulletSpeed, List<Integer> damage, List<Integer> pierce, List<Float> range, List<Float> visualRange, List<Boolean> homing, String imageFileName, String bulletFileName, int cost, List<Integer> upgradeCost) {
		this(name, attackDelay, bulletSpeed, damage, pierce, range, visualRange, homing, imageFileName, bulletFileName, cost, upgradeCost, 0f);
	}

	public Girl(String name, List<Integer> attackDelay, List<Float> bulletSpeed, List<Integer> damage, List<Integer> pierce, List<Float> range, List<Float> visualRange, List<Boolean> homing, String imageFileName, String bulletFileName, int cost, List<Integer> upgradeCost, float spellCardMaxCooldown) {
		this.name = name;
		this.attackDelay = attackDelay;
		this.cooldown = attackDelay.get(0);
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		this.pierce = pierce;
		this.range = range;
		this.visualRange = visualRange;
		this.homing = homing;
		this.imageFileName = imageFileName.startsWith(IMAGE_FOLDER) ? imageFileName : IMAGE_FOLDER + imageFileName;
		this.bulletFileName = bulletFileName;
		this.cost = cost;
		this.upgradeCost = upgradeCost;
		this.spellCardMaxCooldown = spellCardMaxCooldown;
		this.spellCardCooldownTimer = 0f;
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

	public float getSpellCardCooldownTimer() {
		return spellCardCooldownTimer;
	}

	public void setSpellCardCooldownTimer(float spellCardCooldownTimer) {
		this.spellCardCooldownTimer = spellCardCooldownTimer;
	}

	public float getSpellCardMaxCooldown() {
		return spellCardMaxCooldown;
	}

	public void setSpellCardMaxCooldown(float spellCardMaxCooldown) {
		this.spellCardMaxCooldown = spellCardMaxCooldown;
	}

	public boolean hasSpellCard() {
		return spellCardMaxCooldown > 0 && createSpellCard() != null;
	}

	public boolean isSpellCardReady() {
		return hasSpellCard() && spellCardCooldownTimer <= 0;
	}

	public void decrementSpellCardCooldownTimer(float delta) {
		if (spellCardCooldownTimer > 0) {
			spellCardCooldownTimer -= delta;
			if (spellCardCooldownTimer < 0) {
				spellCardCooldownTimer = 0;
			}
		}
	}

	public void decrementSpellCardCooldown(float delta) {
		decrementSpellCardCooldownTimer(delta);
	}

	public void resetSpellCardCooldown() {
		spellCardCooldownTimer = spellCardMaxCooldown;
	}

	public String getSpellCardStatusString() {
		if (!hasSpellCard()) {
			return "Spell Card: N/A";
		}
		if (isSpellCardReady()) {
			return "Spell Card: READY [X]";
		}
		return String.format(Locale.US, "Spell Card: %.1fs", spellCardCooldownTimer);
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
			upgradedGirl.spellCardCooldownTimer = spellCardCooldownTimer;
			return upgradedGirl;
		} else {
			return null;
		}
	}
	
}
