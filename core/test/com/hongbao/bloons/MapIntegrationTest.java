package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.hongbao.bloons.factories.MapFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapIntegrationTest {

	@BeforeClass
	public static void setUpBeforeClass() {
		Gdx.app = mock(Application.class);
		Gdx.files = new HeadlessFiles();
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = mock(GL20.class);
		Gdx.audio = mock(Audio.class);
		when(Gdx.audio.newSound(any(FileHandle.class))).thenReturn(mock(Sound.class));
	}

	@Test
	public void testMapTypeProperties() {
		assertEquals("Basic Map", MapType.BASIC_MAP.getDisplayName());
		assertEquals("basic_map.png", MapType.BASIC_MAP.getImageFileName());
		assertEquals(1, MapType.BASIC_MAP.getMapIndex());
		assertNull(MapType.BASIC_MAP.getPrerequisiteMap());

		assertEquals("Map with Turn", MapType.MAP_WITH_TURN.getDisplayName());
		assertEquals("map_with_turn.png", MapType.MAP_WITH_TURN.getImageFileName());
		assertEquals(2, MapType.MAP_WITH_TURN.getMapIndex());
		assertEquals(MapType.BASIC_MAP, MapType.MAP_WITH_TURN.getPrerequisiteMap());

		assertEquals("Heater Map", MapType.HEATER_MAP.getDisplayName());
		assertEquals("heater.png", MapType.HEATER_MAP.getImageFileName());
		assertEquals(3, MapType.HEATER_MAP.getMapIndex());
		assertEquals(MapType.MAP_WITH_TURN, MapType.HEATER_MAP.getPrerequisiteMap());
	}

	@Test
	public void testMapFactoryHelpers() {
		ProgressionManager pm = new ProgressionManager(new ProgressionManager.InMemoryPreferences());

		assertTrue(MapFactory.isMapUnlocked(MapType.BASIC_MAP, pm));
		assertFalse(MapFactory.isMapUnlocked(MapType.MAP_WITH_TURN, pm));

		pm.unlockMap(MapType.MAP_WITH_TURN);
		assertTrue(MapFactory.isMapUnlocked(MapType.MAP_WITH_TURN, pm));

		assertEquals(MapType.BASIC_MAP, MapFactory.getPrerequisiteMap(MapType.MAP_WITH_TURN));
		assertEquals(MapType.MAP_WITH_TURN, MapFactory.getPrerequisiteMap(MapType.HEATER_MAP));
		assertNull(MapFactory.getPrerequisiteMap(MapType.BASIC_MAP));
	}
}
