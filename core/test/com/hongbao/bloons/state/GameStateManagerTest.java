package com.hongbao.bloons.state;

import com.hongbao.bloons.event.GameOverEvent;
import com.hongbao.bloons.event.PauseEvent;
import com.hongbao.bloons.event.WaveCompleteEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class GameStateManagerTest {

    private File tempSaveDir;
    private PersistenceManager persistenceManager;
    private GameStateManager gameStateManager;

    @Before
    public void setUp() throws IOException {
        tempSaveDir = Files.createTempDirectory("bloons_test_saves").toFile();
        persistenceManager = new PersistenceManager(tempSaveDir);
        gameStateManager = new GameStateManager(persistenceManager);
    }

    @After
    public void tearDown() {
        if (gameStateManager != null) {
            gameStateManager.shutdown();
        }
        deleteDir(tempSaveDir);
    }

    private void deleteDir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteDir(f);
                    } else {
                        f.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    @Test
    public void testSaveAndLoadStateData() throws Exception {
        List<GirlState> girls = new ArrayList<>();
        girls.add(new GirlState("Reimu", 100f, 200f, 1, 45f));
        girls.add(new GirlState("Marisa", 300f, 400f, 0, 90f));

        GameStateData data = new GameStateData(1500, 80, 5, girls, System.currentTimeMillis());

        Future<Boolean> saveFuture = gameStateManager.saveStateAsync("slot_1", data);
        Boolean saveResult = saveFuture.get();
        assertTrue("Save operation should return true", saveResult);

        File slotFile = persistenceManager.getSlotFile("slot_1");
        assertTrue("Slot file should exist on disk", slotFile.exists());

        GameStateData loadedData = persistenceManager.loadState("slot_1");
        assertNotNull("Loaded state should not be null", loadedData);
        assertEquals("Money should match", 1500, loadedData.getMoney());
        assertEquals("Health should match", 80, loadedData.getHealth());
        assertEquals("Level should match", 5, loadedData.getLevel());
        assertEquals("Tower count should match", 2, loadedData.getGirls().size());
        assertEquals("First tower name should match", "Reimu", loadedData.getGirls().get(0).getName());
        assertEquals("First tower x should match", 100f, loadedData.getGirls().get(0).getX(), 0.001f);
    }

    @Test
    public void testPauseEventTriggersAutoSave() throws Exception {
        File autoSaveFile = persistenceManager.getSlotFile(GameStateManager.AUTO_SAVE_SLOT);
        assertFalse("Auto save should not exist prior to event", autoSaveFile.exists());

        gameStateManager.dispatchEvent(new PauseEvent());

        // Wait brief moment for async executor
        Thread.sleep(300);

        assertTrue("PauseEvent should have created auto_save.json asynchronously", autoSaveFile.exists());
    }

    @Test
    public void testWaveCompleteEventTriggersAutoSave() throws Exception {
        File autoSaveFile = persistenceManager.getSlotFile(GameStateManager.AUTO_SAVE_SLOT);
        assertFalse("Auto save should not exist prior to event", autoSaveFile.exists());

        gameStateManager.dispatchEvent(new WaveCompleteEvent(2));

        Thread.sleep(300);

        assertTrue("WaveCompleteEvent should have created auto_save.json asynchronously", autoSaveFile.exists());
    }

    @Test
    public void testGameOverEventClearsAutoSaveSlot() throws Exception {
        // First create auto_save file
        GameStateData data = new GameStateData(500, 0, 3, new ArrayList<>(), System.currentTimeMillis());
        persistenceManager.saveState(GameStateManager.AUTO_SAVE_SLOT, data);

        File autoSaveFile = persistenceManager.getSlotFile(GameStateManager.AUTO_SAVE_SLOT);
        assertTrue("Auto save file should exist before game over", autoSaveFile.exists());

        gameStateManager.dispatchEvent(new GameOverEvent());

        Thread.sleep(300);

        assertFalse("GameOverEvent should have deleted auto_save.json", autoSaveFile.exists());
    }

    @Test
    public void testMultiSlotSaveSupport() throws Exception {
        GameStateData slot1Data = new GameStateData(1000, 100, 1, new ArrayList<>(), System.currentTimeMillis());
        GameStateData slot2Data = new GameStateData(2000, 50, 10, new ArrayList<>(), System.currentTimeMillis());

        gameStateManager.saveStateAsync("slot_1", slot1Data).get();
        gameStateManager.saveStateAsync("slot_2", slot2Data).get();

        assertTrue("slot_1 file exists", persistenceManager.getSlotFile("slot_1").exists());
        assertTrue("slot_2 file exists", persistenceManager.getSlotFile("slot_2").exists());

        GameStateData loaded1 = persistenceManager.loadState("slot_1");
        GameStateData loaded2 = persistenceManager.loadState("slot_2");

        assertEquals(1000, loaded1.getMoney());
        assertEquals(2000, loaded2.getMoney());
        assertEquals(1, loaded1.getLevel());
        assertEquals(10, loaded2.getLevel());
    }

    @Test
    public void testTemporaryFileCleanedUpAfterSave() throws Exception {
        GameStateData data = new GameStateData(800, 90, 2, new ArrayList<>(), System.currentTimeMillis());
        gameStateManager.saveStateAsync("slot_1", data).get();

        File tempFile = persistenceManager.getTempFile("slot_1");
        assertFalse("Temporary file should not exist after save completes", tempFile.exists());
    }
}
