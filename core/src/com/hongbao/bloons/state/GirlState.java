package com.hongbao.bloons.state;

public class GirlState {
    private String name;
    private float x;
    private float y;
    private int level;
    private float rotationAngle;

    public GirlState() {
    }

    public GirlState(String name, float x, float y, int level, float rotationAngle) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.level = level;
        this.rotationAngle = rotationAngle;
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

    public float getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }
}
