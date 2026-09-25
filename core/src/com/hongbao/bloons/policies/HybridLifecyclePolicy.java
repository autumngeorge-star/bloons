package com.hongbao.bloons.policies;

import com.hongbao.bloons.entities.Bullet;

/**
 * Composite lifecycle policy that combines range and duration expiration criteria.
 * Expiration occurs if either the range or duration threshold is met.
 */
public class HybridLifecyclePolicy implements BulletLifecyclePolicy {

    public static final HybridLifecyclePolicy DEFAULT = new HybridLifecyclePolicy();

    private final BulletLifecyclePolicy rangePolicy;
    private final BulletLifecyclePolicy durationPolicy;

    /**
     * Constructs a HybridLifecyclePolicy using default range and duration policies.
     */
    public HybridLifecyclePolicy() {
        this.rangePolicy = RangeLifecyclePolicy.DEFAULT;
        this.durationPolicy = DurationLifecyclePolicy.DEFAULT;
    }

    /**
     * Constructs a HybridLifecyclePolicy with explicit max range and max duration thresholds.
     *
     * @param maxRange    maximum distance threshold
     * @param maxDuration maximum active duration threshold
     */
    public HybridLifecyclePolicy(float maxRange, float maxDuration) {
        this.rangePolicy = new RangeLifecyclePolicy(maxRange);
        this.durationPolicy = new DurationLifecyclePolicy(maxDuration);
    }

    /**
     * Constructs a HybridLifecyclePolicy combining two specific policies.
     *
     * @param rangePolicy    policy for range check
     * @param durationPolicy policy for duration check
     */
    public HybridLifecyclePolicy(BulletLifecyclePolicy rangePolicy, BulletLifecyclePolicy durationPolicy) {
        this.rangePolicy = rangePolicy != null ? rangePolicy : RangeLifecyclePolicy.DEFAULT;
        this.durationPolicy = durationPolicy != null ? durationPolicy : DurationLifecyclePolicy.DEFAULT;
    }

    public BulletLifecyclePolicy getRangePolicy() {
        return rangePolicy;
    }

    public BulletLifecyclePolicy getDurationPolicy() {
        return durationPolicy;
    }

    @Override
    public boolean isExpired(Bullet bullet) {
        if (bullet == null) {
            return false;
        }
        return rangePolicy.isExpired(bullet) || durationPolicy.isExpired(bullet);
    }

    @Override
    public boolean isExpired(float distanceTraveled, float duration) {
        return rangePolicy.isExpired(distanceTraveled, duration) || durationPolicy.isExpired(distanceTraveled, duration);
    }
}
