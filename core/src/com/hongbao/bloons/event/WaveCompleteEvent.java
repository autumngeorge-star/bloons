package com.hongbao.bloons.event;

public class WaveCompleteEvent implements GameEvent {
    private final int level;

    public WaveCompleteEvent(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String getType() {
        return "WAVE_COMPLETE";
    }
}
