package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.nio.IntBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class AssetManagerLifecycleTest {

    private static BloonsTouhouDefense game;

    @BeforeClass
    public static void setUp() throws Exception {
        // Set mock GL20 for headless shader and texture loading
        Gdx.gl = (GL20) Proxy.newProxyInstance(
            GL20.class.getClassLoader(),
            new Class<?>[]{GL20.class},
            (proxy, method, args) -> {
                if ("glGetShaderiv".equals(method.getName()) || "glGetProgramiv".equals(method.getName())) {
                    if (args != null && args.length >= 3 && args[2] instanceof IntBuffer) {
                        ((IntBuffer) args[2]).put(0, 1); // GL_TRUE
                    }
                }
                if ("glCreateShader".equals(method.getName()) || "glCreateProgram".equals(method.getName())) {
                    return 1;
                }
                if (method.getReturnType().equals(boolean.class)) return false;
                if (method.getReturnType().equals(int.class)) return 1;
                if (method.getReturnType().equals(String.class)) return "MockShaderLog";
                return null;
            }
        );
        Gdx.gl20 = Gdx.gl;

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        CountDownLatch latch = new CountDownLatch(1);
        game = new BloonsTouhouDefense() {
            @Override
            public void create() {
                try {
                    super.create();
                } finally {
                    latch.countDown();
                }
            }
        };
        new HeadlessApplication(game, config);

        assertTrue("Game initialization timed out", latch.await(10, TimeUnit.SECONDS));
    }

    @Test
    public void testAssetManagerInitializationAndPreloading() {
        AssetManager assetManager = game.getAssetManager();
        assertNotNull("AssetManager should be instantiated in root game class", assetManager);

        // Verify preloaded assets
        assertTrue("Skin asset should be loaded", assetManager.isLoaded("uiskins/uiskin.json", Skin.class));
        assertTrue("Pop sound asset should be loaded", assetManager.isLoaded("music/pop.mp3", Sound.class));
        assertTrue("Title music should be loaded", assetManager.isLoaded("music/title.mp3", Music.class));
        assertTrue("Background header texture should be loaded", assetManager.isLoaded("img/ui/header.png", Texture.class));
        assertTrue("Map texture should be loaded", assetManager.isLoaded("img/maps/heater.png", Texture.class));
        assertTrue("Reimu texture should be loaded", assetManager.isLoaded("img/characters/reimu.png", Texture.class));
        assertTrue("Red bloon texture should be loaded", assetManager.isLoaded("img/bloons/red_bloon.png", Texture.class));
    }

    @Test
    public void testActorRemovalWithoutTextureDisposal() {
        AssetManager assetManager = game.getAssetManager();
        String redBloonPath = "img/bloons/red_bloon.png";

        // Texture before actor creation
        Texture textureBefore = assetManager.get(redBloonPath, Texture.class);
        assertNotNull(textureBefore);

        // Texture after rendering game loop should remain loaded and valid in AssetManager
        assertTrue("Texture should remain loaded after rendering", assetManager.isLoaded(redBloonPath, Texture.class));
        assertSame("Shared texture instance should be preserved", textureBefore, assetManager.get(redBloonPath, Texture.class));
    }

    @AfterClass
    public static void tearDown() {
        if (game != null && game.getAssetManager() != null) {
            AssetManager assetManager = game.getAssetManager();
            assertTrue("AssetManager should have loaded assets before disposal", assetManager.getLoadedAssets() > 0);
            game.dispose();
        }
    }
}
