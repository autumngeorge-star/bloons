package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.dto.GirlSaveData;
import com.hongbao.bloons.dto.SaveStateData;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SaveManagerTest {

	private static HeadlessApplication application;

	@BeforeClass
	public static void initLibGDX() {
		if (Gdx.app == null) {
			HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
			application = new HeadlessApplication(new ApplicationAdapter() {}, config);
		}
	}

	@Before
	public void setUp() {
		SaveManager.deleteSave();
	}

	@After
	public void tearDown() {
		SaveManager.deleteSave();
	}

	@Test
	public void testDTOJsonSerialization() {
		GirlSaveData girlData = new GirlSaveData("Reimu", 250f, 350f, 1, 45f);
		List<GirlSaveData> girls = new ArrayList<>();
		girls.add(girlData);

		SaveStateData saveData = new SaveStateData(1500, 80, 5, girls);

		Json json = new Json();
		String jsonStr = json.toJson(saveData);

		assertNotNull(jsonStr);
		assertTrue(jsonStr.contains("1500"));
		assertTrue(jsonStr.contains("80"));
		assertTrue(jsonStr.contains("Reimu"));

		SaveStateData loadedData = json.fromJson(SaveStateData.class, jsonStr);
		assertNotNull(loadedData);
		assertEquals(1500, loadedData.getMoney());
		assertEquals(80, loadedData.getHealth());
		assertEquals(5, loadedData.getLevel());
		assertEquals(1, loadedData.getGirls().size());

		GirlSaveData loadedGirl = loadedData.getGirls().get(0);
		assertEquals("Reimu", loadedGirl.getName());
		assertEquals(250f, loadedGirl.getX(), 0.01f);
		assertEquals(350f, loadedGirl.getY(), 0.01f);
		assertEquals(1, loadedGirl.getLevel());
		assertEquals(45f, loadedGirl.getRotationAngle(), 0.01f);
	}

	@Test
	public void testSaveAndLoadFile() {
		GirlSaveData girlData = new GirlSaveData("Marisa", 100f, 200f, 2, 90f);
		List<GirlSaveData> girls = new ArrayList<>();
		girls.add(girlData);

		SaveStateData saveData = new SaveStateData(2000, 100, 3, girls);

		Json json = new Json();
		FileHandle file = Gdx.files.local(SaveManager.SAVE_FILE_NAME);
		file.writeString(json.toJson(saveData), false);

		assertTrue(SaveManager.hasSave());

		SaveStateData readData = SaveManager.loadSaveData();
		assertNotNull(readData);
		assertEquals(2000, readData.getMoney());
		assertEquals(100, readData.getHealth());
		assertEquals(3, readData.getLevel());
		assertEquals(1, readData.getGirls().size());
		assertEquals("Marisa", readData.getGirls().get(0).getName());
	}

	@Test
	public void testHealthZeroDeletesSave() {
		Player player = new Player(1000, 100);

		// Write a dummy save file
		SaveStateData saveData = new SaveStateData(1000, 100, 1, new ArrayList<>());
		Json json = new Json();
		Gdx.files.local(SaveManager.SAVE_FILE_NAME).writeString(json.toJson(saveData), false);
		assertTrue(SaveManager.hasSave());

		// Reduce health to 0
		player.decreaseHealth(100);
		assertEquals(0, player.getHealth());

		// Save should now be deleted
		assertFalse(SaveManager.hasSave());
	}

	@Test
	public void testGirlFactoryCreateGirlByName() {
		Girl reimu = GirlFactory.createGirlByName("Reimu");
		assertNotNull(reimu);
		assertEquals("Reimu", reimu.getName());

		Girl yukari = GirlFactory.createGirlByName("Yukari");
		assertNotNull(yukari);
		assertEquals("Yukari", yukari.getName());

		Girl unknown = GirlFactory.createGirlByName("Unknown");
		assertNull(unknown);
	}

	@Test
	public void testCorruptedSaveHandledGracefully() {
		FileHandle file = Gdx.files.local(SaveManager.SAVE_FILE_NAME);
		file.writeString("NOT VALID JSON {{{", false);

		assertTrue(SaveManager.hasSave());
		SaveStateData loaded = SaveManager.loadSaveData();

		assertNull(loaded);
		assertFalse(SaveManager.hasSave());
	}

	@Test
	public void testSaveGameWithZeroHealthDeletesFile() {
		BloonsTouhouDefense game = org.mockito.Mockito.mock(BloonsTouhouDefense.class);
		Player player = new Player(1000, 0);
		Map map = org.mockito.Mockito.mock(Map.class);
		org.mockito.Mockito.when(game.getPlayer()).thenReturn(player);
		org.mockito.Mockito.when(game.getMap()).thenReturn(map);

		// Pre-create a save file
		Gdx.files.local(SaveManager.SAVE_FILE_NAME).writeString("{}", false);
		assertTrue(SaveManager.hasSave());

		boolean result = SaveManager.saveGame(game);
		assertFalse(result);
		assertFalse(SaveManager.hasSave());
	}

	@Test
	public void testSaveAndLoadGameWithMocks() {
		BloonsTouhouDefense game = org.mockito.Mockito.mock(BloonsTouhouDefense.class);
		Player player = new Player(3500, 85);
		Map map = org.mockito.Mockito.mock(Map.class);
		BloonManager bloonManager = org.mockito.Mockito.mock(BloonManager.class);

		org.mockito.Mockito.when(game.getPlayer()).thenReturn(player);
		org.mockito.Mockito.when(game.getMap()).thenReturn(map);
		org.mockito.Mockito.when(map.getBloonManager()).thenReturn(bloonManager);
		org.mockito.Mockito.when(bloonManager.getLevel()).thenReturn(4);
		org.mockito.Mockito.when(map.getOnStageGirls()).thenReturn(new java.util.HashSet<>());

		// Save game
		boolean saved = SaveManager.saveGame(game);
		assertTrue(saved);
		assertTrue(SaveManager.hasSave());

		// Prepare fresh game for restore
		BloonsTouhouDefense newGame = org.mockito.Mockito.mock(BloonsTouhouDefense.class);
		Player newPlayer = new Player(0, 0);
		Map newMap = org.mockito.Mockito.mock(Map.class);
		BloonManager newBloonManager = org.mockito.Mockito.mock(BloonManager.class);

		org.mockito.Mockito.when(newGame.getPlayer()).thenReturn(newPlayer);
		org.mockito.Mockito.when(newGame.getMap()).thenReturn(newMap);
		org.mockito.Mockito.when(newMap.getBloonManager()).thenReturn(newBloonManager);

		boolean loaded = SaveManager.loadGame(newGame);
		assertTrue(loaded);

		assertEquals(3500, newPlayer.getMoney());
		assertEquals(85, newPlayer.getHealth());
		org.mockito.Mockito.verify(newBloonManager).setLevel(4);
	}

}
