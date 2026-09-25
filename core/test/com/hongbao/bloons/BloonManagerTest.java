package com.hongbao.bloons;

import org.junit.Assert;
import org.junit.Test;

public class BloonManagerTest {

	static class TestAudioService implements AudioService {
		boolean popSoundPlayed = false;
		boolean damageSoundPlayed = false;
		boolean disposed = false;

		@Override
		public void playPopSound() {
			popSoundPlayed = true;
		}

		@Override
		public void playDamageSound() {
			damageSoundPlayed = true;
		}

		@Override
		public void dispose() {
			disposed = true;
		}
	}

	@Test
	public void testBloonManagerInstantiationWithNullAudioService() {
		NullAudioService nullAudioService = new NullAudioService();
		BloonManager bloonManager = new BloonManager(null, null, nullAudioService);
		Assert.assertNotNull(bloonManager);
		Assert.assertEquals(0, bloonManager.getLevel());
	}

	@Test
	public void testNullAudioServiceNoOps() {
		NullAudioService nullAudioService = new NullAudioService();
		// Ensure no exceptions are thrown when methods are called in headless mode
		nullAudioService.playPopSound();
		nullAudioService.playDamageSound();
		nullAudioService.dispose();
	}

	@Test
	public void testLibGdxAudioServiceHeadlessResilience() {
		LibGdxAudioService libGdxAudioService = new LibGdxAudioService();
		// Ensure methods handle null Gdx context safely
		libGdxAudioService.playPopSound();
		libGdxAudioService.playDamageSound();
		libGdxAudioService.dispose();
	}

	@Test
	public void testAudioServiceDelegation() {
		TestAudioService testAudioService = new TestAudioService();
		BloonManager bloonManager = new BloonManager(null, null, testAudioService);

		Assert.assertFalse(testAudioService.popSoundPlayed);
		testAudioService.playPopSound();
		Assert.assertTrue(testAudioService.popSoundPlayed);

		Assert.assertFalse(testAudioService.damageSoundPlayed);
		testAudioService.playDamageSound();
		Assert.assertTrue(testAudioService.damageSoundPlayed);

		Assert.assertFalse(testAudioService.disposed);
		testAudioService.dispose();
		Assert.assertTrue(testAudioService.disposed);
	}
}
