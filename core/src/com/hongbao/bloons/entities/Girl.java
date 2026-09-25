package com.hongbao.bloons.entities;

import java.util.List;
import java.util.Objects;


public class Girl {
	
	public static final String IMAGE_FOLDER = "img/characters/";
	public static final int NO_UPGRADES_AVAILABLE = -1;
	
	private String name;
	private List<GirlTier> tiers;
	private int cooldown;
	private String imageFileName;
	private String bulletFileName;
	private int cost;
	private int level;
	private int totalInvestment;
	
	
	public Girl(String name, List<GirlTier> tiers, String imageFileName, String bulletFileName, int cost) {
		Objects.requireNonNull(name, "Name cannot be null");
		Objects.requireNonNull(tiers, "Tiers list cannot be null");
		if (tiers.isEmpty()) {
			throw new IllegalArgumentException("Tiers list cannot be empty");
		}
		for (GirlTier tier : tiers) {
			Objects.requireNonNull(tier, "GirlTier elements cannot be null");
		}
		Objects.requireNonNull(imageFileName, "Image file name cannot be null");
		Objects.requireNonNull(bulletFileName, "Bullet file name cannot be null");

		this.name = name;
		this.tiers = tiers;
		if (imageFileName.startsWith(IMAGE_FOLDER)) {
			this.imageFileName = imageFileName;
		} else {
			this.imageFileName = IMAGE_FOLDER + imageFileName;
		}
		this.bulletFileName = bulletFileName;
		this.cost = cost;
		this.level = 0;
		this.totalInvestment = cost;
		this.cooldown = getCurrentTier().getAttackDelay();
	}

	public String getName() {
		return name;
	}

	public List<GirlTier> getTiers() {
		return tiers;
	}

	public GirlTier getCurrentTier() {
		return tiers.get(level);
	}
	
	public int getAttackDelay() {
		return getCurrentTier().getAttackDelay();
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public void decrementCooldown() {
		cooldown--;
	}
	
	public void resetCooldown() {
		cooldown = getCurrentTier().getAttackDelay();
	}
	
	public int getDamage() {
		return getCurrentTier().getDamage();
	}
	
	public int getPierce() {
		return getCurrentTier().getPierce();
	}
	
	public float getRange() {
		return getCurrentTier().getRange();
	}
	
	public float getVisualRange() {
		return getCurrentTier().getVisualRange();
	}

	public boolean isHoming() {
		return getCurrentTier().isHoming();
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
		if (level >= tiers.size() - 1 || getCurrentTier().getUpgradeCost() == NO_UPGRADES_AVAILABLE) {
			return NO_UPGRADES_AVAILABLE;
		}
		return getCurrentTier().getUpgradeCost();
	}
	
	public int getSellPrice() {
		return totalInvestment / 2;
	}
	
	public int getLevel() {
		return level;
	}

	public Bullet createBullet() {
		return new Bullet(getCurrentTier().getBulletSpeed(), getDamage(), getPierce(), getRange(), isHoming(), bulletFileName);
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
		if (level >= tiers.size() - 1 || getUpgradeCost() == NO_UPGRADES_AVAILABLE) {
			return false;
		}
		return currentCash >= getUpgradeCost();
	}
	
	public Girl getUpgradedStats() {
		if (level < tiers.size() - 1 && getUpgradeCost() != NO_UPGRADES_AVAILABLE) {
			Girl upgradedGirl = new Girl(
				name,
				tiers,
				imageFileName,
				bulletFileName,
				cost
			);
			upgradedGirl.level = level + 1;
			return upgradedGirl;
		} else {
			return null;
		}
	}
	
}
