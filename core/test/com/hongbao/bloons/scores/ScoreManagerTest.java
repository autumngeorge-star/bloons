package com.hongbao.bloons.scores;

import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameOverEvent;
import com.hongbao.bloons.events.LevelCompletedEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ScoreManagerTest {

    private File tempFile;
    private FileHandle tempFileHandle;
    private ScorePersistence persistence;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test_scores", ".json");
        tempFileHandle = new FileHandle(tempFile);
        persistence = new ScorePersistence(tempFileHandle);
    }

    @After
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testScoreManagerEventHandlingAndCombos() {
        ScoreManager manager = new ScoreManager(persistence);
        assertEquals(0, manager.getCurrentScore());
        assertEquals(0, manager.getComboCount());

        // Event 1: Bloon popped (cash = 10 -> base 100 points, combo 1)
        BloonPoppedEvent event1 = new BloonPoppedEvent(null, null, 1, 10, true);
        manager.onGameplayEvent(event1);

        assertEquals(100, manager.getCurrentScore());
        assertEquals(1, manager.getComboCount());

        // Event 2: Second bloon popped immediately (cash = 10 -> base 100 + combo bonus 5 = 105 points)
        BloonPoppedEvent event2 = new BloonPoppedEvent(null, null, 1, 10, true);
        manager.onGameplayEvent(event2);

        assertEquals(205, manager.getCurrentScore());
        assertEquals(2, manager.getComboCount());

        // Event 3: Third bloon popped immediately (cash = 10 -> base 100 + combo bonus 10 = 110 points)
        BloonPoppedEvent event3 = new BloonPoppedEvent(null, null, 1, 10, true);
        manager.onGameplayEvent(event3);

        assertEquals(315, manager.getCurrentScore());
        assertEquals(3, manager.getComboCount());
    }

    @Test
    public void testLevelCompletedEvent() {
        ScoreManager manager = new ScoreManager(persistence);

        manager.onGameplayEvent(new LevelCompletedEvent(1));
        assertEquals(100, manager.getCurrentScore());
        assertEquals(1, manager.getCurrentLevel());

        manager.onGameplayEvent(new LevelCompletedEvent(2));
        assertEquals(300, manager.getCurrentScore()); // 100 + 200
        assertEquals(2, manager.getCurrentLevel());
    }

    @Test
    public void testLeaderboardDataSortingAndTrimming() {
        LeaderboardData data = new LeaderboardData();

        for (int i = 1; i <= 120; i++) {
            data.addEntry(new LeaderboardEntry(i * 10, 1, System.currentTimeMillis() + i));
        }

        // Should be capped at 100 entries
        assertEquals(100, data.getEntries().size());

        // Highest score should be at index 0 (1200)
        assertEquals(1200, data.getEntries().get(0).getScore());
        assertEquals(210, data.getEntries().get(99).getScore());

        // Top 10 entries check
        List<LeaderboardEntry> top10 = data.getTopEntries(10);
        assertEquals(10, top10.size());
        assertEquals(1200, top10.get(0).getScore());
        assertEquals(1110, top10.get(9).getScore());
    }

    @Test
    public void testScorePersistenceSaveAndLoad() {
        LeaderboardData data = new LeaderboardData();
        data.addEntry(new LeaderboardEntry(500, 5, System.currentTimeMillis()));
        data.addEntry(new LeaderboardEntry(1000, 10, System.currentTimeMillis()));

        boolean saved = persistence.save(data);
        assertTrue(saved);
        assertTrue(tempFile.exists());

        LeaderboardData loaded = persistence.load();
        assertNotNull(loaded);
        assertEquals(2, loaded.getEntries().size());
        assertEquals(1000, loaded.getEntries().get(0).getScore());
        assertEquals(500, loaded.getEntries().get(1).getScore());
    }

    @Test
    public void testCorruptedJsonFallback() throws IOException {
        // Write invalid JSON content
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("{ corrupted json content ::: 123 }");
        }

        LeaderboardData loaded = persistence.load();
        assertNotNull(loaded);
        assertNotNull(loaded.getEntries());
        assertTrue(loaded.getEntries().isEmpty());
    }

    @Test
    public void testSessionArchivingAndReset() {
        ScoreManager manager = new ScoreManager(persistence);

        manager.onGameplayEvent(new BloonPoppedEvent(null, null, 1, 50, true)); // 500 points
        manager.onGameplayEvent(new LevelCompletedEvent(3)); // 300 bonus
        assertEquals(800, manager.getCurrentScore());

        manager.onGameplayEvent(new GameOverEvent(false, 3));

        LeaderboardData loaded = persistence.load();
        assertEquals(1, loaded.getEntries().size());
        assertEquals(800, loaded.getEntries().get(0).getScore());

        manager.resetSession();
        assertEquals(0, manager.getCurrentScore());
        assertEquals(0, manager.getComboCount());
    }
}
