package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
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

	public static final float STAGE_WIDTH = 1500f;
	public static final float STAGE_HEIGHT = 900f;
	public static final float CELL_SIZE = 100f;
	public static final int GRID_COLS = 15;
	public static final int GRID_ROWS = 9;
	public static final float MAX_BLOON_RADIUS = 50f;
	
	private Stage stage;
	private Map map;
	private Set<BloonActor> onstageBloons;
	private final Array<BloonActor>[][] grid = new Array[GRID_COLS][GRID_ROWS];
	private final Array<BloonActor> bloonsToBePopped = new Array<>(false, 16);
	private Sound popSound;
	private BloonQueue bloonQueue;
	
	public BloonManager(Stage stage, Map map) {
		this.stage = stage;
		this.map = map;
		onstageBloons = new HashSet<>();
		for (int c = 0; c < GRID_COLS; c++) {
			for (int r = 0; r < GRID_ROWS; r++) {
				grid[c][r] = new Array<>(false, 16);
			}
		}
		if (Gdx.audio != null && Gdx.files != null) {
			popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
		}
		bloonQueue = BloonFactory.createBloonQueue();
	}

	public int getGridCol(float x) {
		int col = (int) Math.floor(x / CELL_SIZE);
		if (col < 0) return 0;
		if (col >= GRID_COLS) return GRID_COLS - 1;
		return col;
	}

	public int getGridRow(float y) {
		int row = (int) Math.floor(y / CELL_SIZE);
		if (row < 0) return 0;
		if (row >= GRID_ROWS) return GRID_ROWS - 1;
		return row;
	}

	public void registerBloon(BloonActor bloonActor) {
		onstageBloons.add(bloonActor);
		int col = getGridCol(bloonActor.getCenterX());
		int row = getGridRow(bloonActor.getCenterY());
		grid[col][row].add(bloonActor);
		bloonActor.setGridCol(col);
		bloonActor.setGridRow(row);
	}

	public void unregisterBloon(BloonActor bloonActor) {
		onstageBloons.remove(bloonActor);
		int col = bloonActor.getGridCol();
		int row = bloonActor.getGridRow();
		if (col >= 0 && col < GRID_COLS && row >= 0 && row < GRID_ROWS) {
			grid[col][row].removeValue(bloonActor, true);
		}
		bloonActor.setGridCol(-1);
		bloonActor.setGridRow(-1);
	}

	public void updateBloonGridPosition(BloonActor bloonActor) {
		if (bloonActor.getGridCol() < 0 || bloonActor.getGridRow() < 0) {
			return;
		}
		int newCol = getGridCol(bloonActor.getCenterX());
		int newRow = getGridRow(bloonActor.getCenterY());
		if (newCol != bloonActor.getGridCol() || newRow != bloonActor.getGridRow()) {
			int oldCol = bloonActor.getGridCol();
			int oldRow = bloonActor.getGridRow();
			if (oldCol >= 0 && oldCol < GRID_COLS && oldRow >= 0 && oldRow < GRID_ROWS) {
				grid[oldCol][oldRow].removeValue(bloonActor, true);
			}
			grid[newCol][newRow].add(bloonActor);
			bloonActor.setGridCol(newCol);
			bloonActor.setGridRow(newRow);
		}
	}

	public Array<BloonActor>[][] getGrid() {
		return grid;
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
			registerBloon(actor);
		}
	}
	
	public void checkCollision(final BulletActor bulletActor) {
		bloonsToBePopped.clear();
		
		float bulletRadius = bulletActor.getCollisionRadius();
		float bx = bulletActor.getCenterX();
		float by = bulletActor.getCenterY();
		
		int minCol = getGridCol(bx - bulletRadius - MAX_BLOON_RADIUS);
		int maxCol = getGridCol(bx + bulletRadius + MAX_BLOON_RADIUS);
		int minRow = getGridRow(by - bulletRadius - MAX_BLOON_RADIUS);
		int maxRow = getGridRow(by + bulletRadius + MAX_BLOON_RADIUS);

		for (int c = minCol; c <= maxCol; c++) {
			for (int r = minRow; r <= maxRow; r++) {
				Array<BloonActor> cellBloons = grid[c][r];
				for (int i = 0; i < cellBloons.size; i++) {
					BloonActor bloonActor = cellBloons.get(i);
					float collisionDistance = bloonActor.getCollisionRadius() + bulletRadius;
					float collisionDistanceSq = collisionDistance * collisionDistance;
					float distanceSq = Map.distanceSquaredBetweenActors(bulletActor, bloonActor);
					
					if (distanceSq < collisionDistanceSq) {
						if (!bulletActor.hasDamagedBloon(bloonActor)) {
							bulletActor.damageBloon(bloonActor);
							bloonsToBePopped.add(bloonActor);
							bulletActor.decrementPierce();
							
							if (bulletActor.getBullet().getPierce() == 0) {
								break;
							}
						}
					}
				}
				if (bulletActor.getBullet().getPierce() == 0) {
					break;
				}
			}
			if (bulletActor.getBullet().getPierce() == 0) {
				break;
			}
		}
		
		if (bulletActor.getBullet().isHoming()) {
			bulletActor.setTarget(null);
		}
		
		for (int i = 0; i < bloonsToBePopped.size; i++) {
			popBloon(bloonsToBePopped.get(i), bulletActor.getBullet().getDamage());
		}
	}
	
	public void popBloon(BloonActor bloonActor, int damage) {
		Player player = null;
		if (Gdx.app != null && Gdx.app.getApplicationListener() instanceof BloonsTouhouDefense) {
			player = ((BloonsTouhouDefense) Gdx.app.getApplicationListener()).getPlayer();
		}
		
		if (bloonActor.getBloon().willPopBloon(damage)) {
			unregisterBloon(bloonActor);
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
				stage.addActor(generatedBloonActor);
				registerBloon(generatedBloonActor);
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
		if (onstageBloons.isEmpty()) {
			return false;
		}

		float gx = girlActor.getCenterX();
		float gy = girlActor.getCenterY();
		float visualRange = girlActor.getGirl().getVisualRange();
		
		int minCol = getGridCol(gx - visualRange - MAX_BLOON_RADIUS);
		int maxCol = getGridCol(gx + visualRange + MAX_BLOON_RADIUS);
		int minRow = getGridRow(gy - visualRange - MAX_BLOON_RADIUS);
		int maxRow = getGridRow(gy + visualRange + MAX_BLOON_RADIUS);

		BloonActor bestBloon = null;

		for (int c = minCol; c <= maxCol; c++) {
			for (int r = minRow; r <= maxRow; r++) {
				Array<BloonActor> cellBloons = grid[c][r];
				for (int i = 0; i < cellBloons.size; i++) {
					BloonActor bloonActor = cellBloons.get(i);
					float maxDist = visualRange + bloonActor.getCollisionRadius();
					float maxDistSq = maxDist * maxDist;
					float distSq = Map.distanceSquaredBetweenActors(girlActor, bloonActor);

					if (distSq < maxDistSq) {
						if (bestBloon == null || bloonActor.getBloon().getDistanceTravelled() > bestBloon.getBloon().getDistanceTravelled()) {
							bestBloon = bloonActor;
						}
					}
				}
			}
		}

		if (bestBloon == null) {
			return false;
		} else {
			BulletActor bulletActor = girlActor.createBulletActor(bestBloon);
			stage.addActor(bulletActor);
			return true;
		}
	}
	
	public void lookAtBloon(GirlActor girlActor) {
		if (onstageBloons.isEmpty()) {
			return;
		}

		float gx = girlActor.getCenterX();
		float gy = girlActor.getCenterY();
		float visualRange = girlActor.getGirl().getVisualRange();

		int minCol = getGridCol(gx - visualRange - MAX_BLOON_RADIUS);
		int maxCol = getGridCol(gx + visualRange + MAX_BLOON_RADIUS);
		int minRow = getGridRow(gy - visualRange - MAX_BLOON_RADIUS);
		int maxRow = getGridRow(gy + visualRange + MAX_BLOON_RADIUS);

		BloonActor bestBloon = null;

		for (int c = minCol; c <= maxCol; c++) {
			for (int r = minRow; r <= maxRow; r++) {
				Array<BloonActor> cellBloons = grid[c][r];
				for (int i = 0; i < cellBloons.size; i++) {
					BloonActor bloonActor = cellBloons.get(i);
					float maxDist = visualRange + bloonActor.getCollisionRadius();
					float maxDistSq = maxDist * maxDist;
					float distSq = Map.distanceSquaredBetweenActors(girlActor, bloonActor);

					if (distSq < maxDistSq) {
						if (bestBloon == null || bloonActor.getBloon().getDistanceTravelled() > bestBloon.getBloon().getDistanceTravelled()) {
							bestBloon = bloonActor;
						}
					}
				}
			}
		}

		if (bestBloon != null) {
			girlActor.lookAtBloon(bestBloon);
		}
	}
	
	public boolean containsBloon(BloonActor target) {
		return onstageBloons.contains(target);
	}

	public void removeBloonFromStage(BloonActor actor) {
		onstageBloons.remove(actor);
		unregisterBloon(actor);
	}
	
	public BloonActor getNewHomingTarget(BulletActor bulletActor) {
		if (onstageBloons.isEmpty()) {
			return null;
		}

		float bx = bulletActor.getCenterX();
		float by = bulletActor.getCenterY();
		int startCol = getGridCol(bx);
		int startRow = getGridRow(by);
		
		BloonActor closestBloon = null;
		float minDistSq = Float.MAX_VALUE;
		
		int maxRing = Math.max(GRID_COLS, GRID_ROWS);
		for (int ring = 0; ring <= maxRing; ring++) {
			int minCol = Math.max(0, startCol - ring);
			int maxCol = Math.min(GRID_COLS - 1, startCol + ring);
			int minRow = Math.max(0, startRow - ring);
			int maxRow = Math.min(GRID_ROWS - 1, startRow + ring);

			for (int c = minCol; c <= maxCol; c++) {
				for (int r = minRow; r <= maxRow; r++) {
					if (ring > 0 && c > minCol && c < maxCol && r > minRow && r < maxRow) {
						continue;
					}
					Array<BloonActor> cellBloons = grid[c][r];
					for (int i = 0; i < cellBloons.size; i++) {
						BloonActor actor = cellBloons.get(i);
						if (!bulletActor.hasDamagedBloon(actor)) {
							float distSq = Map.distanceSquaredBetweenActors(actor, bulletActor);
							if (distSq < minDistSq) {
								minDistSq = distSq;
								closestBloon = actor;
							}
						}
					}
				}
			}

			if (closestBloon != null) {
				float nextRingMinDist = (ring + 1) * CELL_SIZE;
				if (minDistSq <= nextRingMinDist * nextRingMinDist) {
					break;
				}
			}
		}
		
		return closestBloon;
	}
	
}
