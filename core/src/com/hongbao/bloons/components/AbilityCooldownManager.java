package com.hongbao.bloons.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Modular component that manages ability cooldowns using real-time delta-time tracking.
 */
public class AbilityCooldownManager {

    public enum AbilityState {
        READY,
        ON_COOLDOWN
    }

    public interface AbilityStateChangeListener {
        void onStateChanged(AbilityState newState);
    }

    private float cooldownDuration;
    private float remainingSeconds;
    private AbilityState currentState;
    private final List<AbilityStateChangeListener> listeners;

    public AbilityCooldownManager(float cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
        this.remainingSeconds = 0f;
        this.currentState = AbilityState.READY;
        this.listeners = new ArrayList<>();
    }

    /**
     * Updates the cooldown timer using the frame delta time in seconds.
     *
     * @param delta frame delta time in seconds
     */
    public void update(float delta) {
        if (!hasAbility()) {
            return;
        }

        if (remainingSeconds > 0) {
            remainingSeconds -= delta;
            if (remainingSeconds <= 0) {
                remainingSeconds = 0f;
                setState(AbilityState.READY);
            }
        }
    }

    /**
     * Checks if the ability is ready for activation.
     *
     * @return true if the tower has an ability and remaining cooldown is 0
     */
    public boolean isReady() {
        return hasAbility() && remainingSeconds <= 0f;
    }

    /**
     * Triggers the ability if it is currently ready, resetting the cooldown timer.
     *
     * @return true if triggered successfully, false if on cooldown or no ability
     */
    public boolean trigger() {
        if (!isReady()) {
            return false;
        }

        remainingSeconds = cooldownDuration;
        setState(AbilityState.ON_COOLDOWN);
        return true;
    }

    /**
     * Returns the remaining cooldown time in real-time seconds.
     *
     * @return remaining seconds
     */
    public float getRemainingSeconds() {
        return remainingSeconds;
    }

    /**
     * Returns the progress of the cooldown from 0.0 (just triggered) to 1.0 (ready).
     *
     * @return float progress in range [0.0, 1.0]
     */
    public float getCooldownProgress() {
        if (!hasAbility()) {
            return 1.0f;
        }
        if (cooldownDuration <= 0f) {
            return 1.0f;
        }
        float progress = (cooldownDuration - remainingSeconds) / cooldownDuration;
        if (progress < 0.0f) {
            progress = 0.0f;
        } else if (progress > 1.0f) {
            progress = 1.0f;
        }
        return progress;
    }

    /**
     * Returns whether this manager represents an active ability (cooldownDuration > 0).
     */
    public boolean hasAbility() {
        return cooldownDuration > 0f;
    }

    public float getCooldownDuration() {
        return cooldownDuration;
    }

    public void setCooldownDuration(float cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
        if (!hasAbility()) {
            remainingSeconds = 0f;
            setState(AbilityState.READY);
        }
    }

    public AbilityState getCurrentState() {
        return currentState;
    }

    public void addStateChangeListener(AbilityStateChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeStateChangeListener(AbilityStateChangeListener listener) {
        listeners.remove(listener);
    }

    private void setState(AbilityState newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            for (AbilityStateChangeListener listener : listeners) {
                listener.onStateChanged(newState);
            }
        }
    }
}
