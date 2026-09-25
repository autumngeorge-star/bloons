package com.hongbao.bloons.helpers;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
	private List<Bloon> bloonsGenerated;
	
	public BloonPoppedResult(Bloon initialBloon, int initialDamage) {
		bloonsGenerated = new ArrayList<>();
		cashGenerated = 0;

		Queue<DamageEvent> damageQueue = new ArrayDeque<>();
		damageQueue.add(DamageEvent.obtain(initialBloon, initialDamage));

		while (!damageQueue.isEmpty()) {
			DamageEvent event = damageQueue.poll();
			Bloon currentBloon = event.getBloon();
			int remDamage = event.getDamage();

			int threshold = currentBloon.getLayerPopThreshold();

			if (remDamage < threshold) {
				currentBloon.damage(remDamage);
				cashGenerated += remDamage;
				bloonsGenerated.add(currentBloon);
			} else {
				cashGenerated += threshold;
				int residualDamage = remDamage - threshold;
				List<Bloon> children = createChildrenForBloon(currentBloon);

				for (Bloon child : children) {
					if (residualDamage > 0) {
						damageQueue.add(DamageEvent.obtain(child, residualDamage));
					} else {
						bloonsGenerated.add(child);
					}
				}
			}

			DamageEvent.free(event);
		}
	}

	public static List<Bloon> createChildrenForBloon(Bloon parent) {
		List<Bloon> children = new ArrayList<>();
		Bloon.Color color = parent.getColor();

		switch (color) {
			case ZOMG:
				for (int i = 0; i < 4; i++) children.add(BloonFactory.createBFB());
				break;
			case BFB:
				for (int i = 0; i < 4; i++) children.add(BloonFactory.createMOAB());
				break;
			case MOAB:
				for (int i = 0; i < 4; i++) children.add(BloonFactory.createCeramicBloon());
				break;
			case CERAMIC:
				for (int i = 0; i < 2; i++) children.add(BloonFactory.createRainbowBloon());
				break;
			case RAINBOW:
				for (int i = 0; i < 2; i++) children.add(BloonFactory.createZebraBloon());
				break;
			case ZEBRA:
			case LEAD:
				for (int i = 0; i < 2; i++) children.add(BloonFactory.createBlackBloon());
				break;
			case BLACK:
				for (int i = 0; i < 2; i++) children.add(BloonFactory.createPinkBloon());
				break;
			case PINK:
				children.add(BloonFactory.createYellowBloon());
				break;
			case YELLOW:
				children.add(BloonFactory.createGreenBloon());
				break;
			case GREEN:
				children.add(BloonFactory.createBlueBloon());
				break;
			case BLUE:
				children.add(BloonFactory.createRedBloon());
				break;
			case RED:
			default:
				break;
		}

		for (Bloon child : children) {
			child.setDistanceTravelled(parent.getDistanceTravelled());
			child.setCamo(parent.isCamo());
			child.setRegen(parent.isRegen());
		}

		return children;
	}
	
	public int getCashGenerated() {
		return cashGenerated;
	}
	
	public List<Bloon> getBloonsGenerated() {
		return bloonsGenerated;
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
