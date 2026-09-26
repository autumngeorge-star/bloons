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
		BloonsTouhouDefense app = (BloonsTouhouDefense) Gdx.app.getApplicationListener();
		textureRegion = app.getTextureRegion(bloon.getImageFileName());

		collisionRadius = textureRegion.getRegionWidth() * SCALE / 2f;
		
		setZIndex(ZIndex.BLOON_Z_INDEX);
		setBounds(x - textureRegion.getRegionWidth() * SCALE / 2f, y - textureRegion.getRegionHeight() * SCALE / 2f, textureRegion.getRegionWidth() * SCALE, textureRegion.getRegionHeight() * SCALE);
		
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
		return getX() + textureRegion.getRegionWidth() * SCALE / 2f;
	}
	
	@Override
	public float getCenterY() {
		return getY() + textureRegion.getRegionHeight() * SCALE / 2f;
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
		remove();
		return bloonPoppedResult;
	}
	
	public void release() {
		((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer().decreaseHealth(BloonPoppedResult.getTotalHealthOfBloon(bloon));
		remove();
	}
	
	public void move(Pair<Float, Float> direction) {
		setX(getX() + direction.getFirst() * bloon.getSpeed() / 5);
		setY(getY() + direction.getSecond() * bloon.getSpeed() / 5);
		
		bloon.incrementDistanceTravelled();

		if (getCenterX() > 1500) {
			release();
			((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager().removeBloonFromStage(this);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (bloon.isBlimp()) {
			BloonsTouhouDefense app = (BloonsTouhouDefense)Gdx.app.getApplicationListener();
			Pair<Float, Float> direction = app.getMap().getDirection(getCenterX(), getCenterY());
			float rotationAngle = (float)(Math.atan2(direction.getFirst(), direction.getSecond()) / Math.PI * 180);
			batch.draw(
			 textureRegion,
			 getX(),
			 getY(),
			 getCenterX() - getX(),
			 getCenterY() - getY(),
			 textureRegion.getRegionWidth() * SCALE,
			 textureRegion.getRegionHeight() * SCALE,
			 1f,
			 1f,
			 -rotationAngle
			);
		} else {
			batch.draw(textureRegion, getX(), getY(), textureRegion.getRegionWidth() * SCALE, textureRegion.getRegionHeight() * SCALE);
		}
	}
	
	@Override
	public void act(float delta) {
		BloonsTouhouDefense app = (BloonsTouhouDefense)Gdx.app.getApplicationListener();
		Pair<Float, Float> direction = app.getMap().getDirection(getCenterX(), getCenterY());
		if (direction.getFirst() < 0) {
			System.out.println(direction.getFirst());
		}
		move(direction);
	}
}
