package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.ZIndex;
import com.hongbao.bloons.helpers.Pair;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class BloonActor extends RenderableActor {
	
	public static final float SCALE = 0.5f;
	public static final Random RANDOM = new Random();
	
	private Set<Long> parentBloonIds;
	private Long bloonId;
	private Bloon bloon;
	private float collisionRadius;
	
	public BloonActor(Bloon bloon, float x, float y, BloonActor parent) {
		this.bloon = bloon;
		setZIndex(ZIndex.BLOON_Z_INDEX);
		if (Gdx.files != null) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal(bloon.getImageFileName())));
			collisionRadius = textureRegion.getTexture().getWidth() * SCALE / 2f;
			setBounds(x - textureRegion.getTexture().getWidth() * SCALE / 2f, y - textureRegion.getTexture().getHeight() * SCALE / 2f, textureRegion.getTexture().getWidth() * SCALE, textureRegion.getTexture().getHeight() * SCALE);
		} else {
			collisionRadius = 15f;
			setBounds(x - 15f, y - 15f, 30f, 30f);
		}
		
		if (parent != null) {
			parentBloonIds = new HashSet(parent.getParentBloonIds());
			parentBloonIds.add(parent.getBloonId());
		} else {
			parentBloonIds = new HashSet<>();
		}
		bloonId = RANDOM.nextLong();
	}

	public Bloon getBloon() {
		return bloon;
	}

	public void setBloon(Bloon bloon) {
		this.bloon = bloon;
	}
	
	@Override
	public float getCenterX() {
		if (textureRegion != null && textureRegion.getTexture() != null) {
			return getX() + textureRegion.getTexture().getWidth() * SCALE / 2f;
		}
		return getX() + getWidth() / 2f;
	}
	
	@Override
	public float getCenterY() {
		if (textureRegion != null && textureRegion.getTexture() != null) {
			return getY() + textureRegion.getTexture().getHeight() * SCALE / 2f;
		}
		return getY() + getHeight() / 2f;
	}
	
	public float getCollisionRadius() {
		return collisionRadius;
	}
	
	public void setCollisionRadius(float collisionRadius) {
		this.collisionRadius = collisionRadius;
	}
	
	public Long getBloonId() {
		return bloonId;
	}
	
	public Set<Long> getParentBloonIds() {
		return parentBloonIds;
	}
	
	// Please avoid calling this method directly, instead use the BloonManager damage()
	public void damage(int damage) {
		bloon.damage(damage);
	}
	
	// Please avoid calling this method directly, instead use the BloonManager pop()
	public BloonPoppedResult pop(int damage) {
		BloonPoppedResult bloonPoppedResult = bloon.pop(damage);
		if (textureRegion != null && textureRegion.getTexture() != null) {
			textureRegion.getTexture().dispose();
		}
		remove();
		return bloonPoppedResult;
	}
	
	public void release() {
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer().decreaseHealth(BloonPoppedResult.getTotalHealthOfBloon(bloon));
		}
		if (textureRegion != null && textureRegion.getTexture() != null) {
			textureRegion.getTexture().dispose();
		}
		remove();
	}
	
	public void move(Pair<Float, Float> direction) {
		move(direction, 1f / 60f);
	}

	public void move(Pair<Float, Float> direction, float delta) {
		float effectiveSpeed = bloon.getEffectiveSpeed();
		float stepFactor = delta * 60f;
		setX(getX() + direction.getFirst() * effectiveSpeed / 5f * stepFactor);
		setY(getY() + direction.getSecond() * effectiveSpeed / 5f * stepFactor);
		
		bloon.setDistanceTravelled((int)(bloon.getDistanceTravelled() + effectiveSpeed * stepFactor));

		if (getCenterX() > 1500) {
			release();
			if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
				((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager().removeBloonFromStage(this);
			}
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (textureRegion == null || textureRegion.getTexture() == null) {
			return;
		}
		if (bloon.isBlimp()) {
			if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
				BloonsTouhouDefense app = (BloonsTouhouDefense)Gdx.app.getApplicationListener();
				Pair<Float, Float> direction = app.getMap().getDirection(getCenterX(), getCenterY());
				float rotationAngle = (float)(Math.atan2(direction.getFirst(), direction.getSecond()) / Math.PI * 180);
				batch.draw(
				 textureRegion,
				 getX(),
				 getY(),
				 getCenterX() - getX(),
				 getCenterY() - getY(),
				 textureRegion.getTexture().getWidth() * SCALE,
				 textureRegion.getTexture().getHeight() * SCALE,
				 1f,
				 1f,
				 -rotationAngle
				);
			}
		} else {
			batch.draw(textureRegion.getTexture(), getX(), getY(), textureRegion.getTexture().getWidth() * SCALE, textureRegion.getTexture().getHeight() * SCALE);
		}
	}
	
	@Override
	public void act(float delta) {
		Pair<Float, Float> direction = new Pair<>(1f, 0f);
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			BloonsTouhouDefense app = (BloonsTouhouDefense)Gdx.app.getApplicationListener();
			direction = app.getMap().getDirection(getCenterX(), getCenterY());
			if (direction.getFirst() < 0) {
				System.out.println(direction.getFirst());
			}
		}
		move(direction, delta);
	}
}
