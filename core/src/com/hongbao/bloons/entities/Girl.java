package com.hongbao.bloons.entities;

import java.util.Collections;
import java.util.List;

public class Girl {
	
	public static final String IMAGE_FOLDER = "img/characters/";
	public static final int NO_UPGRADES_AVAILABLE = -1;
	
	private String name;
	private UpgradeNode rootNode;
	private UpgradeNode currentNode;
	private int cooldown;
	private String imageFileName;
	private String bulletFileName;
	private int cost;
	private int totalInvestment;
	
	public Girl(String name, UpgradeNode rootNode, String imageFileName, String bulletFileName, int cost) {
		this.name = name;
		this.rootNode = rootNode;
		this.currentNode = rootNode;
		this.imageFileName = imageFileName.startsWith(IMAGE_FOLDER) ? imageFileName : IMAGE_FOLDER + imageFileName;
		this.bulletFileName = bulletFileName;
		this.cost = cost;
		this.totalInvestment = cost;
		this.cooldown = rootNode.getStats().getAttackDelay();
	}

	public Girl(String name, List<Integer> attackDelay, List<Float> bulletSpeed, List<Integer> damage, List<Integer> pierce, List<Float> range, List<Float> visualRange, List<Boolean> homing, String imageFileName, String bulletFileName, int cost, List<Integer> upgradeCost) {
		this(name, createLinearGraph(attackDelay, bulletSpeed, damage, pierce, range, visualRange, homing, upgradeCost), imageFileName, bulletFileName, cost);
	}

	public static UpgradeNode createLinearGraph(List<Integer> attackDelay, List<Float> bulletSpeed, List<Integer> damage, List<Integer> pierce, List<Float> range, List<Float> visualRange, List<Boolean> homing, List<Integer> upgradeCost) {
		int n = attackDelay.size();
		UpgradeNode[] nodes = new UpgradeNode[n];
		for (int i = n - 1; i >= 0; i--) {
			GirlTier tier = new GirlTier(
				attackDelay.get(i),
				bulletSpeed.get(i),
				damage.get(i),
				pierce.get(i),
				range.get(i),
				visualRange.get(i),
				homing.get(i)
			);
			int costToThisNode = (i == 0) ? 0 : upgradeCost.get(i - 1);
			String title = (i == 0) ? "Base" : "Tier " + (i + 1);
			List<UpgradeNode> children = (i < n - 1) ? Collections.singletonList(nodes[i + 1]) : Collections.emptyList();
			nodes[i] = new UpgradeNode(title, costToThisNode, tier, children, i);
		}
		return nodes[0];
	}

	public String getName() {
		return name;
	}

	public UpgradeNode getCurrentNode() {
		return currentNode;
	}

	public UpgradeNode getRootNode() {
		return rootNode;
	}

	public List<UpgradeNode> getAvailableUpgrades() {
		return currentNode.getChildren();
	}

	public int getAttackDelay() {
		return currentNode.getStats().getAttackDelay();
	}

	public int getCooldown() {
		return cooldown;
	}

	public void decrementCooldown() {
		cooldown--;
	}

	public void resetCooldown() {
		cooldown = currentNode.getStats().getAttackDelay();
	}

	public int getDamage() {
		return currentNode.getStats().getDamage();
	}

	public int getPierce() {
		return currentNode.getStats().getPierce();
	}

	public float getRange() {
		return currentNode.getStats().getRange();
	}

	public float getVisualRange() {
		return currentNode.getStats().getVisualRange();
	}

	public boolean isHoming() {
		return currentNode.getStats().isHoming();
	}

	public float getBulletSpeed() {
		return currentNode.getStats().getBulletSpeed();
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
		List<UpgradeNode> available = getAvailableUpgrades();
		if (available.isEmpty()) {
			return NO_UPGRADES_AVAILABLE;
		}
		return available.get(0).getUpgradeCost();
	}

	public int getSellPrice() {
		return totalInvestment / 2;
	}

	public int getLevel() {
		return currentNode.getLevel();
	}

	public Bullet createBullet() {
		return new Bullet(currentNode.getStats().getBulletSpeed(), getDamage(), getPierce(), getRange(), isHoming(), bulletFileName);
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
		List<UpgradeNode> available = getAvailableUpgrades();
		if (!available.isEmpty()) {
			return upgrade(available.get(0));
		}
		return 0;
	}

	public int upgrade(UpgradeNode targetNode) {
		int upgradeCost = targetNode.getUpgradeCost();
		currentNode = targetNode;
		totalInvestment += upgradeCost;
		resetCooldown();
		return upgradeCost;
	}

	public boolean canUpgrade(int currentCash) {
		if (getAvailableUpgrades().isEmpty()) {
			return false;
		}
		for (UpgradeNode option : getAvailableUpgrades()) {
			if (currentCash >= option.getUpgradeCost()) {
				return true;
			}
		}
		return false;
	}

	public boolean canUpgrade(UpgradeNode targetNode, int currentCash) {
		if (targetNode == null) {
			return false;
		}
		return currentCash >= targetNode.getUpgradeCost();
	}

	public Girl getUpgradedStats() {
		List<UpgradeNode> available = getAvailableUpgrades();
		if (!available.isEmpty()) {
			return getUpgradedStats(available.get(0));
		}
		return null;
	}

	public Girl getUpgradedStats(UpgradeNode targetNode) {
		if (targetNode != null) {
			Girl upgradedGirl = new Girl(name, rootNode, imageFileName, bulletFileName, cost);
			upgradedGirl.currentNode = targetNode;
			upgradedGirl.totalInvestment = this.totalInvestment + targetNode.getUpgradeCost();
			return upgradedGirl;
		} else {
			return null;
		}
	}

}
