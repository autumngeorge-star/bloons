package com.hongbao.bloons.persistence;

import java.util.Objects;

/**
 * Data Transfer Object representing persistent player and game state.
 */
public class PlayerData {

    private int money;
    private int health;
    private int level;

    /**
     * Default constructor initializes to default initial game values.
     */
    public PlayerData() {
        this.money = 1000;
        this.health = 100;
        this.level = 0;
    }

    public PlayerData(int money, int health, int level) {
        this.money = money;
        this.health = health;
        this.level = level;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerData that = (PlayerData) o;
        return money == that.money && health == that.health && level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(money, health, level);
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "money=" + money +
                ", health=" + health +
                ", level=" + level +
                '}';
    }
}
