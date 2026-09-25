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
		List<BloonActor> collisionCandidates = new ArrayList<>();
		
		for (BloonActor bloonActor : onstageBloons) {
			float collisionDistance = bloonActor.getCollisionRadius() + bulletActor.getCollisionRadius();
			float distance = Map.distanceBetweenActors(bulletActor, bloonActor);
			
			if (distance < collisionDistance) {
				if (!bulletActor.hasDamagedBloon(bloonActor)) {
					collisionCandidates.add(bloonActor);
				}
			}
		}

		if (!collisionCandidates.isEmpty()) {
			collisionCandidates.sort((a, b) -> {
				int distCompare = Integer.compare(b.getBloon().getDistanceTravelled(), a.getBloon().getDistanceTravelled());
				if (distCompare != 0) {
					return distCompare;
				}
				return Long.compare(a.getBloonId(), b.getBloonId());
			});

			List<BloonActor> bloonsToBePopped = new ArrayList<>();
			for (BloonActor bloonActor : collisionCandidates) {
				if (bulletActor.getBullet().getPierce() == 0) {
					break;
				}
				bulletActor.damageBloon(bloonActor);
				bloonsToBePopped.add(bloonActor);
				bulletActor.decrementPierce();
			}

			bloonsToBePopped.forEach((bloonActor) -> popBloon(bloonActor, bulletActor.getBullet().getDamage()));
		}
		
		if (bulletActor.getBullet().isHoming()) {
			bulletActor.setTarget(null);
		}
	}
	
	public void popBloon(BloonActor bloonActor, int damage) {
		Player player = null;
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			player = ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getPlayer();
		}
		
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
			
			if (popSound != null) {
				popSound.play(0.5f);
			}
		} else {
			bloonActor.damage(damage);
			if (player != null) {
				player.earnMoney(damage);
			}
		}
	}
	
	public void addBulletToStage(BulletActor bulletActor) {
		stage.addActor(bulletActor);
	}
	
	public boolean attackBloonIfInRange(GirlActor girlActor) {
		BloonActor targetBloon = null;
		
		for (BloonActor bloonActor : onstageBloons) {
			float distance = Map.distanceBetweenActors(girlActor, bloonActor);
			
			if (distance - bloonActor.getCollisionRadius() < girlActor.getGirl().getVisualRange()) {
				if (targetBloon == null) {
					targetBloon = bloonActor;
				} else {
					int comp = Integer.compare(bloonActor.getBloon().getDistanceTravelled(), targetBloon.getBloon().getDistanceTravelled());
					if (comp > 0) {
						targetBloon = bloonActor;
					} else if (comp == 0 && Long.compare(bloonActor.getBloonId(), targetBloon.getBloonId()) < 0) {
						targetBloon = bloonActor;
					}
				}
			}
		}
		
		if (targetBloon == null) {
			return false;
		} else {
			BulletActor bulletActor = girlActor.createBulletActor(targetBloon);
			stage.addActor(bulletActor);
			return true;
		}
	}
	
	public void lookAtBloon(GirlActor girlActor) {
		BloonActor targetBloon = null;
		
		for (BloonActor bloonActor : onstageBloons) {
			float distance = Map.distanceBetweenActors(girlActor, bloonActor);
			
			if (distance - bloonActor.getCollisionRadius() < girlActor.getGirl().getVisualRange()) {
				if (targetBloon == null) {
					targetBloon = bloonActor;
				} else {
					int comp = Integer.compare(bloonActor.getBloon().getDistanceTravelled(), targetBloon.getBloon().getDistanceTravelled());
					if (comp > 0) {
						targetBloon = bloonActor;
					} else if (comp == 0 && Long.compare(bloonActor.getBloonId(), targetBloon.getBloonId()) < 0) {
						targetBloon = bloonActor;
					}
				}
			}
		}
		
		if (targetBloon != null) {
			girlActor.lookAtBloon(targetBloon);
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
		
		BloonActor closestActor = null;
		float closestDistance = Float.MAX_VALUE;
		
		for (BloonActor actor : onstageBloons) {
			if (!bulletActor.hasDamagedBloon(actor)) {
				float distance = Map.distanceBetweenActors(actor, bulletActor);
				if (closestActor == null) {
					closestActor = actor;
					closestDistance = distance;
				} else if (distance < closestDistance) {
					closestActor = actor;
					closestDistance = distance;
				} else if (distance == closestDistance) {
					int distComp = Integer.compare(actor.getBloon().getDistanceTravelled(), closestActor.getBloon().getDistanceTravelled());
					if (distComp > 0) {
						closestActor = actor;
						closestDistance = distance;
					} else if (distComp == 0 && Long.compare(actor.getBloonId(), closestActor.getBloonId()) < 0) {
						closestActor = actor;
						closestDistance = distance;
					}
				}
			}
		}
		
		return closestActor;
	}
	
}
