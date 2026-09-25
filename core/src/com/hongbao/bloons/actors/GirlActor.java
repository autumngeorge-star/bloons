package com.hongbao.bloons.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.helpers.ZIndex;


public class GirlActor extends RenderableActor {
	
	// In hindsight, this was not a good choice of class name.
	
	private Girl girl;
	private float rotationAngle;
	private float collisionRadius;
	private boolean active;
	protected final Circle rangeCircle = new Circle();
	private final Vector2 tempDir = new Vector2();
	
	public GirlActor(Girl girl, float x, float y) {
		this.girl = girl;
		textureRegion = new TextureRegion(new Texture(Gdx.files.internal(girl.getImageFileName())));
		rotationAngle = 0;
		collisionRadius = textureRegion.getTexture().getWidth() / 2f;
		active = false;
		
		setZIndex(ZIndex.GIRL_Z_INDEX);
		setBounds(
		 x - textureRegion.getTexture().getWidth() / 2f,
		 y - textureRegion.getTexture().getHeight() / 2f,
		 textureRegion.getTexture().getWidth(),
		 textureRegion.getTexture().getHeight()
		);
	}

	public Circle getRangeCircle() {
		rangeCircle.set(getCenter(), girl != null ? girl.getVisualRange() : 0f);
		return rangeCircle;
	}
	
	public Girl getGirl() {
		return girl;
	}
	
	public void setGirl(Girl girl) {
		this.girl = girl;
	}
	
	public float getRotationAngle() {
		return rotationAngle;
	}
	
	public void setRotationAngle(float rotationAngle) {
		this.rotationAngle = rotationAngle;
	}
	
	@Override
	public float getCollisionRadius() {
		return collisionRadius;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public BulletActor createBulletActor(BloonActor target) {
		tempDir.set(target.getCenter()).sub(getCenter()).nor();
		
		lookAtBloon(target);
		
		Bullet bullet = girl.createBullet();
		return new BulletActor(bullet, getCenterX(), getCenterY(), tempDir.x, tempDir.y);
	}
	
	public void lookAtBloon(BloonActor target) {
		tempDir.set(target.getCenter()).sub(getCenter());
		rotationAngle = (float)(Math.atan2(tempDir.x, tempDir.y) / Math.PI * 180);
	}
	
	public SpellCardActor createSpellCardActor() {
		if (true) { // todo Girl should have a method that checks the cooldown or something
			// maybe some direction based on the girl's direction
			return new SpellCardActor(girl.createSpellCard(), getCenterX(), getCenterY());
		}
		return null;
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
		if (active) {
			if (girl.getCooldown() == 0) {
				boolean attacked = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager().attackBloonIfInRange(this);
				if (attacked) {
					girl.resetCooldown();
				}
			} else {
				((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getMap().getBloonManager().lookAtBloon(this);
				girl.decrementCooldown();
			}
		}
	}
	
}
