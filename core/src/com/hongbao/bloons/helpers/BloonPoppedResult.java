package com.hongbao.bloons.helpers;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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


public class BloonPoppedResult {
	
	public static final Map<Bloon.Color, Integer> COLOR_TO_RATIO = new HashMap<Bloon.Color, Integer>() {
		{
			put(ZOMG, 1);
			put(BFB, 4);
			put(MOAB, 16);
			put(CERAMIC, 64);
			put(RAINBOW, 128);
			put(ZEBRA, 256);
			put(LEAD, 256);
			put(BLACK, 512);
			put(PINK, 1024);
			put(YELLOW, 1024);
			put(GREEN, 1024);
			put(BLUE, 1024);
			put(RED, 1024);
		}
	};
	
	private int cashGenerated;
	private Set<Bloon> bloonsGenerated;
	
	public BloonPoppedResult(Bloon bloon, int damage) {
		bloonsGenerated = new LinkedHashSet<>();
		processBloonDamage(bloon, damage, bloonsGenerated);
		cashGenerated = calculateHealthDifferenceBetweenBloons(bloon, bloonsGenerated);
	}
	
	public int getCashGenerated() {
		return cashGenerated;
	}
	
	public Set<Bloon> getBloonsGenerated() {
		return bloonsGenerated;
	}
	
	private static void processBloonDamage(Bloon currentBloon, int remainingDamage, Set<Bloon> resultBloons) {
		int currentHealth = currentBloon.getHealth();
		int threshold = getPopThresholdHealth(currentHealth);
		int shellHp = currentHealth - threshold;
		
		if (remainingDamage < shellHp) {
			int newHealth = currentHealth - remainingDamage;
			currentBloon.setHealth(newHealth);
			currentBloon.setColor(Bloon.getColorFromHealth(newHealth));
			currentBloon.setSpeed(Bloon.COLOR_TO_SPEED.get(currentBloon.getColor()));
			resultBloons.add(currentBloon);
		} else {
			int excessDamage = remainingDamage - shellHp;
			Set<Bloon> children = createChildrenForThreshold(currentBloon, threshold);
			if (children.isEmpty() || excessDamage == 0) {
				resultBloons.addAll(children);
			} else {
				for (Bloon child : children) {
					processBloonDamage(child, excessDamage, resultBloons);
				}
			}
		}
	}

	private static int getPopThresholdHealth(int health) {
		if (health > 918) {
			return 918;
		} else if (health > 218) {
			return 218;
		} else if (health > 18) {
			return 18;
		} else if (health > 8) {
			return 8;
		} else {
			return health - 1;
		}
	}

	private static Set<Bloon> createChildrenForThreshold(Bloon parent, int threshold) {
		Set<Bloon> children = new LinkedHashSet<>();
		boolean camo = parent.isCamo();
		boolean regen = parent.isRegen();
		int dist = parent.getDistanceTravelled();

		if (threshold == 918) {
			for (int i = 0; i < 4; i++) {
				Bloon child = BloonFactory.createBFB();
				child.setCamo(camo);
				child.setRegen(regen);
				child.setDistanceTravelled(dist);
				children.add(child);
			}
		} else if (threshold == 218) {
			for (int i = 0; i < 4; i++) {
				Bloon child = BloonFactory.createMOAB();
				child.setCamo(camo);
				child.setRegen(regen);
				child.setDistanceTravelled(dist);
				children.add(child);
			}
		} else if (threshold == 18) {
			for (int i = 0; i < 4; i++) {
				Bloon child = BloonFactory.createCeramicBloon();
				child.setCamo(camo);
				child.setRegen(regen);
				child.setDistanceTravelled(dist);
				children.add(child);
			}
		} else if (threshold == 8) {
			for (int i = 0; i < 2; i++) {
				Bloon child = BloonFactory.createRainbowBloon();
				child.setCamo(camo);
				child.setRegen(regen);
				child.setDistanceTravelled(dist);
				children.add(child);
			}
		} else if (threshold == 7) {
			for (int i = 0; i < 2; i++) {
				Bloon child = BloonFactory.createZebraBloon();
				child.setCamo(camo);
				child.setRegen(regen);
				child.setDistanceTravelled(dist);
				children.add(child);
			}
		} else if (threshold == 6) {
			for (int i = 0; i < 2; i++) {
				Bloon child = BloonFactory.createBlackBloon();
				child.setCamo(camo);
				child.setRegen(regen);
				child.setDistanceTravelled(dist);
				children.add(child);
			}
		} else if (threshold == 5) {
			for (int i = 0; i < 2; i++) {
				Bloon child = BloonFactory.createPinkBloon();
				child.setCamo(camo);
				child.setRegen(regen);
				child.setDistanceTravelled(dist);
				children.add(child);
			}
		} else if (threshold == 4) {
			Bloon child = BloonFactory.createYellowBloon();
			child.setCamo(camo);
			child.setRegen(regen);
			child.setDistanceTravelled(dist);
			children.add(child);
		} else if (threshold == 3) {
			Bloon child = BloonFactory.createGreenBloon();
			child.setCamo(camo);
			child.setRegen(regen);
			child.setDistanceTravelled(dist);
			children.add(child);
		} else if (threshold == 2) {
			Bloon child = BloonFactory.createBlueBloon();
			child.setCamo(camo);
			child.setRegen(regen);
			child.setDistanceTravelled(dist);
			children.add(child);
		} else if (threshold == 1) {
			Bloon child = BloonFactory.createRedBloon();
			child.setCamo(camo);
			child.setRegen(regen);
			child.setDistanceTravelled(dist);
			children.add(child);
		}
		return children;
	}
	
	private static int calculateHealthDifferenceBetweenBloons(Bloon bloon, Set<Bloon> bloons) {
		int healthDifference = getTotalHealthOfBloon(bloon);
		
		for (Bloon resultBloon : bloons) {
			healthDifference -= getTotalHealthOfBloon(resultBloon);
		}
		
		if (healthDifference <= 0) {
			throw new RuntimeException("Something went wrong.");
		}
		
		return healthDifference;
	}
	
	public static int getTotalHealthOfBloon(Bloon bloon) {
		if (bloon.getHealth() <= 5) {
			// Normal bloon (Red to Pink)
			return bloon.getHealth();
		} else if (bloon.getHealth() == 6) {
			// Black bloon
			return 11;
		} else if (bloon.getHealth() == 7) {
			// Lead or zebra bloon
			return 23;
		} else if (bloon.getHealth() == 8) {
			// Rainbow bloon
			return 47;
		} else if (bloon.getHealth() <= 18) {
			// Ceramic bloon (two rainbow bloons plus whatever health is left on the shell of the bloon)
			return 94 + (bloon.getHealth() - 8);
		} else if (bloon.getHealth() <= 218) {
			// Moab (with 4 ceramic bloons)
			return 416 + (bloon.getHealth() - 18);
		} else if (bloon.getHealth() <= 918) {
			// BFB (with 4 MOABs)
			return 2464 + (bloon.getHealth() - 218);
		} else {
			// ZOMG (with 4 BFBs)
			return 12656 + (bloon.getHealth() - 918);
		}
	}
	
}
