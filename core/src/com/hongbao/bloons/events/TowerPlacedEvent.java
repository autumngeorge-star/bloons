package com.hongbao.bloons.events;

import com.hongbao.bloons.actors.GirlActor;

public class TowerPlacedEvent {
    private final GirlActor girlActor;

    public TowerPlacedEvent(GirlActor girlActor) {
        this.girlActor = girlActor;
    }

    public GirlActor getGirlActor() {
        return girlActor;
    }
}
