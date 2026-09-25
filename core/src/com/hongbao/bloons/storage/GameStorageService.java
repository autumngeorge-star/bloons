package com.hongbao.bloons.storage;

public interface GameStorageService {

    int loadHighScore();

    void saveHighScore(int highScore);

    int loadScore(String key, int defaultValue);

    void saveScore(String key, int value);

    void clear();
}
