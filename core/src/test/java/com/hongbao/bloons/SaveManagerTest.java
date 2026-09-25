package com.hongbao.bloons;

import com.badlogic.gdx.files.FileHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaveManagerTest {

    private FileHandle saveFile;
    private FileHandle backupFile;
    private FileHandle tempFile;

    @Before
    public void setUp() {
        saveFile = new FileHandle(SaveManager.SAVE_FILE_NAME);
        backupFile = new FileHandle(SaveManager.BACKUP_FILE_NAME);
        tempFile = new FileHandle(SaveManager.TEMP_FILE_NAME);

        cleanUpFiles();
    }

    @After
    public void tearDown() {
        cleanUpFiles();
    }

    private void cleanUpFiles() {
        if (saveFile.exists()) {
            saveFile.delete();
        }
        if (backupFile.exists()) {
            backupFile.delete();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testSaveAndLoadGameState() {
        GameState originalState = new GameState(5000, 10, 350, 12000, 5, 9);
        boolean saved = SaveManager.saveGameState(originalState);

        assertTrue("Save operation should return true", saved);
        assertTrue("Save file gamestate.json should exist", saveFile.exists());
        assertTrue("Save file size should be less than 100 KB", saveFile.length() < 100 * 1024);

        GameState loadedState = SaveManager.loadGameState();
        assertNotNull("Loaded state should not be null", loadedState);
        assertEquals(5000, loadedState.getHighScore());
        assertEquals(10, loadedState.getUnlockedLevel());
        assertEquals(350, loadedState.getTotalBloonsPopped());
        assertEquals(12000, loadedState.getTotalMoneyEarned());
        assertEquals(5, loadedState.getGamesPlayed());
        assertEquals(9, loadedState.getLevelsCompleted());
        assertEquals(GameState.CURRENT_SCHEMA_VERSION, loadedState.getSchemaVersion());
    }

    @Test
    public void testSchemaVersionTagInFile() {
        GameState state = new GameState();
        state.setHighScore(1234);
        SaveManager.saveGameState(state);

        String jsonContent = saveFile.readString("UTF-8");
        assertTrue("JSON document should contain schemaVersion tag", jsonContent.contains("schemaVersion"));
        assertTrue("JSON document should contain value 1 for schemaVersion", jsonContent.contains("\"schemaVersion\": 1"));
    }

    @Test
    public void testBackupCreationOnOverwrite() {
        GameState state1 = new GameState(100, 2, 10, 500, 1, 1);
        SaveManager.saveGameState(state1);

        assertFalse("Backup file should not exist on first save", backupFile.exists());

        GameState state2 = new GameState(200, 3, 25, 1000, 2, 2);
        SaveManager.saveGameState(state2);

        assertTrue("Backup file gamestate.json.bak should exist on second save", backupFile.exists());
        assertTrue("Backup file should contain previous state data", backupFile.readString("UTF-8").contains("100"));
    }

    @Test
    public void testRecoveryFromCorruptedPrimaryFile() {
        GameState state1 = new GameState(3000, 5, 100, 5000, 3, 4);
        SaveManager.saveGameState(state1);

        GameState state2 = new GameState(4000, 6, 150, 7000, 4, 5);
        SaveManager.saveGameState(state2);

        // Corrupt primary save file
        saveFile.writeString("{ invalid json corruption ###", false, "UTF-8");

        GameState loaded = SaveManager.loadGameState();
        assertNotNull("State should recover from backup file", loaded);
        assertEquals(3000, loaded.getHighScore());
        assertEquals(5, loaded.getUnlockedLevel());
    }

    @Test
    public void testFallbackToDefaultStateWhenBothCorrupted() {
        saveFile.writeString("corrupted primary content", false, "UTF-8");
        backupFile.writeString("corrupted backup content", false, "UTF-8");

        GameState loaded = SaveManager.loadGameState();
        assertNotNull("State should safely fallback to default state", loaded);
        assertEquals(0, loaded.getHighScore());
        assertEquals(1, loaded.getUnlockedLevel());
        assertEquals(0, loaded.getGamesPlayed());
        assertEquals(GameState.CURRENT_SCHEMA_VERSION, loaded.getSchemaVersion());
    }

    @Test
    public void testAtomicWriteTempFileCleanup() {
        GameState state = new GameState();
        SaveManager.saveGameState(state);

        assertFalse("Temporary file gamestate.json.tmp should be cleaned up / moved after save", tempFile.exists());
        assertTrue("Save file gamestate.json should exist", saveFile.exists());
    }
}
