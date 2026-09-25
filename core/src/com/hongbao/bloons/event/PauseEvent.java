package com.hongbao.bloons.event;

public class PauseEvent implements GameEvent {
    @Override
    public String getType() {
        return "PAUSE";
    }
}
