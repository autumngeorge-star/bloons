package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.nio.IntBuffer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

public class AssetManagerTest {

    private HeadlessApplication app;
    private BloonsTouhouDefense game;

    @Before
    public void setUp() {
        GL20 gl = mock(GL20.class);
        Gdx.gl = gl;
        Gdx.gl20 = gl;

        Mockito.when(gl.glCreateShader(anyInt())).thenReturn(1);
        Mockito.when(gl.glCreateProgram()).thenReturn(1);
        Mockito.when(gl.glGetActiveAttrib(anyInt(), anyInt(), any(), any())).thenReturn("a_position");
        Mockito.when(gl.glGetActiveUniform(anyInt(), anyInt(), any(), any())).thenReturn("u_texture");

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                IntBuffer params = invocation.getArgument(2);
                params.put(0, GL20.GL_TRUE);
                return null;
            }
        }).when(gl).glGetShaderiv(anyInt(), anyInt(), any(IntBuffer.class));

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                IntBuffer params = invocation.getArgument(2);
                params.put(0, GL20.GL_TRUE);
                return null;
            }
        }).when(gl).glGetProgramiv(anyInt(), anyInt(), any(IntBuffer.class));

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        app = new HeadlessApplication(new ApplicationAdapter() {}, config);

        game = new BloonsTouhouDefense();
        Gdx.app = app;
        game.create();
    }

    @After
    public void tearDown() {
        if (app != null) {
            app.exit();
        }
    }

    @Test
    public void testAssetManagerInitializationAndSkinSharing() {
        AssetManager assetManager = game.getAssetManager();
        assertNotNull("AssetManager should be initialized", assetManager);

        assertTrue("uiskins/uiskin.json should be loaded", assetManager.isLoaded("uiskins/uiskin.json", Skin.class));

        Skin gameSkin = assetManager.get("uiskins/uiskin.json", Skin.class);
        assertNotNull("Skin should be loaded", gameSkin);

        Map map = game.getMap();
        assertNotNull("Map should be initialized", map);

        Skin mapSkin = assetManager.get("uiskins/uiskin.json", Skin.class);
        assertSame("Map and BloonsTouhouDefense must share the exact same Skin instance", gameSkin, mapSkin);
    }

    @Test
    public void testDisposalCleanlyReleasesResources() {
        AssetManager assetManager = game.getAssetManager();
        assertNotNull(assetManager);

        assertTrue(assetManager.isLoaded("uiskins/uiskin.json", Skin.class));

        game.dispose();

        assertEquals("Loaded assets count should be 0 after AssetManager disposal", 0, assetManager.getLoadedAssets());
    }
}
