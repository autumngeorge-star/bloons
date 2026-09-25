package com.hongbao.bloons.helpers;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.HashMap;
import java.util.HashSet;
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
		bloonsGenerated = new HashSet<>();
		
		if (bloon.getHealth() > damage) {
			Bloon.Color originalColor = bloon.getColor();
			int newBloonHealth = bloon.getHealth() - damage;
			Bloon.Color poppedColor = Bloon.getColorFromHealth(newBloonHealth);
			
			int bloonsToBeCreated = COLOR_TO_RATIO.get(poppedColor) / COLOR_TO_RATIO.get(originalColor);
			
			for (int x = 0; x < bloonsToBeCreated; x++) {
				Bloon bloonOfType = BloonFactory.createChildBloon(bloon, poppedColor, newBloonHealth);
				bloonOfType.setDistanceTravelled(bloon.getDistanceTravelled());
				bloonsGenerated.add(bloonOfType);
			}
		}
		
		cashGenerated = calculateHealthDifferenceBetweenBloons(bloon, bloonsGenerated);
	}
	
	public int getCashGenerated() {
		return cashGenerated;
	}
	
	public Set<Bloon> getBloonsGenerated() {
		return bloonsGenerated;
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
