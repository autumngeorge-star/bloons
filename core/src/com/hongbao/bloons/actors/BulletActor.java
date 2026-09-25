package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.helpers.ZIndex;
import com.hongbao.bloons.helpers.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BulletActor extends RenderableActor implements Poolable {
	
	private static final Map<String, TextureRegion> textureCache = new HashMap<>();

	public static TextureRegion getTextureRegion(String imageFileName) {
		TextureRegion region = textureCache.get(imageFileName);
		if (region == null) {
			Texture texture = new Texture(Gdx.files.internal(imageFileName));
			region = new TextureRegion(texture);
			textureCache.put(imageFileName, region);
		}
		return region;
	}

	public static void clearTextureCache() {
		textureCache.clear();
	}

	private Bullet bullet;
	private float dx; // This should be a unit vector
	private float dy;
	private float rotationAngle;
	private float collisionRadius;
	private int frames;
	private BloonActor target;
	private Set<Long> damagedBloons;
	private String spellCardOverride; // todo could be an enum
	private boolean allocated;
	
	public BulletActor() {
		this.damagedBloons = new HashSet<>();
		this.allocated = false;
	}

	public BulletActor(Bullet bullet, float x, float y, float dx, float dy) {
		this();
		init(bullet, x, y, dx, dy);
	}

	public void init(Bullet bullet, float x, float y, float dx, float dy) {
		this.bullet = bullet;
		this.textureRegion = getTextureRegion(bullet.getImageFileName());
		x += bullet.getInitialXOffset();
		y += bullet.getInitialYOffset();
		this.dx = dx;
		this.dy = dy;
		calculateRotationAngle();
		collisionRadius = textureRegion.getTexture().getWidth() / 2f;
		target = null; // this'll get automatically set as the bullet moves
		frames = 0;
		spellCardOverride = null;
		
		setZIndex(ZIndex.BULLET_Z_INDEX);
		setBounds(
		 x - textureRegion.getTexture().getWidth() / 2f,
		 y - textureRegion.getTexture().getHeight() / 2f,
		 textureRegion.getTexture().getWidth(),
		 textureRegion.getTexture().getHeight()
		);
		
		if (damagedBloons == null) {
			damagedBloons = new HashSet<>(bullet.getPierce());
		} else {
			damagedBloons.clear();
		}
		this.allocated = true;
	}

	public boolean isAllocated() {
		return allocated;
	}

	public void setAllocated(boolean allocated) {
		this.allocated = allocated;
	}

	@Override
	public void reset() {
		remove();
		this.bullet = null;
		this.dx = 0;
		this.dy = 0;
		this.rotationAngle = 0;
		this.collisionRadius = 0;
		this.frames = 0;
		this.target = null;
		if (this.damagedBloons != null) {
			this.damagedBloons.clear();
		}
		this.spellCardOverride = null;
		this.textureRegion = null;
		this.allocated = false;
		setPosition(0, 0);
		setSize(0, 0);
		clearActions();
		clearListeners();
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
		if (bullet == null) return;
		bullet.decrementPierce();
		if (bullet.getPierce() <= 0) {
			BloonManager bloonManager = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager();
			bloonManager.freeBulletActor(this);
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
		if (textureRegion == null) return;
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
		if (bullet == null) return;
		frames++;
		BloonManager bloonManager = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager();
		
		setDirectionIfApplicable(bloonManager);
		
		setX(getX() + dx * bullet.getSpeed() / 5);
		setY(getY() + dy * bullet.getSpeed() / 5);
		
		if (getY() < 0 || getY() > 900 || getX() < 0 || getX() > 1500) {
			bloonManager.freeBulletActor(this);
			return;
		}
		
		bullet.incrementDistanceTraveled();
		if (bullet.getDistanceTraveled() >= bullet.getMaxRange()) {
			bloonManager.freeBulletActor(this);
			return;
		}
		
		bloonManager.checkCollision(this);
	}
	
	private void setDirectionIfApplicable(BloonManager bloonManager) {
		if (spellCardOverride != null) {
			Pair<Float, Float> overrideDirection = null;
			if (spellCardOverride.equals("Reimu")) {
				overrideDirection = reimuSpellCardOverride();
			} else if (spellCardOverride.equals("Yuyuko")) {
				overrideDirection = yuyukoSpellCardOverride();
			}

			if (overrideDirection != null) {
				dx = overrideDirection.getFirst();
				dy = overrideDirection.getSecond();
				calculateRotationAngle();
				return;
			}
		}
		
		if (bullet != null && bullet.isHoming()) {
			if (!bloonManager.containsBloon(target)) {
				target = bloonManager.getNewHomingTarget(this);
			}
			
			if (target != null) {
				dx = target.getCenterX() - getCenterX();
				dy = target.getCenterY() - getCenterY();
				
				// make it a unit vector
				float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
				dx /= distance;
				dy /= distance;
			}
			calculateRotationAngle();
		}
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
