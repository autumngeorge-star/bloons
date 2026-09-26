package com.hongbao.bloons.events;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;

public class BloonPoppedEvent extends GameplayEvent {
    private final BloonActor bloonActor;
    private final Bloon bloon;
    private final int damage;
    private final int cashGenerated;
    private final boolean fullyPopped;

    public BloonPoppedEvent(BloonActor bloonActor, Bloon bloon, int damage, int cashGenerated, boolean fullyPopped) {
        super();
        this.bloonActor = bloonActor;
        this.bloon = bloon;
        this.damage = damage;
        this.cashGenerated = cashGenerated;
        this.fullyPopped = fullyPopped;
    }

    public BloonActor getBloonActor() {
        return bloonActor;
    }

    public Bloon getBloon() {
        return bloon;
    }

    public int getDamage() {
        return damage;
    }

    public int getCashGenerated() {
        return cashGenerated;
    }

    public boolean isFullyPopped() {
        return fullyPopped;
    }
}
