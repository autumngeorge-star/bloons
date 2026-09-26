package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.helpers.ZIndex;
import com.hongbao.bloons.helpers.Pair;

import java.util.HashSet;
import java.util.Set;


public class BulletActor extends RenderableActor {
	
	private Bullet bullet;
	private float dx; // This should be a unit vector
	private float dy;
	private float rotationAngle;
	private float collisionRadius;
	private int frames;
	private BloonActor target;
	private Set<Long> damagedBloons;
	private String spellCardOverride; // todo could be an enum
	
	public BulletActor(Bullet bullet, float x, float y, float dx, float dy) {
		this.bullet = bullet;
		textureRegion = new TextureRegion(new Texture(Gdx.files.internal(bullet.getImageFileName())));
		x += bullet.getInitialXOffset();
		y += bullet.getInitialYOffset();
		this.dx = dx;
		this.dy = dy;
		calculateRotationAngle();
		collisionRadius = textureRegion.getTexture().getWidth() / 2f;
		target = null; // this'll get automatically set as the bullet moves
		
		setZIndex(ZIndex.BULLET_Z_INDEX);
		setBounds(
		 x - textureRegion.getTexture().getWidth() / 2f,
		 y - textureRegion.getTexture().getHeight() / 2f,
		 textureRegion.getTexture().getWidth(),
		 textureRegion.getTexture().getHeight()
		);
		
		damagedBloons = new HashSet<>(bullet.getPierce());
	}
	
	public Bullet getBullet() {
		return bullet;
	}
	
	public void setBullet(Bullet bullet) {
		this.bullet = bullet;
	}
	
	private void calculateRotationAngle() {
		rotationAngle = (float)(Math.atan2(dx, dy) / Math.PI * 180);
	}
	
	public float getCollisionRadius() {
		return collisionRadius;
	}
	
	public void setCollisionRadius(float collisionRadius) {
		this.collisionRadius = collisionRadius;
	}
	
	public void setSpellCardOverride(String spellCardOverride) {
		this.spellCardOverride = spellCardOverride;
	}
	
	public void decrementPierce() {
		bullet.decrementPierce();
		if (bullet.getPierce() == 0) {
			remove();
		}
	}
	
	public boolean hasDamagedBloon(BloonActor bloonActor) {
		if (damagedBloons.contains(bloonActor.getBloonId())) {
			return true;
		}
		
		Set<Long> parentIds = bloonActor.getParentBloonIds();
		for (Long parentId : parentIds) {
			if (damagedBloons.contains(parentId)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void damageBloon(BloonActor bloonActor) {
		damagedBloons.add(bloonActor.getBloonId());
	}
	
	public BloonActor getTarget() {
		return target;
	}
	
	public void setTarget(BloonActor target) {
		this.target = target;
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
		 1f,
		 1f,
		 -rotationAngle
		);
	}
	
	@Override
	public void act(float delta) {
		frames++;
		BloonManager bloonManager = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager();
		
		setDirectionIfApplicable(bloonManager);
		
		setX(getX() + dx * bullet.getSpeed() / 5);
		setY(getY() + dy * bullet.getSpeed() / 5);
		
		if (getY() < 0 || getY() > 900 || getX() < 0 || getX() > 1500) {
			remove();
		}
		
		bullet.incrementDistanceTraveled();
		if (bullet.getDistanceTraveled() >= bullet.getMaxRange()) {
			remove();
		}
		
		bloonManager.checkCollision(this);
	}
	
	private void setDirectionIfApplicable(BloonManager bloonManager) {
		Pair<Float, Float> patternDirection = null;
		if (spellCardOverride != null) {
			if (spellCardOverride.equals("Reimu")) {
				patternDirection = reimuSpellCardOverride();
			} else if (spellCardOverride.equals("Yuyuko")) {
				patternDirection = yuyukoSpellCardOverride();
			}
		}

		Pair<Float, Float> homingDirection = null;
		float distanceToTarget = 0.0f;

		if (bullet.isHoming()) {
			if (!bloonManager.containsBloon(target)) {
				target = bloonManager.getNewHomingTarget(this);
			}

			if (target != null) {
				float targetDx = target.getCenterX() - getCenterX();
				float targetDy = target.getCenterY() - getCenterY();
				distanceToTarget = (float) Math.sqrt(targetDx * targetDx + targetDy * targetDy);
				if (distanceToTarget > 1e-5f) {
					homingDirection = new Pair<>(targetDx / distanceToTarget, targetDy / distanceToTarget);
				}
			}
		}

		if (patternDirection != null && homingDirection != null) {
			float alpha = computeAlpha(distanceToTarget, frames);
			Pair<Float, Float> blended = blendVectors(
				patternDirection.getFirst(), patternDirection.getSecond(),
				homingDirection.getFirst(), homingDirection.getSecond(),
				alpha
			);
			dx = blended.getFirst();
			dy = blended.getSecond();
			calculateRotationAngle();
		} else if (patternDirection != null) {
			dx = patternDirection.getFirst();
			dy = patternDirection.getSecond();
			calculateRotationAngle();
		} else if (homingDirection != null) {
			dx = homingDirection.getFirst();
			dy = homingDirection.getSecond();
			calculateRotationAngle();
		}
	}

	public static float computeAlpha(float distanceToTarget, int frames) {
		float maxDist = 300.0f;
		float fDist = Math.max(0.0f, Math.min(1.0f, 1.0f - distanceToTarget / maxDist));
		float fTime = Math.max(0.0f, Math.min(1.0f, frames / 200.0f));
		float progress = Math.max(fDist, fTime);
		float alpha = 0.4f + 0.6f * progress;
		return Math.max(0.0f, Math.min(1.0f, alpha));
	}

	public static Pair<Float, Float> blendVectors(float dxPattern, float dyPattern, float dxHoming, float dyHoming, float alpha) {
		float clampedAlpha = Math.max(0.0f, Math.min(1.0f, alpha));
		float finalDx = (1.0f - clampedAlpha) * dxPattern + clampedAlpha * dxHoming;
		float finalDy = (1.0f - clampedAlpha) * dyPattern + clampedAlpha * dyHoming;

		float len = (float) Math.sqrt(finalDx * finalDx + finalDy * finalDy);
		if (len > 1e-5f) {
			return new Pair<>(finalDx / len, finalDy / len);
		}

		float homingLen = (float) Math.sqrt(dxHoming * dxHoming + dyHoming * dyHoming);
		if (homingLen > 1e-5f) {
			return new Pair<>(dxHoming / homingLen, dyHoming / homingLen);
		}

		float patternLen = (float) Math.sqrt(dxPattern * dxPattern + dyPattern * dyPattern);
		if (patternLen > 1e-5f) {
			return new Pair<>(dxPattern / patternLen, dyPattern / patternLen);
		}

		return new Pair<>(0.0f, 0.0f);
	}
	
	// I don't like how this code is but I couldn't think of any better ways for the time being
	// spell card directional overrides
	// they return null if they no longer override the direction of the bullet
	
	private Pair<Float, Float> reimuSpellCardOverride() {
		if (frames > 200) {
			// Beyond 200 frames, use the default bullet behavior (homing)
			return null;
		} else if (frames < 20) {
			// For the first few frames, go in a straight line (direction unchanged)
			return new Pair<>(dx, dy);
		} else {
			// For the middle frames, start going in a circle
			double currentAngle = Math.atan2(dy, dx);
			double desiredAngle = currentAngle + (2 * Math.PI / 120);

			return new Pair<>((float) Math.cos(desiredAngle), (float) Math.sin(desiredAngle));
		}
	}

	private Pair<Float, Float> yuyukoSpellCardOverride() {
		// Alternate between turning left and right
		if (frames % 150 < 75) {
			double currentAngle = Math.atan2(dy, dx);
			double desiredAngle = currentAngle - (2 * Math.PI / 300);

			return new Pair<>((float) Math.cos(desiredAngle), (float) Math.sin(desiredAngle));
		} else {
			double currentAngle = Math.atan2(dy, dx);
			double desiredAngle = currentAngle + (2 * Math.PI / 300);

			return new Pair<>((float) Math.cos(desiredAngle), (float) Math.sin(desiredAngle));
		}
	}

}
