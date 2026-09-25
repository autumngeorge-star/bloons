package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ScorePersistenceServiceTest {

	private Application mockApp;
	private Preferences mockPrefs;
	private Map<String, Object> storage;

	@Before
	public void setUp() {
		mockApp = mock(Application.class);
		mockPrefs = mock(Preferences.class);
		storage = new HashMap<>();

		when(mockApp.getPreferences(ScorePersistenceService.PREFERENCE_NAME)).thenReturn(mockPrefs);

		when(mockPrefs.contains(anyString())).thenAnswer(invocation -> storage.containsKey(invocation.getArgument(0)));
		when(mockPrefs.getInteger(anyString(), anyInt())).thenAnswer(invocation -> {
			String key = invocation.getArgument(0);
			int def = invocation.getArgument(1);
			return storage.containsKey(key) ? (Integer) storage.get(key) : def;
		});
		doAnswer(invocation -> {
			String key = invocation.getArgument(0);
			int val = invocation.getArgument(1);
			storage.put(key, val);
			return mockPrefs;
		}).when(mockPrefs).putInteger(anyString(), anyInt());

		Gdx.app = mockApp;
	}

	@After
	public void tearDown() {
		Gdx.app = null;
	}

	@Test
	public void testGetHighScoreDefaultsToZero() {
		ScorePersistenceService service = new ScorePersistenceService();
		assertEquals(0, service.getHighScore());
	}

	@Test
	public void testSaveAndGetHighScore() {
		ScorePersistenceService service = new ScorePersistenceService();
		service.saveHighScore(150);

		assertEquals(150, service.getHighScore());
		verify(mockPrefs, atLeastOnce()).flush();
	}

	@Test
	public void testGracefulFallbackWhenGdxAppIsNull() {
		Gdx.app = null;
		ScorePersistenceService service = new ScorePersistenceService();

		assertEquals(0, service.getHighScore());

		service.saveHighScore(300);
		assertEquals(300, service.getHighScore());
	}
}
