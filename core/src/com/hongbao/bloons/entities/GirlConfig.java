package com.hongbao.bloons.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GirlConfig {

	private String name;
	private List<Integer> attackDelay;
	private List<Float> bulletSpeed;
	private List<Integer> damage;
	private List<Integer> pierce;
	private List<Float> range;
	private List<Float> visualRange;
	private List<Boolean> homing;
	private String imageFileName;
	private String bulletFileName;
	private String shopIcon;
	private int cost;
	private List<Integer> upgradeCost;

	public GirlConfig() {
	}

	public GirlConfig(String name, List<Integer> attackDelay, List<Float> bulletSpeed, List<Integer> damage,
	                  List<Integer> pierce, List<Float> range, List<Float> visualRange, List<Boolean> homing,
	                  String imageFileName, String bulletFileName, String shopIcon, int cost, List<Integer> upgradeCost) {
		this.name = name;
		this.attackDelay = attackDelay;
		this.bulletSpeed = bulletSpeed;
		this.damage = damage;
		this.pierce = pierce;
		this.range = range;
		this.visualRange = visualRange;
		this.homing = homing;
		this.imageFileName = imageFileName;
		this.bulletFileName = bulletFileName;
		this.shopIcon = shopIcon;
		this.cost = cost;
		this.upgradeCost = upgradeCost;
	}

	public static GirlConfig fromJsonValue(JsonValue json) {
		GirlConfig config = new GirlConfig();
		if (json == null) {
			logError("Received null JsonValue when parsing GirlConfig.");
			return config;
		}

		config.name = json.getString("name", "Unknown");

		config.attackDelay = parseIntList(json.get("attackDelay"), "attackDelay", config.name, Arrays.asList(50, 50, 50));
		config.bulletSpeed = parseFloatList(json.get("bulletSpeed"), "bulletSpeed", config.name, Arrays.asList(20f, 20f, 20f));
		config.damage = parseIntList(json.get("damage"), "damage", config.name, Arrays.asList(1, 1, 1));
		config.pierce = parseIntList(json.get("pierce"), "pierce", config.name, Arrays.asList(1, 1, 1));
		config.range = parseFloatList(json.get("range"), "range", config.name, Arrays.asList(500f, 500f, 500f));
		config.visualRange = parseFloatList(json.get("visualRange"), "visualRange", config.name, Arrays.asList(200f, 200f, 200f));
		config.homing = parseBooleanList(json.get("homing"), "homing", config.name, Arrays.asList(false, false, false));

		config.imageFileName = json.getString("imageFileName", "reimu.png");
		config.bulletFileName = json.getString("bulletFileName", "red_spell_card.png");
		config.shopIcon = json.getString("shopIcon", "reimu_box.png");

		config.cost = json.getInt("cost", 300);
		config.upgradeCost = parseIntList(json.get("upgradeCost"), "upgradeCost", config.name, Arrays.asList(200, 300, Girl.NO_UPGRADES_AVAILABLE));

		return config;
	}

	private static List<Integer> parseIntList(JsonValue arrayValue, String fieldName, String towerName, List<Integer> fallback) {
		if (arrayValue == null || !arrayValue.isArray()) {
			logError("Missing or malformed field '" + fieldName + "' for tower '" + towerName + "'. Using fallback values.");
			return fallback;
		}
		List<Integer> list = new ArrayList<>();
		for (JsonValue entry = arrayValue.child; entry != null; entry = entry.next) {
			list.add(entry.asInt());
		}
		if (list.isEmpty()) {
			logError("Empty field '" + fieldName + "' for tower '" + towerName + "'. Using fallback values.");
			return fallback;
		}
		return list;
	}

	private static List<Float> parseFloatList(JsonValue arrayValue, String fieldName, String towerName, List<Float> fallback) {
		if (arrayValue == null || !arrayValue.isArray()) {
			logError("Missing or malformed field '" + fieldName + "' for tower '" + towerName + "'. Using fallback values.");
			return fallback;
		}
		List<Float> list = new ArrayList<>();
		for (JsonValue entry = arrayValue.child; entry != null; entry = entry.next) {
			list.add(entry.asFloat());
		}
		if (list.isEmpty()) {
			logError("Empty field '" + fieldName + "' for tower '" + towerName + "'. Using fallback values.");
			return fallback;
		}
		return list;
	}

	private static List<Boolean> parseBooleanList(JsonValue arrayValue, String fieldName, String towerName, List<Boolean> fallback) {
		if (arrayValue == null || !arrayValue.isArray()) {
			logError("Missing or malformed field '" + fieldName + "' for tower '" + towerName + "'. Using fallback values.");
			return fallback;
		}
		List<Boolean> list = new ArrayList<>();
		for (JsonValue entry = arrayValue.child; entry != null; entry = entry.next) {
			list.add(entry.asBoolean());
		}
		if (list.isEmpty()) {
			logError("Empty field '" + fieldName + "' for tower '" + towerName + "'. Using fallback values.");
			return fallback;
		}
		return list;
	}

	private static void logError(String message) {
		if (Gdx.app != null) {
			Gdx.app.error("GirlConfig", message);
		} else {
			System.err.println("[GirlConfig ERROR] " + message);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getAttackDelay() {
		return attackDelay;
	}

	public void setAttackDelay(List<Integer> attackDelay) {
		this.attackDelay = attackDelay;
	}

	public List<Float> getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(List<Float> bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public List<Integer> getDamage() {
		return damage;
	}

	public void setDamage(List<Integer> damage) {
		this.damage = damage;
	}

	public List<Integer> getPierce() {
		return pierce;
	}

	public void setPierce(List<Integer> pierce) {
		this.pierce = pierce;
	}

	public List<Float> getRange() {
		return range;
	}

	public void setRange(List<Float> range) {
		this.range = range;
	}

	public List<Float> getVisualRange() {
		return visualRange;
	}

	public void setVisualRange(List<Float> visualRange) {
		this.visualRange = visualRange;
	}

	public List<Boolean> getHoming() {
		return homing;
	}

	public void setHoming(List<Boolean> homing) {
		this.homing = homing;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getBulletFileName() {
		return bulletFileName;
	}

	public void setBulletFileName(String bulletFileName) {
		this.bulletFileName = bulletFileName;
	}

	public String getShopIcon() {
		return shopIcon;
	}

	public void setShopIcon(String shopIcon) {
		this.shopIcon = shopIcon;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public List<Integer> getUpgradeCost() {
		return upgradeCost;
	}

	public void setUpgradeCost(List<Integer> upgradeCost) {
		this.upgradeCost = upgradeCost;
	}

}
