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
		textureRegion = new TextureRegion(new Texture(Gdx.files.internal(spellCard.getImageFileName())));
		this.rotationAngle = rotationAngle;
		
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
					float initialXOffset = bullet.getInitialXOffset();
					float initialYOffset = bullet.getInitialYOffset();

					float rotatedXOffset = initialXOffset * cos + initialYOffset * sin;
					float rotatedYOffset = initialYOffset * cos - initialXOffset * sin;

					float spawnX = getCenterX() + rotatedXOffset;
					float spawnY = getCenterY() + rotatedYOffset;

					float dx = bullet.getInitialDXOverride();
					float dy = bullet.getInitialDYOverride();

					float rotatedDX = dx * cos + dy * sin;
					float rotatedDY = dy * cos - dx * sin;

					BulletActor bulletActor = new BulletActor(
					 bullet,
					 spawnX - initialXOffset,
					 spawnY - initialYOffset,
					 rotatedDX,
					 rotatedDY
					);
					bulletActor.setSpellCardOverride(spellCard.getOverrideName());
					bloonManager.addBulletToStage(bulletActor);
				}
			}
		}
	}
	
}
