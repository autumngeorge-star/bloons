package com.hongbao.bloons.descriptors;

public class SpawnDescriptor {
    private final int amount;
    private final long delay;
    private final String bloonTypes;

    public SpawnDescriptor(int amount, long delay, String bloonTypes) {
        this.amount = amount;
        this.delay = delay;
        this.bloonTypes = bloonTypes;
    }

    public int getAmount() {
        return amount;
    }

    public long getDelay() {
        return delay;
    }

    public String getBloonTypes() {
        return bloonTypes;
    }
}
