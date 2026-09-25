package com.hongbao.bloons.modifiers;

public class DefaultStatModifier implements StatModifier {
    private final StatType statType;
    private final Operation operation;
    private final float value;

    public DefaultStatModifier(StatType statType, Operation operation, float value) {
        this.statType = statType;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public StatType getStatType() {
        return statType;
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DefaultStatModifier{" +
                "statType=" + statType +
                ", operation=" + operation +
                ", value=" + value +
                '}';
    }
}
