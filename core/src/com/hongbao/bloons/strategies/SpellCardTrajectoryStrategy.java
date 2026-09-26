package com.hongbao.bloons.strategies;

import com.hongbao.bloons.helpers.Pair;

/**
 * Strategy interface for calculating custom spell card bullet trajectory directions.
 */
public interface SpellCardTrajectoryStrategy {

	/**
	 * Calculates the updated direction vector for a spell card bullet.
	 *
	 * @param frames Number of frames elapsed since bullet creation.
	 * @param dx Current x-direction unit vector component.
	 * @param dy Current y-direction unit vector component.
	 * @return Pair containing the new (dx, dy) direction vector, or null if the strategy phase has ended
	 *         and movement should hand off to default bullet behavior (e.g., target homing).
	 */
	Pair<Float, Float> calculateDirection(int frames, float dx, float dy);
}
