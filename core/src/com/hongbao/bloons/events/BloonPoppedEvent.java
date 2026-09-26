package com.hongbao.bloons.events;

/**
 * Domain event emitted when a bloon is popped or damaged.
 */
public class BloonPoppedEvent {

    private final String bloonType;
    private final float x;
    private final float y;
    private final int damage;
    private final boolean popped;

    public BloonPoppedEvent(String bloonType, float x, float y, int damage, boolean popped) {
        this.bloonType = bloonType;
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.popped = popped;
    }

    public String getBloonType() {
        return bloonType;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isPopped() {
        return popped;
    }
}
