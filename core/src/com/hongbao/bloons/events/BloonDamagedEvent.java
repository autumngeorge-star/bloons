package com.hongbao.bloons.events;

import com.hongbao.bloons.actors.BloonActor;

public class BloonDamagedEvent {
    private final BloonActor bloonActor;
    private final int damage;

    public BloonDamagedEvent(BloonActor bloonActor, int damage) {
        this.bloonActor = bloonActor;
        this.damage = damage;
    }

    public BloonActor getBloonActor() {
        return bloonActor;
    }

    public int getDamage() {
        return damage;
    }
}
