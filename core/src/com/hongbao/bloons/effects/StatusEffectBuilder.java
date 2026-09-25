package com.hongbao.bloons.effects;

public class StatusEffectBuilder {

    private float duration = 0f;
    private float intensity = 0f;
    private boolean stackable = false;
    private int maxStacks = 1;
    private float tickInterval = 0f;
    private int tickDamage = 0;
    private StatusEffectType effectType = StatusEffectType.GENERIC;

    public StatusEffectBuilder() {
    }

    public StatusEffectBuilder duration(float duration) {
        this.duration = duration;
        return this;
    }

    public StatusEffectBuilder intensity(float intensity) {
        this.intensity = intensity;
        return this;
    }

    public StatusEffectBuilder stackable(boolean stackable) {
        this.stackable = stackable;
        return this;
    }

    public StatusEffectBuilder maxStacks(int maxStacks) {
        this.maxStacks = maxStacks;
        return this;
    }

    public StatusEffectBuilder tickInterval(float tickInterval) {
        this.tickInterval = tickInterval;
        return this;
    }

    public StatusEffectBuilder tickDamage(int tickDamage) {
        this.tickDamage = tickDamage;
        return this;
    }

    public StatusEffectBuilder effectType(StatusEffectType effectType) {
        this.effectType = effectType;
        return this;
    }

    public StatusEffectFactory build() {
        return new StatusEffectFactory(
                duration,
                intensity,
                stackable,
                maxStacks,
                tickInterval,
                tickDamage,
                effectType
        );
    }
}
