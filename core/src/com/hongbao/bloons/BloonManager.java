package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.DefaultGameEventBus;
import com.hongbao.bloons.events.GameEventBus;
import com.hongbao.bloons.events.LevelChangedEvent;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;


public class BloonManager {
	
	private Stage stage;
	private Map map;
	// A dedicated collection of onstage bloons is maintained to (probably) speed up collision checking
	// especially when there are a lot of bullets on screen.
	private Set<BloonActor> onstageBloons;
	private BloonQueue bloonQueue;
	private GameEventBus eventBus;
	private Player player;
	private BooleanSupplier instructionsSupplier;
	
	public BloonManager(Stage stage, Map map, GameEventBus eventBus, Player player, BooleanSupplier instructionsSupplier) {
		this.stage = stage;
		this.map = map;
		this.eventBus = eventBus != null ? eventBus : new DefaultGameEventBus();
		this.player = player;
		this.instructionsSupplier = instructionsSupplier != null ? instructionsSupplier : () -> true;
		this.onstageBloons = new HashSet<>();
		this.bloonQueue = BloonFactory.createBloonQueue();
	}

	public BloonManager(Stage stage, Map map, GameEventBus eventBus) {
		this(stage, map, eventBus, null, () -> true);
	}

	public BloonManager(Stage stage, Map map) {
		this(stage, map, new DefaultGameEventBus(), null, () -> true);
	}

	public void setEventBus(GameEventBus eventBus) {
		this.eventBus = eventBus;
	}

	public GameEventBus getEventBus() {
		return eventBus;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void setInstructionsSupplier(BooleanSupplier instructionsSupplier) {
		this.instructionsSupplier = instructionsSupplier;
	}

	public void nextLevel() {
		if (canGoToNextLevel()) {
			bloonQueue.nextLevel();
			if (eventBus != null) {
				eventBus.publish(new LevelChangedEvent(getLevel()));
			}
		}
	}

	public boolean canGoToNextLevel() {
		boolean instructionsEmpty = instructionsSupplier == null || instructionsSupplier.getAsBoolean();
		return instructionsEmpty && bloonQueue.hasNextLevel() && onstageBloons.isEmpty() && bloonQueue.isEmpty();
	}

	public void setBloonQueue(BloonQueue bloonQueue) {
		this.bloonQueue = bloonQueue;
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
			if (stage != null) {
				stage.addActor(actor);
			}
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
					Pair<Float, Float> direction = map.getDirection(previousBloonActor.getCenterX(), previousBloonActor.getCenterY());
					generatedBloonActor = new BloonActor(bloon, previousBloonActor.getCenterX() - direction.getFirst(), previousBloonActor.getCenterY() - direction.getSecond(), bloonActor);
				}
				if (stage != null) {
					stage.addActor(generatedBloonActor);
				}
				onstageBloons.add(generatedBloonActor);
				previousBloonActor = generatedBloonActor;
			}
			
			if (eventBus != null) {
				eventBus.publish(new BloonPoppedEvent());
			}
		} else {
			bloonActor.damage(damage);
			if (player != null) {
				player.earnMoney(damage);
			}
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
	
	public Set<BloonActor> getOnstageBloons() {
		return onstageBloons;
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
