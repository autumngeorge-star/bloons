package com.hongbao.bloons.events;

import com.hongbao.bloons.actors.GirlActor;

public class TowerUpgradedEvent {
    private final GirlActor girlActor;

    public TowerUpgradedEvent(GirlActor girlActor) {
        this.girlActor = girlActor;
    }

    public GirlActor getGirlActor() {
        return girlActor;
    }
}
