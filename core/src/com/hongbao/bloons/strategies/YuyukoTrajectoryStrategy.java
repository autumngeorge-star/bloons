package com.hongbao.bloons.strategies;

import com.hongbao.bloons.helpers.Pair;

/**
 * Trajectory strategy for Yuyuko's spell card bullets.
 * - Initial phase (<= 150 frames): Fan formation, alternating between turning left and right.
 * - After initial phase (> 150 frames): Hands off to target homing (returns null).
 */
public class YuyukoTrajectoryStrategy implements SpellCardTrajectoryStrategy {

	private static final int DEFAULT_INITIAL_PHASE_FRAMES = 150;
	private final int initialPhaseFrames;

	public YuyukoTrajectoryStrategy() {
		this(DEFAULT_INITIAL_PHASE_FRAMES);
	}

	public YuyukoTrajectoryStrategy(int initialPhaseFrames) {
		this.initialPhaseFrames = initialPhaseFrames;
	}

	@Override
	public Pair<Float, Float> calculateDirection(int frames, float dx, float dy) {
		if (frames > initialPhaseFrames) {
			// Transition to target homing after initial fan formation phase
			return null;
		}

		// Alternate between turning left and right
		if (frames % 150 < 75) {
			double currentAngle = Math.atan2(dy, dx);
			double desiredAngle = currentAngle - (2 * Math.PI / 300);

			return new Pair<>((float) Math.cos(desiredAngle), (float) Math.sin(desiredAngle));
		} else {
			double currentAngle = Math.atan2(dy, dx);
			double desiredAngle = currentAngle + (2 * Math.PI / 300);

			return new Pair<>((float) Math.cos(desiredAngle), (float) Math.sin(desiredAngle));
		}
	}
}
