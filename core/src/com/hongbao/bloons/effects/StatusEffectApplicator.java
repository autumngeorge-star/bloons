package com.hongbao.bloons.effects;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;

/**
 * Functional strategy interface for applying dynamic status effects
 * during projectile-bloon collision resolution.
 */
@FunctionalInterface
public interface StatusEffectApplicator {

    /**
     * Executes status effect logic on the target bloon within the context
     * of the colliding projectile.
     *
     * @param target      The target bloon actor involved in the collision.
     * @param bulletActor The bullet actor causing the collision.
     */
    void apply(BloonActor target, BulletActor bulletActor);
}
