package com.hongbao.bloons.scores;

public interface ScoreChangeListener {
    void onScoreChanged(int currentScore, int comboCount, int rank);
}
