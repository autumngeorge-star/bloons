package com.hongbao.bloons.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a node in the directed acyclic upgrade graph for character towers.
 */
public class UpgradeNode {

	private final String title;
	private final int upgradeCost;
	private final GirlTier stats;
	private final List<UpgradeNode> children;
	private int level;

	public UpgradeNode(String title, int upgradeCost, GirlTier stats) {
		this(title, upgradeCost, stats, new ArrayList<>(), 0);
	}

	public UpgradeNode(String title, int upgradeCost, GirlTier stats, int level) {
		this(title, upgradeCost, stats, new ArrayList<>(), level);
	}

	public UpgradeNode(String title, int upgradeCost, GirlTier stats, List<UpgradeNode> children) {
		this(title, upgradeCost, stats, children, 0);
	}

	public UpgradeNode(String title, int upgradeCost, GirlTier stats, UpgradeNode... children) {
		this(title, upgradeCost, stats, new ArrayList<>(Arrays.asList(children)), 0);
	}

	public UpgradeNode(String title, int upgradeCost, GirlTier stats, List<UpgradeNode> children, int level) {
		this.title = title;
		this.upgradeCost = upgradeCost;
		this.stats = stats;
		this.children = new ArrayList<>();
		this.level = level;
		if (children != null) {
			for (UpgradeNode child : children) {
				addChild(child);
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public String getDisplayTitle() {
		return title;
	}

	public String getBranchTitle() {
		return title;
	}

	public int getUpgradeCost() {
		return upgradeCost;
	}

	public int getCost() {
		return upgradeCost;
	}

	public GirlTier getStats() {
		return stats;
	}

	public List<UpgradeNode> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public List<UpgradeNode> getBranches() {
		return getChildren();
	}

	public List<UpgradeNode> getAvailableUpgrades() {
		return getChildren();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Adds a child node branch to this node.
	 * Checks for cycles to maintain acyclic property.
	 */
	public void addChild(UpgradeNode child) {
		if (child == null) {
			return;
		}
		if (detectCycle(child, this)) {
			throw new IllegalArgumentException("Adding child creates a cycle in UpgradeNode graph: " + child.getTitle());
		}
		if (child.getLevel() == 0 && this.level >= 0) {
			child.setLevel(this.level + 1);
		}
		this.children.add(child);
	}

	public void addBranch(UpgradeNode child) {
		addChild(child);
	}

	private boolean detectCycle(UpgradeNode startNode, UpgradeNode target) {
		if (startNode == target) {
			return true;
		}
		for (UpgradeNode child : startNode.getChildren()) {
			if (detectCycle(child, target)) {
				return true;
			}
		}
		return false;
	}
}
