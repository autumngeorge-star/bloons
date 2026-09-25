package com.hongbao.bloons.events;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;

/**
 * Event published whenever a bloon is popped.
 */
public class BloonPoppedEvent implements GameEvent {

    private final BloonActor bloonActor;
    private final Bloon bloon;
    private final int damage;

    public BloonPoppedEvent(BloonActor bloonActor, int damage) {
        this.bloonActor = bloonActor;
        this.bloon = (bloonActor != null) ? bloonActor.getBloon() : null;
        this.damage = damage;
    }

    public BloonPoppedEvent(BloonActor bloonActor) {
        this(bloonActor, 0);
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

    public float getX() {
        return bloonActor != null ? bloonActor.getCenterX() : 0f;
    }

    public float getY() {
        return bloonActor != null ? bloonActor.getCenterY() : 0f;
    }
}
