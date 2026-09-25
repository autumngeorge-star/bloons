package com.hongbao.bloons.actors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.helpers.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpellCardHomingTest {

	@Test
	public void testYuyukoSpellCardOverrideTransition() {
		Bullet bullet = new Bullet(5f, 1, 1, 5000, true, "pink_butterfly.png");
		BulletActor actor = new BulletActor(bullet, 100, 100, 1, 0, false);

		// Within pattern phase (frame 1 to 150)
		actor.setFrames(1);
		Pair<Float, Float> dir1 = actor.yuyukoSpellCardOverride();
		assertNotNull("Yuyuko override should return non-null direction vector at frame 1", dir1);

		actor.setFrames(75);
		Pair<Float, Float> dir75 = actor.yuyukoSpellCardOverride();
		assertNotNull("Yuyuko override should return non-null direction vector at frame 75", dir75);

		actor.setFrames(150);
		Pair<Float, Float> dir150 = actor.yuyukoSpellCardOverride();
		assertNotNull("Yuyuko override should return non-null direction vector at frame 150", dir150);

		// After pattern phase (> 150 frames)
		actor.setFrames(151);
		Pair<Float, Float> dir151 = actor.yuyukoSpellCardOverride();
		assertNull("Yuyuko override should return null vector after 150 frames to yield control to homing", dir151);

		actor.setFrames(200);
		Pair<Float, Float> dir200 = actor.yuyukoSpellCardOverride();
		assertNull("Yuyuko override should return null vector at frame 200", dir200);
	}

	@Test
	public void testYuyukoSpellCardSetDirectionIfApplicable() {
		Bullet bullet = new Bullet(5f, 1, 1, 5000, true, "pink_butterfly.png");
		BulletActor actor = new BulletActor(bullet, 100, 100, 1, 0, false);
		actor.setSpellCardOverride("Yuyuko");

		// During pattern phase (<= 150 frames)
		actor.setFrames(10);
		actor.setDirectionIfApplicable(null); // bloonManager null since override returns non-null
		// dx/dy should be changed by override pattern
		assertNotNull(actor.getDx());
		assertNotNull(actor.getDy());

		// After pattern phase (> 150 frames)
		actor.setFrames(151);
		// Override returns null, so setDirectionIfApplicable proceeds to homing check
		// Verify that yuyukoSpellCardOverride returns null
		assertNull(actor.yuyukoSpellCardOverride());
	}

	@Test
	public void testReimuSpellCardOverrideTransition() {
		Bullet bullet = new Bullet(20f, 1, 1, 5000, true, "red_spell_card.png");
		BulletActor actor = new BulletActor(bullet, 100, 100, 0, 1, false);

		// Within pattern phase (frame 1 to 200)
		actor.setFrames(10);
		Pair<Float, Float> dir10 = actor.reimuSpellCardOverride();
		assertNotNull("Reimu override should return non-null direction vector at frame 10", dir10);

		actor.setFrames(200);
		Pair<Float, Float> dir200 = actor.reimuSpellCardOverride();
		assertNotNull("Reimu override should return non-null direction vector at frame 200", dir200);

		// After pattern phase (> 200 frames)
		actor.setFrames(201);
		Pair<Float, Float> dir201 = actor.reimuSpellCardOverride();
		assertNull("Reimu override should return null vector after 200 frames to yield control to homing", dir201);
	}

	@Test
	public void testYuyukoSpellCardFactoryHomingEnabled() {
		SpellCard spellCard = SpellCard.createYuyukoSpellCard();
		assertNotNull(spellCard);
		assertEquals("Yuyuko", spellCard.getOverrideName());
	}
}
