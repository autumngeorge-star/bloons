package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.helpers.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

public class SpatialGridTest {

    private static BloonsTouhouDefense gameApp;

    @BeforeClass
    public static void initGdx() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        gameApp = new BloonsTouhouDefense();
        new HeadlessApplication(gameApp, config);
        
        GL20 gl = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("glCreateShader") || method.getName().equals("glCreateProgram")) {
                        return 1;
                    }
                    if (method.getName().equals("glGetShaderiv") || method.getName().equals("glGetProgramiv")) {
                        if (args != null && args.length > 2 && args[2] instanceof java.nio.IntBuffer) {
                            ((java.nio.IntBuffer) args[2]).put(0, 1);
                        }
                        return null;
                    }
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 0;
                    if (method.getReturnType().equals(float.class)) return 0f;
                    return null;
                }
        );
        Gdx.gl = gl;
        Gdx.gl20 = gl;
    }

    private BloonManager bloonManager;
    private Stage stage;
    private Map map;

    @Before
    public void setUp() {
        com.badlogic.gdx.graphics.g2d.Batch mockBatch = (com.badlogic.gdx.graphics.g2d.Batch) Proxy.newProxyInstance(
                com.badlogic.gdx.graphics.g2d.Batch.class.getClassLoader(),
                new Class<?>[]{com.badlogic.gdx.graphics.g2d.Batch.class},
                (proxy, method, args) -> {
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 0;
                    if (method.getReturnType().equals(float.class)) return 0f;
                    return null;
                }
        );
        stage = new Stage(new com.badlogic.gdx.utils.viewport.ScreenViewport(), mockBatch);
        
        // Use a lightweight Map test instance that doesn't load UI skins
        map = new MapStub(stage);
        bloonManager = map.getBloonManager();
    }

    private static class MapStub extends Map {
        private BloonManager bm;

        public MapStub(Stage stage) {
            super(stage);
            this.bm = new BloonManager(stage, this);
        }

        @Override
        public BloonManager getBloonManager() {
            return bm;
        }

        @Override
        public Pair<Float, Float> getDirection(float balloonX, float balloonY) {
            return new Pair<>(0f, 0f);
        }
    }

    @Test
    public void testGridInitialization() {
        assertEquals(15, BloonManager.GRID_COLS);
        assertEquals(9, BloonManager.GRID_ROWS);
        assertEquals(100f, BloonManager.CELL_SIZE, 0.001f);
        assertEquals(1500f, BloonManager.STAGE_WIDTH, 0.001f);
        assertEquals(900f, BloonManager.STAGE_HEIGHT, 0.001f);

        Array<BloonActor>[][] grid = bloonManager.getGrid();
        assertNotNull(grid);
        assertEquals(15, grid.length);
        assertEquals(9, grid[0].length);

        for (int c = 0; c < 15; c++) {
            for (int r = 0; r < 9; r++) {
                assertNotNull("Grid cell [" + c + "][" + r + "] should be initialized", grid[c][r]);
                assertEquals(0, grid[c][r].size);
            }
        }
    }

    @Test
    public void testBloonRegistrationAndIndexing() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actor = new BloonActor(bloon, 250, 350, null);
        
        bloonManager.registerBloon(actor);

        int expectedCol = (int) Math.floor(actor.getCenterX() / BloonManager.CELL_SIZE);
        int expectedRow = (int) Math.floor(actor.getCenterY() / BloonManager.CELL_SIZE);

        assertEquals(expectedCol, actor.getGridCol());
        assertEquals(expectedRow, actor.getGridRow());
        assertTrue(bloonManager.getGrid()[expectedCol][expectedRow].contains(actor, true));

        // Move actor to a different cell
        actor.setPosition(650, 750);
        bloonManager.updateBloonGridPosition(actor);

        int newCol = (int) Math.floor(actor.getCenterX() / BloonManager.CELL_SIZE);
        int newRow = (int) Math.floor(actor.getCenterY() / BloonManager.CELL_SIZE);

        assertEquals(newCol, actor.getGridCol());
        assertEquals(newRow, actor.getGridRow());
        assertFalse(bloonManager.getGrid()[expectedCol][expectedRow].contains(actor, true));
        assertTrue(bloonManager.getGrid()[newCol][newRow].contains(actor, true));

        // Unregister actor
        bloonManager.unregisterBloon(actor);
        assertEquals(-1, actor.getGridCol());
        assertEquals(-1, actor.getGridRow());
        assertFalse(bloonManager.getGrid()[newCol][newRow].contains(actor, true));
    }

    @Test
    public void testBulletCollisionLocalQuery() {
        Bloon bloonNear = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor bloonActorNear = new BloonActor(bloonNear, 200, 200, null);

        Bloon bloonFar = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor bloonActorFar = new BloonActor(bloonFar, 1200, 800, null);

        stage.addActor(bloonActorNear);
        stage.addActor(bloonActorFar);
        bloonManager.registerBloon(bloonActorNear);
        bloonManager.registerBloon(bloonActorFar);

        Bullet bullet = new Bullet(20f, 1, 10, 500f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 200, 200, 1, 0);

        bloonManager.checkCollision(bulletActor);

        // Bloon near (at 200, 200) should be damaged/popped
        assertTrue("Bullet damaged nearby bloon", bulletActor.hasDamagedBloon(bloonActorNear));
        assertFalse("Bullet did not damage far bloon", bulletActor.hasDamagedBloon(bloonActorFar));
    }

    @Test
    public void testSquaredDistanceMathAndPerformance() {
        // Create 200 bloons and 100 bullets across the grid
        for (int i = 0; i < 200; i++) {
            float x = (i * 37) % 1400 + 50;
            float y = (i * 53) % 800 + 50;
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            BloonActor actor = new BloonActor(bloon, x, y, null);
            stage.addActor(actor);
            bloonManager.registerBloon(actor);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            float x = (i * 29) % 1400 + 50;
            float y = (i * 41) % 800 + 50;
            Bullet bullet = new Bullet(20f, 1, 10, 500f, false, "red_spell_card.png");
            BulletActor bulletActor = new BulletActor(bullet, x, y, 1, 0);
            bloonManager.checkCollision(bulletActor);
        }
        long elapsedTimeMs = (System.nanoTime() - startTime) / 1000000;

        // Collision checks for 100 bullets against 200 bloons should take < 100ms
        assertTrue("High density collision check elapsed time (" + elapsedTimeMs + " ms) should be fast", elapsedTimeMs < 100);
    }

    @Test
    public void testHomingTargetRingSearch() {
        Bloon bloonClose = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actorClose = new BloonActor(bloonClose, 300, 300, null);

        Bloon bloonDistant = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actorDistant = new BloonActor(bloonDistant, 800, 800, null);

        stage.addActor(actorClose);
        stage.addActor(actorDistant);
        bloonManager.registerBloon(actorClose);
        bloonManager.registerBloon(actorDistant);

        Bullet bullet = new Bullet(20f, 1, 10, 500f, true, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 250, 250, 1, 0);

        BloonActor target = bloonManager.getNewHomingTarget(bulletActor);
        assertNotNull("Homing target found", target);
        assertEquals("Homing target should be closest bloon", actorClose, target);
    }

    @Test
    public void testGenerateSpatialGridVisualization() {
        // Populate grid with bloons and bullets
        for (int i = 0; i < 60; i++) {
            float x = (i * 73) % 1400 + 50;
            float y = (i * 37) % 800 + 50;
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            BloonActor actor = new BloonActor(bloon, x, y, null);
            stage.addActor(actor);
            bloonManager.registerBloon(actor);
        }

        Bullet bullet = new Bullet(20f, 1, 10, 500f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 450, 450, 1, 0);

        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1500, 900, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(com.badlogic.gdx.graphics.Color.NAVY);
        pixmap.fill();

        // Draw 100x100 spatial grid lines
        pixmap.setColor(com.badlogic.gdx.graphics.Color.SLATE);
        for (int x = 0; x <= 1500; x += 100) {
            pixmap.drawLine(x, 0, x, 900);
        }
        for (int y = 0; y <= 900; y += 100) {
            pixmap.drawLine(0, y, 1500, y);
        }

        // Draw bloons
        pixmap.setColor(com.badlogic.gdx.graphics.Color.CORAL);
        for (int c = 0; c < 15; c++) {
            for (int r = 0; r < 9; r++) {
                Array<BloonActor> cellBloons = bloonManager.getGrid()[c][r];
                for (int i = 0; i < cellBloons.size; i++) {
                    BloonActor b = cellBloons.get(i);
                    pixmap.fillCircle((int) b.getCenterX(), (int) (900 - b.getCenterY()), 12);
                }
            }
        }

        // Draw bullet query bounding box
        float bx = bulletActor.getCenterX();
        float by = bulletActor.getCenterY();
        float br = bulletActor.getCollisionRadius();
        int minCol = bloonManager.getGridCol(bx - br - BloonManager.MAX_BLOON_RADIUS);
        int maxCol = bloonManager.getGridCol(bx + br + BloonManager.MAX_BLOON_RADIUS);
        int minRow = bloonManager.getGridRow(by - br - BloonManager.MAX_BLOON_RADIUS);
        int maxRow = bloonManager.getGridRow(by + br + BloonManager.MAX_BLOON_RADIUS);

        pixmap.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        int boxX = minCol * 100;
        int boxY = (8 - maxRow) * 100;
        int boxW = (maxCol - minCol + 1) * 100;
        int boxH = (maxRow - minRow + 1) * 100;
        pixmap.drawRectangle(boxX, boxY, boxW, boxH);

        pixmap.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        pixmap.fillCircle((int) bx, (int) (900 - by), 8);

        com.badlogic.gdx.graphics.PixmapIO.writePNG(Gdx.files.absolute("/tmp/spatial_grid_visualization.png"), pixmap);
        pixmap.dispose();

        assertTrue(Gdx.files.absolute("/tmp/spatial_grid_visualization.png").exists());
    }

}
