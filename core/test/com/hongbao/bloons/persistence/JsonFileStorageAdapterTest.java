package com.hongbao.bloons.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class JsonFileStorageAdapterTest {

    private File tempFile;
    private JsonFileStorageAdapter adapter;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test_player_data", ".json");
        tempFile.deleteOnExit();
        // Delete it first so we can test loading when file doesn't exist yet
        if (tempFile.exists()) {
            tempFile.delete();
        }
        adapter = new JsonFileStorageAdapter(tempFile);
    }

    @After
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testLoadDefaultDataWhenFileDoesNotExist() {
        PlayerData data = adapter.loadData();
        assertNotNull(data);
        assertEquals(1000, data.getMoney());
        assertEquals(100, data.getHealth());
        assertEquals(0, data.getLevel());
    }

    @Test
    public void testSaveAndLoadData() {
        PlayerData dataToSave = new PlayerData(2500, 90, 3);
        adapter.saveData(dataToSave);

        assertTrue(tempFile.exists());

        PlayerData loadedData = adapter.loadData();
        assertNotNull(loadedData);
        assertEquals(2500, loadedData.getMoney());
        assertEquals(90, loadedData.getHealth());
        assertEquals(3, loadedData.getLevel());
    }

    @Test
    public void testResetDataDeletesFile() {
        PlayerData dataToSave = new PlayerData(3000, 100, 7);
        adapter.saveData(dataToSave);
        assertTrue(tempFile.exists());

        adapter.resetData();
        assertFalse(tempFile.exists());

        PlayerData loaded = adapter.loadData();
        assertEquals(1000, loaded.getMoney());
    }

    @Test(expected = PersistenceException.class)
    public void testCorruptJsonFileThrowsException() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{ corrupt_json: ");
        }

        adapter.loadData();
    }

    @Test(expected = PersistenceException.class)
    public void testSaveNullDataThrowsException() {
        adapter.saveData(null);
    }
}
