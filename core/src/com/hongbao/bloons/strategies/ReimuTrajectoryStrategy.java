package com.hongbao.bloons.strategies;

import com.hongbao.bloons.helpers.Pair;

/**
 * Trajectory strategy for Reimu's spell card bullets.
 * - Frames < 20: Flies in a straight line.
 * - Frames 20..200: Curves in a circular arc.
 * - Frames > 200: Hands off to target homing (returns null).
 */
public class ReimuTrajectoryStrategy implements SpellCardTrajectoryStrategy {

	private static final int INITIAL_STRAIGHT_FRAMES = 20;
	private static final int MAX_OVERRIDE_FRAMES = 200;

	@Override
	public Pair<Float, Float> calculateDirection(int frames, float dx, float dy) {
		if (frames > MAX_OVERRIDE_FRAMES) {
			// Beyond 200 frames, use the default bullet behavior (homing)
			return null;
		} else if (frames < INITIAL_STRAIGHT_FRAMES) {
			// For the first few frames, go in a straight line (direction unchanged)
			return new Pair<>(dx, dy);
		} else {
			// For the middle frames, start going in a circle
			double currentAngle = Math.atan2(dy, dx);
			double desiredAngle = currentAngle + (2 * Math.PI / 120);

			return new Pair<>((float) Math.cos(desiredAngle), (float) Math.sin(desiredAngle));
		}
	}
}
