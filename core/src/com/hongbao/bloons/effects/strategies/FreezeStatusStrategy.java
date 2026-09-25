package com.hongbao.bloons.effects.strategies;

import com.hongbao.bloons.effects.StatusEffectStrategy;
import com.hongbao.bloons.effects.StatusEffectType;
import com.hongbao.bloons.entities.Bloon;

public class FreezeStatusStrategy implements StatusEffectStrategy {

    private final float initialDuration;
    private float remainingDuration;
    private final float intensity;
    private final float tickInterval;
    private final int tickDamage;
    private float timeSinceLastTick;
    private boolean expired;

    public FreezeStatusStrategy(float duration, float intensity, float tickInterval, int tickDamage) {
        this.initialDuration = duration;
        this.remainingDuration = duration;
        this.intensity = intensity <= 0f ? 1.0f : intensity; // 1.0f = 100% freeze (stop movement)
        this.tickInterval = tickInterval;
        this.tickDamage = tickDamage;
        this.timeSinceLastTick = 0f;
        this.expired = false;
    }

    @Override
    public void onApply(Bloon target) {
        target.recalculateSpeed();
    }

    @Override
    public void onTick(Bloon target, float delta) {
        if (expired) {
            return;
        }

        remainingDuration -= delta;

        if (tickInterval > 0f && tickDamage > 0) {
            timeSinceLastTick += delta;
            while (timeSinceLastTick >= tickInterval && !expired) {
                timeSinceLastTick -= tickInterval;
                target.damage(tickDamage);
            }
        }

        if (remainingDuration <= 0) {
            remainingDuration = 0;
            expired = true;
            onExpire(target);
        }
    }

    @Override
    public void onExpire(Bloon target) {
        target.recalculateSpeed();
    }

    @Override
    public boolean isExpired() {
        return expired || remainingDuration <= 0;
    }

    @Override
    public StatusEffectType getType() {
        return StatusEffectType.FREEZE;
    }

    @Override
    public float getIntensity() {
        return intensity;
    }

    @Override
    public int getStacks() {
        return 1;
    }

    @Override
    public void refreshDuration(float duration) {
        this.remainingDuration = Math.max(this.remainingDuration, duration);
        this.expired = false;
    }

    @Override
    public boolean addStack() {
        return false;
    }

    public float getDuration() {
        return initialDuration;
    }

    public float getRemainingDuration() {
        return remainingDuration;
    }
}
