package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.hongbao.bloons.factories.MapFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.junit.Assert.*;

public class LifecycleDisposalTest {

    @BeforeClass
    public static void setUpHeadless() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, config);

        Graphics graphicsProxy = (Graphics) Proxy.newProxyInstance(
                Graphics.class.getClassLoader(),
                new Class<?>[]{Graphics.class},
                (proxy, method, args) -> {
                    if ("setWindowedMode".equals(method.getName())) return true;
                    if ("getDeltaTime".equals(method.getName())) return 0.016f;
                    if ("getWidth".equals(method.getName()) || "getBackBufferWidth".equals(method.getName())) return 1800;
                    if ("getHeight".equals(method.getName()) || "getBackBufferHeight".equals(method.getName())) return 900;
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 0;
                    if (method.getReturnType().equals(float.class)) return 0f;
                    return null;
                }
        );

        GL20 glProxy = (GL20) Proxy.newProxyInstance(
                GL20.class.getClassLoader(),
                new Class<?>[]{GL20.class},
                (proxy, method, args) -> {
                    if ("glGetShaderiv".equals(method.getName()) || "glGetProgramiv".equals(method.getName())) {
                        if (args != null && args.length >= 3 && args[2] instanceof IntBuffer) {
                            ((IntBuffer) args[2]).put(0, GL20.GL_TRUE);
                        }
                        return null;
                    }
                    if ("glGetIntegerv".equals(method.getName())) {
                        if (args != null && args.length >= 2 && args[1] instanceof IntBuffer) {
                            ((IntBuffer) args[1]).put(0, 4096);
                        }
                        return null;
                    }
                    if ("glCreateShader".equals(method.getName()) || "glCreateProgram".equals(method.getName())) {
                        return 1;
                    }
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(int.class)) return 1;
                    if (method.getReturnType().equals(String.class)) return "";
                    return null;
                }
        );

        Audio audioProxy = (Audio) Proxy.newProxyInstance(
                Audio.class.getClassLoader(),
                new Class<?>[]{Audio.class},
                (proxy, method, args) -> {
                    if ("newMusic".equals(method.getName())) {
                        return Proxy.newProxyInstance(
                                com.badlogic.gdx.audio.Music.class.getClassLoader(),
                                new Class<?>[]{com.badlogic.gdx.audio.Music.class},
                                (p, m, a) -> null
                        );
                    }
                    if ("newSound".equals(method.getName())) {
                        return Proxy.newProxyInstance(
                                com.badlogic.gdx.audio.Sound.class.getClassLoader(),
                                new Class<?>[]{com.badlogic.gdx.audio.Sound.class},
                                (p, m, a) -> null
                        );
                    }
                    return null;
                }
        );

        Gdx.graphics = graphicsProxy;
        Gdx.gl = Gdx.gl20 = glProxy;
        Gdx.audio = audioProxy;
    }

    @Test
    public void testBloonsTouhouDefenseAndMapDisposal() {
        BloonsTouhouDefense game = new BloonsTouhouDefense();
        game.create();

        assertNotNull("Map should be initialized", game.getMap());
        assertEquals("Initial instruction count should be 6", 6, game.instructions.size());

        // Simulate dismissing all instructions
        while (!game.instructions.isEmpty()) {
            game.updateInstructions();
        }

        assertEquals("Instructions should be empty after dismissing all", 0, game.instructions.size());

        // Test calling dispose on game and map
        game.dispose();
    }

    @Test
    public void testMapDisposeStandalone() {
        StageMock stageMock = new StageMock();
        // Create Map and test dispose
        Map map = MapFactory.createHeaterMap(null);
        map.dispose();
    }

    private static class StageMock extends com.badlogic.gdx.scenes.scene2d.Stage {
        public StageMock() {
            super();
        }
    }
}
