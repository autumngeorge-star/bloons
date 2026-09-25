package com.hongbao.bloons.effects.strategies;

import com.hongbao.bloons.effects.StatusEffectStrategy;
import com.hongbao.bloons.effects.StatusEffectType;
import com.hongbao.bloons.entities.Bloon;

public class SlowStatusStrategy implements StatusEffectStrategy {

    private final float initialDuration;
    private float remainingDuration;
    private final float intensity;
    private final boolean stackable;
    private final int maxStacks;
    private int stacks;
    private boolean expired;

    public SlowStatusStrategy(float duration, float intensity, boolean stackable, int maxStacks) {
        this.initialDuration = duration;
        this.remainingDuration = duration;
        this.intensity = intensity;
        this.stackable = stackable;
        this.maxStacks = Math.max(1, maxStacks);
        this.stacks = 1;
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
        return StatusEffectType.SLOW;
    }

    @Override
    public float getIntensity() {
        return intensity * stacks;
    }

    @Override
    public int getStacks() {
        return stacks;
    }

    @Override
    public void refreshDuration(float duration) {
        this.remainingDuration = Math.max(this.remainingDuration, duration);
        this.expired = false;
    }

    @Override
    public boolean addStack() {
        if (stackable && stacks < maxStacks) {
            stacks++;
            return true;
        }
        return false;
    }

    public float getDuration() {
        return initialDuration;
    }

    public float getRemainingDuration() {
        return remainingDuration;
    }
}
