package com.hongbao.bloons.entities;

import com.hongbao.bloons.modifiers.Operation;
import com.hongbao.bloons.modifiers.StatModifier;
import com.hongbao.bloons.modifiers.StatType;
import com.hongbao.bloons.modifiers.UpgradeDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Girl {

	public static final String IMAGE_FOLDER = "img/characters/";
	public static final int NO_UPGRADES_AVAILABLE = -1;

	private String name;

	// Base attributes (Tier 0)
	private int baseAttackDelay;
	private float baseBulletSpeed;
	private int baseDamage;
	private int basePierce;
	private float baseRange;
	private float baseVisualRange;
	private boolean baseHoming;

	// Calculated effective attributes
	private int effectiveAttackDelay;
	private float effectiveBulletSpeed;
	private int effectiveDamage;
	private int effectivePierce;
	private float effectiveRange;
	private float effectiveVisualRange;
	private boolean effectiveHoming;

	private int cooldown;
	private String imageFileName;
	private String bulletFileName;
	private int cost;
	private List<UpgradeDefinition> upgradeDefinitions;
	private List<UpgradeDefinition> activeUpgrades;
	private int level;
	private int totalInvestment;

	public Girl(
			String name,
			int baseAttackDelay,
			float baseBulletSpeed,
			int baseDamage,
			int basePierce,
			float baseRange,
			float baseVisualRange,
			boolean baseHoming,
			String imageFileName,
			String bulletFileName,
			int cost,
			List<UpgradeDefinition> upgradeDefinitions
	) {
		this.name = name;
		this.baseAttackDelay = baseAttackDelay;
		this.baseBulletSpeed = baseBulletSpeed;
		this.baseDamage = baseDamage;
		this.basePierce = basePierce;
		this.baseRange = baseRange;
		this.baseVisualRange = baseVisualRange;
		this.baseHoming = baseHoming;

		if (imageFileName != null && !imageFileName.startsWith(IMAGE_FOLDER)) {
			this.imageFileName = IMAGE_FOLDER + imageFileName;
		} else {
			this.imageFileName = imageFileName;
		}
		this.bulletFileName = bulletFileName;
		this.cost = cost;
		this.upgradeDefinitions = upgradeDefinitions != null ? upgradeDefinitions : Collections.emptyList();
		this.activeUpgrades = new ArrayList<>();
		this.level = 0;
		this.totalInvestment = cost;

		recalculateStats();
		this.cooldown = effectiveAttackDelay;
	}

	public void recalculateStats() {
		float flatAttackDelay = 0;
		float multAttackDelay = 1.0f;

		float flatBulletSpeed = 0;
		float multBulletSpeed = 1.0f;

		float flatDamage = 0;
		float multDamage = 1.0f;

		float flatPierce = 0;
		float multPierce = 1.0f;

		float flatRange = 0;
		float multRange = 1.0f;

		float flatVisualRange = 0;
		float multVisualRange = 1.0f;

		boolean homingFlag = baseHoming;

		for (UpgradeDefinition upgrade : activeUpgrades) {
			if (upgrade == null || upgrade.getModifiers() == null) continue;
			for (StatModifier modifier : upgrade.getModifiers()) {
				if (modifier == null || modifier.getStatType() == null) continue;

				switch (modifier.getStatType()) {
					case ATTACK_DELAY:
						if (modifier.getOperation() == Operation.ADD) {
							flatAttackDelay += modifier.getValue();
						} else if (modifier.getOperation() == Operation.MULTIPLY) {
							multAttackDelay *= modifier.getValue();
						}
						break;
					case BULLET_SPEED:
						if (modifier.getOperation() == Operation.ADD) {
							flatBulletSpeed += modifier.getValue();
						} else if (modifier.getOperation() == Operation.MULTIPLY) {
							multBulletSpeed *= modifier.getValue();
						}
						break;
					case DAMAGE:
						if (modifier.getOperation() == Operation.ADD) {
							flatDamage += modifier.getValue();
						} else if (modifier.getOperation() == Operation.MULTIPLY) {
							multDamage *= modifier.getValue();
						}
						break;
					case PIERCE:
						if (modifier.getOperation() == Operation.ADD) {
							flatPierce += modifier.getValue();
						} else if (modifier.getOperation() == Operation.MULTIPLY) {
							multPierce *= modifier.getValue();
						}
						break;
					case RANGE:
						if (modifier.getOperation() == Operation.ADD) {
							flatRange += modifier.getValue();
						} else if (modifier.getOperation() == Operation.MULTIPLY) {
							multRange *= modifier.getValue();
						}
						break;
					case VISUAL_RANGE:
						if (modifier.getOperation() == Operation.ADD) {
							flatVisualRange += modifier.getValue();
						} else if (modifier.getOperation() == Operation.MULTIPLY) {
							multVisualRange *= modifier.getValue();
						}
						break;
					case HOMING:
						if (modifier.getValue() > 0.5f) {
							homingFlag = true;
						}
						break;
				}
			}
		}

		effectiveAttackDelay = Math.max(1, Math.round((baseAttackDelay + flatAttackDelay) * multAttackDelay));
		effectiveBulletSpeed = Math.max(0f, (baseBulletSpeed + flatBulletSpeed) * multBulletSpeed);
		effectiveDamage = Math.max(1, Math.round((baseDamage + flatDamage) * multDamage));
		effectivePierce = Math.max(1, Math.round((basePierce + flatPierce) * multPierce));
		effectiveRange = Math.max(0f, (baseRange + flatRange) * multRange);
		effectiveVisualRange = Math.max(0f, (baseVisualRange + flatVisualRange) * multVisualRange);
		effectiveHoming = homingFlag;
	}

	public String getName() {
		return name;
	}

	public int getAttackDelay() {
		return effectiveAttackDelay;
	}

	public float getBulletSpeed() {
		return effectiveBulletSpeed;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void decrementCooldown() {
		cooldown--;
	}

	public void resetCooldown() {
		cooldown = effectiveAttackDelay;
	}

	public int getDamage() {
		return effectiveDamage;
	}

	public int getPierce() {
		return effectivePierce;
	}

	public float getRange() {
		return effectiveRange;
	}

	public float getVisualRange() {
		return effectiveVisualRange;
	}

	public boolean isHoming() {
		return effectiveHoming;
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
		if (level < upgradeDefinitions.size()) {
			return upgradeDefinitions.get(level).getCost();
		}
		return NO_UPGRADES_AVAILABLE;
	}

	public int getSellPrice() {
		return totalInvestment / 2;
	}

	public int getLevel() {
		return level;
	}

	public List<UpgradeDefinition> getUpgradeDefinitions() {
		return upgradeDefinitions;
	}

	public List<UpgradeDefinition> getActiveUpgrades() {
		return activeUpgrades;
	}

	public Bullet createBullet() {
		return new Bullet(effectiveBulletSpeed, effectiveDamage, effectivePierce, effectiveRange, effectiveHoming, bulletFileName);
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
		if (level < upgradeDefinitions.size()) {
			UpgradeDefinition upgrade = upgradeDefinitions.get(level);
			activeUpgrades.add(upgrade);
			level++;
			totalInvestment += upgrade.getCost();
			recalculateStats();
			return upgrade.getCost();
		}
		return 0;
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
					baseAttackDelay,
					baseBulletSpeed,
					baseDamage,
					basePierce,
					baseRange,
					baseVisualRange,
					baseHoming,
					imageFileName,
					bulletFileName,
					cost,
					upgradeDefinitions
			);
			for (UpgradeDefinition upgrade : activeUpgrades) {
				upgradedGirl.activeUpgrades.add(upgrade);
			}
			upgradedGirl.upgrade();
			return upgradedGirl;
		} else {
			return null;
		}
	}

}
