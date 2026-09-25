package com.hongbao.bloons.services;

import com.badlogic.gdx.assets.AssetManager;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AssetProviderServiceTest {

	private AtomicBoolean disposed;
	private AssetManager stubAssetManager;
	private LibGdxAssetProviderService service;

	@Before
	public void setUp() {
		disposed = new AtomicBoolean(false);
		stubAssetManager = new AssetManager() {
			@Override
			public synchronized void dispose() {
				disposed.set(true);
				super.dispose();
			}
		};
		service = new LibGdxAssetProviderService(stubAssetManager);
	}

	@Test
	public void testDisposal() {
		service.dispose();
		assertTrue("Disposing AssetProviderService should dispose underlying AssetManager", disposed.get());
	}

	@Test
	public void testEntityFileNameMapping() {
		Bloon redBloon = BloonFactory.createRedBloon();
		assertEquals("img/bloons/red_bloon.png", redBloon.getImageFileName());

		Girl reimu = GirlFactory.createReimu();
		assertEquals("img/characters/reimu.png", reimu.getImageFileName());

		Bullet bullet = reimu.createBullet();
		assertEquals("img/projectiles/red_spell_card.png", bullet.getImageFileName());

		SpellCard spellCard = SpellCard.createReimuSpellCard();
		assertEquals("img/spellcards/reimu_spell.png", spellCard.getImageFileName());
	}
}
