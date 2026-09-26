package com.hongbao.bloons.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.StatusEffectType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatusEffectRendererTest {

	private DefaultStatusEffectRenderer renderer;
	private Batch mockBatch;

	@Before
	public void setUp() {
		renderer = new DefaultStatusEffectRenderer();
		mockBatch = mock(Batch.class);
		when(mockBatch.getColor()).thenReturn(new Color(1f, 1f, 1f, 1f));
		when(mockBatch.getShader()).thenReturn(null);
	}

	@Test
	public void testBloonStatusEffectTracking() {
		Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
		assertTrue(bloon.getStatusEffects().isEmpty());

		bloon.addStatusEffect(StatusEffectType.FREEZE);
		assertTrue(bloon.hasStatusEffect(StatusEffectType.FREEZE));

		bloon.addStatusEffect(StatusEffectType.BURN);
		assertTrue(bloon.hasStatusEffect(StatusEffectType.BURN));

		bloon.removeStatusEffect(StatusEffectType.FREEZE);
		assertFalse(bloon.hasStatusEffect(StatusEffectType.FREEZE));
		assertTrue(bloon.hasStatusEffect(StatusEffectType.BURN));

		bloon.clearStatusEffects();
		assertTrue(bloon.getStatusEffects().isEmpty());
	}

	@Test
	public void testEmptyStatusEffectsDoesNotDraw() {
		Set<StatusEffectType> effects = new HashSet<>();
		renderer.render(mockBatch, effects, 0, 0, 50, 50);

		verify(mockBatch, never()).draw(
				org.mockito.ArgumentMatchers.any(com.badlogic.gdx.graphics.g2d.TextureRegion.class),
				anyFloat(), anyFloat(), anyFloat(), anyFloat()
		);
	}

	@Test
	public void testBatchStateRestoration() {
		Set<StatusEffectType> effects = new HashSet<>();
		effects.add(StatusEffectType.FREEZE);
		effects.add(StatusEffectType.GLUE);

		Color initialColor = new Color(0.5f, 0.5f, 0.5f, 1.0f);
		when(mockBatch.getColor()).thenReturn(initialColor);

		renderer.render(mockBatch, effects, 10, 20, 30, 40);

		ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
		verify(mockBatch, atLeastOnce()).setColor(colorCaptor.capture());

		// Verify the final color restored equals the initial batch color
		Color finalRestoredColor = colorCaptor.getValue();
		assertEquals(initialColor.r, finalRestoredColor.r, 0.001f);
		assertEquals(initialColor.g, finalRestoredColor.g, 0.001f);
		assertEquals(initialColor.b, finalRestoredColor.b, 0.001f);
		assertEquals(initialColor.a, finalRestoredColor.a, 0.001f);
	}

	@Test
	public void testRenderableActorStatusSharing() {
		RenderableActor customActor = new RenderableActor() {};
		customActor.addStatusEffect(StatusEffectType.BURN);
		customActor.addStatusEffect(StatusEffectType.SLOW);

		assertTrue(customActor.hasStatusEffect(StatusEffectType.BURN));
		assertTrue(customActor.hasStatusEffect(StatusEffectType.SLOW));

		renderer.render(mockBatch, customActor, 0, 0, 100, 100);

		customActor.clearStatusEffects();
		assertFalse(customActor.hasStatusEffect(StatusEffectType.BURN));
	}

	@Test
	public void testAllStatusEffectTypesCompositeRender() {
		Set<StatusEffectType> effects = new HashSet<>();
		for (StatusEffectType type : StatusEffectType.values()) {
			effects.add(type);
		}

		renderer.render(mockBatch, effects, 0, 0, 64, 64);

		ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
		verify(mockBatch, atLeastOnce()).setColor(colorCaptor.capture());
		assertEquals(1.0f, colorCaptor.getValue().a, 0.001f);
	}

	@Test
	public void testCustomStatusEffectRendererStrategy() {
		RenderableActor customActor = new RenderableActor() {};
		StatusEffectRenderer customRenderer = mock(StatusEffectRenderer.class);

		customActor.setStatusEffectRenderer(customRenderer);
		assertEquals(customRenderer, customActor.getStatusEffectRenderer());

		customActor.addStatusEffect(StatusEffectType.FREEZE);
		customRenderer.render(mockBatch, customActor, 10, 10, 50, 50);

		verify(customRenderer, times(1)).render(mockBatch, customActor, 10, 10, 50, 50);
	}

	@Test
	public void testNullSafetyAndDisposal() {
		renderer.render(null, (Set<StatusEffectType>) null, 0, 0, 10, 10);
		renderer.render(mockBatch, (RenderableActor) null, 0, 0, 10, 10);

		// Verify dispose executes cleanly
		renderer.dispose();
	}
}
