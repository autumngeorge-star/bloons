package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ResourceDisposalTest {

	@BeforeClass
	public static void initLibGDX() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new BloonsTouhouDefense() {
			@Override
			public void create() {
				// Don't auto-run full window setup in headless init
			}
		}, config);
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
	}

	@Test
	public void testMusicPlayerDisposable() {
		assertTrue("MusicPlayer must implement Disposable", Disposable.class.isAssignableFrom(MusicPlayer.class));

		MusicPlayer musicPlayer = new MusicPlayer();
		musicPlayer.playTitleMusic();
		musicPlayer.playStageMusic();
		musicPlayer.playFinalBossMusic();
		musicPlayer.stopMusic();
		musicPlayer.dispose();
		// Repeated dispose call should be safe
		musicPlayer.dispose();
	}

	@Test
	public void testMapDisposable() {
		assertTrue("Map must implement Disposable", Disposable.class.isAssignableFrom(Map.class));
	}

	@Test
	public void testBloonManagerDisposable() {
		assertTrue("BloonManager must implement Disposable", Disposable.class.isAssignableFrom(BloonManager.class));
	}

	@Test
	public void testBloonsTouhouDefenseDisposal() {
		BloonsTouhouDefense game = new BloonsTouhouDefense();
		Gdx.app.postRunnable(() -> {
			game.create();
			game.dispose();
		});
	}

}
