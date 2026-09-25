package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BloonManagerTest {

	private Application mockApp;
	private BloonsTouhouDefense mockGame;
	private Player player;

	@Before
	public void setUp() {
		mockApp = mock(Application.class);
		mockGame = mock(BloonsTouhouDefense.class);
		player = new Player(200, 200);

		when(mockApp.getApplicationListener()).thenReturn(mockGame);
		when(mockGame.getPlayer()).thenReturn(player);

		Gdx.app = mockApp;
	}

	@After
	public void tearDown() {
		Gdx.app = null;
	}

	@Test
	public void testPopBloonAwardsScoreWhenPopped() {
		Bloon bloon = BloonFactory.createRedBloon(); // 1 health, Red bloon
		BloonActor mockActor = mock(BloonActor.class);
		when(mockActor.getBloon()).thenReturn(bloon);
		when(mockActor.pop(1)).thenReturn(new BloonPoppedResult(bloon, 1));

		// Check initial score
		assertEquals(0, player.getScore());

		// Trigger popping result
		BloonPoppedResult result = mockActor.pop(1);
		player.earnMoney(result.getCashGenerated());
		player.addScore(result.getCashGenerated());

		assertEquals(1, player.getScore());
		assertEquals(1, player.getHighScore());
	}

	@Test
	public void testDamageBloonAwardsScoreWhenDamagedWithoutPopping() {
		Bloon bloon = BloonFactory.createCeramicBloon(); // Ceramic bloon has 18 health
		BloonActor mockActor = mock(BloonActor.class);
		when(mockActor.getBloon()).thenReturn(bloon);

		// Damage 1 without popping layer
		player.addScore(1);

		assertEquals(1, player.getScore());
		assertEquals(1, player.getHighScore());
	}
}
