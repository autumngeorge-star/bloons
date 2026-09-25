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
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;


public class BloonActor extends RenderableActor {
	
	public static final float SCALE = 0.5f;
	private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
	
	private Set<Long> parentBloonIds;
	private Long bloonId;
	private Bloon bloon;
	private float collisionRadius;

	public static void resetIdGenerator() {
		ID_GENERATOR.set(0);
	}

	public static void resetIdGenerator(long initialValue) {
		ID_GENERATOR.set(initialValue);
	}
	
	public BloonActor(Bloon bloon, float x, float y, float collisionRadius) {
		this.bloon = bloon;
		this.collisionRadius = collisionRadius;
		setZIndex(ZIndex.BLOON_Z_INDEX);
		setBounds(x - collisionRadius, y - collisionRadius, collisionRadius * 2, collisionRadius * 2);
		parentBloonIds = new HashSet<>();
		bloonId = ID_GENERATOR.incrementAndGet();
	}

	public BloonActor(Bloon bloon, float x, float y, BloonActor parent) {
		this.bloon = bloon;
		textureRegion = new TextureRegion(new Texture(Gdx.files.internal(bloon.getImageFileName())));

		collisionRadius = textureRegion.getTexture().getWidth() * SCALE / 2f;
		
		setZIndex(ZIndex.BLOON_Z_INDEX);
		setBounds(x - textureRegion.getTexture().getWidth() * SCALE / 2f, y - textureRegion.getTexture().getHeight() * SCALE / 2f, textureRegion.getTexture().getWidth() * SCALE, textureRegion.getTexture().getHeight() * SCALE);
		
		if (parent != null) {
			parentBloonIds = new HashSet(parent.getParentBloonIds());
			parentBloonIds.add(parent.getBloonId());
		} else {
			parentBloonIds = new HashSet<>();
		}
		bloonId = ID_GENERATOR.incrementAndGet();
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
			 textureRegion.getTexture().getWidth() * SCALE,
			 textureRegion.getTexture().getHeight() * SCALE,
			 1f,
			 1f,
			 -rotationAngle
			);
		} else {
			batch.draw(textureRegion.getTexture(), getX(), getY(), textureRegion.getTexture().getWidth() * SCALE, textureRegion.getTexture().getHeight() * SCALE);
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
