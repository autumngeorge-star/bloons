package com.hongbao.bloons.state;

import java.util.ArrayList;
import java.util.List;

public class GameStateData {
    private int money;
    private int health;
    private int level;
    private List<GirlState> girls;
    private long timestamp;

    public GameStateData() {
        this.girls = new ArrayList<>();
    }

    public GameStateData(int money, int health, int level, List<GirlState> girls, long timestamp) {
        this.money = money;
        this.health = health;
        this.level = level;
        this.girls = girls != null ? girls : new ArrayList<>();
        this.timestamp = timestamp;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<GirlState> getGirls() {
        return girls;
    }

    public void setGirls(List<GirlState> girls) {
        this.girls = girls;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
