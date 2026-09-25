package com.hongbao.bloons.dto;

public class PlayerStateData {

    private int money;
    private int health;

    public PlayerStateData() {
    }

    public PlayerStateData(int money, int health) {
        this.money = money;
        this.health = health;
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
}
