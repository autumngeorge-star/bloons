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
	
	public SpellCardActor(SpellCard spellCard, float x, float y, float rotationAngle) {
		this.spellCard = spellCard;
		this.rotationAngle = rotationAngle;
		textureRegion = new TextureRegion(new Texture(Gdx.files.internal(spellCard.getImageFileName())));
		
		setZIndex(ZIndex.SPELL_CARD_Z_INDEX);
		setBounds(
		 x - textureRegion.getTexture().getWidth() / 2f,
		 y - textureRegion.getTexture().getHeight() / 2f,
		 textureRegion.getTexture().getWidth(),
		 textureRegion.getTexture().getHeight()
		);
	}

	public SpellCardActor(SpellCard spellCard, float x, float y) {
		this(spellCard, x, y, 0f);
	}
	
	public SpellCard getSpellCard() {
		return spellCard;
	}

	public float getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(float rotationAngle) {
		this.rotationAngle = rotationAngle;
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
				float rad = (float) Math.toRadians(rotationAngle);
				float cos = (float) Math.cos(rad);
				float sin = (float) Math.sin(rad);

				for (Bullet bullet : bulletsToCreate) {
					float dx = bullet.getInitialDXOverride();
					float dy = bullet.getInitialDYOverride();
					float rotatedDX = dx * cos + dy * sin;
					float rotatedDY = -dx * sin + dy * cos;
					bullet.setInitialDXOverride(rotatedDX);
					bullet.setInitialDYOverride(rotatedDY);

					float xOff = bullet.getInitialXOffset();
					float yOff = bullet.getInitialYOffset();
					float rotatedXOffset = xOff * cos + yOff * sin;
					float rotatedYOffset = -xOff * sin + yOff * cos;
					bullet.setInitialXOffset(rotatedXOffset);
					bullet.setInitialYOffset(rotatedYOffset);

					BulletActor bulletActor = new BulletActor(
					 bullet,
					 getCenterX(),
					 getCenterY(),
					 bullet.getInitialDXOverride(),
					 bullet.getInitialDYOverride()
					);
					bulletActor.setSpellCardOverride(spellCard.getOverrideName());
					bloonManager.addBulletToStage(bulletActor);
				}
			}
		}
	}
	
}
