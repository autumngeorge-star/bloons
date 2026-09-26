package com.hongbao.bloons.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hongbao.bloons.actors.RenderableActor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class GameStageTest {

	@BeforeClass
	public static void initGdx() {
		Graphics graphics = Mockito.mock(Graphics.class);
		Mockito.when(graphics.getWidth()).thenReturn(800);
		Mockito.when(graphics.getHeight()).thenReturn(600);
		Gdx.graphics = graphics;
		Gdx.gl = Mockito.mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
	}

	private GameStage createStage() {
		return new GameStage(Mockito.mock(Viewport.class), Mockito.mock(Batch.class));
	}

	@Test
	public void testInitialStateIsDirty() {
		GameStage stage = createStage();
		Assert.assertTrue("New GameStage should be dirty initially", stage.isDirty());
	}

	@Test
	public void testClearDirtyFlag() {
		GameStage stage = createStage();
		stage.setDirty(false);
		Assert.assertFalse("Dirty flag should be false after setDirty(false)", stage.isDirty());
	}

	@Test
	public void testAddActorMarksDirty() {
		GameStage stage = createStage();
		stage.setDirty(false);
		Assert.assertFalse(stage.isDirty());

		Actor actor = new Actor();
		stage.addActor(actor);
		Assert.assertTrue("Adding actor should mark stage dirty", stage.isDirty());
	}

	@Test
	public void testRemoveActorMarksDirty() {
		GameStage stage = createStage();
		Actor actor = new Actor();
		stage.addActor(actor);
		stage.setDirty(false);
		Assert.assertFalse(stage.isDirty());

		actor.remove();
		Assert.assertTrue("Removing actor should mark stage dirty", stage.isDirty());
	}

	@Test
	public void testRenderableActorSetZIndexMarksDirty() {
		GameStage stage = createStage();
		TestRenderableActor actor = new TestRenderableActor(5);
		stage.addActor(actor);
		stage.setDirty(false);
		Assert.assertFalse(stage.isDirty());

		actor.setZIndex(15);
		Assert.assertTrue("Changing ZIndex of actor on GameStage should mark stage dirty", stage.isDirty());
	}

	private static class TestRenderableActor extends RenderableActor {
		public TestRenderableActor(int zIndex) {
			setZIndex(zIndex);
		}
	}
}
