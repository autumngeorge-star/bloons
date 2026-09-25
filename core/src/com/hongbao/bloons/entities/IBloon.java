package com.hongbao.bloons.entities;

import com.hongbao.bloons.helpers.BloonPoppedResult;

/**
 * Domain interface for Bloon entities, isolating domain state and progress
 * calculation from rendering and UI framework constructs.
 */
public interface IBloon {

    Bloon.Color getColor();

    void setColor(Bloon.Color color);

    String getImageFileName();

    void setImageFileName(String imageFileName);

    int getHealth();

    void setHealth(int health);

    int getSpeed();

    void setSpeed(int speed);

    PathProgress getPathProgress();

    void setPathProgress(PathProgress pathProgress);

    int getDistanceTravelled();

    void setDistanceTravelled(int distanceTravelled);

    void incrementDistanceTravelled();

    void updatePathProgress(float displacement, float segmentLength);

    boolean isCamo();

    void setCamo(boolean camo);

    boolean isRegen();

    void setRegen(boolean regen);

    boolean willPopBloon(int damage);

    void damage(int damage);

    BloonPoppedResult pop(int damage);

    boolean isBlimp();
}
