package com.hongbao.bloons.dto;

public class GirlPlacementData {

    private String name;
    private float x;
    private float y;
    private int level;

    public GirlPlacementData() {
    }

    public GirlPlacementData(String name, float x, float y, int level) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
