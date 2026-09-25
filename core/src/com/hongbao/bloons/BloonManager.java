package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BloonManager {
	
	private Stage stage;
	private Map map;
	// A dedicated collection of onstage bloons is maintained to (probably) speed up collision checking
	// especially when there are a lot of bullets on screen.
	private Set<BloonActor> onstageBloons;
	private List<SoundListener> soundListeners;
	private BloonQueue bloonQueue;
	
	public BloonManager(Stage stage, Map map) {
		this(stage, map, null);
	}

	public BloonManager(Stage stage, Map map, SoundListener soundListener) {
		this.stage = stage;
		this.map = map;
		this.onstageBloons = new HashSet<>();
		this.soundListeners = new ArrayList<>();
		if (soundListener != null) {
			addSoundListener(soundListener);
		}
		this.bloonQueue = BloonFactory.createBloonQueue();
	}

	public void addSoundListener(SoundListener listener) {
		if (listener != null && !soundListeners.contains(listener)) {
			soundListeners.add(listener);
		}
	}

	public void removeSoundListener(SoundListener listener) {
		soundListeners.remove(listener);
	}

	public void setSoundListener(SoundListener listener) {
		soundListeners.clear();
		if (listener != null) {
			soundListeners.add(listener);
		}
	}

	public Set<BloonActor> getOnstageBloons() {
		return onstageBloons;
	}

	private void notifyBloonPopped() {
		for (SoundListener listener : soundListeners) {
			listener.onBloonPopped();
		}
	}

	private void notifyBloonDamaged() {
		for (SoundListener listener : soundListeners) {
			listener.onBloonDamaged();
		}
	}

	private Player getPlayer() {
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			return ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getPlayer();
		}
		return null;
	}

	public void nextLevel() {
		if (canGoToNextLevel()) {
			bloonQueue.nextLevel();
			if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
				MusicPlayer musicPlayer = ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getMusicPlayer();
				if (getLevel() == 1) {
					musicPlayer.playStageMusic();
				} else if (getLevel() == 40) {
					musicPlayer.playFinalBossMusic();
				}
			}
		}
	}

	public boolean canGoToNextLevel() {
		boolean instructionsEmpty = true;
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			instructionsEmpty = ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).instructions.isEmpty();
		}
		return instructionsEmpty && bloonQueue.hasNextLevel() && onstageBloons.isEmpty() && bloonQueue.isEmpty();
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
		
		if (bulletActor.getBullet().isHoming()) {
			bulletActor.setTarget(null);
		}
		
		bloonsToBePopped.forEach((bloonActor) -> popBloon(bloonActor, bulletActor.getBullet().getDamage()));
	}
	
	public void popBloon(BloonActor bloonActor, int damage) {
		Player player = getPlayer();
		
		if (bloonActor.getBloon().willPopBloon(damage)) {
			onstageBloons.remove(bloonActor);
			BloonPoppedResult result = bloonActor.pop(damage);
			if (player != null) {
				player.earnMoney(result.getCashGenerated());
			}
			
			BloonActor previousBloonActor = null;
			for (Bloon bloon : result.getBloonsGenerated()) {
				BloonActor generatedBloonActor;
				if (previousBloonActor == null) {
					 generatedBloonActor = new BloonActor(bloon, bloonActor.getCenterX(), bloonActor.getCenterY(), bloonActor);
				} else {
					Pair<Float, Float> direction = map != null ? map.getDirection(previousBloonActor.getCenterX(), previousBloonActor.getCenterY()) : new Pair<>(0f, 0f);
					generatedBloonActor = new BloonActor(bloon, previousBloonActor.getCenterX() - direction.getFirst(), previousBloonActor.getCenterY() - direction.getSecond(), bloonActor);
				}
				if (stage != null) {
					stage.addActor(generatedBloonActor);
				}
				onstageBloons.add(generatedBloonActor);
				previousBloonActor = generatedBloonActor;
			}
			
			notifyBloonPopped();
		} else {
			bloonActor.damage(damage);
			if (player != null) {
				player.earnMoney(damage);
			}
			notifyBloonDamaged();
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

	public void removeBloonFromStage(BloonActor actor) {
		onstageBloons.remove(actor);
	}
	
	public BloonActor getNewHomingTarget(BulletActor bulletActor) {
		// Gets the closest bloon to the current bullet
		if (onstageBloons.isEmpty()) {
			return null;
		}
		
		BloonActor bloonActor = null;
		
		for (BloonActor actor : onstageBloons) {
			if (!bulletActor.hasDamagedBloon(actor)) {
				if (bloonActor == null) {
					bloonActor = actor;
				} else if (Map.distanceBetweenActors(actor, bulletActor) < Map.distanceBetweenActors(bloonActor, bulletActor)) {
					bloonActor = actor;
				}
			}
		}
		
		return bloonActor;
	}
	
}
