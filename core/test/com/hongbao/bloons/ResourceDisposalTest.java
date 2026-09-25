package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ResourceDisposalTest {

    @BeforeClass
    public static void initHeadless() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, config);
        GL20 gl = mock(GL20.class);
        org.mockito.Mockito.when(gl.glCreateShader(org.mockito.ArgumentMatchers.anyInt())).thenReturn(1);
        org.mockito.Mockito.when(gl.glCreateProgram()).thenReturn(1);
        org.mockito.Mockito.doAnswer(invocation -> {
            java.nio.IntBuffer params = invocation.getArgument(2);
            params.put(0, 1);
            return null;
        }).when(gl).glGetShaderiv(org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.any(java.nio.IntBuffer.class));
        org.mockito.Mockito.doAnswer(invocation -> {
            int pname = invocation.getArgument(1);
            java.nio.IntBuffer params = invocation.getArgument(2);
            if (pname == GL20.GL_LINK_STATUS) {
                params.put(0, 1);
            } else {
                params.put(0, 0);
            }
            return null;
        }).when(gl).glGetProgramiv(org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.any(java.nio.IntBuffer.class));
        org.mockito.Mockito.when(gl.glGenTexture()).thenReturn(1);
        Gdx.gl = gl;
        Gdx.gl20 = gl;
    }

    @Test
    public void testBloonsTouhouDefenseResourceDisposal() throws Exception {
        for (int cycle = 0; cycle < 3; cycle++) {
            BloonsTouhouDefense app = new BloonsTouhouDefense();
            app.create();

            // Verify fields are populated in BloonsTouhouDefense
            Field shapeRendererField = BloonsTouhouDefense.class.getDeclaredField("shapeRenderer");
            shapeRendererField.setAccessible(true);
            assertNotNull("shapeRenderer should be stored", shapeRendererField.get(app));

            Field skinField = BloonsTouhouDefense.class.getDeclaredField("skin");
            skinField.setAccessible(true);
            assertNotNull("skin should be stored", skinField.get(app));

            Field mapBackgroundTextureField = BloonsTouhouDefense.class.getDeclaredField("mapBackgroundTexture");
            mapBackgroundTextureField.setAccessible(true);
            assertNotNull("mapBackgroundTexture should be stored", mapBackgroundTextureField.get(app));

            Field headerTextureField = BloonsTouhouDefense.class.getDeclaredField("headerTexture");
            headerTextureField.setAccessible(true);
            assertNotNull("headerTexture should be stored", headerTextureField.get(app));

            Field instructionTexturesField = BloonsTouhouDefense.class.getDeclaredField("instructionTextures");
            instructionTexturesField.setAccessible(true);
            List<?> instructionTextures = (List<?>) instructionTexturesField.get(app);
            assertEquals("6 instruction textures should be stored", 6, instructionTextures.size());

            Field menuTexturesField = BloonsTouhouDefense.class.getDeclaredField("menuTextures");
            menuTexturesField.setAccessible(true);
            List<?> menuTextures = (List<?>) menuTexturesField.get(app);
            assertEquals("8 menu character textures should be stored", 8, menuTextures.size());

            // Verify Map resources
            Map map = app.getMap();
            assertNotNull("Map should be instantiated", map);

            Field mapSkinField = Map.class.getDeclaredField("skin");
            mapSkinField.setAccessible(true);
            assertNotNull("Map skin should be stored", mapSkinField.get(map));

            Field infoBackgroundTextureField = Map.class.getDeclaredField("infoBackgroundTexture");
            infoBackgroundTextureField.setAccessible(true);
            assertNotNull("Map infoBackgroundTexture should be stored", infoBackgroundTextureField.get(map));

            // Call dispose and verify cleanup
            app.dispose();

            assertNull("Map skin should be null after disposal", mapSkinField.get(map));
            assertNull("Map infoBackgroundTexture should be null after disposal", infoBackgroundTextureField.get(map));
            assertEquals("instructionTextures list should be empty after disposal", 0, instructionTextures.size());
            assertEquals("menuTextures list should be empty after disposal", 0, menuTextures.size());
        }
    }
}
