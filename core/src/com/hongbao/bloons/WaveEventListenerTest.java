package com.hongbao.bloons;

import java.util.concurrent.atomic.AtomicInteger;

public class WaveEventListenerTest {

	public static void main(String[] args) {
		com.badlogic.gdx.Gdx.files = new com.badlogic.gdx.backends.headless.HeadlessFiles();
		testWaveEventListenerRegistrationAndNotification();
		testCustomWaveFilePathConstructor();
		System.out.println("ALL WAVE EVENT LISTENER TESTS PASSED SUCCESSFULY!");
	}

	public static void testWaveEventListenerRegistrationAndNotification() {
		final AtomicInteger lastLevelStarted = new AtomicInteger(-1);
		final AtomicInteger callbackCount = new AtomicInteger(0);

		WaveEventListener listener = level -> {
			lastLevelStarted.set(level);
			callbackCount.incrementAndGet();
		};

		// Create BloonManager with explicit wave file path
		BloonManager bloonManager = new BloonManager(null, null, "default.txt");
		bloonManager.addWaveEventListener(listener);

		// Assert initial state
		if (bloonManager.getLevel() != 0) {
			throw new AssertionError("Expected initial level to be 0, got " + bloonManager.getLevel());
		}

		// Call nextLevel and verify listener was notified automatically
		bloonManager.nextLevel();
		if (bloonManager.getLevel() != 1) {
			throw new AssertionError("Expected level to advance to 1, got " + bloonManager.getLevel());
		}
		if (lastLevelStarted.get() != 1 || callbackCount.get() != 1) {
			throw new AssertionError("Expected level 1 notification via nextLevel(), got level " + lastLevelStarted.get());
		}

		// Drain remaining bloons in current level so canGoToNextLevel() becomes true
		while (!bloonManager.getBloonQueue().isEmpty()) {
			bloonManager.getBloonQueue().getBloons();
		}

		// Remove listener and call nextLevel again
		bloonManager.removeWaveEventListener(listener);
		bloonManager.nextLevel();
		if (bloonManager.getLevel() != 2) {
			throw new AssertionError("Expected level to advance to 2, got " + bloonManager.getLevel());
		}
		if (callbackCount.get() != 1) {
			throw new AssertionError("Expected no new notifications after listener removal, got count " + callbackCount.get());
		}

		System.out.println("testWaveEventListenerRegistrationAndNotification passed.");
	}

	public static void testCustomWaveFilePathConstructor() {
		BloonManager defaultManager = new BloonManager(null, null, "default.txt");
		if (defaultManager.getLevel() != 0) {
			throw new AssertionError("Expected level 0 for default.txt");
		}

		BloonManager hellaManager = new BloonManager(null, null, "hella_bloons.txt");
		if (hellaManager.getLevel() != 0) {
			throw new AssertionError("Expected level 0 for hella_bloons.txt");
		}

		System.out.println("testCustomWaveFilePathConstructor passed.");
	}
}
