package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BloonsTouhouDefenseTest {

    private Application originalApp;

    @Before
    public void setUp() {
        originalApp = Gdx.app;
    }

    @After
    public void tearDown() {
        Gdx.app = originalApp;
    }

    @Test
    public void testScoreAndHighScoreFormatting() {
        Player player = new Player(1000, 100);

        String initialScoreText = "Score: " + player.getScore();
        String initialHighScoreText = "High Score: " + player.getHighScore();

        assertEquals("Score: 0", initialScoreText);
        assertEquals("High Score: 0", initialHighScoreText);

        player.addScore(250);

        String updatedScoreText = "Score: " + player.getScore();
        String updatedHighScoreText = "High Score: " + player.getHighScore();

        assertEquals("Score: 250", updatedScoreText);
        assertEquals("High Score: 250", updatedHighScoreText);
    }
}
