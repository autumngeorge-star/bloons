package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.junit.Assert.*;

public class BloonQueueTest {

    @BeforeClass
    public static void setUp() {
        if (Gdx.app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {}, config);
        }
    }

    @Test
    public void testParseValidJsonWaveConfig() {
        String json = "{\n" +
                "  \"waves\": [\n" +
                "    {\n" +
                "      \"title\": \"Stage 1: Introductory Wave\",\n" +
                "      \"music\": \"stage\",\n" +
                "      \"spawns\": [\n" +
                "        {\n" +
                "          \"count\": 5,\n" +
                "          \"delay\": 10,\n" +
                "          \"types\": [\"red\"]\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"title\": \"Stage 2: Boss Fight\",\n" +
                "      \"music\": \"final_boss\",\n" +
                "      \"spawns\": [\n" +
                "        {\n" +
                "          \"count\": 1,\n" +
                "          \"delay\": 1,\n" +
                "          \"types\": [\"moab\"]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        BloonQueue queue = BloonFactory.parseJsonWaveConfig(json, "test_valid.json");
        assertNotNull(queue);

        // Level 0
        assertEquals("Stage 1: Introductory Wave", queue.getCurrentTitle());
        assertEquals("stage", queue.getCurrentMusicTrack());
        assertFalse(queue.isEmpty());

        Set<Bloon> bloons0 = queue.getBloons();
        assertEquals(1, bloons0.size());

        // Advance to Level 1
        queue.nextLevel();
        assertEquals("Stage 2: Boss Fight", queue.getCurrentTitle());
        assertEquals("final_boss", queue.getCurrentMusicTrack());
    }

    @Test
    public void testParseDefaultJsonFile() {
        File file = new File("assets/bloon_queues/default.json");
        if (!file.exists()) file = new File("core/assets/bloon_queues/default.json");
        assertTrue("default.json file should exist", file.exists());

        BloonQueue queue = BloonFactory.createBloonQueueFromFile("default.json");
        assertNotNull(queue);
        assertTrue(queue.hasNextLevel());

        assertEquals("Level 0", queue.getCurrentTitle());
        queue.nextLevel();
        assertEquals("Level 1", queue.getCurrentTitle());
        assertEquals("stage", queue.getCurrentMusicTrack());
    }

    @Test
    public void testLegacyTxtFallbackAndDeprecationWarning() {
        File file = new File("assets/bloon_queues/default.txt");
        if (!file.exists()) file = new File("core/assets/bloon_queues/default.txt");
        assertTrue("default.txt file should exist", file.exists());

        BloonQueue queue = BloonFactory.createBloonQueueFromFile("default.txt");
        assertNotNull(queue);
        assertTrue(queue.hasNextLevel());

        queue.nextLevel();
        assertEquals("Level 1", queue.getCurrentTitle());
        assertEquals("stage", queue.getCurrentMusicTrack());
    }

    @Test
    public void testSchemaValidationMissingSpawns() {
        String invalidJson = "{\n" +
                "  \"waves\": [\n" +
                "    {\n" +
                "      \"title\": \"Invalid Wave\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            BloonFactory.parseJsonWaveConfig(invalidJson, "missing_spawns.json");
            fail("Expected IllegalArgumentException for missing 'spawns' property");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Missing required property 'spawns'"));
        }
    }

    @Test
    public void testSchemaValidationMissingCount() {
        String invalidJson = "{\n" +
                "  \"waves\": [\n" +
                "    {\n" +
                "      \"spawns\": [\n" +
                "        {\n" +
                "          \"delay\": 10,\n" +
                "          \"types\": [\"red\"]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            BloonFactory.parseJsonWaveConfig(invalidJson, "missing_count.json");
            fail("Expected IllegalArgumentException for missing 'count' property");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Missing required property 'count'"));
        }
    }

    @Test
    public void testSchemaValidationMissingDelay() {
        String invalidJson = "{\n" +
                "  \"waves\": [\n" +
                "    {\n" +
                "      \"spawns\": [\n" +
                "        {\n" +
                "          \"count\": 5,\n" +
                "          \"types\": [\"red\"]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            BloonFactory.parseJsonWaveConfig(invalidJson, "missing_delay.json");
            fail("Expected IllegalArgumentException for missing 'delay' property");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Missing required property 'delay'"));
        }
    }

    @Test
    public void testSchemaValidationMissingTypes() {
        String invalidJson = "{\n" +
                "  \"waves\": [\n" +
                "    {\n" +
                "      \"spawns\": [\n" +
                "        {\n" +
                "          \"count\": 5,\n" +
                "          \"delay\": 10\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            BloonFactory.parseJsonWaveConfig(invalidJson, "missing_types.json");
            fail("Expected IllegalArgumentException for missing 'types' property");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Missing required property 'types'"));
        }
    }

    @Test
    public void testSchemaValidationInvalidBloonType() {
        String invalidJson = "{\n" +
                "  \"waves\": [\n" +
                "    {\n" +
                "      \"spawns\": [\n" +
                "        {\n" +
                "          \"count\": 5,\n" +
                "          \"delay\": 10,\n" +
                "          \"types\": [\"non_existent_bloon\"]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            BloonFactory.parseJsonWaveConfig(invalidJson, "invalid_type.json");
            fail("Expected IllegalArgumentException for invalid bloon type");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unknown bloon type 'non_existent_bloon'"));
        }
    }
}
