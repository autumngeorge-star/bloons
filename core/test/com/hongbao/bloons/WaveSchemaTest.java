package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.exceptions.WaveSchemaValidationException;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class WaveSchemaTest {

    @BeforeClass
    public static void setUpGdxFiles() {
        Gdx.files = new Files() {
            @Override
            public FileHandle getFileHandle(String path, Files.FileType type) {
                return new FileHandle(new File(path));
            }

            @Override
            public FileHandle classpath(String path) {
                return new FileHandle(new File(path));
            }

            @Override
            public FileHandle internal(String path) {
                File file = new File(path);
                if (!file.exists()) {
                    file = new File("assets/" + path);
                }
                if (!file.exists()) {
                    file = new File("core/assets/" + path);
                }
                if (!file.exists()) {
                    file = new File("../core/assets/" + path);
                }
                return new FileHandle(file);
            }

            @Override
            public FileHandle external(String path) {
                return new FileHandle(new File(path));
            }

            @Override
            public FileHandle absolute(String path) {
                return new FileHandle(new File(path));
            }

            @Override
            public FileHandle local(String path) {
                return new FileHandle(new File(path));
            }

            @Override
            public String getExternalStoragePath() {
                return null;
            }

            @Override
            public boolean isExternalStorageAvailable() {
                return false;
            }

            @Override
            public String getLocalStoragePath() {
                return null;
            }

            @Override
            public boolean isLocalStorageAvailable() {
                return false;
            }
        };
    }

    @Test
    public void testDefaultJsonLoading() {
        BloonQueue queue = BloonFactory.createBloonQueueFromJson("default.json");
        assertNotNull(queue);
        assertTrue(queue.hasNextLevel());
        assertEquals(0, queue.getLevel());

        queue.nextLevel(); // Level 1
        assertEquals(1, queue.getLevel());
        assertEquals("Level 1", queue.getCurrentTitle());
        assertEquals("music/demystify_feast.mp3", queue.getCurrentMusicTrack());

        Set<Bloon> bloons = queue.getBloons();
        assertNotNull(bloons);
        assertFalse(bloons.isEmpty());
    }

    @Test
    public void testHellaBloonsJsonLoading() {
        BloonQueue queue = BloonFactory.createBloonQueueFromJson("hella_bloons.json");
        assertNotNull(queue);
        assertTrue(queue.hasNextLevel());

        queue.nextLevel();
        assertEquals(1, queue.getLevel());
        assertEquals("Hella Bloons", queue.getCurrentTitle());
        assertEquals("music/demystify_feast.mp3", queue.getCurrentMusicTrack());

        Set<Bloon> bloons = queue.getBloons();
        assertNotNull(bloons);
        assertEquals(1, bloons.size());
    }

    @Test
    public void testMissingNameFieldThrowsException() throws IOException {
        File tempFile = File.createTempFile("invalid_no_name", ".json");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{\"waves\": []}");
        }

        try {
            BloonFactory.createBloonQueueFromJson(tempFile.getAbsolutePath());
            fail("Expected WaveSchemaValidationException due to missing 'name'");
        } catch (WaveSchemaValidationException e) {
            assertTrue(e.getMessage().contains("Missing required property 'name'"));
        }
    }

    @Test
    public void testMissingWavesFieldThrowsException() throws IOException {
        File tempFile = File.createTempFile("invalid_no_waves", ".json");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{\"name\": \"Test Waves\"}");
        }

        try {
            BloonFactory.createBloonQueueFromJson(tempFile.getAbsolutePath());
            fail("Expected WaveSchemaValidationException due to missing 'waves'");
        } catch (WaveSchemaValidationException e) {
            assertTrue(e.getMessage().contains("Missing required property 'waves'"));
        }
    }

    @Test
    public void testMissingWaveLevelThrowsException() throws IOException {
        File tempFile = File.createTempFile("invalid_no_level", ".json");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{\"name\": \"Test\", \"waves\": [{\"title\": \"L1\", \"musicTrack\": \"m.mp3\", \"bloons\": []}]}");
        }

        try {
            BloonFactory.createBloonQueueFromJson(tempFile.getAbsolutePath());
            fail("Expected WaveSchemaValidationException due to missing 'level'");
        } catch (WaveSchemaValidationException e) {
            assertTrue(e.getMessage().contains("Missing required property 'level'"));
        }
    }

    @Test
    public void testMissingMusicTrackThrowsException() throws IOException {
        File tempFile = File.createTempFile("invalid_no_music", ".json");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{\"name\": \"Test\", \"waves\": [{\"level\": 1, \"title\": \"L1\", \"bloons\": []}]}");
        }

        try {
            BloonFactory.createBloonQueueFromJson(tempFile.getAbsolutePath());
            fail("Expected WaveSchemaValidationException due to missing 'musicTrack'");
        } catch (WaveSchemaValidationException e) {
            assertTrue(e.getMessage().contains("Missing required property 'musicTrack'"));
        }
    }

    @Test
    public void testMissingBloonGroupTypesThrowsException() throws IOException {
        File tempFile = File.createTempFile("invalid_no_types", ".json");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{\"name\": \"Test\", \"waves\": [{\"level\": 1, \"title\": \"L1\", \"musicTrack\": \"m.mp3\", \"bloons\": [{\"amount\": 5, \"delay\": 20}]}]}");
        }

        try {
            BloonFactory.createBloonQueueFromJson(tempFile.getAbsolutePath());
            fail("Expected WaveSchemaValidationException due to missing 'types'");
        } catch (WaveSchemaValidationException e) {
            assertTrue(e.getMessage().contains("Missing required property 'types'"));
        }
    }

    @Test
    public void testMalformedJsonThrowsException() throws IOException {
        File tempFile = File.createTempFile("malformed", ".json");
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{this is not valid json}");
        }

        try {
            BloonFactory.createBloonQueueFromJson(tempFile.getAbsolutePath());
            fail("Expected WaveSchemaValidationException due to malformed JSON");
        } catch (WaveSchemaValidationException e) {
            assertTrue(e.getMessage().contains("Failed to parse wave JSON file"));
        }
    }
}
