package com.hongbao.bloons.scores;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameOverEvent;
import com.hongbao.bloons.events.LevelCompletedEvent;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonsScoringIntegrationTest {

    private static HeadlessApplication app;

    @BeforeClass
    public static void setUpGdx() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        app = new HeadlessApplication(new ApplicationAdapter() {}, config);
    }

    @AfterClass
    public static void tearDownGdx() {
        if (app != null) {
            app.exit();
        }
    }

    @Test
    public void testScoreManagerWithGdxLocalFile() {
        // Ensure scores.json local file handle is accessible
        if (Gdx.files.local("scores.json").exists()) {
            Gdx.files.local("scores.json").delete();
        }

        ScorePersistence persistence = new ScorePersistence();
        ScoreManager manager = new ScoreManager(persistence);

        // Emit gameplay events
        manager.onGameplayEvent(new BloonPoppedEvent(null, null, 1, 20, true)); // 200 base points
        manager.onGameplayEvent(new LevelCompletedEvent(1)); // 100 level bonus
        assertEquals(300, manager.getCurrentScore());

        // Game over event triggers auto-archiving to scores.json
        manager.onGameplayEvent(new GameOverEvent(false, 1));

        assertTrue(Gdx.files.local("scores.json").exists());

        // Load new manager from same file handle
        ScoreManager loadedManager = new ScoreManager(new ScorePersistence());
        LeaderboardData leaderboard = loadedManager.getLeaderboardData();

        assertNotNull(leaderboard);
        assertEquals(1, leaderboard.getEntries().size());
        assertEquals(300, leaderboard.getEntries().get(0).getScore());

        // Cleanup test file
        Gdx.files.local("scores.json").delete();
    }
}
