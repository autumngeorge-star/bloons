package com.hongbao.bloons.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates ability state, delta-time cooldown timers, active max duration,
 * and state transition listeners for tower spell card abilities.
 */
public class SpellCardAbility {

	public enum State {
		READY,
		ACTIVE,
		RECHARGING
	}

	public interface StateChangeListener {
		void onStateChanged(State oldState, State newState);
	}

	public static final float DEFAULT_COOLDOWN_DURATION = 15.0f;
	public static final float DEFAULT_MAX_DURATION = 2.0f;

	private float cooldownDuration;
	private float maxDuration;
	private float cooldownRemaining;
	private float activeRemaining;
	private State currentState;
	private final List<StateChangeListener> listeners;

	public SpellCardAbility() {
		this(DEFAULT_COOLDOWN_DURATION, DEFAULT_MAX_DURATION);
	}

	public SpellCardAbility(float cooldownDuration, float maxDuration) {
		this.cooldownDuration = cooldownDuration;
		this.maxDuration = maxDuration;
		this.cooldownRemaining = 0f;
		this.activeRemaining = 0f;
		this.currentState = State.READY;
		this.listeners = new ArrayList<>();
	}

	public synchronized void addListener(StateChangeListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public synchronized void removeListener(StateChangeListener listener) {
		listeners.remove(listener);
	}

	public boolean isReady() {
		return currentState == State.READY;
	}

	public boolean isActive() {
		return currentState == State.ACTIVE;
	}

	public boolean isRecharging() {
		return currentState == State.RECHARGING;
	}

	public State getState() {
		return currentState;
	}

	public float getCooldownDuration() {
		return cooldownDuration;
	}

	public void setCooldownDuration(float cooldownDuration) {
		this.cooldownDuration = cooldownDuration;
	}

	public float getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(float maxDuration) {
		this.maxDuration = maxDuration;
	}

	public float getCooldownRemaining() {
		return cooldownRemaining;
	}

	public float getActiveRemaining() {
		return activeRemaining;
	}

	/**
	 * Returns the fraction of cooldown remaining, from 1.0 (just triggered) down to 0.0 (ready).
	 */
	public float getRemainingCooldownRatio() {
		if (currentState == State.READY) {
			return 0.0f;
		}
		if (cooldownDuration <= 0f) {
			return 0.0f;
		}
		return Math.max(0.0f, Math.min(1.0f, cooldownRemaining / cooldownDuration));
	}

	/**
	 * Returns the cooldown progress fraction, from 0.0 (just triggered) up to 1.0 (ready).
	 */
	public float getCooldownProgressRatio() {
		if (currentState == State.READY) {
			return 1.0f;
		}
		return 1.0f - getRemainingCooldownRatio();
	}

	/**
	 * Attempts to trigger the ability.
	 * Returns true if the ability was READY and successfully triggered, false otherwise.
	 */
	public boolean trigger() {
		if (!isReady()) {
			return false;
		}
		State oldState = currentState;
		if (maxDuration > 0f) {
			currentState = State.ACTIVE;
			activeRemaining = maxDuration;
		} else {
			currentState = State.RECHARGING;
			cooldownRemaining = cooldownDuration;
		}
		notifyListeners(oldState, currentState);
		return true;
	}

	/**
	 * Updates ability timers based on delta time in seconds.
	 */
	public void update(float delta) {
		if (delta <= 0f) {
			return;
		}

		if (currentState == State.ACTIVE) {
			activeRemaining -= delta;
			if (activeRemaining <= 0f) {
				activeRemaining = 0f;
				State oldState = currentState;
				currentState = State.RECHARGING;
				cooldownRemaining = cooldownDuration;
				notifyListeners(oldState, currentState);
			}
		} else if (currentState == State.RECHARGING) {
			cooldownRemaining -= delta;
			if (cooldownRemaining <= 0f) {
				cooldownRemaining = 0f;
				State oldState = currentState;
				currentState = State.READY;
				notifyListeners(oldState, currentState);
			}
		}
	}

	public void reset() {
		State oldState = currentState;
		currentState = State.READY;
		cooldownRemaining = 0f;
		activeRemaining = 0f;
		if (oldState != State.READY) {
			notifyListeners(oldState, currentState);
		}
	}

	private void notifyListeners(State oldState, State newState) {
		List<StateChangeListener> copy;
		synchronized (this) {
			copy = new ArrayList<>(listeners);
		}
		for (StateChangeListener listener : copy) {
			listener.onStateChanged(oldState, newState);
		}
	}
}
