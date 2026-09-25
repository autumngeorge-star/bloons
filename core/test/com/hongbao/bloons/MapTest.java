package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.factories.MapFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Proxy;
import java.nio.IntBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapTest {

    @BeforeClass
    public static void initGdx() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, config);

        Gdx.gl = Gdx.gl20 = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("glGetActiveAttrib".equals(name)) return "a_attrib";
                    if ("glGetActiveUniform".equals(name)) return "u_attrib";
                    if (args != null && args.length > 2 && ("glGetShaderiv".equals(name) || "glGetProgramiv".equals(name))) {
                        if (args[2] instanceof IntBuffer) {
                            IntBuffer buf = (IntBuffer) args[2];
                            buf.put(0, 1);
                        }
                        return null;
                    }
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 1;
                    if (method.getReturnType().equals(float.class)) return 0f;
                    if (method.getReturnType().equals(String.class)) return "test";
                    return null;
                }
        );
    }

    @Test
    public void testGetCenterXOfTile() {
        assertEquals(25.0f, Map.getCenterXOfTile(1), 0.001f);
        assertEquals(75.0f, Map.getCenterXOfTile(2), 0.001f);
        assertEquals(-25.0f, Map.getCenterXOfTile(0), 0.001f);
        assertEquals(1475.0f, Map.getCenterXOfTile(30), 0.001f);
    }

    @Test
    public void testBoundsValidation() {
        Map map = MapFactory.createBasicMap(new Stage(new ScreenViewport()));
        GirlActor girl = new GirlActor(GirlFactory.createReimu(), 0, 0);

        // Out of bounds X < 0
        girl.setPosition(-10f - girl.getWidth() / 2f, 200f - girl.getHeight() / 2f);
        assertEquals(-10f, girl.getCenterX(), 0.001f);
        assertFalse("Placement should be rejected when X < 0", map.canPlaceGirl(girl));

        // Out of bounds X > 1500
        girl.setPosition(1510f - girl.getWidth() / 2f, 200f - girl.getHeight() / 2f);
        assertEquals(1510f, girl.getCenterX(), 0.001f);
        assertFalse("Placement should be rejected when X > 1500", map.canPlaceGirl(girl));

        // Valid bounds X in [0, 1500] on open terrain (y = 200 is row 4, basic map path is row 8)
        girl.setPosition(0f - girl.getWidth() / 2f, 200f - girl.getHeight() / 2f);
        assertEquals(0f, girl.getCenterX(), 0.001f);
        assertTrue("Placement should be allowed when X = 0 on open terrain", map.canPlaceGirl(girl));

        girl.setPosition(1500f - girl.getWidth() / 2f, 200f - girl.getHeight() / 2f);
        assertEquals(1500f, girl.getCenterX(), 0.001f);
        assertTrue("Placement should be allowed when X = 1500 on open terrain", map.canPlaceGirl(girl));
    }

    @Test
    public void testActivePathPlacementRejected() {
        Map map = MapFactory.createBasicMap(new Stage(new ScreenViewport()));
        GirlActor girl = new GirlActor(GirlFactory.createReimu(), 0, 0);

        // BasicMap has path at yTile = 8 (y center = 425f) across xTile 0 to 31
        // Tile xTile = 1 center X is 25f
        girl.setPosition(25f - girl.getWidth() / 2f, 425f - girl.getHeight() / 2f);
        assertEquals(25f, girl.getCenterX(), 0.001f);
        assertEquals(425f, girl.getCenterY(), 0.001f);

        assertFalse("Placement on active bloon path tile must return false", map.canPlaceGirl(girl));
    }

    @Test
    public void testOpenTerrainAdjacentToPathAllowed() {
        Map map = MapFactory.createBasicMap(new Stage(new ScreenViewport()));
        GirlActor girl = new GirlActor(GirlFactory.createReimu(), 0, 0);

        // Open terrain tile adjacent to path (e.g., xTile = 1 center X = 25f, yTile = 9 center Y = 475f)
        girl.setPosition(25f - girl.getWidth() / 2f, 475f - girl.getHeight() / 2f);
        assertEquals(25f, girl.getCenterX(), 0.001f);
        assertEquals(475f, girl.getCenterY(), 0.001f);

        assertTrue("Placement on open terrain adjacent to path tile must return true", map.canPlaceGirl(girl));
    }

    @Test
    public void generateValidationScreenshot() throws Exception {
        int width = 1500;
        int height = 900;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // Background open terrain
        g.setColor(new Color(220, 240, 220));
        g.fillRect(0, 0, width, height);

        // Map setup
        Map map = MapFactory.createBasicMap(new Stage(new ScreenViewport()));
        GirlActor girl = new GirlActor(GirlFactory.createReimu(), 0, 0);

        // Draw grid and path
        int tileSize = 50;
        for (int tileX = 1; tileX <= 30; tileX++) {
            for (int tileY = 0; tileY < 18; tileY++) {
                float centerX = Map.getCenterXOfTile(tileX);
                float centerY = tileY * tileSize + 25f;
                int screenX = (int) (centerX - 25f);
                int screenY = height - (int) (centerY + 25f);

                if (tileY == 8) {
                    // Path tile
                    g.setColor(new Color(230, 200, 170));
                    g.fillRect(screenX, screenY, tileSize, tileSize);
                }

                g.setColor(new Color(180, 180, 180, 100));
                g.drawRect(screenX, screenY, tileSize, tileSize);
            }
        }

        // Test Placement 1: On Path Tile (Rejected - Red Circle)
        float pathCenterX = Map.getCenterXOfTile(5); // 225.0f
        float pathCenterY = 8 * 50 + 25.0f; // 425.0f
        girl.setPosition(pathCenterX - girl.getWidth() / 2f, pathCenterY - girl.getHeight() / 2f);
        boolean canPlaceOnPath = map.canPlaceGirl(girl);

        int pX1 = (int) pathCenterX;
        int pY1 = height - (int) pathCenterY;
        g.setColor(canPlaceOnPath ? Color.GREEN : Color.RED);
        g.setStroke(new BasicStroke(3));
        g.drawOval(pX1 - 25, pY1 - 25, 50, 50);
        g.drawString("Path Placement (Rejected: " + !canPlaceOnPath + ")", pX1 - 70, pY1 - 30);

        // Test Placement 2: On Adjacent Open Terrain (Allowed - Green Circle)
        float openCenterX = Map.getCenterXOfTile(5); // 225.0f
        float openCenterY = 9 * 50 + 25.0f; // 475.0f
        girl.setPosition(openCenterX - girl.getWidth() / 2f, openCenterY - girl.getHeight() / 2f);
        boolean canPlaceOnOpen = map.canPlaceGirl(girl);

        int pX2 = (int) openCenterX;
        int pY2 = height - (int) openCenterY;
        g.setColor(canPlaceOnOpen ? Color.GREEN : Color.RED);
        g.drawOval(pX2 - 25, pY2 - 25, 50, 50);
        g.drawString("Open Terrain Placement (Allowed: " + canPlaceOnOpen + ")", pX2 - 80, pY2 - 30);

        g.dispose();
        ImageIO.write(img, "png", new File("/tmp/placement_validation.png"));
        assertTrue(new File("/tmp/placement_validation.png").exists());
    }
}
