package com.hongbao.bloons;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PlayerTest {

	private ScorePersistenceService mockService;
	private Player player;

	@Before
	public void setUp() {
		mockService = mock(ScorePersistenceService.class);
		when(mockService.getHighScore()).thenReturn(100);

		player = new Player(200, 200);
		player.setScorePersistenceService(mockService);
	}

	@Test
	public void testInitialState() {
		assertEquals(0, player.getScore());
		assertEquals(100, player.getHighScore());
	}

	@Test
	public void testAddScoreBelowHighScoreDoesNotUpdateHighScore() {
		player.addScore(50);
		assertEquals(50, player.getScore());
		assertEquals(100, player.getHighScore());
		verify(mockService, never()).saveHighScore(anyInt());
	}

	@Test
	public void testAddScoreSurpassingHighScoreUpdatesHighScoreAndSaves() {
		player.addScore(150);
		assertEquals(150, player.getScore());
		assertEquals(150, player.getHighScore());
		verify(mockService).saveHighScore(150);
	}

	@Test
	public void testSetScoreUpdatesHighScoreWhenExceeded() {
		player.setScore(250);
		assertEquals(250, player.getScore());
		assertEquals(250, player.getHighScore());
		verify(mockService).saveHighScore(250);
	}

	@Test
	public void testSetHighScoreDirectly() {
		player.setHighScore(500);
		assertEquals(500, player.getHighScore());
		verify(mockService).saveHighScore(500);
	}
}
