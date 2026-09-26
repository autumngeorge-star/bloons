package com.hongbao.bloons.listeners;

public interface GameLifecycleListener {
    void onGamePaused();
    void onGameResumed();
    void onGameDisposed();
}
