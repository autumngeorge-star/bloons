package com.hongbao.bloons.entities;

import com.hongbao.bloons.helpers.BloonPoppedResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.hongbao.bloons.entities.Bloon.Color.BFB;
import static com.hongbao.bloons.entities.Bloon.Color.BLACK;
import static com.hongbao.bloons.entities.Bloon.Color.BLUE;
import static com.hongbao.bloons.entities.Bloon.Color.CERAMIC;
import static com.hongbao.bloons.entities.Bloon.Color.GREEN;
import static com.hongbao.bloons.entities.Bloon.Color.LEAD;
import static com.hongbao.bloons.entities.Bloon.Color.MOAB;
import static com.hongbao.bloons.entities.Bloon.Color.PINK;
import static com.hongbao.bloons.entities.Bloon.Color.RAINBOW;
import static com.hongbao.bloons.entities.Bloon.Color.RED;
import static com.hongbao.bloons.entities.Bloon.Color.YELLOW;
import static com.hongbao.bloons.entities.Bloon.Color.ZEBRA;
import static com.hongbao.bloons.entities.Bloon.Color.ZOMG;


public class Bloon {

	public enum Color {

		RED("red"),
		BLUE("blue"),
		GREEN("green"),
		YELLOW("yellow"),
		PINK("pink"),
		BLACK("black"),
		LEAD("lead"),
		ZEBRA("zebra"),
		RAINBOW("rainbow"),
		CERAMIC("ceramic"),
		MOAB("moab"),
		BFB("bfb"),
		ZOMG("zomg");

		private String value;

		public String getValue() {
			return value;
		}

		private Color(String value) {
			this.value = value;
		}
	}

	public static final String IMAGE_FOLDER = "img/bloons/";
	public static final String CAMO_BLOON_DENOTATION = "_camo";
	public static final String REGROWTH_BLOON_DENOTATION = "_regrowth";
	public static final String IMAGE_FILE_EXTENSION = ".png";

	private static final Map<Integer, Color> HEALTH_TO_COLOR = new HashMap<Integer, Color>() {
		{
			put(1, RED);
			put(2, BLUE);
			put(3, GREEN);
			put(4, YELLOW);
			put(5, PINK);
			put(6, BLACK);
			put(7, ZEBRA);
			put(8, RAINBOW);
		}
	};
	
	public static final Map<Color, Integer> COLOR_TO_SPEED = new HashMap<Color, Integer>() {
		{
			put(RED, 5);
			put(BLUE, 6);
			put(GREEN, 7);
			put(YELLOW, 8);
			put(PINK, 9);
			put(BLACK, 7);
			put(LEAD, 5);
			put(ZEBRA, 7);
			put(RAINBOW, 8);
			put(CERAMIC, 7);
			put(MOAB, 5);
			put(BFB, 3);
			put(ZOMG, 2);
		}
	};


	private Color color;
	private String imageFileName;
	private int health;
	private float speedMultiplier = 1.0f;
	private Map<String, StatusEffect> statusEffects = new LinkedHashMap<>();
	private int distanceTravelled;
	private boolean camo;
	private boolean regen;

	public Bloon(Color color, int health, boolean camo, boolean regen) {
		this.color = color;
		this.health = health;
		this.camo = camo;
		this.regen = regen;
		this.imageFileName = createImageFileName(color.getValue(), camo, regen);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getBaseSpeed() {
		Integer base = COLOR_TO_SPEED.get(color);
		return base != null ? base : 0;
	}

	public float getSpeedMultiplier() {
		float totalMultiplier = this.speedMultiplier;
		for (StatusEffect effect : statusEffects.values()) {
			totalMultiplier *= effect.getSpeedMultiplier();
		}
		return totalMultiplier;
	}

	public void setSpeedMultiplier(float speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	public int getSpeed() {
		return Math.round(getBaseSpeed() * getSpeedMultiplier());
	}
	
	public void setSpeed(int speed) {
		int baseSpeed = getBaseSpeed();
		if (baseSpeed > 0) {
			this.speedMultiplier = (float) speed / baseSpeed;
		}
	}
	
	public int getDistanceTravelled() {
		return distanceTravelled;
	}
	
	public void setDistanceTravelled(int distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	
	public void incrementDistanceTravelled() {
		distanceTravelled += getSpeed();
	}

	public void addStatusEffect(StatusEffect effect) {
		if (effect != null && effect.getName() != null) {
			statusEffects.put(effect.getName(), effect.copy());
		}
	}

	public boolean removeStatusEffect(String name) {
		return statusEffects.remove(name) != null;
	}

	public StatusEffect getStatusEffect(String name) {
		StatusEffect effect = statusEffects.get(name);
		return effect != null ? effect.copy() : null;
	}

	public boolean hasStatusEffect(String name) {
		return statusEffects.containsKey(name);
	}

	public List<StatusEffect> getStatusEffects() {
		List<StatusEffect> copyList = new ArrayList<>();
		for (StatusEffect effect : statusEffects.values()) {
			copyList.add(effect.copy());
		}
		return copyList;
	}

	public void clearStatusEffects() {
		statusEffects.clear();
	}

	public void updateStatusEffects(float delta) {
		Iterator<Map.Entry<String, StatusEffect>> iterator = statusEffects.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, StatusEffect> entry = iterator.next();
			StatusEffect effect = entry.getValue();
			effect.update(delta);
			if (effect.isExpired()) {
				iterator.remove();
			}
		}
	}

	public void inheritStatusFrom(Bloon parent) {
		if (parent == null) return;
		this.speedMultiplier = parent.speedMultiplier;
		this.statusEffects.clear();
		for (StatusEffect effect : parent.statusEffects.values()) {
			this.statusEffects.put(effect.getName(), effect.copy());
		}
	}
	
	public boolean isCamo() {
		return camo;
	}

	public void setCamo(boolean camo) {
		this.camo = camo;
	}

	public boolean isRegen() {
		return regen;
	}

	public void setRegen(boolean regen) {
		this.regen = regen;
	}
	
	public boolean willPopBloon(int damage) {
		int resultingHealth = health - damage;
		
		if (resultingHealth <= 0) {
			return true;
		}
		
		Color resultingColor = getColorFromHealth(resultingHealth);
		return resultingColor != color;
	}
	
	public void damage(int damage) {
		this.health -= damage;
	}

	public BloonPoppedResult pop(int damage) {
		return new BloonPoppedResult(this, damage);
	}
	
	private static String createImageFileName(String color, boolean camo, boolean regen) {
		StringBuilder fileNameBuilder = new StringBuilder(IMAGE_FOLDER);
		fileNameBuilder.append(color);
		if (camo) {
			fileNameBuilder.append(CAMO_BLOON_DENOTATION);
		}
		if (regen) {
			fileNameBuilder.append(REGROWTH_BLOON_DENOTATION);
		}
		fileNameBuilder.append("_bloon");
		fileNameBuilder.append(IMAGE_FILE_EXTENSION);

		return fileNameBuilder.toString();
	}
	
	public static Color getColorFromHealth(int health) {
		if (HEALTH_TO_COLOR.containsKey(health)) {
			return HEALTH_TO_COLOR.get(health);
		}
		
		if (health <= 18) {
			return CERAMIC;
		}
		
		if (health <= 218) {
			return MOAB;
		}
		
		if (health <= 918) {
			return BFB;
		}
		
		return ZOMG;
	}
	
	public boolean isBlimp() {
		return color == MOAB || color == BFB || color == ZOMG;
	}
	
}
