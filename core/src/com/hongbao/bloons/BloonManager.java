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
		if (Gdx.audio != null && Gdx.files != null) {
			popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
		}
		if (Gdx.files != null) {
			bloonQueue = BloonFactory.createBloonQueue();
		}
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
		return bloonQueue != null ? bloonQueue.getLevel() : 1;
	}

	public boolean hasWonGame() {
		return bloonQueue != null && !bloonQueue.hasNextLevel() && onstageBloons.isEmpty() && bloonQueue.isEmpty();
	}
	
	public void createBloons() {
		if (bloonQueue == null) return;
		Set<Bloon> bloonsToBeCreated = bloonQueue.getBloons();
		
		for (Bloon bloon : bloonsToBeCreated) {
			BloonActor actor = new BloonActor(bloon, -25, 425, null); // todo make these numbers an attribute in map or something
			if (stage != null) stage.addActor(actor);
			onstageBloons.add(actor);
		}
	}

	public Set<BloonActor> getOnstageBloons() {
		return onstageBloons;
	}

	public void addBloonActor(BloonActor bloonActor) {
		onstageBloons.add(bloonActor);
		if (stage != null) {
			stage.addActor(bloonActor);
		}
	}
	
	public void checkCollision(final BulletActor bulletActor) {
		List<BloonActor> candidateBloons = new ArrayList<>();
		
		for (BloonActor bloonActor : onstageBloons) {
			float collisionDistance = bloonActor.getCollisionRadius() + bulletActor.getCollisionRadius();
			float distance = Map.distanceBetweenActors(bulletActor, bloonActor);
			
			if (distance < collisionDistance && !bulletActor.hasDamagedBloon(bloonActor)) {
				candidateBloons.add(bloonActor);
			}
		}
		
		if (!candidateBloons.isEmpty()) {
			candidateBloons.sort((b1, b2) -> {
				float dist1 = Map.distanceBetweenActors(bulletActor, b1);
				float dist2 = Map.distanceBetweenActors(bulletActor, b2);
				
				if (Math.abs(dist1 - dist2) > 1e-5f) {
					return Float.compare(dist1, dist2);
				}
				
				// Secondary tie-breaker: track progress distance (furthest along track first)
				int trackDiff = Integer.compare(b2.getBloon().getDistanceTravelled(), b1.getBloon().getDistanceTravelled());
				if (trackDiff != 0) {
					return trackDiff;
				}
				
				// Tertiary tie-breaker: sequence counter (earlier created bloon first)
				return Long.compare(b1.getSequenceNumber(), b2.getSequenceNumber());
			});
			
			List<BloonActor> bloonsToBePopped = new ArrayList<>();
			
			for (BloonActor bloonActor : candidateBloons) {
				if (bulletActor.getBullet().getPierce() == 0) {
					break;
				}
				
				bulletActor.damageBloon(bloonActor);
				bloonsToBePopped.add(bloonActor);
				bulletActor.decrementPierce();
				
				if (bulletActor.getBullet().getPierce() == 0) {
					break;
				}
			}
			
			for (BloonActor bloonActor : bloonsToBePopped) {
				popBloon(bloonActor, bulletActor.getBullet().getDamage());
			}
		}
		
		if (bulletActor.getBullet().isHoming()) {
			bulletActor.setTarget(null);
		}
	}
	
	public void popBloon(BloonActor bloonActor, int damage) {
		Player player = (Gdx.app != null) ? ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer() : null;
		
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
					Pair<Float, Float> direction = (map != null) ? map.getDirection(previousBloonActor.getCenterX(), previousBloonActor.getCenterY()) : new Pair<>(0f, 0f);
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
		if (stage != null) stage.addActor(bulletActor);
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
			BloonActor bestBloon = null;
			for (BloonActor actor : bloonsInRange) {
				if (bestBloon == null) {
					bestBloon = actor;
				} else {
					int cmp = Integer.compare(actor.getBloon().getDistanceTravelled(), bestBloon.getBloon().getDistanceTravelled());
					if (cmp > 0) {
						bestBloon = actor;
					} else if (cmp == 0 && Long.compare(actor.getSequenceNumber(), bestBloon.getSequenceNumber()) < 0) {
						bestBloon = actor;
					}
				}
			}
			
			BulletActor bulletActor = girlActor.createBulletActor(bestBloon);
			if (stage != null) stage.addActor(bulletActor);
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
			BloonActor bestBloon = null;
			for (BloonActor actor : bloonsInRange) {
				if (bestBloon == null) {
					bestBloon = actor;
				} else {
					int cmp = Integer.compare(actor.getBloon().getDistanceTravelled(), bestBloon.getBloon().getDistanceTravelled());
					if (cmp > 0) {
						bestBloon = actor;
					} else if (cmp == 0 && Long.compare(actor.getSequenceNumber(), bestBloon.getSequenceNumber()) < 0) {
						bestBloon = actor;
					}
				}
			}
			
			girlActor.lookAtBloon(bestBloon);
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
		
		BloonActor bestBloon = null;
		
		for (BloonActor actor : onstageBloons) {
			if (!bulletActor.hasDamagedBloon(actor)) {
				if (bestBloon == null) {
					bestBloon = actor;
				} else {
					float d1 = Map.distanceBetweenActors(actor, bulletActor);
					float d2 = Map.distanceBetweenActors(bestBloon, bulletActor);
					if (Math.abs(d1 - d2) > 1e-5f) {
						if (d1 < d2) {
							bestBloon = actor;
						}
					} else {
						int trackDiff = Integer.compare(actor.getBloon().getDistanceTravelled(), bestBloon.getBloon().getDistanceTravelled());
						if (trackDiff > 0) {
							bestBloon = actor;
						} else if (trackDiff == 0 && Long.compare(actor.getSequenceNumber(), bestBloon.getSequenceNumber()) < 0) {
							bestBloon = actor;
						}
					}
				}
			}
		}
		
		return bestBloon;
	}
	
}
