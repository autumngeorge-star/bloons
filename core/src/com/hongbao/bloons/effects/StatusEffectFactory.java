package com.hongbao.bloons.effects;

import com.hongbao.bloons.effects.strategies.DamageOverTimeStrategy;
import com.hongbao.bloons.effects.strategies.FreezeStatusStrategy;
import com.hongbao.bloons.effects.strategies.GenericStatusStrategy;
import com.hongbao.bloons.effects.strategies.SlowStatusStrategy;
import com.hongbao.bloons.entities.Bloon;

public class StatusEffectFactory {

    private final float duration;
    private final float intensity;
    private final boolean stackable;
    private final int maxStacks;
    private final float tickInterval;
    private final int tickDamage;
    private final StatusEffectType effectType;

    public StatusEffectFactory(float duration, float intensity, boolean stackable, int maxStacks,
                               float tickInterval, int tickDamage, StatusEffectType effectType) {
        this.duration = duration;
        this.intensity = intensity;
        this.stackable = stackable;
        this.maxStacks = Math.max(1, maxStacks);
        this.tickInterval = tickInterval;
        this.tickDamage = tickDamage;
        this.effectType = effectType != null ? effectType : StatusEffectType.GENERIC;
    }

    public static StatusEffectBuilder builder() {
        return new StatusEffectBuilder();
    }

    public float getDuration() {
        return duration;
    }

    public float getIntensity() {
        return intensity;
    }

    public boolean isStackable() {
        return stackable;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public float getTickInterval() {
        return tickInterval;
    }

    public int getTickDamage() {
        return tickDamage;
    }

    public StatusEffectType getEffectType() {
        return effectType;
    }

    public StatusEffectStrategy createEffect() {
        switch (effectType) {
            case SLOW:
                return new SlowStatusStrategy(duration, intensity, stackable, maxStacks);
            case FREEZE:
                return new FreezeStatusStrategy(duration, intensity, tickInterval, tickDamage);
            case DOT:
                return new DamageOverTimeStrategy(duration, tickInterval, tickDamage, stackable, maxStacks);
            case GENERIC:
            default:
                return new GenericStatusStrategy(duration, intensity, tickInterval, tickDamage, effectType);
        }
    }

    public void applyEffect(Bloon target) {
        if (target != null) {
            StatusEffectStrategy effect = createEffect();
            target.addStatusEffect(effect);
        }
    }
}
