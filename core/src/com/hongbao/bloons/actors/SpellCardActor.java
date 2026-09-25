package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.SoundEventListener;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.helpers.ZIndex;

import java.util.ArrayList;
import java.util.List;


public class SpellCardActor extends RenderableActor {
	
	private SpellCard spellCard;
	private float rotationAngle;
	private final List<SoundEventListener> soundEventListeners = new ArrayList<>();
	
	public SpellCardActor(SpellCard spellCard, float x, float y) {
		this.spellCard = spellCard;
		if (Gdx.files != null && Gdx.graphics != null && Gdx.gl != null) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal(spellCard.getImageFileName())));
			setBounds(
			 x - textureRegion.getTexture().getWidth() / 2f,
			 y - textureRegion.getTexture().getHeight() / 2f,
			 textureRegion.getTexture().getWidth(),
			 textureRegion.getTexture().getHeight()
			);
		} else {
			setBounds(x - 25f, y - 25f, 50f, 50f);
		}
		rotationAngle = 0;
		
		setZIndex(ZIndex.SPELL_CARD_Z_INDEX);
		notifySpellActivated();
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
					 bullet.getInitialDYOverride()
					); // todo the spell needs a direction maybe
					bulletActor.setSpellCardOverride(spellCard.getOverrideName());
					bloonManager.addBulletToStage(bulletActor);
				}
			}
		}
	}

	public void addSoundEventListener(SoundEventListener listener) {
		if (listener != null && !soundEventListeners.contains(listener)) {
			soundEventListeners.add(listener);
		}
	}

	public void removeSoundEventListener(SoundEventListener listener) {
		soundEventListeners.remove(listener);
	}

	public List<SoundEventListener> getSoundEventListeners() {
		return soundEventListeners;
	}

	public void notifySpellActivated() {
		for (SoundEventListener listener : new ArrayList<>(getSoundEventListeners())) {
			listener.onSpellActivated(this);
		}
	}
	
}
