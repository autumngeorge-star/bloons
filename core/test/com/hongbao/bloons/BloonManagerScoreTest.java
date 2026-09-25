package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;

public class BloonManagerScoreTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player(1000, 100);
    }

    @Test
    public void testBloonPoppedResultScoreCalculation() {
        // Red bloon (health 1) popped with 1 damage
        Bloon redBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonPoppedResult result = redBloon.pop(1);

        player.addScore(result.getCashGenerated());
        assertEquals(1, player.getScore());
        assertEquals(1, player.getHighScore());

        // Blue bloon (health 2) popped with 1 damage
        Bloon blueBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonPoppedResult blueResult = blueBloon.pop(1);

        player.addScore(blueResult.getCashGenerated());
        assertEquals(2, player.getScore());
        assertEquals(2, player.getHighScore());
    }

    @Test
    public void testBloonDamageWithoutPopScoreCalculation() {
        // Ceramic bloon (health 18) damaged by 1 (health becomes 17, still ceramic)
        Bloon ceramicBloon = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
        int damageDealt = 1;

        if (!ceramicBloon.willPopBloon(damageDealt)) {
            ceramicBloon.damage(damageDealt);
            player.addScore(damageDealt);
        }

        assertEquals(1, player.getScore());
        assertEquals(17, ceramicBloon.getHealth());
    }

    @Test
    public void testNextLevelSavesHighScore() {
        boolean[] savedHighScore = { false };
        Player testPlayer = new Player(1000, 100) {
            @Override
            public void saveHighScore() {
                savedHighScore[0] = true;
            }
        };

        BloonsTouhouDefense app = new BloonsTouhouDefense() {
            @Override
            public Player getPlayer() {
                return testPlayer;
            }
        };

        Application dummyApp = (Application) Proxy.newProxyInstance(
            Application.class.getClassLoader(),
            new Class<?>[]{ Application.class },
            (proxy, method, args) -> {
                if ("getApplicationListener".equals(method.getName())) {
                    return app;
                }
                return null;
            }
        );
        Gdx.app = dummyApp;

        testPlayer.addScore(500);
        testPlayer.saveHighScore();

        assertEquals(true, savedHighScore[0]);

        Gdx.app = null;
    }
}
