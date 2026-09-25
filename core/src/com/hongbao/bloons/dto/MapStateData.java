package com.hongbao.bloons.dto;

import java.util.ArrayList;
import java.util.List;

public class MapStateData {

    private int level;
    private boolean autoContinue;
    private boolean tripleSpeed;
    private List<GirlPlacementData> girls;

    public MapStateData() {
        this.girls = new ArrayList<>();
    }

    public MapStateData(int level, boolean autoContinue, boolean tripleSpeed, List<GirlPlacementData> girls) {
        this.level = level;
        this.autoContinue = autoContinue;
        this.tripleSpeed = tripleSpeed;
        this.girls = girls != null ? girls : new ArrayList<>();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isAutoContinue() {
        return autoContinue;
    }

    public void setAutoContinue(boolean autoContinue) {
        this.autoContinue = autoContinue;
    }

    public boolean isTripleSpeed() {
        return tripleSpeed;
    }

    public void setTripleSpeed(boolean tripleSpeed) {
        this.tripleSpeed = tripleSpeed;
    }

    public List<GirlPlacementData> getGirls() {
        return girls;
    }

    public void setGirls(List<GirlPlacementData> girls) {
        this.girls = girls;
    }
}
