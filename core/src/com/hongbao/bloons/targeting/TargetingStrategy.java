package com.hongbao.bloons.targeting;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.IBloon;

import java.util.Collection;

/**
 * Encapsulates targeting prioritization logic for selecting bloon targets
 * based on normalized path progress.
 */
public class TargetingStrategy {

    /**
     * Selects the bloon actor with the highest normalized path progress from a collection of candidate bloons.
     *
     * @param bloonsInRange collection of candidate bloon actors in range
     * @return the bloon actor with the highest normalized progress, or null if collection is empty or null
     */
    public static BloonActor selectTarget(Collection<BloonActor> bloonsInRange) {
        if (bloonsInRange == null || bloonsInRange.isEmpty()) {
            return null;
        }

        BloonActor target = null;
        float maxProgress = -Float.MAX_VALUE;

        for (BloonActor actor : bloonsInRange) {
            if (actor != null && actor.getBloon() != null) {
                IBloon bloon = actor.getBloon();
                float progress = bloon.getPathProgress() != null ?
                        bloon.getPathProgress().getNormalizedProgress() :
                        bloon.getDistanceTravelled();

                if (target == null || progress > maxProgress) {
                    target = actor;
                    maxProgress = progress;
                }
            }
        }

        return target;
    }
}
