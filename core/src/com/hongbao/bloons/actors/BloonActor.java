package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

	private static TextureRegion iceOverlayRegion;
	private static TextureRegion glueOverlayRegion;
	private static boolean overlaysInitialized = false;

	private Set<Long> parentBloonIds;
	private Long bloonId;
	private Bloon bloon;
	private float collisionRadius;
	
	public static synchronized void loadOverlayTextures() {
		if (!overlaysInitialized && Gdx.files != null) {
			try {
				if (Gdx.files.internal("img/overlays/ice.png").exists()) {
					iceOverlayRegion = new TextureRegion(new Texture(Gdx.files.internal("img/overlays/ice.png")));
				}
				if (Gdx.files.internal("img/overlays/glue.png").exists()) {
					glueOverlayRegion = new TextureRegion(new Texture(Gdx.files.internal("img/overlays/glue.png")));
				}
			} catch (Exception ignored) {
			}
			overlaysInitialized = true;
		}
	}

	public static TextureRegion getIceOverlayRegion() {
		return iceOverlayRegion;
	}

	public static void setIceOverlayRegion(TextureRegion region) {
		iceOverlayRegion = region;
	}

	public static TextureRegion getGlueOverlayRegion() {
		return glueOverlayRegion;
	}

	public static void setGlueOverlayRegion(TextureRegion region) {
		glueOverlayRegion = region;
	}

	public BloonActor(Bloon bloon, float x, float y, BloonActor parent) {
		this.bloon = bloon;
		if (Gdx.files != null && bloon != null && bloon.getImageFileName() != null) {
			textureRegion = new TextureRegion(new Texture(Gdx.files.internal(bloon.getImageFileName())));
			collisionRadius = textureRegion.getTexture().getWidth() * SCALE / 2f;
			setBounds(x - textureRegion.getTexture().getWidth() * SCALE / 2f, y - textureRegion.getTexture().getHeight() * SCALE / 2f, textureRegion.getTexture().getWidth() * SCALE, textureRegion.getTexture().getHeight() * SCALE);
		} else {
			collisionRadius = 15f;
			setBounds(x - 15f, y - 15f, 30f, 30f);
		}

		loadOverlayTextures();

		setZIndex(ZIndex.BLOON_Z_INDEX);
		if (parent != null) {
			parentBloonIds = new HashSet<>(parent.getParentBloonIds());
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
		return getX() + textureRegion.getTexture().getWidth() * SCALE / 2f;
	}
	
	@Override
	public float getCenterY() {
		return getY() + textureRegion.getTexture().getHeight() * SCALE / 2f;
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
		textureRegion.getTexture().dispose();
		remove();
		return bloonPoppedResult;
	}
	
	public void release() {
		((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer().decreaseHealth(BloonPoppedResult.getTotalHealthOfBloon(bloon));
		textureRegion.getTexture().dispose();
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
		loadOverlayTextures();

		Color tintColor = Color.WHITE;
		TextureRegion overlayRegion = null;

		if (bloon != null) {
			if (bloon.isFrozen()) {
				tintColor = Color.CYAN;
				overlayRegion = iceOverlayRegion;
			} else if (bloon.isSlowed()) {
				tintColor = Color.YELLOW;
				overlayRegion = glueOverlayRegion;
			}
		}

		batch.setColor(tintColor);

		try {
			if (bloon != null && bloon.isBlimp()) {
				BloonsTouhouDefense app = (BloonsTouhouDefense) Gdx.app.getApplicationListener();
				Pair<Float, Float> direction = (app != null && app.getMap() != null) ? app.getMap().getDirection(getCenterX(), getCenterY()) : new Pair<>(0f, 0f);
				float rotationAngle = (float) (Math.atan2(direction.getFirst(), direction.getSecond()) / Math.PI * 180);
				float originX = getCenterX() - getX();
				float originY = getCenterY() - getY();
				float width = textureRegion != null && textureRegion.getTexture() != null ? textureRegion.getTexture().getWidth() * SCALE : getWidth();
				float height = textureRegion != null && textureRegion.getTexture() != null ? textureRegion.getTexture().getHeight() * SCALE : getHeight();

				if (textureRegion != null) {
					batch.draw(
					 textureRegion,
					 getX(),
					 getY(),
					 originX,
					 originY,
					 width,
					 height,
					 1f,
					 1f,
					 -rotationAngle
					);
				}

				batch.setColor(Color.WHITE);

				if (overlayRegion != null) {
					batch.draw(
					 overlayRegion,
					 getX(),
					 getY(),
					 originX,
					 originY,
					 width,
					 height,
					 1f,
					 1f,
					 -rotationAngle
					);
				}
			} else {
				float width = textureRegion != null && textureRegion.getTexture() != null ? textureRegion.getTexture().getWidth() * SCALE : getWidth();
				float height = textureRegion != null && textureRegion.getTexture() != null ? textureRegion.getTexture().getHeight() * SCALE : getHeight();

				if (textureRegion != null && textureRegion.getTexture() != null) {
					batch.draw(textureRegion.getTexture(), getX(), getY(), width, height);
				}

				batch.setColor(Color.WHITE);

				if (overlayRegion != null) {
					batch.draw(overlayRegion, getX(), getY(), width, height);
				}
			}
		} finally {
			batch.setColor(Color.WHITE);
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
