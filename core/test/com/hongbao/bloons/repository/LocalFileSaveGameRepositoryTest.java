package com.hongbao.bloons.repository;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.dto.GirlPlacementData;
import com.hongbao.bloons.dto.MapStateData;
import com.hongbao.bloons.dto.PlayerStateData;
import com.hongbao.bloons.dto.SaveProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LocalFileSaveGameRepositoryTest {

    private static final String TEST_SAVE_DIR = "test_saves/";
    private LocalFileSaveGameRepository repository;

    @BeforeClass
    public static void initHeadless() {
        if (Gdx.files == null) {
            Gdx.files = new HeadlessFiles();
        }
    }

    @Before
    public void setUp() {
        repository = new LocalFileSaveGameRepository(TEST_SAVE_DIR);
        cleanupDir();
    }

    @After
    public void tearDown() {
        cleanupDir();
    }

    private void cleanupDir() {
        try {
            FileHandle dir = Gdx.files.local(TEST_SAVE_DIR);
            if (dir.exists()) {
                dir.deleteDirectory();
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testSaveAndLoadProfile() {
        PlayerStateData playerData = new PlayerStateData(1500, 85);

        List<GirlPlacementData> girls = new ArrayList<>();
        girls.add(new GirlPlacementData("Reimu", 400.0f, 300.0f, 2));
        girls.add(new GirlPlacementData("Marisa", 600.0f, 450.0f, 1));

        MapStateData mapData = new MapStateData(5, true, true, girls);
        SaveProfile originalProfile = new SaveProfile("slot_a", playerData, mapData);

        repository.save("slot_a", originalProfile);

        assertTrue(repository.exists("slot_a"));

        SaveProfile loadedProfile = repository.load("slot_a");

        assertNotNull(loadedProfile);
        assertEquals(SaveProfile.CURRENT_VERSION, loadedProfile.getVersion());
        assertEquals("slot_a", loadedProfile.getSlotId());

        assertNotNull(loadedProfile.getPlayerData());
        assertEquals(1500, loadedProfile.getPlayerData().getMoney());
        assertEquals(85, loadedProfile.getPlayerData().getHealth());

        assertNotNull(loadedProfile.getMapData());
        assertEquals(5, loadedProfile.getMapData().getLevel());
        assertTrue(loadedProfile.getMapData().isAutoContinue());
        assertTrue(loadedProfile.getMapData().isTripleSpeed());

        List<GirlPlacementData> loadedGirls = loadedProfile.getMapData().getGirls();
        assertNotNull(loadedGirls);
        assertEquals(2, loadedGirls.size());

        assertEquals("Reimu", loadedGirls.get(0).getName());
        assertEquals(400.0f, loadedGirls.get(0).getX(), 0.01f);
        assertEquals(300.0f, loadedGirls.get(0).getY(), 0.01f);
        assertEquals(2, loadedGirls.get(0).getLevel());

        assertEquals("Marisa", loadedGirls.get(1).getName());
        assertEquals(600.0f, loadedGirls.get(1).getX(), 0.01f);
        assertEquals(450.0f, loadedGirls.get(1).getY(), 0.01f);
        assertEquals(1, loadedGirls.get(1).getLevel());
    }

    @Test
    public void testAtomicWrite() {
        SaveProfile profile = repository.createDefaultProfile("slot_atomic");
        repository.save("slot_atomic", profile);

        FileHandle jsonFile = Gdx.files.local(TEST_SAVE_DIR + "slot_atomic.json");
        FileHandle tmpFile = Gdx.files.local(TEST_SAVE_DIR + "slot_atomic.json.tmp");

        assertTrue(jsonFile.exists());
        assertFalse("Temporary file should be moved and not remain after save", tmpFile.exists());

        String content = jsonFile.readString("UTF-8");
        assertTrue("Formatted JSON should contain slot_atomic", content.contains("slot_atomic"));
        assertTrue("Formatted JSON should contain playerData", content.contains("playerData"));
    }

    @Test
    public void testMultiSlotIsolation() {
        SaveProfile profile1 = new SaveProfile("slot_1", new PlayerStateData(500, 100), new MapStateData(1, false, false, new ArrayList<>()));
        SaveProfile profile2 = new SaveProfile("slot_2", new PlayerStateData(2000, 50), new MapStateData(10, true, false, new ArrayList<>()));

        repository.save("slot_1", profile1);
        repository.save("slot_2", profile2);

        assertTrue(repository.exists("slot_1"));
        assertTrue(repository.exists("slot_2"));

        List<String> slots = repository.listSlots();
        assertEquals(2, slots.size());
        assertTrue(slots.contains("slot_1"));
        assertTrue(slots.contains("slot_2"));

        SaveProfile loaded1 = repository.load("slot_1");
        SaveProfile loaded2 = repository.load("slot_2");

        assertEquals(500, loaded1.getPlayerData().getMoney());
        assertEquals(2000, loaded2.getPlayerData().getMoney());
        assertEquals(1, loaded1.getMapData().getLevel());
        assertEquals(10, loaded2.getMapData().getLevel());

        // Modifying slot 1 and re-saving
        loaded1.getPlayerData().setMoney(777);
        repository.save("slot_1", loaded1);

        assertEquals(777, repository.load("slot_1").getPlayerData().getMoney());
        assertEquals(2000, repository.load("slot_2").getPlayerData().getMoney());
    }

    @Test
    public void testSchemaVersionValidationAndMigration() {
        // Save an older version profile (version 0)
        String legacyJson = "{\n" +
                "  \"version\": 0,\n" +
                "  \"slotId\": \"slot_legacy\",\n" +
                "  \"playerData\": {\n" +
                "    \"money\": 1200,\n" +
                "    \"health\": 90\n" +
                "  }\n" +
                "}";

        FileHandle dir = Gdx.files.local(TEST_SAVE_DIR);
        dir.mkdirs();
        FileHandle file = Gdx.files.local(TEST_SAVE_DIR + "slot_legacy.json");
        file.writeString(legacyJson, false, "UTF-8");

        SaveProfile loaded = repository.load("slot_legacy");

        assertNotNull(loaded);
        assertEquals("Migrated profile should be updated to current version", SaveProfile.CURRENT_VERSION, loaded.getVersion());
        assertEquals(1200, loaded.getPlayerData().getMoney());
        assertEquals(90, loaded.getPlayerData().getHealth());
        assertNotNull("Migrated profile should have initialized mapData", loaded.getMapData());
    }

    @Test
    public void testCorruptedSaveFileHandling() {
        FileHandle dir = Gdx.files.local(TEST_SAVE_DIR);
        dir.mkdirs();
        FileHandle file = Gdx.files.local(TEST_SAVE_DIR + "slot_corrupt.json");
        file.writeString("{ INVALID JSON CONTENT ### !!! }", false, "UTF-8");

        SaveProfile loaded = repository.load("slot_corrupt");

        assertNotNull("Corrupted file should trigger fallback to safe default profile", loaded);
        assertEquals("slot_corrupt", loaded.getSlotId());
        assertEquals(1000, loaded.getPlayerData().getMoney());
        assertEquals(100, loaded.getPlayerData().getHealth());
    }

    @Test
    public void testDeleteAndExists() {
        SaveProfile profile = repository.createDefaultProfile("slot_delete");
        repository.save("slot_delete", profile);

        assertTrue(repository.exists("slot_delete"));

        repository.delete("slot_delete");

        assertFalse(repository.exists("slot_delete"));
    }
}
