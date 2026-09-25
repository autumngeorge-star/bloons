package com.hongbao.bloons.events;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.helpers.BloonPoppedResult;

public class BloonPoppedEvent {
    private final BloonActor bloonActor;
    private final BloonPoppedResult poppedResult;

    public BloonPoppedEvent(BloonActor bloonActor, BloonPoppedResult poppedResult) {
        this.bloonActor = bloonActor;
        this.poppedResult = poppedResult;
    }

    public BloonActor getBloonActor() {
        return bloonActor;
    }

    public BloonPoppedResult getPoppedResult() {
        return poppedResult;
    }
}
