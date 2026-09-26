package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Comparator;

/**
 * Deterministic Y-depth comparator for dynamic actor layers.
 * Sorts actors higher on screen (larger Y) before actors lower on screen (smaller Y),
 * so that lower objects render on top of higher objects in 2D visual perspective.
 * Uses System.identityHashCode as a tie-breaker to strictly satisfy the Comparator contract.
 */
public class YDepthComparator implements Comparator<Actor> {

	@Override
	public int compare(Actor a, Actor b) {
		if (a == b) {
			return 0;
		}
		if (a == null) return -1;
		if (b == null) return 1;

		int yCompare = Float.compare(b.getY(), a.getY());
		if (yCompare != 0) {
			return yCompare;
		}

		int identityCompare = Integer.compare(System.identityHashCode(a), System.identityHashCode(b));
		if (identityCompare != 0) {
			return identityCompare;
		}

		return Integer.compare(a.hashCode(), b.hashCode());
	}
}
