package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.helpers.ZIndex;

import java.util.List;


public class SpellCardActor extends RenderableActor {
	
	private SpellCard spellCard;
	private float rotationAngle;
	
	public SpellCardActor(SpellCard spellCard, float x, float y) {
		this.spellCard = spellCard;
		BloonsTouhouDefense app = (BloonsTouhouDefense) Gdx.app.getApplicationListener();
		textureRegion = app.getAtlasRegion(spellCard.getImageFileName());
		rotationAngle = 0;
		
		setZIndex(ZIndex.SPELL_CARD_Z_INDEX);
		setBounds(
		 x - textureRegion.getRegionWidth() / 2f,
		 y - textureRegion.getRegionHeight() / 2f,
		 textureRegion.getRegionWidth(),
		 textureRegion.getRegionHeight()
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
		 textureRegion.getRegionWidth(),
		 textureRegion.getRegionHeight(),
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
					 bullet.getInitialDYOverride()
					); // todo the spell needs a direction maybe
					bulletActor.setSpellCardOverride(spellCard.getOverrideName());
					bloonManager.addBulletToStage(bulletActor);
				}
			}
		}
	}
	
}
