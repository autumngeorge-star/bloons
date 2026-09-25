package com.hongbao.bloons.score;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ScoreManagerTest {

    private String testFileName;
    private File testFile;

    public static void main(String[] args) {
        ScoreManagerTest test = new ScoreManagerTest();
        test.runAllTests();
    }

    public void runAllTests() {
        int passed = 0;
        int failed = 0;

        System.out.println("=== RUNNING SCORE MANAGER TESTS ===");

        try { setUp(); testScoreEventsCalculation(); tearDown(); System.out.println("[PASS] testScoreEventsCalculation"); passed++; }
        catch (Throwable t) { System.err.println("[FAIL] testScoreEventsCalculation: " + t.getMessage()); t.printStackTrace(); failed++; tearDown(); }

        try { setUp(); testSessionScoreSavingOnGameOver(); tearDown(); System.out.println("[PASS] testSessionScoreSavingOnGameOver"); passed++; }
        catch (Throwable t) { System.err.println("[FAIL] testSessionScoreSavingOnGameOver: " + t.getMessage()); t.printStackTrace(); failed++; tearDown(); }

        try { setUp(); testSessionScoreSavingOnVictory(); tearDown(); System.out.println("[PASS] testSessionScoreSavingOnVictory"); passed++; }
        catch (Throwable t) { System.err.println("[FAIL] testSessionScoreSavingOnVictory: " + t.getMessage()); t.printStackTrace(); failed++; tearDown(); }

        try { setUp(); testCorruptedFileHandling(); tearDown(); System.out.println("[PASS] testCorruptedFileHandling"); passed++; }
        catch (Throwable t) { System.err.println("[FAIL] testCorruptedFileHandling: " + t.getMessage()); t.printStackTrace(); failed++; tearDown(); }

        try { setUp(); testMissingFileHandling(); tearDown(); System.out.println("[PASS] testMissingFileHandling"); passed++; }
        catch (Throwable t) { System.err.println("[FAIL] testMissingFileHandling: " + t.getMessage()); t.printStackTrace(); failed++; tearDown(); }

        try { setUp(); testHighScoreLoadingOnApplicationBoot(); tearDown(); System.out.println("[PASS] testHighScoreLoadingOnApplicationBoot"); passed++; }
        catch (Throwable t) { System.err.println("[FAIL] testHighScoreLoadingOnApplicationBoot: " + t.getMessage()); t.printStackTrace(); failed++; tearDown(); }

        System.out.println("=== TEST SUMMARY: " + passed + " PASSED, " + failed + " FAILED ===");
        if (failed > 0) {
            System.exit(1);
        }
    }

    public void setUp() {
        testFileName = "test_scores_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + ".json";
        testFile = new File(testFileName);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    public void tearDown() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    public void testScoreEventsCalculation() {
        ScoreRepository repo = new ScoreRepository(testFileName);
        ScoreManager scoreManager = new ScoreManager(repo);

        assertEquals(0, scoreManager.getCurrentScore());
        assertEquals(0, scoreManager.getHighScore());

        scoreManager.onScoreEvent(new ScoreEvent(ScoreEvent.Type.BLOON_DAMAGED, 10));
        assertEquals(10, scoreManager.getCurrentScore());
        assertEquals(10, scoreManager.getHighScore());

        scoreManager.onScoreEvent(new ScoreEvent(ScoreEvent.Type.BLOON_POPPED, 50));
        assertEquals(60, scoreManager.getCurrentScore());
        assertEquals(60, scoreManager.getHighScore());
    }

    public void testSessionScoreSavingOnGameOver() {
        ScoreRepository repo = new ScoreRepository(testFileName);
        ScoreManager scoreManager = new ScoreManager(repo);

        scoreManager.onScoreEvent(new ScoreEvent(ScoreEvent.Type.BLOON_POPPED, 120));
        scoreManager.onScoreEvent(new ScoreEvent(ScoreEvent.Type.GAME_OVER, 5));

        List<ScoreEntry> history = scoreManager.getScoreHistory();
        assertEquals(1, history.size());
        assertEquals(120, history.get(0).getScore());
        assertEquals(5, history.get(0).getLevel());
        assertFalse(history.get(0).isVictory());

        assertTrue(testFile.exists());
    }

    public void testSessionScoreSavingOnVictory() {
        ScoreRepository repo = new ScoreRepository(testFileName);
        ScoreManager scoreManager = new ScoreManager(repo);

        scoreManager.onScoreEvent(new ScoreEvent(ScoreEvent.Type.BLOON_POPPED, 500));
        scoreManager.onScoreEvent(new ScoreEvent(ScoreEvent.Type.GAME_WON, 40));

        List<ScoreEntry> history = scoreManager.getScoreHistory();
        assertEquals(1, history.size());
        assertEquals(500, history.get(0).getScore());
        assertEquals(40, history.get(0).getLevel());
        assertTrue(history.get(0).isVictory());
    }

    public void testCorruptedFileHandling() throws IOException {
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("{ corrupted_json_invalid_structure : [[[ ");
        }

        ScoreRepository repo = new ScoreRepository(testFileName);
        List<ScoreEntry> scores = repo.loadScores();

        assertNotNull(scores);
        assertTrue(scores.isEmpty());

        ScoreManager manager = new ScoreManager(repo);
        assertEquals(0, manager.getHighScore());
        assertTrue(manager.getScoreHistory().isEmpty());
    }

    public void testMissingFileHandling() {
        assertFalse(testFile.exists());

        ScoreRepository repo = new ScoreRepository(testFileName);
        List<ScoreEntry> scores = repo.loadScores();

        assertNotNull(scores);
        assertTrue(scores.isEmpty());
    }

    public void testHighScoreLoadingOnApplicationBoot() {
        ScoreRepository repo = new ScoreRepository(testFileName);
        ScoreManager manager1 = new ScoreManager(repo);

        manager1.onScoreEvent(new ScoreEvent(ScoreEvent.Type.BLOON_POPPED, 250));
        manager1.saveSessionScore(false, 3);

        ScoreManager manager2 = new ScoreManager(repo);
        assertEquals(250, manager2.getHighScore());
        assertEquals(1, manager2.getScoreHistory().size());
        assertEquals(250, manager2.getScoreHistory().get(0).getScore());
    }

    private void assertEquals(long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }

    private void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but was true");
        }
    }

    private void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected non-null object");
        }
    }
}
