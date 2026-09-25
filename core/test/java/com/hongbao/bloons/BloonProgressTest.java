package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BloonProgressTest {

	@BeforeClass
	public static void setUpGdx() {
		Gdx.app = mock(Application.class);
		Gdx.audio = mock(Audio.class);
		Gdx.files = mock(Files.class);
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;

		FileHandle mockFileHandle = mock(FileHandle.class);
		when(mockFileHandle.readString()).thenReturn("1 100 red\nEND");
		when(Gdx.files.internal(anyString())).thenReturn(mockFileHandle);

		Sound mockSound = mock(Sound.class);
		when(Gdx.audio.newSound(any(FileHandle.class))).thenReturn(mockSound);
	}

	@Test
	public void testBloonDistanceAccumulationWithSpatialDisplacement() {
		Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
		assertEquals(0f, bloon.getDistanceTravelled(), 0.0001f);

		// Moving along horizontal vector (1.0, 0.0) with speed 5 -> dx = 1.0, dy = 0.0
		float dx = 1.0f * bloon.getSpeed() / 5f;
		float dy = 0.0f * bloon.getSpeed() / 5f;
		float displacement = (float) Math.sqrt(dx * dx + dy * dy);

		bloon.incrementDistanceTravelled(displacement);
		assertEquals(1.0f, bloon.getDistanceTravelled(), 0.0001f);

		// Moving along diagonal vector (0.7071, 0.7071)
		float dxDiag = 0.70710678f * bloon.getSpeed() / 5f;
		float dyDiag = 0.70710678f * bloon.getSpeed() / 5f;
		float displacementDiag = (float) Math.sqrt(dxDiag * dxDiag + dyDiag * dyDiag);

		bloon.incrementDistanceTravelled(displacementDiag);
		assertEquals(2.0f, bloon.getDistanceTravelled(), 0.001f);
	}

	@Test
	public void testChildBloonInheritsParentDistanceTravelled() {
		// Create a parent bloon (Blue bloon, health 2) and set accumulated distance
		Bloon parentBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
		parentBloon.setDistanceTravelled(150.75f);

		// Pop parent with 1 damage -> generates 1 Red bloon child
		BloonPoppedResult result = new BloonPoppedResult(parentBloon, 1);
		Set<Bloon> children = result.getBloonsGenerated();

		assertEquals(1, children.size());
		Bloon child = children.iterator().next();
		assertEquals(150.75f, child.getDistanceTravelled(), 0.0001f);
	}

	@Test
	public void testChildBloonInheritsDistanceForHigherTierPops() {
		// Create a Ceramic bloon (health 10) and set accumulated distance
		Bloon parentBloon = new Bloon(Bloon.Color.CERAMIC, 10, false, false);
		parentBloon.setDistanceTravelled(320.5f);

		// Pop parent bloon into lower health tier
		BloonPoppedResult result = new BloonPoppedResult(parentBloon, 2);
		Set<Bloon> children = result.getBloonsGenerated();

		assertTrue(children.size() > 0);
		for (Bloon child : children) {
			assertEquals(320.5f, child.getDistanceTravelled(), 0.0001f);
		}
	}

	@Test
	public void testTowerTargetingSelectsBloonWithHighestTrackProgress() {
		Stage mockStage = mock(Stage.class);
		Map mockMap = mock(Map.class);
		BloonManager manager = new BloonManager(mockStage, mockMap);

		// Create two bloons within range of a tower
		Bloon bloon1 = new Bloon(Bloon.Color.RED, 1, false, false);
		bloon1.setDistanceTravelled(50.0f); // Traveled less

		Bloon bloon2 = new Bloon(Bloon.Color.RED, 1, false, false);
		bloon2.setDistanceTravelled(200.0f); // Traveled more (leading bloon)

		// Create mock actors for targeting test
		BloonActor actor1 = mock(BloonActor.class);
		when(actor1.getBloon()).thenReturn(bloon1);
		when(actor1.getCollisionRadius()).thenReturn(10f);
		when(actor1.getCenterX()).thenReturn(100f);
		when(actor1.getCenterY()).thenReturn(100f);

		BloonActor actor2 = mock(BloonActor.class);
		when(actor2.getBloon()).thenReturn(bloon2);
		when(actor2.getCollisionRadius()).thenReturn(10f);
		when(actor2.getCenterX()).thenReturn(105f);
		when(actor2.getCenterY()).thenReturn(105f);

		manager.addOnstageBloon(actor1);
		manager.addOnstageBloon(actor2);

		GirlActor girlActor = mock(GirlActor.class);
		Girl girl = mock(Girl.class);
		when(girlActor.getGirl()).thenReturn(girl);
		when(girl.getVisualRange()).thenReturn(500f);
		when(girlActor.getCenterX()).thenReturn(100f);
		when(girlActor.getCenterY()).thenReturn(100f);

		boolean attacked = manager.attackBloonIfInRange(girlActor);
		assertTrue(attacked);

		// Verify that createBulletActor was called with actor2 (the leading bloon with progress 200.0f)
		org.mockito.Mockito.verify(girlActor).createBulletActor(actor2);
	}

	@Test
	public void testLookAtBloonSelectsBloonWithHighestTrackProgress() {
		Stage mockStage = mock(Stage.class);
		Map mockMap = mock(Map.class);
		BloonManager manager = new BloonManager(mockStage, mockMap);

		Bloon bloon1 = new Bloon(Bloon.Color.GREEN, 3, false, false);
		bloon1.setDistanceTravelled(80.0f);

		Bloon bloon2 = new Bloon(Bloon.Color.GREEN, 3, false, false);
		bloon2.setDistanceTravelled(250.0f);

		BloonActor actor1 = mock(BloonActor.class);
		when(actor1.getBloon()).thenReturn(bloon1);
		when(actor1.getCollisionRadius()).thenReturn(10f);
		when(actor1.getCenterX()).thenReturn(100f);
		when(actor1.getCenterY()).thenReturn(100f);

		BloonActor actor2 = mock(BloonActor.class);
		when(actor2.getBloon()).thenReturn(bloon2);
		when(actor2.getCollisionRadius()).thenReturn(10f);
		when(actor2.getCenterX()).thenReturn(120f);
		when(actor2.getCenterY()).thenReturn(120f);

		manager.addOnstageBloon(actor1);
		manager.addOnstageBloon(actor2);

		GirlActor girlActor = mock(GirlActor.class);
		Girl girl = mock(Girl.class);
		when(girlActor.getGirl()).thenReturn(girl);
		when(girl.getVisualRange()).thenReturn(500f);
		when(girlActor.getCenterX()).thenReturn(100f);
		when(girlActor.getCenterY()).thenReturn(100f);

		manager.lookAtBloon(girlActor);
		org.mockito.Mockito.verify(girlActor).lookAtBloon(actor2);
	}

}
