package com.hongbao.bloons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;

public class ResourceDisposalTest {

    @Test
    public void testMapImplementsDisposable() {
        assertTrue("Map should implement Disposable interface", Disposable.class.isAssignableFrom(Map.class));
    }

    @Test
    public void testMapDisposeReleasesSkinAndTexture() throws Exception {
        Map map = Mockito.mock(Map.class);
        doCallRealMethod().when(map).dispose();

        Skin mockSkin = Mockito.mock(Skin.class);
        Texture mockInfoTex = Mockito.mock(Texture.class);

        Field skinField = Map.class.getDeclaredField("skin");
        skinField.setAccessible(true);
        skinField.set(map, mockSkin);

        Field infoTexField = Map.class.getDeclaredField("infoBackgroundTexture");
        infoTexField.setAccessible(true);
        infoTexField.set(map, mockInfoTex);

        map.dispose();

        verify(mockSkin).dispose();
        verify(mockInfoTex).dispose();
        assertNull(skinField.get(map));
        assertNull(infoTexField.get(map));
    }

    @Test
    public void testBloonsTouhouDefenseUpdateInstructionsDisposesTexture() throws Exception {
        BloonsTouhouDefense game = new BloonsTouhouDefense();

        Texture mockTex1 = Mockito.mock(Texture.class);
        Texture mockTex2 = Mockito.mock(Texture.class);

        List<Texture> texList = new ArrayList<>();
        texList.add(mockTex1);
        texList.add(mockTex2);

        Field texListField = BloonsTouhouDefense.class.getDeclaredField("instructionTextures");
        texListField.setAccessible(true);
        texListField.set(game, texList);

        // Populate dummy button in instructions list
        com.hongbao.bloons.actors.RenderableImageButton dummyBtn1 = Mockito.mock(com.hongbao.bloons.actors.RenderableImageButton.class);
        com.hongbao.bloons.actors.RenderableImageButton dummyBtn2 = Mockito.mock(com.hongbao.bloons.actors.RenderableImageButton.class);
        game.instructions = new ArrayList<>();
        game.instructions.add(dummyBtn1);
        game.instructions.add(dummyBtn2);

        // Invoke updateInstructions via reflection
        java.lang.reflect.Method updateInstructionsMethod = BloonsTouhouDefense.class.getDeclaredMethod("updateInstructions");
        updateInstructionsMethod.setAccessible(true);
        updateInstructionsMethod.invoke(game);

        // Assert first texture was disposed and removed
        verify(mockTex1).dispose();
        assertEquals(1, game.instructionTextures.size());
        assertEquals(mockTex2, game.instructionTextures.get(0));
    }

    @Test
    public void testBloonsTouhouDefenseDisposeReleasesAllUiTexturesAndSkin() throws Exception {
        BloonsTouhouDefense game = new BloonsTouhouDefense();

        Map mockMap = Mockito.mock(Map.class);
        Skin mockSkin = Mockito.mock(Skin.class);
        Texture mockHeader = Mockito.mock(Texture.class);
        Texture mockShopBox = Mockito.mock(Texture.class);
        Texture mockMapBg = Mockito.mock(Texture.class);
        Texture mockRemInst = Mockito.mock(Texture.class);

        Field mapField = BloonsTouhouDefense.class.getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(game, mockMap);

        Field skinField = BloonsTouhouDefense.class.getDeclaredField("skin");
        skinField.setAccessible(true);
        skinField.set(game, mockSkin);

        Field headerField = BloonsTouhouDefense.class.getDeclaredField("headerTexture");
        headerField.setAccessible(true);
        headerField.set(game, mockHeader);

        Field reimuField = BloonsTouhouDefense.class.getDeclaredField("reimuBoxTexture");
        reimuField.setAccessible(true);
        reimuField.set(game, mockShopBox);

        Field mapBgField = BloonsTouhouDefense.class.getDeclaredField("mapBackgroundTexture");
        mapBgField.setAccessible(true);
        mapBgField.set(game, mockMapBg);

        List<Texture> instList = new ArrayList<>();
        instList.add(mockRemInst);
        Field instListField = BloonsTouhouDefense.class.getDeclaredField("instructionTextures");
        instListField.setAccessible(true);
        instListField.set(game, instList);

        game.dispose();

        verify(mockMap).dispose();
        verify(mockSkin).dispose();
        verify(mockHeader).dispose();
        verify(mockShopBox).dispose();
        verify(mockMapBg).dispose();
        verify(mockRemInst).dispose();
        assertTrue(instList.isEmpty());
    }
}
