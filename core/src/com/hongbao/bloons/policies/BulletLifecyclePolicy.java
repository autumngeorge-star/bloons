package com.hongbao.bloons.policies;

import com.hongbao.bloons.entities.Bullet;

/**
 * Strategy interface defining the expiration criteria for a projectile bullet.
 */
public interface BulletLifecyclePolicy {

    /**
     * Determines whether the projectile has expired based on the bullet state.
     *
     * @param bullet the projectile entity being evaluated
     * @return true if the projectile has expired and should be removed, false otherwise
     */
    boolean isExpired(Bullet bullet);

    /**
     * Determines whether the projectile has expired given its distance and duration state.
     *
     * @param distanceTraveled cumulative distance traveled by the bullet
     * @param duration cumulative duration (active time) of the bullet
     * @return true if the projectile has expired and should be removed, false otherwise
     */
    boolean isExpired(float distanceTraveled, float duration);
}
