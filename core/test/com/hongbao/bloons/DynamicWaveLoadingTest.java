package com.hongbao.bloons;

import com.hongbao.bloons.descriptors.LevelWaveDescriptor;
import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicWaveLoadingTest {

    private static final String MOCK_40_LEVEL_DESCRIPTOR =
            "END\n" +                       // Level 0 (empty)
            "20 30 red\nEND\n" +             // Level 1: 20 red
            "35 30 red\nEND\n" +             // Level 2: 35 red
            "25 20 red\n5 30 blue\nEND\n" +  // Level 3: 25 red, 5 blue
            "1 1 moab\nEND\n";              // Level 4: 1 moab

    private static final String MOCK_JSON_DESCRIPTOR = "[\n" +
            "  {\n" +
            "    \"level\": 0,\n" +
            "    \"spawns\": []\n" +
            "  },\n" +
            "  {\n" +
            "    \"level\": 1,\n" +
            "    \"spawns\": [\n" +
            "      { \"amount\": 10, \"delay\": 20, \"types\": \"blue\" }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    @Test
    public void testStartupQueueInitializationReadsOnlyActiveLevel() {
        WaveLoader waveLoader = new WaveLoader(MOCK_40_LEVEL_DESCRIPTOR, "test_descriptor");
        BloonQueue queue = new BloonQueue(waveLoader);

        assertEquals(0, queue.getLevel());
        assertTrue(queue.getActiveBloons().isEmpty());
        assertTrue(queue.isEmpty());
        assertTrue(queue.hasNextLevel());
    }

    @Test
    public void testLevelTransitionLogicFetchesAndParsesNextLevelDescriptors() {
        WaveLoader waveLoader = new WaveLoader(MOCK_40_LEVEL_DESCRIPTOR, "test_descriptor");
        BloonQueue queue = new BloonQueue(waveLoader);

        // Advance to Level 1
        queue.nextLevel();
        assertEquals(1, queue.getLevel());
        assertEquals(20, queue.getActiveBloons().size());
        assertEquals(Bloon.Color.RED, queue.getActiveBloons().get(0).getColor());

        // Advance to Level 2
        queue.nextLevel();
        assertEquals(2, queue.getLevel());
        assertEquals(35, queue.getActiveBloons().size());
        assertEquals(Bloon.Color.RED, queue.getActiveBloons().get(0).getColor());

        // Advance to Level 3
        queue.nextLevel();
        assertEquals(3, queue.getLevel());
        assertEquals(30, queue.getActiveBloons().size()); // 25 + 5
        assertEquals(Bloon.Color.BLUE, queue.getActiveBloons().get(25).getColor());
    }

    @Test
    public void testFreesCompletedLevelDescriptorMemoryBetweenLevels() {
        WaveLoader waveLoader = new WaveLoader(MOCK_40_LEVEL_DESCRIPTOR, "test_descriptor");
        BloonQueue queue = new BloonQueue(waveLoader);

        queue.nextLevel(); // Level 1
        assertEquals(20, queue.getActiveBloons().size());

        queue.nextLevel(); // Level 2
        // Active bloons should now be Level 2 bloons (35 bloons), not holding Level 1 bloons
        assertEquals(35, queue.getActiveBloons().size());
    }

    @Test
    public void testJsonWaveDescriptorLoading() {
        WaveLoader waveLoader = new WaveLoader(MOCK_JSON_DESCRIPTOR, "test_json");
        BloonQueue queue = new BloonQueue(waveLoader);

        assertEquals(0, queue.getLevel());
        assertTrue(queue.getActiveBloons().isEmpty());

        queue.nextLevel(); // Level 1 from JSON
        assertEquals(1, queue.getLevel());
        assertEquals(10, queue.getActiveBloons().size());
        assertEquals(Bloon.Color.BLUE, queue.getActiveBloons().get(0).getColor());
    }

    @Test
    public void testCorruptedOrMissingFileFallback() {
        // Missing asset
        WaveLoader missingLoader = WaveLoader.fromAsset("non_existent_file.txt");
        LevelWaveDescriptor wave = missingLoader.loadLevel(1);
        assertNotNull(wave);
        assertTrue(wave.isEmpty());

        BloonQueue queue = new BloonQueue(missingLoader);
        assertTrue(queue.isEmpty());
        assertFalse(queue.hasNextLevel());

        // Corrupted format string
        String corruptedContent = "NOT_A_VALID_FORMAT 123 456\nINVALID_LINE";
        WaveLoader corruptedLoader = new WaveLoader(corruptedContent, "corrupted");
        BloonQueue corruptedQueue = new BloonQueue(corruptedLoader);
        assertTrue(corruptedQueue.isEmpty());
    }

    @Test
    public void testHasNextLevelAccuracy() {
        WaveLoader waveLoader = new WaveLoader(MOCK_40_LEVEL_DESCRIPTOR, "test_descriptor");
        BloonQueue queue = new BloonQueue(waveLoader);

        assertTrue(queue.hasNextLevel()); // 0 -> 1 exists
        queue.nextLevel(); // 1
        assertTrue(queue.hasNextLevel()); // 1 -> 2 exists
        queue.nextLevel(); // 2
        assertTrue(queue.hasNextLevel()); // 2 -> 3 exists
        queue.nextLevel(); // 3
        assertTrue(queue.hasNextLevel()); // 3 -> 4 exists
        queue.nextLevel(); // 4
        assertFalse(queue.hasNextLevel()); // 4 -> 5 does NOT exist
    }
}
