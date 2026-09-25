package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class MapProgressTest {

	public static void main(String[] args) {
		System.out.println("Running MapProgressTest...");

		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new ApplicationAdapter() {
			@Override
			public void create() {
				try {
					testMapTypeDependencies();
					testPreferencesPersistence();
					testUnlockSequence();
					System.out.println("ALL MAP PROGRESS TESTS PASSED SUCCESSFULLY!");
					Gdx.app.exit();
				} catch (Throwable t) {
					t.printStackTrace();
					System.exit(1);
				}
			}
		}, config);
	}

	public static void testMapTypeDependencies() {
		System.out.println("Testing MapType enum dependencies...");
		check(MapType.BASIC_MAP.getRequiredMap() == null, "BASIC_MAP required map should be null");
		check(MapType.MAP_WITH_TURN.getRequiredMap() == MapType.BASIC_MAP, "MAP_WITH_TURN required map should be BASIC_MAP");
		check(MapType.HEATER.getRequiredMap() == MapType.MAP_WITH_TURN, "HEATER required map should be MAP_WITH_TURN");

		check("Basic Map".equals(MapType.BASIC_MAP.getDisplayName()), "Basic Map display name match");
		check("Map with Turn".equals(MapType.MAP_WITH_TURN.getDisplayName()), "Map with Turn display name match");
		check("Heater Map".equals(MapType.HEATER.getDisplayName()), "Heater Map display name match");
		System.out.println("MapType enum dependencies verified.");
	}

	public static void testPreferencesPersistence() {
		System.out.println("Testing Preferences persistence and MapProgressManager...");
		Preferences prefs = Gdx.app.getPreferences(MapProgressManager.PREFERENCE_NAME);
		prefs.clear();
		prefs.flush();

		MapProgressManager.init();

		check(MapProgressManager.isMapUnlocked(MapType.BASIC_MAP), "BASIC_MAP should be unlocked by default");
		check(!MapProgressManager.isMapUnlocked(MapType.MAP_WITH_TURN), "MAP_WITH_TURN should be locked initially");
		check(!MapProgressManager.isMapUnlocked(MapType.HEATER), "HEATER should be locked initially");

		// Update level progress
		MapProgressManager.updateHighestLevel(MapType.BASIC_MAP, 5);
		check(MapProgressManager.getHighestLevel(MapType.BASIC_MAP) == 5, "Highest level should be 5");

		MapProgressManager.updateHighestLevel(MapType.BASIC_MAP, 3);
		check(MapProgressManager.getHighestLevel(MapType.BASIC_MAP) == 5, "Highest level should remain 5");

		MapProgressManager.updateHighestLevel(MapType.BASIC_MAP, 12);
		check(MapProgressManager.getHighestLevel(MapType.BASIC_MAP) == 12, "Highest level should update to 12");

		System.out.println("Preferences persistence verified.");
	}

	public static void testUnlockSequence() {
		System.out.println("Testing Map unlock sequence...");
		Preferences prefs = Gdx.app.getPreferences(MapProgressManager.PREFERENCE_NAME);
		prefs.clear();
		prefs.flush();

		MapProgressManager.init();

		check(!MapProgressManager.isMapUnlocked(MapType.MAP_WITH_TURN), "MAP_WITH_TURN initially locked");

		// Winning BASIC_MAP unlocks MAP_WITH_TURN
		MapProgressManager.saveMapWin(MapType.BASIC_MAP);
		check(MapProgressManager.isMapUnlocked(MapType.MAP_WITH_TURN), "MAP_WITH_TURN should be unlocked after winning BASIC_MAP");
		check(!MapProgressManager.isMapUnlocked(MapType.HEATER), "HEATER should still be locked");

		// Winning MAP_WITH_TURN unlocks HEATER
		MapProgressManager.saveMapWin(MapType.MAP_WITH_TURN);
		check(MapProgressManager.isMapUnlocked(MapType.HEATER), "HEATER should be unlocked after winning MAP_WITH_TURN");

		System.out.println("Unlock sequence verified.");
	}

	private static void check(boolean condition, String message) {
		if (!condition) {
			throw new RuntimeException("Assertion failed: " + message);
		}
	}
}
