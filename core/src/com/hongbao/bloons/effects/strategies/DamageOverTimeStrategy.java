package com.hongbao.bloons.effects.strategies;

import com.hongbao.bloons.effects.StatusEffectStrategy;
import com.hongbao.bloons.effects.StatusEffectType;
import com.hongbao.bloons.entities.Bloon;

public class DamageOverTimeStrategy implements StatusEffectStrategy {

    private final float initialDuration;
    private float remainingDuration;
    private final float tickInterval;
    private final int tickDamage;
    private final boolean stackable;
    private final int maxStacks;
    private int stacks;
    private float timeSinceLastTick;
    private boolean expired;

    public DamageOverTimeStrategy(float duration, float tickInterval, int tickDamage, boolean stackable, int maxStacks) {
        this.initialDuration = duration;
        this.remainingDuration = duration;
        this.tickInterval = tickInterval;
        this.tickDamage = tickDamage;
        this.stackable = stackable;
        this.maxStacks = Math.max(1, maxStacks);
        this.stacks = 1;
        this.timeSinceLastTick = 0f;
        this.expired = false;
    }

    @Override
    public void onApply(Bloon target) {
        // Option to trigger initial damage or just wait for tick
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
                target.damage(tickDamage * stacks);
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
        // Cleanup if needed
    }

    @Override
    public boolean isExpired() {
        return expired || remainingDuration <= 0;
    }

    @Override
    public StatusEffectType getType() {
        return StatusEffectType.DOT;
    }

    @Override
    public float getIntensity() {
        return (float) (tickDamage * stacks);
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
