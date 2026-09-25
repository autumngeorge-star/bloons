package com.hongbao.bloons.actors;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class BulletActorAncestryTest {

	@Test
	public void testParentHitIdentifierAndFrameCounterStored() {
		Bullet bullet = new Bullet(20f, 1, 3, 500f, false, "red_spell_card.png");
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);
		bulletActor.setFrames(1);

		Bloon parentBloon = BloonFactory.createRedBloon();
		BloonActor parentActor = new BloonActor(parentBloon, 100f, 100f, null);

		assertFalse(bulletActor.hasDamagedBloon(parentActor));

		bulletActor.damageBloon(parentActor);

		assertTrue(bulletActor.hasDamagedBloon(parentActor));
	}

	@Test
	public void testChildBloonExemptDuringHitFrame() {
		Bullet bullet = new Bullet(20f, 1, 3, 500f, false, "red_spell_card.png");
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);
		bulletActor.setFrames(1);

		Bloon parentBloon = BloonFactory.createBlueBloon();
		BloonActor parentActor = new BloonActor(parentBloon, 100f, 100f, null);

		// Record parent hit on frame 1
		bulletActor.damageBloon(parentActor);

		// Spawn child bloon inheriting parent ancestry
		Bloon childBloon = BloonFactory.createRedBloon();
		BloonActor childActor = new BloonActor(childBloon, 100f, 100f, parentActor);

		// During hit frame 1, child bloon must be exempt (immune)
		assertTrue(bulletActor.hasDamagedBloon(childActor));
	}

	@Test
	public void testChildBloonSusceptibleAfterExemptionWindow() {
		Bullet bullet = new Bullet(20f, 1, 3, 500f, false, "red_spell_card.png");
		bullet.setExemptionWindow(0);
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);

		// Frame 1: Hit parent
		bulletActor.setFrames(1);
		Bloon parentBloon = BloonFactory.createBlueBloon();
		BloonActor parentActor = new BloonActor(parentBloon, 100f, 100f, null);
		bulletActor.damageBloon(parentActor);
		bulletActor.decrementPierce();
		assertEquals(2, bullet.getPierce());

		Bloon childBloon = BloonFactory.createRedBloon();
		BloonActor childActor = new BloonActor(childBloon, 100f, 100f, parentActor);

		// Frame 1: Child is exempt
		assertTrue(bulletActor.hasDamagedBloon(childActor));

		// Frame 2: Exemption window (0 frames) expires, child becomes susceptible
		bulletActor.setFrames(2);
		assertFalse(bulletActor.hasDamagedBloon(childActor));

		// Hit child on frame 2
		bulletActor.damageBloon(childActor);
		bulletActor.decrementPierce();
		assertEquals(1, bullet.getPierce());
		assertTrue(bulletActor.hasDamagedBloon(childActor));
	}

	@Test
	public void testMultiLayerConsecutiveFrameProgression() {
		Bullet bullet = new Bullet(20f, 1, 3, 500f, false, "red_spell_card.png");
		bullet.setExemptionWindow(0);
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);

		// Frame 1: Hits Green Bloon
		bulletActor.setFrames(1);
		Bloon green = BloonFactory.createGreenBloon();
		BloonActor greenActor = new BloonActor(green, 100f, 100f, null);
		bulletActor.damageBloon(greenActor);
		bulletActor.decrementPierce();
		assertEquals(2, bullet.getPierce());

		Bloon blue = BloonFactory.createBlueBloon();
		BloonActor blueActor = new BloonActor(blue, 100f, 100f, greenActor);

		// Frame 1: Blue child bloon exempt
		assertTrue("Child blue bloon should be exempt on frame 1", bulletActor.hasDamagedBloon(blueActor));

		// Frame 2: Blue child bloon becomes susceptible
		bulletActor.setFrames(2);
		assertFalse("Child blue bloon should be susceptible on frame 2", bulletActor.hasDamagedBloon(blueActor));

		bulletActor.damageBloon(blueActor);
		bulletActor.decrementPierce();
		assertEquals(1, bullet.getPierce());

		Bloon red = BloonFactory.createRedBloon();
		BloonActor redActor = new BloonActor(red, 100f, 100f, blueActor);

		// Frame 2: Red grandchild bloon exempt
		assertTrue("Grandchild red bloon should be exempt on frame 2", bulletActor.hasDamagedBloon(redActor));

		// Frame 3: Red grandchild bloon becomes susceptible
		bulletActor.setFrames(3);
		assertFalse("Grandchild red bloon should be susceptible on frame 3", bulletActor.hasDamagedBloon(redActor));

		bulletActor.damageBloon(redActor);
		bulletActor.decrementPierce();
		assertEquals(0, bullet.getPierce());
	}

	@Test
	public void testExemptionWindowConfigurationAndBounds() {
		Bullet bullet = new Bullet(20f, 1, 3, 500f, false, "red_spell_card.png");

		// Configurable exemption window = 1 frame
		bullet.setExemptionWindow(1);
		assertEquals(1, bullet.getExemptionWindow());

		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);
		assertEquals(1, bulletActor.getExemptionWindow());

		bulletActor.setFrames(1);
		Bloon parent = BloonFactory.createBlueBloon();
		BloonActor parentActor = new BloonActor(parent, 100f, 100f, null);
		bulletActor.damageBloon(parentActor);

		Bloon child = BloonFactory.createRedBloon();
		BloonActor childActor = new BloonActor(child, 100f, 100f, parentActor);

		// Frame 1: Exempt (frameDelta = 0 <= 1)
		assertTrue(bulletActor.hasDamagedBloon(childActor));

		// Frame 2: Still exempt for 1-frame window (frameDelta = 1 <= 1)
		bulletActor.setFrames(2);
		assertTrue(bulletActor.hasDamagedBloon(childActor));

		// Frame 3: Exemption window expired (frameDelta = 2 > 1)
		bulletActor.setFrames(3);
		assertFalse(bulletActor.hasDamagedBloon(childActor));

		// Test bounds enforcement (0 <= exemptionWindow <= 2)
		bullet.setExemptionWindow(5);
		assertEquals(2, bullet.getExemptionWindow());

		bullet.setExemptionWindow(-3);
		assertEquals(0, bullet.getExemptionWindow());

		bulletActor.setExemptionWindow(10);
		assertEquals(2, bulletActor.getExemptionWindow());

		bulletActor.setExemptionWindow(-1);
		assertEquals(0, bulletActor.getExemptionWindow());
	}

	@Test
	public void testMemoryClearedOnProjectileExpiry() {
		Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
		BulletActor bulletActor = new BulletActor(bullet, 100f, 100f, 1f, 0f);
		bulletActor.setFrames(1);

		Bloon parent = BloonFactory.createRedBloon();
		BloonActor parentActor = new BloonActor(parent, 100f, 100f, null);

		bulletActor.damageBloon(parentActor);
		assertTrue(bulletActor.hasDamagedBloon(parentActor));

		// Remove projectile
		bulletActor.remove();

		// Hit tracking memory cleared upon remove
		assertFalse(bulletActor.hasDamagedBloon(parentActor));
	}
}
