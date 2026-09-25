package com.hongbao.bloons.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class BloonActorTest {

    private static class TestableBloonActorHelper {
        public static BloonActor create(Bloon bloon, float x, float y) {
            return new BloonActor(bloon, x, y, null) {
                @Override
                protected TextureRegion loadTextureRegion(String imageFileName) {
                    Texture mockTexture = Mockito.mock(Texture.class);
                    Mockito.when(mockTexture.getWidth()).thenReturn(80);
                    Mockito.when(mockTexture.getHeight()).thenReturn(80);

                    TextureRegion mockRegion = Mockito.mock(TextureRegion.class);
                    Mockito.when(mockRegion.getTexture()).thenReturn(mockTexture);
                    return mockRegion;
                }
            };
        }
    }

    @Test
    public void testBloonActorDamageUpdatesTextureBoundsAndCollisionRadius() {
        Bloon ceramic = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
        BloonActor actor = TestableBloonActorHelper.create(ceramic, 100f, 100f);

        float initialCenterX = actor.getCenterX();
        float initialCenterY = actor.getCenterY();

        assertEquals(100f, initialCenterX, 0.01f);
        assertEquals(100f, initialCenterY, 0.01f);
        assertEquals(20f, actor.getCollisionRadius(), 0.01f); // 80 * SCALE(0.5) / 2

        // Apply non-popping damage
        actor.damage(1);

        assertEquals(17, actor.getBloon().getHealth());
        assertEquals(100f, actor.getCenterX(), 0.01f);
        assertEquals(100f, actor.getCenterY(), 0.01f);
        assertEquals(20f, actor.getCollisionRadius(), 0.01f);
        assertEquals(40f, actor.getWidth(), 0.01f);
        assertEquals(40f, actor.getHeight(), 0.01f);
    }
}
