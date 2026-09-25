package com.hongbao.bloons.policies;

import com.hongbao.bloons.entities.Bullet;

/**
 * Expiration policy based on active duration (time elapsed) of the projectile.
 */
public class DurationLifecyclePolicy implements BulletLifecyclePolicy {

    public static final DurationLifecyclePolicy DEFAULT = new DurationLifecyclePolicy();

    private final Float maxDuration;

    /**
     * Constructs a DurationLifecyclePolicy that queries the bullet's max duration threshold.
     */
    public DurationLifecyclePolicy() {
        this.maxDuration = null;
    }

    /**
     * Constructs a DurationLifecyclePolicy with a specific max duration threshold.
     *
     * @param maxDuration maximum active duration before expiration
     */
    public DurationLifecyclePolicy(float maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Float getMaxDuration() {
        return maxDuration;
    }

    @Override
    public boolean isExpired(Bullet bullet) {
        if (bullet == null) {
            return false;
        }
        float limit = (maxDuration != null) ? maxDuration : bullet.getMaxDuration();
        return bullet.getDuration() >= limit;
    }

    @Override
    public boolean isExpired(float distanceTraveled, float duration) {
        if (maxDuration != null) {
            return duration >= maxDuration;
        }
        return false;
    }
}
