package com.hongbao.bloons;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LevelSaveServiceTest {

    private LevelSaveService service;

    @Before
    public void setUp() {
        File file = new File(LevelSaveService.SAVE_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        service = new LevelSaveService();
    }

    @After
    public void tearDown() {
        File file = new File(LevelSaveService.SAVE_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testDefaultSaveFileCreation() {
        service.load();
        assertEquals(1, service.getVersion());
        assertEquals(1, service.getMaxUnlockedLevel());

        File file = new File(LevelSaveService.SAVE_FILE_NAME);
        assertTrue(file.exists());
    }

    @Test
    public void testUnlockLevel() {
        service.load();
        service.unlockLevel(5);

        assertEquals(5, service.getMaxUnlockedLevel());

        LevelSaveService newService = new LevelSaveService();
        newService.load();
        assertEquals(5, newService.getMaxUnlockedLevel());
        assertEquals(1, newService.getVersion());
    }

    @Test
    public void testJsonContentFormat() throws Exception {
        service.load();
        service.unlockLevel(3);

        File file = new File(LevelSaveService.SAVE_FILE_NAME);
        assertTrue(file.exists());

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        String jsonStr = sb.toString();
        assertTrue(jsonStr.contains("\"version\":1") || jsonStr.contains("\"version\": 1"));
        assertTrue(jsonStr.contains("\"maxUnlockedLevel\":3") || jsonStr.contains("\"maxUnlockedLevel\": 3"));
    }
}
