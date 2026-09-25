package com.hongbao.bloons.modifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpgradeDefinition {
    private final String name;
    private final int cost;
    private final List<StatModifier> modifiers;

    public UpgradeDefinition(String name, int cost, List<StatModifier> modifiers) {
        this.name = name;
        this.cost = cost;
        this.modifiers = modifiers != null ? Collections.unmodifiableList(new ArrayList<>(modifiers)) : Collections.emptyList();
    }

    public UpgradeDefinition(String name, int cost, StatModifier... modifiers) {
        this(name, cost, Arrays.asList(modifiers));
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public List<StatModifier> getModifiers() {
        return modifiers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name = "";
        private int cost = 0;
        private final List<StatModifier> modifiers = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder cost(int cost) {
            this.cost = cost;
            return this;
        }

        public Builder addModifier(StatModifier modifier) {
            if (modifier != null) {
                this.modifiers.add(modifier);
            }
            return this;
        }

        public Builder addModifier(StatType statType, Operation operation, float value) {
            this.modifiers.add(new DefaultStatModifier(statType, operation, value));
            return this;
        }

        public Builder addFlatModifier(StatType statType, float value) {
            this.modifiers.add(StatModifier.add(statType, value));
            return this;
        }

        public Builder addMultiplyModifier(StatType statType, float value) {
            this.modifiers.add(StatModifier.multiply(statType, value));
            return this;
        }

        public Builder addPercentModifier(StatType statType, float percent) {
            this.modifiers.add(StatModifier.percent(statType, percent));
            return this;
        }

        public UpgradeDefinition build() {
            return new UpgradeDefinition(name, cost, modifiers);
        }
    }
}
