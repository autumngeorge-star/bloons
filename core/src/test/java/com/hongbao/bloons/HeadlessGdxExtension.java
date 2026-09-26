package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;

import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Reusable JUnit 5 extension that initializes a headless LibGDX application harness
 * for running domain and actor unit tests without a display hardware context.
 */
public class HeadlessGdxExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

    private static HeadlessApplication app;
    private static BloonsTouhouDefense mockAppListener;
    private static Player testPlayer;
    private static MusicPlayer mockMusicPlayer;
    private static Map currentMap;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            mockAppListener = Mockito.mock(BloonsTouhouDefense.class);
            testPlayer = new Player(BloonsTouhouDefense.MONEY, BloonsTouhouDefense.HEALTH);
            mockMusicPlayer = Mockito.mock(MusicPlayer.class);

            Mockito.lenient().when(mockAppListener.getPlayer()).thenAnswer(invocation -> testPlayer);
            Mockito.lenient().when(mockAppListener.getMusicPlayer()).thenAnswer(invocation -> mockMusicPlayer);
            Mockito.lenient().when(mockAppListener.getMap()).thenAnswer(invocation -> currentMap);
            mockAppListener.instructions = new ArrayList<>();

            app = new HeadlessApplication(mockAppListener, config);
        }

        if (Gdx.gl == null) {
            GL20 gl = Mockito.mock(GL20.class);

            Mockito.lenient().when(gl.glCreateShader(Mockito.anyInt())).thenReturn(1);
            Mockito.lenient().when(gl.glCreateProgram()).thenReturn(1);

            Mockito.lenient().doAnswer(invocation -> {
                int pname = invocation.getArgument(1);
                IntBuffer buffer = invocation.getArgument(2);
                if (buffer != null) {
                    if (pname == GL20.GL_COMPILE_STATUS) {
                        buffer.put(0, 1);
                    } else {
                        buffer.put(0, 0);
                    }
                }
                return null;
            }).when(gl).glGetShaderiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(IntBuffer.class));

            Mockito.lenient().doAnswer(invocation -> {
                int pname = invocation.getArgument(1);
                IntBuffer buffer = invocation.getArgument(2);
                if (buffer != null) {
                    if (pname == GL20.GL_LINK_STATUS || pname == GL20.GL_COMPILE_STATUS) {
                        buffer.put(0, 1);
                    } else {
                        buffer.put(0, 0);
                    }
                }
                return null;
            }).when(gl).glGetProgramiv(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(IntBuffer.class));

            Gdx.gl = gl;
            Gdx.gl20 = gl;
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (mockAppListener != null) {
            testPlayer = new Player(BloonsTouhouDefense.MONEY, BloonsTouhouDefense.HEALTH);
            currentMap = null;
            if (mockAppListener.instructions == null) {
                mockAppListener.instructions = new ArrayList<>();
            } else {
                mockAppListener.instructions.clear();
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // Native cleanup at end of test execution
    }

    public static HeadlessApplication getApp() {
        return app;
    }

    public static BloonsTouhouDefense getMockAppListener() {
        return mockAppListener;
    }

    public static void setCurrentMap(Map map) {
        currentMap = map;
    }

    public static void setTestPlayer(Player player) {
        testPlayer = player;
    }
}
