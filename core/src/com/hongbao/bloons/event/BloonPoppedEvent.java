package com.hongbao.bloons.event;

import com.hongbao.bloons.actors.BloonActor;

public class BloonPoppedEvent {
    private final BloonActor bloonActor;
    private final int damage;

    public BloonPoppedEvent(BloonActor bloonActor, int damage) {
        this.bloonActor = bloonActor;
        this.damage = damage;
    }

    public BloonPoppedEvent(BloonActor bloonActor) {
        this(bloonActor, 1);
    }

    public BloonActor getBloonActor() {
        return bloonActor;
    }

    public int getDamage() {
        return damage;
    }
}
