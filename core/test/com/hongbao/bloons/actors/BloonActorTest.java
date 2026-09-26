package com.hongbao.bloons.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.entities.Bloon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BloonActorTest {

    private Batch mockBatch;
    private TextureRegion mockIceOverlay;
    private TextureRegion mockGlueOverlay;

    @Before
    public void setUp() {
        mockBatch = mock(Batch.class);
        mockIceOverlay = mock(TextureRegion.class);
        mockGlueOverlay = mock(TextureRegion.class);

        BloonActor.setIceOverlayRegion(mockIceOverlay);
        BloonActor.setGlueOverlayRegion(mockGlueOverlay);
    }

    @Test
    public void testDrawNormalBloonRestoresWhite() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actor = new BloonActor(bloon, 100, 100, null);

        actor.draw(mockBatch, 1.0f);

        verify(mockBatch, atLeastOnce()).setColor(Color.WHITE);
        verify(mockBatch, never()).setColor(Color.CYAN);
        verify(mockBatch, never()).setColor(Color.YELLOW);
    }

    @Test
    public void testDrawFrozenBloonAppliesCyanAndRestoresWhite() {
        Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        bloon.setFrozen(true);
        BloonActor actor = new BloonActor(bloon, 100, 100, null);

        actor.draw(mockBatch, 1.0f);

        InOrder inOrder = inOrder(mockBatch);
        inOrder.verify(mockBatch).setColor(Color.CYAN);
        inOrder.verify(mockBatch, atLeastOnce()).setColor(Color.WHITE);

        verify(mockBatch, times(1)).draw(eq(mockIceOverlay), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawSlowedBloonAppliesYellowAndRestoresWhite() {
        Bloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false);
        bloon.setSlowed(true);
        BloonActor actor = new BloonActor(bloon, 100, 100, null);

        actor.draw(mockBatch, 1.0f);

        InOrder inOrder = inOrder(mockBatch);
        inOrder.verify(mockBatch).setColor(Color.YELLOW);
        inOrder.verify(mockBatch, atLeastOnce()).setColor(Color.WHITE);

        verify(mockBatch, times(1)).draw(eq(mockGlueOverlay), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testDrawRevertingBloonRestoresDefault() {
        Bloon bloon = new Bloon(Bloon.Color.YELLOW, 4, false, false);
        bloon.setFrozen(true);
        BloonActor actor = new BloonActor(bloon, 100, 100, null);

        // First draw while frozen
        actor.draw(mockBatch, 1.0f);
        verify(mockBatch).setColor(Color.CYAN);

        reset(mockBatch);

        // Clear frozen status
        bloon.setFrozen(false);
        actor.draw(mockBatch, 1.0f);

        verify(mockBatch, never()).setColor(Color.CYAN);
        verify(mockBatch, never()).setColor(Color.YELLOW);
        verify(mockBatch, atLeastOnce()).setColor(Color.WHITE);
        verify(mockBatch, never()).draw(eq(mockIceOverlay), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }
}
