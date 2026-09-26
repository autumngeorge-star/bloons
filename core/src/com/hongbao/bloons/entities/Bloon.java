package com.hongbao.bloons.entities;

import com.hongbao.bloons.helpers.BloonPoppedResult;

import java.util.ArrayList;
import java.util.HashMap;
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

	public static class ActiveStatusEffect {
		private final StatusEffectPayload payload;
		private float remainingDuration;
		private float tickTimer;

		public ActiveStatusEffect(StatusEffectPayload payload, float duration) {
			this.payload = payload;
			this.remainingDuration = duration;
			this.tickTimer = 0f;
		}

		public StatusEffectPayload getPayload() {
			return payload;
		}

		public float getRemainingDuration() {
			return remainingDuration;
		}

		public void setRemainingDuration(float remainingDuration) {
			this.remainingDuration = remainingDuration;
		}

		public float getTickTimer() {
			return tickTimer;
		}

		public void setTickTimer(float tickTimer) {
			this.tickTimer = tickTimer;
		}
	}

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

	public static final int MAX_STACKS_PER_TYPE = 3;
	public static final float MAX_TOTAL_DURATION = 15.0f;

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
	private int speed;
	private int distanceTravelled;
	private boolean camo;
	private boolean regen;
	private List<ActiveStatusEffect> activeStatusEffects = new ArrayList<>();

	public Bloon(Color color, int health, boolean camo, boolean regen) {
		this.color = color;
		this.health = health;
		speed = COLOR_TO_SPEED.get(color);
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
	
	public int getSpeed() {
		if (activeStatusEffects.isEmpty()) {
			return speed;
		}

		boolean frozen = false;
		float maxSlowPotency = 0f;

		for (ActiveStatusEffect effect : activeStatusEffects) {
			if (effect.getRemainingDuration() > 0) {
				if (effect.getPayload().getType() == StatusEffectPayload.Type.FREEZE) {
					frozen = true;
					break;
				} else if (effect.getPayload().getType() == StatusEffectPayload.Type.SLOW) {
					if (effect.getPayload().getPotency() > maxSlowPotency) {
						maxSlowPotency = effect.getPayload().getPotency();
					}
				}
			}
		}

		if (frozen) {
			return 0;
		}

		if (maxSlowPotency > 0f) {
			int effective = (int) Math.round(speed * (1.0f - maxSlowPotency));
			return Math.max(0, effective);
		}

		return speed;
	}

	public int getBaseSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
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

	public List<ActiveStatusEffect> getActiveStatusEffects() {
		return activeStatusEffects;
	}

	public void applyStatusEffect(StatusEffectPayload payload) {
		if (payload == null || payload.getType() == null) {
			return;
		}

		List<ActiveStatusEffect> sameTypeEffects = new ArrayList<>();
		for (ActiveStatusEffect effect : activeStatusEffects) {
			if (effect.getPayload().getType() == payload.getType()) {
				sameTypeEffects.add(effect);
			}
		}

		if (sameTypeEffects.size() >= MAX_STACKS_PER_TYPE) {
			ActiveStatusEffect shortest = sameTypeEffects.get(0);
			for (ActiveStatusEffect effect : sameTypeEffects) {
				if (effect.getRemainingDuration() < shortest.getRemainingDuration()) {
					shortest = effect;
				}
			}
			float newDuration = Math.min(MAX_TOTAL_DURATION, Math.max(shortest.getRemainingDuration(), payload.getDuration()));
			shortest.setRemainingDuration(newDuration);
		} else {
			float duration = Math.min(MAX_TOTAL_DURATION, payload.getDuration());
			activeStatusEffects.add(new ActiveStatusEffect(payload, duration));
		}
	}

	public void applyStatusEffects(List<StatusEffectPayload> payloads) {
		if (payloads == null) return;
		for (StatusEffectPayload payload : payloads) {
			applyStatusEffect(payload);
		}
	}

	public int updateStatusEffects(float delta) {
		int accumulatedTickDamage = 0;
		List<ActiveStatusEffect> expired = new ArrayList<>();

		for (ActiveStatusEffect effect : activeStatusEffects) {
			effect.setRemainingDuration(effect.getRemainingDuration() - delta);
			if (effect.getRemainingDuration() <= 0) {
				expired.add(effect);
				continue;
			}

			float tickInterval = effect.getPayload().getTickInterval();
			if (tickInterval > 0) {
				effect.setTickTimer(effect.getTickTimer() + delta);
				if (effect.getTickTimer() >= tickInterval) {
					int dmg = (int) Math.max(1, effect.getPayload().getPotency());
					accumulatedTickDamage += dmg;
					effect.setTickTimer(effect.getTickTimer() - tickInterval);
				}
			}
		}

		activeStatusEffects.removeAll(expired);
		return accumulatedTickDamage;
	}

	public void clearStatusEffects() {
		activeStatusEffects.clear();
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
