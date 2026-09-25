package com.hongbao.bloons.dto;

public class SaveProfile {

    public static final int CURRENT_VERSION = 1;

    private int version;
    private String slotId;
    private long timestamp;
    private PlayerStateData playerData;
    private MapStateData mapData;

    public SaveProfile() {
        this.version = CURRENT_VERSION;
        this.timestamp = System.currentTimeMillis();
        this.playerData = new PlayerStateData();
        this.mapData = new MapStateData();
    }

    public SaveProfile(String slotId, PlayerStateData playerData, MapStateData mapData) {
        this.version = CURRENT_VERSION;
        this.slotId = slotId;
        this.timestamp = System.currentTimeMillis();
        this.playerData = playerData;
        this.mapData = mapData;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public PlayerStateData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerStateData playerData) {
        this.playerData = playerData;
    }

    public MapStateData getMapData() {
        return mapData;
    }

    public void setMapData(MapStateData mapData) {
        this.mapData = mapData;
    }
}
