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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


public class BloonManager {
	
	private Stage stage;
	private Map map;
	// A dedicated collection of onstage bloons is maintained to (probably) speed up collision checking
	// especially when there are a lot of bullets on screen.
	private Set<BloonActor> onstageBloons;
	private Sound popSound; // todo another sound for damaging bloons
	private BloonQueue bloonQueue;
	private Queue<BloonPopJob> popJobQueue;
	
	public BloonManager(Stage stage, Map map) {
		this.stage = stage;
		this.map = map;
		onstageBloons = new HashSet<>();
		if (Gdx.files != null) {
			if (Gdx.audio != null) {
				popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
			}
			bloonQueue = BloonFactory.createBloonQueue();
		}
		popJobQueue = new LinkedList<>();
	}

	public Queue<BloonPopJob> getPopJobQueue() {
		return popJobQueue;
	}

	public static int getShellHealth(Bloon bloon) {
		int health = bloon.getHealth();
		if (health <= 8) {
			return 1;
		} else if (health <= 18) {
			return health - 8;
		} else if (health <= 218) {
			return health - 18;
		} else if (health <= 918) {
			return health - 218;
		} else {
			return health - 918;
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
		
		if (bulletActor.getBullet().isHoming()) {
			bulletActor.setTarget(null);
		}
		
		for (BloonActor bloonActor : bloonsToBePopped) {
			enqueuePopJob(bloonActor, bulletActor.getBullet().getDamage());
		}
		processPopQueue();
	}

	public void enqueuePopJob(BloonActor bloonActor, int damage) {
		if (bloonActor == null || damage <= 0) {
			return;
		}
		popJobQueue.add(new BloonPopJob(bloonActor, damage, bloonActor.getCenterX(), bloonActor.getCenterY()));
	}
	
	public void popBloon(BloonActor bloonActor, int damage) {
		if (bloonActor == null || damage <= 0) {
			return;
		}
		enqueuePopJob(bloonActor, damage);
		processPopQueue();
	}

	public void processPopQueue() {
		List<BloonActor> newlyCreatedActors = new ArrayList<>();
		int iterations = 0;
		
		while (!popJobQueue.isEmpty() && iterations < 1000) {
			iterations++;
			BloonPopJob job = popJobQueue.poll();
			BloonActor bloonActor = job.getBloonActor();
			int damage = job.getDamage();
			
			if (bloonActor == null || damage <= 0) {
				continue;
			}
			
			Bloon bloon = bloonActor.getBloon();
			int shellHealth = getShellHealth(bloon);
			Player player = getPlayer();
			
			if (damage >= shellHealth) {
				int damageApplied = shellHealth;
				int remainingDamage = damage - damageApplied;
				
				if (onstageBloons.contains(bloonActor)) {
					onstageBloons.remove(bloonActor);
				}
				
				BloonPoppedResult result = bloonActor.pop(damageApplied);
				
				if (player != null) {
					player.earnMoney(result.getCashGenerated());
				}
				
				if (popSound != null) {
					popSound.play(0.5f);
				}
				
				BloonActor previousBloonActor = null;
				for (Bloon childBloon : result.getBloonsGenerated()) {
					BloonActor childActor;
					if (previousBloonActor == null) {
						childActor = new BloonActor(childBloon, job.getX(), job.getY(), bloonActor);
					} else {
						Pair<Float, Float> direction = map != null ? map.getDirection(previousBloonActor.getCenterX(), previousBloonActor.getCenterY()) : new Pair<>(0f, 0f);
						childActor = new BloonActor(childBloon, previousBloonActor.getCenterX() - direction.getFirst(), previousBloonActor.getCenterY() - direction.getSecond(), bloonActor);
					}
					previousBloonActor = childActor;
					
					if (remainingDamage > 0) {
						popJobQueue.add(new BloonPopJob(childActor, remainingDamage, childActor.getCenterX(), childActor.getCenterY()));
					} else {
						newlyCreatedActors.add(childActor);
					}
				}
			} else {
				bloonActor.damage(damage);
				if (player != null) {
					player.earnMoney(damage);
				}
				
				if (!onstageBloons.contains(bloonActor) && !newlyCreatedActors.contains(bloonActor)) {
					newlyCreatedActors.add(bloonActor);
				}
			}
		}
		
		for (BloonActor actor : newlyCreatedActors) {
			if (stage != null) {
				stage.addActor(actor);
			}
			onstageBloons.add(actor);
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
