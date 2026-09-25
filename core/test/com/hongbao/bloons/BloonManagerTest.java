package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.mock.audio.MockAudio;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class BloonManagerTest {

	private static class MockSoundListener implements SoundListener {
		int poppedCount = 0;
		int damagedCount = 0;

		@Override
		public void onBloonPopped() {
			poppedCount++;
		}

		@Override
		public void onBloonDamaged() {
			damagedCount++;
		}
	}

	@BeforeClass
	public static void setUpClass() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new ApplicationAdapter() {}, config);
	}

	@Test
	public void testNoAudioDriverLoaded() {
		// Verify MockAudio is used in headless mode instead of a native OpenAL audio driver
		assertTrue("Gdx.audio should be MockAudio instance in headless mode", Gdx.audio instanceof MockAudio);
	}

	@Test
	public void testPopBloonTriggersOnBloonPopped() {
		MockSoundListener mockListener = new MockSoundListener();
		BloonManager bloonManager = new BloonManager(null, null, mockListener);

		Bloon redBloon = BloonFactory.createRedBloon();
		BloonActor bloonActor = new BloonActor(redBloon, 100, 100, null);
		bloonManager.getOnstageBloons().add(bloonActor);

		// Red bloon pops from 1 damage
		bloonManager.popBloon(bloonActor, 1);

		assertEquals("onBloonPopped should be called once", 1, mockListener.poppedCount);
		assertEquals("onBloonDamaged should not be called", 0, mockListener.damagedCount);
		assertFalse("Popped bloon should be removed from onstage bloons", bloonManager.getOnstageBloons().contains(bloonActor));
	}

	@Test
	public void testDamageBloonTriggersOnBloonDamaged() {
		MockSoundListener mockListener = new MockSoundListener();
		BloonManager bloonManager = new BloonManager(null, null);
		bloonManager.addSoundListener(mockListener);

		// Ceramic bloon takes 10+ damage to pop, so 1 damage damages it without popping
		Bloon ceramicBloon = BloonFactory.createCeramicBloon();
		BloonActor bloonActor = new BloonActor(ceramicBloon, 100, 100, null);
		bloonManager.getOnstageBloons().add(bloonActor);

		bloonManager.popBloon(bloonActor, 1);

		assertEquals("onBloonDamaged should be called once", 1, mockListener.damagedCount);
		assertEquals("onBloonPopped should not be called", 0, mockListener.poppedCount);
	}

	@Test
	public void testCollisionTriggersSoundListener() {
		MockSoundListener mockListener = new MockSoundListener();
		BloonManager bloonManager = new BloonManager(null, null, mockListener);

		Bloon redBloon = BloonFactory.createRedBloon();
		BloonActor bloonActor = new BloonActor(redBloon, 100, 100, null);
		bloonManager.getOnstageBloons().add(bloonActor);

		Bullet bullet = new Bullet(10f, 1, 1, 100f, false, "purple_energy.png");
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);

		bloonManager.checkCollision(bulletActor);

		assertEquals("Collision triggering a pop should call onBloonPopped", 1, mockListener.poppedCount);
	}

	@Test
	public void testRemoveSoundListener() {
		MockSoundListener mockListener = new MockSoundListener();
		BloonManager bloonManager = new BloonManager(null, null, mockListener);
		bloonManager.removeSoundListener(mockListener);

		Bloon redBloon = BloonFactory.createRedBloon();
		BloonActor bloonActor = new BloonActor(redBloon, 100, 100, null);
		bloonManager.getOnstageBloons().add(bloonActor);

		bloonManager.popBloon(bloonActor, 1);

		assertEquals("Removed listener should not receive event", 0, mockListener.poppedCount);
	}

}
