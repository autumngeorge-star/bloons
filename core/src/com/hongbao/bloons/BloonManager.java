package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.Pair;

import java.util.HashSet;
import java.util.Set;


public class BloonManager {
	
	private Stage stage;
	private Map map;
	// A dedicated collection of onstage bloons is maintained to (probably) speed up collision checking
	// especially when there are a lot of bullets on screen.
	private Set<BloonActor> onstageBloons;
	private Sound popSound; // todo another sound for damaging bloons
	private BloonQueue bloonQueue;
	
	public BloonManager(Stage stage, Map map) {
		this.stage = stage;
		this.map = map;
		onstageBloons = new HashSet<>();
		popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
		bloonQueue = BloonFactory.createBloonQueue();
	}

	public void nextLevel() {
		if (canGoToNextLevel()) {
			bloonQueue.nextLevel();
			MusicPlayer musicPlayer = ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getMusicPlayer();
			if (map.getBloonManager().getLevel() == 1) {
				musicPlayer.playStageMusic();
			} else if (map.getBloonManager().getLevel() == 40) {
				musicPlayer.playFinalBossMusic();
			}
		}
	}

	public boolean canGoToNextLevel() {
		return ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).instructions.isEmpty() && bloonQueue.hasNextLevel() && onstageBloons.isEmpty() && bloonQueue.isEmpty();
	}

	public int getLevel() {
		return bloonQueue.getLevel();
	}

	public boolean hasWonGame() {
		return !bloonQueue.hasNextLevel() && onstageBloons.isEmpty() && bloonQueue.isEmpty();
	}
	
	public void createBloons() {
		Set<Bloon> bloonsToBeCreated = bloonQueue.getBloons();
		
		for (Bloon bloon : bloonsToBeCreated) {
			BloonActor actor = new BloonActor(bloon, -25, 425, null); // todo make these numbers an attribute in map or something
			stage.addActor(actor);
			onstageBloons.add(actor);
		}
	}
	
	public void checkCollision(final BulletActor bulletActor) {
		Set<BloonActor> bloonsToBePopped = new HashSet<>(); // to avoid ConcurrentModificationException
		
		for (BloonActor bloonActor : onstageBloons) {
			float collisionDistance = bloonActor.getCollisionRadius() + bulletActor.getCollisionRadius();
			float distance = Map.distanceBetweenActors(bulletActor, bloonActor);
			
			if (distance < collisionDistance) {
				if (!bulletActor.hasDamagedBloon(bloonActor)) {
					bulletActor.damageBloon(bloonActor);
					bloonsToBePopped.add(bloonActor);
					bulletActor.decrementPierce();
					
					if (bulletActor.getBullet().getPierce() == 0) {
						// don't bother checking collisions if the bullet is used up.
						break;
					}
				}
			}
		}
		
		if (bulletActor.getBullet().isHoming() && bulletActor.getTarget() != null) {
			if (bulletActor.hasDamagedBloon(bulletActor.getTarget()) || !containsBloon(bulletActor.getTarget())) {
				bulletActor.setTarget(null);
			}
		}
		
		bloonsToBePopped.forEach((bloonActor) -> popBloon(bloonActor, bulletActor.getBullet().getDamage()));
	}
	
	public void popBloon(BloonActor bloonActor, int damage) {
		Player player = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer();
		
		if (bloonActor.getBloon().willPopBloon(damage)) {
			onstageBloons.remove(bloonActor);
			BloonPoppedResult result = bloonActor.pop(damage);
			player.earnMoney(result.getCashGenerated());
			
			BloonActor previousBloonActor = null;
			for (Bloon bloon : result.getBloonsGenerated()) {
				BloonActor generatedBloonActor;
				if (previousBloonActor == null) {
					 generatedBloonActor = new BloonActor(bloon, bloonActor.getCenterX(), bloonActor.getCenterY(), bloonActor);
				} else {
					Pair<Float, Float> direction = map.getDirection(previousBloonActor.getCenterX(), previousBloonActor.getCenterY());
					generatedBloonActor = new BloonActor(bloon, previousBloonActor.getCenterX() - direction.getFirst(), previousBloonActor.getCenterY() - direction.getSecond(), bloonActor);
				}
				stage.addActor(generatedBloonActor);
				onstageBloons.add(generatedBloonActor);
				previousBloonActor = generatedBloonActor;
			}
			
			popSound.play(0.5f);
		} else {
			bloonActor.damage(damage);
			player.earnMoney(damage);
			// todo play some other sound I guess
		}
	}
	
	public void addBulletToStage(BulletActor bulletActor) {
		stage.addActor(bulletActor);
	}
	
	public boolean attackBloonIfInRange(GirlActor girlActor) {
		Set<BloonActor> bloonsInRange = new HashSet<>();
		
		for (BloonActor bloonActor : onstageBloons) {
			float distance = Map.distanceBetweenActors(girlActor, bloonActor);
			
			if (distance - bloonActor.getCollisionRadius() < girlActor.getGirl().getVisualRange()) {
				bloonsInRange.add(bloonActor);
			}
		}
		
		if (bloonsInRange.isEmpty()) {
			return false;
		} else {
			BloonActor bloonActor = bloonsInRange.iterator().next();
			
			for (BloonActor actor : bloonsInRange) {
				if (actor.getBloon().getDistanceTravelled() > bloonActor.getBloon().getDistanceTravelled()) {
					bloonActor = actor;
				}
			}
			
			BulletActor bulletActor = girlActor.createBulletActor(bloonActor);
			stage.addActor(bulletActor);
			return true;
		}
	}
	
	public void lookAtBloon(GirlActor girlActor) {
		Set<BloonActor> bloonsInRange = new HashSet<>();
		
		for (BloonActor bloonActor : onstageBloons) {
			float distance = Map.distanceBetweenActors(girlActor, bloonActor);
			
			if (distance - bloonActor.getCollisionRadius() < girlActor.getGirl().getVisualRange()) {
				bloonsInRange.add(bloonActor);
			}
		}
		
		if (!bloonsInRange.isEmpty()) {
			BloonActor bloonActor = bloonsInRange.iterator().next();
			
			for (BloonActor actor : bloonsInRange) {
				if (actor.getBloon().getDistanceTravelled() > bloonActor.getBloon().getDistanceTravelled()) {
					bloonActor = actor;
				}
			}
			
			girlActor.lookAtBloon(bloonActor);
		}
	}
	
	public boolean containsBloon(BloonActor target) {
		return onstageBloons.contains(target);
	}

	public void addBloonToStage(BloonActor bloonActor) {
		stage.addActor(bloonActor);
		onstageBloons.add(bloonActor);
	}

	public void removeBloonFromStage(BloonActor actor) {
		onstageBloons.remove(actor);
	}
	
	public boolean hasForwardHomingTarget(BulletActor bulletActor) {
		if (onstageBloons.isEmpty()) {
			return false;
		}
		
		float vx = bulletActor.getDx();
		float vy = bulletActor.getDy();
		if (vx == 0 && vy == 0) {
			return true;
		}
		
		for (BloonActor actor : onstageBloons) {
			if (!bulletActor.hasDamagedBloon(actor)) {
				float targetDx = actor.getCenterX() - bulletActor.getCenterX();
				float targetDy = actor.getCenterY() - bulletActor.getCenterY();
				float dot = vx * targetDx + vy * targetDy;
				if (dot > 0) {
					return true;
				}
			}
		}
		
		return false;
	}

	public BloonActor getNewHomingTarget(BulletActor bulletActor) {
		if (onstageBloons.isEmpty()) {
			return null;
		}
		
		float vx = bulletActor.getDx();
		float vy = bulletActor.getDy();
		boolean hasVelocity = (vx != 0 || vy != 0);
		
		BloonActor bestForwardActor = null;
		float bestForwardScore = -Float.MAX_VALUE;
		
		BloonActor bestBackwardActor = null;
		float bestBackwardScore = -Float.MAX_VALUE;
		
		for (BloonActor actor : onstageBloons) {
			if (!bulletActor.hasDamagedBloon(actor)) {
				float targetDx = actor.getCenterX() - bulletActor.getCenterX();
				float targetDy = actor.getCenterY() - bulletActor.getCenterY();
				float dist = (float) Math.sqrt(targetDx * targetDx + targetDy * targetDy);
				
				float dot = vx * targetDx + vy * targetDy;
				boolean isForward = !hasVelocity || (dot > 0);
				
				float cosTheta = (hasVelocity && dist > 0) ? (dot / dist) : 1.0f;
				float progress = actor.getBloon().getDistanceTravelled();
				float score = progress - dist * 0.5f + cosTheta * 100.0f;
				
				if (isForward) {
					if (bestForwardActor == null || score > bestForwardScore) {
						bestForwardActor = actor;
						bestForwardScore = score;
					}
				} else {
					if (bestBackwardActor == null || score > bestBackwardScore) {
						bestBackwardActor = actor;
						bestBackwardScore = score;
					}
				}
			}
		}
		
		return (bestForwardActor != null) ? bestForwardActor : bestBackwardActor;
	}
	
}
