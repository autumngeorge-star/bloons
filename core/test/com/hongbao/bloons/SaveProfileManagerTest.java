package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.factories.MapFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaveProfileManagerTest {

	private static HeadlessApplication app;

	@BeforeClass
	public static void initLibGdx() {
		if (Gdx.app == null) {
			HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
			app = new HeadlessApplication(new BloonsTouhouDefense(), config);
		}
		Gdx.gl = org.mockito.Mockito.mock(com.badlogic.gdx.graphics.GL20.class);
		Gdx.gl20 = Gdx.gl;
	}

	@Before
	public void setUp() {
		if (Gdx.files.local(SaveProfileManager.SAVE_FILE_NAME).exists()) {
			Gdx.files.local(SaveProfileManager.SAVE_FILE_NAME).delete();
		}
	}

	@After
	public void tearDown() {
		if (Gdx.files.local(SaveProfileManager.SAVE_FILE_NAME).exists()) {
			Gdx.files.local(SaveProfileManager.SAVE_FILE_NAME).delete();
		}
	}

	@Test
	public void testDefaultProfileInitialization() {
		SaveProfileManager manager = new SaveProfileManager();
		SaveProfile profile = manager.getProfile();

		assertNotNull(profile);
		assertTrue(manager.isMapUnlocked("basic"));
		assertFalse(manager.isMapUnlocked("turn"));
		assertFalse(manager.isMapUnlocked("heater"));

		SaveProfile.MapProfile basicProf = manager.getMapProfile("basic");
		assertEquals(0, basicProf.getHighestWave());
		assertEquals(0, basicProf.getHighScore());
		assertFalse(basicProf.isCompleted());
	}

	@Test
	public void testWaveCompletionAndVictoryUnlocks() {
		SaveProfileManager manager = new SaveProfileManager();

		manager.recordWaveCompletion("basic", 5);
		assertEquals(5, manager.getMapProfile("basic").getHighestWave());

		// Victory on basic map -> unlocks turn
		manager.recordMapVictory("basic", 1500);
		assertTrue(manager.getMapProfile("basic").isCompleted());
		assertEquals(1500, manager.getMapProfile("basic").getHighScore());
		assertTrue(manager.isMapUnlocked("turn"));
		assertFalse(manager.isMapUnlocked("heater"));

		// Victory on turn map -> unlocks heater
		manager.recordMapVictory("turn", 2500);
		assertTrue(manager.getMapProfile("turn").isCompleted());
		assertTrue(manager.isMapUnlocked("heater"));
	}

	@Test
	public void testCorruptedSaveFileRecovery() {
		// Write corrupted JSON content to save file
		Gdx.files.local(SaveProfileManager.SAVE_FILE_NAME).writeString("{ INVALID_JSON_CONTENT :::: }", false);

		SaveProfileManager manager = new SaveProfileManager();
		SaveProfile profile = manager.getProfile();

		assertNotNull(profile);
		assertTrue(manager.isMapUnlocked("basic"));
		assertFalse(manager.isMapUnlocked("turn"));
		assertFalse(manager.isMapUnlocked("heater"));
	}

	@Test
	public void testMapTypeEnumAndFactoryMappings() {
		assertEquals(MapType.BASIC, MapType.fromId("basic"));
		assertEquals(MapType.TURN, MapType.fromId("turn"));
		assertEquals(MapType.HEATER, MapType.fromId("heater"));
		assertEquals(MapType.BASIC, MapType.fromId("unknown_id"));

		assertEquals("basic", MapType.BASIC.getId());
		assertEquals("turn", MapType.TURN.getId());
		assertEquals("heater", MapType.HEATER.getId());

		assertEquals("basic_map.png", MapType.BASIC.getImageFileName());
		assertEquals("map_with_turn.png", MapType.TURN.getImageFileName());
		assertEquals("heater.png", MapType.HEATER.getImageFileName());
	}
}
