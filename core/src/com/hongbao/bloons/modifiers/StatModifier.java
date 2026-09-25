package com.hongbao.bloons.modifiers;

public interface StatModifier {
    StatType getStatType();
    Operation getOperation();
    float getValue();

    static StatModifier add(StatType statType, float value) {
        return new DefaultStatModifier(statType, Operation.ADD, value);
    }

    static StatModifier multiply(StatType statType, float value) {
        return new DefaultStatModifier(statType, Operation.MULTIPLY, value);
    }

    static StatModifier percent(StatType statType, float percent) {
        return new DefaultStatModifier(statType, Operation.MULTIPLY, 1.0f + percent);
    }

    static StatModifier homing(boolean enabled) {
        return new DefaultStatModifier(StatType.HOMING, Operation.ADD, enabled ? 1.0f : 0.0f);
    }
}
