package com.hongbao.bloons.effects;

import com.hongbao.bloons.entities.Bloon;

public interface StatusEffectStrategy {
    /**
     * Executed when the status effect is first applied to the target bloon.
     *
     * @param target The target bloon receiving the effect.
     */
    void onApply(Bloon target);

    /**
     * Executed on every frame update tick while the status effect is active.
     *
     * @param target The target bloon receiving the effect.
     * @param delta  The elapsed time since the last frame update in seconds.
     */
    void onTick(Bloon target, float delta);

    /**
     * Executed when the status effect expires or is removed from the target bloon.
     *
     * @param target The target bloon receiving the effect.
     */
    void onExpire(Bloon target);

    /**
     * Checks if the status effect duration has expired.
     *
     * @return true if the status effect is expired, false otherwise.
     */
    boolean isExpired();

    /**
     * Gets the status effect type.
     *
     * @return StatusEffectType of this strategy.
     */
    StatusEffectType getType();

    /**
     * Gets the initial duration of this status effect strategy in seconds.
     *
     * @return float initial duration.
     */
    float getDuration();

    /**
     * Gets the remaining duration of this status effect strategy in seconds.
     *
     * @return float remaining duration.
     */
    float getRemainingDuration();

    /**
     * Gets the intensity value associated with this status effect strategy.
     *
     * @return float intensity.
     */
    float getIntensity();

    /**
     * Gets the current stack count of this status effect strategy.
     *
     * @return int stack count.
     */
    int getStacks();

    /**
     * Refreshes or resets the active duration of the effect.
     *
     * @param duration New duration in seconds.
     */
    void refreshDuration(float duration);

    /**
     * Attempts to increment stack count if stackable and below maxStacks.
     *
     * @return true if stack was added, false if max stacks reached.
     */
    boolean addStack();
}
