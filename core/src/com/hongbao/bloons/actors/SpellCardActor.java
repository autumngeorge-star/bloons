package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.helpers.ZIndex;
import com.hongbao.bloons.services.AssetProviderService;

import java.util.List;


public class SpellCardActor extends RenderableActor {
	
	private SpellCard spellCard;
	private float rotationAngle;
	private AssetProviderService assetProviderService;
	
	public SpellCardActor(SpellCard spellCard, float x, float y, AssetProviderService assetProviderService) {
		this.spellCard = spellCard;
		this.assetProviderService = assetProviderService;
		textureRegion = new TextureRegion(assetProviderService.getSpellCardTexture(spellCard));
		rotationAngle = 0;
		
		setZIndex(ZIndex.SPELL_CARD_Z_INDEX);
		setBounds(
		 x - textureRegion.getTexture().getWidth() / 2f,
		 y - textureRegion.getTexture().getHeight() / 2f,
		 textureRegion.getTexture().getWidth(),
		 textureRegion.getTexture().getHeight()
		);
	}
	
	public SpellCard getSpellCard() {
		return spellCard;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(
		 textureRegion,
		 getX(),
		 getY(),
		 getCenterX() - getX(),
		 getCenterY() - getY(),
		 textureRegion.getTexture().getWidth(),
		 textureRegion.getTexture().getHeight(),
		 spellCard.getScale(),
		 spellCard.getScale(),
		 -rotationAngle
		);
	}
	
	@Override
	public void act(float delta) {
		if (spellCard.isExpired()) {
			remove();
		} else {
			List<Bullet> bulletsToCreate = spellCard.getBulletsToCreateAndIncrementFrame();
			
			if (bulletsToCreate != null) {
				BloonManager bloonManager = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager();
				
				for (Bullet bullet : bulletsToCreate) {
					BulletActor bulletActor = new BulletActor(
					 bullet,
					 getCenterX(),
					 getCenterY(),
					 bullet.getInitialDXOverride(),
					 bullet.getInitialDYOverride(),
					 assetProviderService
					); // todo the spell needs a direction maybe
					bulletActor.setSpellCardOverride(spellCard.getOverrideName());
					bloonManager.addBulletToStage(bulletActor);
				}
			}
		}
	}
	
}
