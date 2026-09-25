package com.hongbao.bloons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class BloonManagerTest {

    @Test
    public void testNonPoppingDamageUpdatesBloonActorAndDomainState() {
        Bloon ceramic = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
        BloonActor actor = new BloonActor(ceramic, 200f, 200f, null) {
            @Override
            protected TextureRegion loadTextureRegion(String imageFileName) {
                Texture mockTexture = Mockito.mock(Texture.class);
                Mockito.when(mockTexture.getWidth()).thenReturn(60);
                Mockito.when(mockTexture.getHeight()).thenReturn(60);

                TextureRegion mockRegion = Mockito.mock(TextureRegion.class);
                Mockito.when(mockRegion.getTexture()).thenReturn(mockTexture);
                return mockRegion;
            }
        };

        // Initially health is 18
        assertFalse(actor.getBloon().willPopBloon(1));

        // Apply non-popping damage directly to actor (as BloonManager does)
        actor.damage(1);

        assertEquals(17, actor.getBloon().getHealth());
        assertEquals(Bloon.Color.CERAMIC, actor.getBloon().getColor());
        assertEquals(7, actor.getBloon().getSpeed());
        assertEquals("img/bloons/ceramic_bloon.png", actor.getBloon().getImageFileName());
        assertEquals(200f, actor.getCenterX(), 0.01f);
        assertEquals(200f, actor.getCenterY(), 0.01f);
        assertEquals(15f, actor.getCollisionRadius(), 0.01f); // 60 * 0.5 / 2
    }
}
