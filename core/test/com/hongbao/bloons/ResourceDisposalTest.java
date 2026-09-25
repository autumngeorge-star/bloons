package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Music;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.utils.GdxNativesLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.IntBuffer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResourceDisposalTest {

    private static HeadlessApplication app;

    @BeforeClass
    public static void setUp() {
        GdxNativesLoader.load();
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        app = new HeadlessApplication(new ApplicationAdapter() {}, config);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
        when(Gdx.gl.glCreateShader(anyInt())).thenReturn(1);
        when(Gdx.gl.glCreateProgram()).thenReturn(1);
        when(Gdx.gl.glGetActiveAttrib(anyInt(), anyInt(), any(IntBuffer.class), any(IntBuffer.class))).thenReturn("a_position");
        when(Gdx.gl.glGetActiveUniform(anyInt(), anyInt(), any(IntBuffer.class), any(IntBuffer.class))).thenReturn("u_texture");
        doAnswer(invocation -> {
            IntBuffer buf = invocation.getArgument(2);
            buf.put(0, 1);
            return null;
        }).when(Gdx.gl).glGetShaderiv(anyInt(), anyInt(), any(IntBuffer.class));
        doAnswer(invocation -> {
            IntBuffer buf = invocation.getArgument(2);
            buf.put(0, 1);
            return null;
        }).when(Gdx.gl).glGetProgramiv(anyInt(), anyInt(), any(IntBuffer.class));

        Gdx.audio = mock(Audio.class);
        Music dummyMusic = mock(Music.class);
        when(Gdx.audio.newMusic(any())).thenReturn(dummyMusic);
    }

    @AfterClass
    public static void tearDown() {
        if (app != null) {
            app.exit();
        }
    }

    @Test
    public void testBloonsTouhouDefenseFieldsExist() throws NoSuchFieldException {
        Class<BloonsTouhouDefense> clazz = BloonsTouhouDefense.class;

        assertNotNull(clazz.getDeclaredField("shapeRenderer"));
        assertEquals(ShapeRenderer.class, clazz.getDeclaredField("shapeRenderer").getType());

        assertNotNull(clazz.getDeclaredField("skin"));
        assertEquals(Skin.class, clazz.getDeclaredField("skin").getType());

        assertNotNull(clazz.getDeclaredField("menuBackgroundTexture"));
        assertEquals(Texture.class, clazz.getDeclaredField("menuBackgroundTexture").getType());

        assertNotNull(clazz.getDeclaredField("mapBackgroundTexture"));
        assertEquals(Texture.class, clazz.getDeclaredField("mapBackgroundTexture").getType());

        String[] characterBoxes = {
            "reimuBoxTexture", "yukariBoxTexture", "marisaBoxTexture", "aliceBoxTexture",
            "sakuyaBoxTexture", "remiliaBoxTexture", "youmuBoxTexture", "yuyukoBoxTexture"
        };
        for (String box : characterBoxes) {
            Field field = clazz.getDeclaredField(box);
            assertNotNull("Field " + box + " should exist", field);
            assertEquals(Texture.class, field.getType());
        }

        for (int i = 1; i <= 6; i++) {
            String name = "instructionTexture" + i;
            Field field = clazz.getDeclaredField(name);
            assertNotNull("Field " + name + " should exist", field);
            assertEquals(Texture.class, field.getType());
        }
    }

    @Test
    public void testMapFieldsAndDisposeMethodExist() throws NoSuchFieldException, NoSuchMethodException {
        Class<Map> clazz = Map.class;

        Field skinField = clazz.getDeclaredField("skin");
        assertNotNull(skinField);
        assertEquals(Skin.class, skinField.getType());

        Field uiBgField = clazz.getDeclaredField("uiBackgroundTexture");
        assertNotNull(uiBgField);
        assertEquals(Texture.class, uiBgField.getType());

        Method disposeMethod = clazz.getMethod("dispose");
        assertNotNull(disposeMethod);
    }

    @Test
    public void testLifecycleAndDisposal() throws Exception {
        BloonsTouhouDefense game = new BloonsTouhouDefense();
        game.create();

        // Verify fields are populated after create()
        Field shapeRendererField = BloonsTouhouDefense.class.getDeclaredField("shapeRenderer");
        shapeRendererField.setAccessible(true);
        assertNotNull(shapeRendererField.get(game));

        Field skinField = BloonsTouhouDefense.class.getDeclaredField("skin");
        skinField.setAccessible(true);
        assertNotNull(skinField.get(game));

        Field menuBgField = BloonsTouhouDefense.class.getDeclaredField("menuBackgroundTexture");
        menuBgField.setAccessible(true);
        assertNotNull(menuBgField.get(game));

        Field mapBgField = BloonsTouhouDefense.class.getDeclaredField("mapBackgroundTexture");
        mapBgField.setAccessible(true);
        assertNotNull(mapBgField.get(game));

        Field mapField = BloonsTouhouDefense.class.getDeclaredField("map");
        mapField.setAccessible(true);
        Map map = (Map) mapField.get(game);
        assertNotNull(map);

        Field mapSkinField = Map.class.getDeclaredField("skin");
        mapSkinField.setAccessible(true);
        assertNotNull(mapSkinField.get(map));

        Field mapUiBgField = Map.class.getDeclaredField("uiBackgroundTexture");
        mapUiBgField.setAccessible(true);
        assertNotNull(mapUiBgField.get(map));

        Field stageField = BloonsTouhouDefense.class.getDeclaredField("stage");
        stageField.setAccessible(true);
        stageField.set(game, mock(Stage.class));

        // Call dispose() on the game
        game.dispose();

        // Verify retained disposable fields are set to null after dispose()
        assertNull("shapeRenderer should be nulled after dispose", shapeRendererField.get(game));
        assertNull("skin should be nulled after dispose", skinField.get(game));
        assertNull("menuBackgroundTexture should be nulled after dispose", menuBgField.get(game));
        assertNull("mapBackgroundTexture should be nulled after dispose", mapBgField.get(game));

        assertNull("map skin should be nulled after dispose", mapSkinField.get(map));
        assertNull("map uiBackgroundTexture should be nulled after dispose", mapUiBgField.get(map));

        // Test idempotency: calling dispose again should not throw NPE or exception
        game.dispose();
    }
}
