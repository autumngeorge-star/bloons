package com.hongbao.bloons.policies;

import com.hongbao.bloons.entities.Bullet;

/**
 * Expiration policy based on distance traveled by the projectile.
 */
public class RangeLifecyclePolicy implements BulletLifecyclePolicy {

    public static final RangeLifecyclePolicy DEFAULT = new RangeLifecyclePolicy();

    private final Float maxRange;

    /**
     * Constructs a RangeLifecyclePolicy that queries the bullet's max range threshold.
     */
    public RangeLifecyclePolicy() {
        this.maxRange = null;
    }

    /**
     * Constructs a RangeLifecyclePolicy with a specific max range threshold.
     *
     * @param maxRange maximum distance threshold before expiration
     */
    public RangeLifecyclePolicy(float maxRange) {
        this.maxRange = maxRange;
    }

    public Float getMaxRange() {
        return maxRange;
    }

    @Override
    public boolean isExpired(Bullet bullet) {
        if (bullet == null) {
            return false;
        }
        float limit = (maxRange != null) ? maxRange : bullet.getMaxRange();
        return bullet.getDistanceTraveled() >= limit;
    }

    @Override
    public boolean isExpired(float distanceTraveled, float duration) {
        if (maxRange != null) {
            return distanceTraveled >= maxRange;
        }
        return false;
    }
}
