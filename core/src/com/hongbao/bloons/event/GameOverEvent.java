package com.hongbao.bloons.event;

public class GameOverEvent implements GameEvent {
    @Override
    public String getType() {
        return "GAME_OVER";
    }
}
