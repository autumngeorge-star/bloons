package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BloonManagerTest {

	@BeforeClass
	public static void initGdx() {
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
		Gdx.audio = mock(Audio.class);
		Sound mockSound = mock(Sound.class);
		when(Gdx.audio.newSound(any(FileHandle.class))).thenReturn(mockSound);

		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		BloonsTouhouDefense game = mock(BloonsTouhouDefense.class);
		Player player = new Player();
		when(game.getPlayer()).thenReturn(player);

		new HeadlessApplication(game, config);
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
		Gdx.audio = mock(Audio.class);
		when(Gdx.audio.newSound(any(FileHandle.class))).thenReturn(mockSound);
	}

	@Test
	public void testChildBloonSpatialSpacing() {
		Stage mockStage = mock(Stage.class);
		Map map = mock(Map.class);
		when(map.getDirection(anyFloat(), anyFloat())).thenReturn(new Pair<>(1f, 0f));

		BloonManager bloonManager = new BloonManager(mockStage, map);

		Bloon rainbow = BloonFactory.createRainbowBloon();
		BloonActor parentActor = new BloonActor(rainbow, 500f, 400f, null);

		// Pop rainbow with 1 damage -> spawns 2 Zebra bloons
		bloonManager.popBloon(parentActor, 1);

		ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
		verify(mockStage, atLeastOnce()).addActor(captor.capture());

		List<Actor> added = captor.getAllValues();
		assertEquals(2, added.size());

		BloonActor child0 = (BloonActor) added.get(0);
		BloonActor child1 = (BloonActor) added.get(1);

		float dist = (float) Math.sqrt(
				Math.pow(child0.getCenterX() - child1.getCenterX(), 2) +
				Math.pow(child0.getCenterY() - child1.getCenterY(), 2)
		);

		// Distance between spawned children must be at least 16 pixels along track preventing overlap
		assertTrue("Child bloons should have distinct spatial separation along track (dist=" + dist + ")", dist >= 16f);
	}
}
