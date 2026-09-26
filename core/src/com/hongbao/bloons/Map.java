package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.math.Vector2;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.actors.RenderableImageButton;
import com.hongbao.bloons.actors.RenderableLabel;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.helpers.ZIndex;
import com.hongbao.bloons.helpers.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Map {

	public static final String BACKGROUND_MAPS_FOLDER = "img/maps/";
	public static final int TILE_LENGTH = 50;
	public static final int TILE_HEIGHT = 50;

	private String backgroundImage;
	private BloonManager bloonManager;
	private List<Vector2> waypoints = new ArrayList<>();
	private Set<GirlActor> onStageGirls;
	private GirlActor selectedGirl;
	private Stage stage;
	private RenderableImageButton infoBackground;
	private RenderableLabel leftDataActor;
	private RenderableLabel rightDataActor;
	private RenderableLabel upgradeActor;
	private RenderableLabel sellActor;
	private boolean hoveringOverUpgrade;

	public Map(String backgroundImage, Stage stage) {
		this.backgroundImage = backgroundImage;
		this.bloonManager = new BloonManager(stage, this);
		onStageGirls = new HashSet<>();
		selectedGirl = null;
		this.stage = stage;
		hoveringOverUpgrade = false;

		if (stage != null) {
			Skin skin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));

			ImageButton infoBackground = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/girl_details_template.png")))));
			infoBackground.setPosition(1504, 4);
			this.infoBackground = new RenderableImageButton(infoBackground, ZIndex.MENU_ITEM_Z_INDEX);

			Label leftDataBackground = new Label("DATA", skin);
			leftDataBackground.setBounds(1510, 56, 292, 110);
			leftDataBackground.setColor(Color.BLACK);
			final RunnableAction leftDataLabelAction = new RunnableAction();
			leftDataLabelAction.setRunnable(() -> {
				Girl girl = getSelectedGirl().getGirl();
				Girl upgradedStats = girl.getUpgradedStats();
				if (hoveringOverUpgrade && girl.getUpgradeCost() != Girl.NO_UPGRADES_AVAILABLE) {
					leftDataActor.getActor().setText(
					 girl.getName() + " " + (girl.getLevel() + 1) + "\n" +
					  "Damage: " + girl.getDamage() + " (" + upgradedStats.getDamage() + ")\n" +
					  "Pierce: " + girl.getPierce() + " (" + upgradedStats.getPierce() + ")\n" +
					  "Cooldown: " + girl.getAttackDelay() + " (" + upgradedStats.getAttackDelay() + ")\n" +
					  "Sight: " + (int)girl.getVisualRange() + " (" + (int)upgradedStats.getVisualRange() + ")"
					);
				} else {
					leftDataActor.getActor().setText(
					 girl.getName() + " " + (girl.getLevel() + 1) + "\n" +
					  "Damage: " + girl.getDamage() + "\n" +
					  "Pierce: " + girl.getPierce() +"\n" +
					  "Cooldown: " + girl.getAttackDelay() +"\n" +
					  "Sight: " + (int)girl.getVisualRange()
					);
				}
			});
			leftDataBackground.addAction(Actions.repeat(RepeatAction.FOREVER, leftDataLabelAction));
			leftDataActor = new RenderableLabel(leftDataBackground, ZIndex.MENU_ITEM_Z_INDEX);

			Label rightDataBackground = new Label("DATA", skin);
			rightDataBackground.setBounds(1658, 56, 292, 110);
			rightDataBackground.setColor(Color.BLACK);
			final RunnableAction rightDataLabelAction = new RunnableAction();
			rightDataLabelAction.setRunnable(() -> {
				Girl girl = getSelectedGirl().getGirl();
				Girl upgradedStats = girl.getUpgradedStats();
				if (hoveringOverUpgrade && girl.getUpgradeCost() != Girl.NO_UPGRADES_AVAILABLE) {
					rightDataActor.getActor().setText(
					  "Range: " + (int)girl.getRange() + " (" + (int)upgradedStats.getRange() + ")\n" +
					  "Upgrade: " + girl.getUpgradeCostString() + " (" + upgradedStats.getUpgradeCostString() + ")\n" +
					  "Sell: $" + girl.getSellPrice() + "\n" +
					  " \n" +
					  " "
					);
				} else {
					rightDataActor.getActor().setText(
					 "Range: " + (int)girl.getRange() +"\n" +
					  "Upgrade: " + girl.getUpgradeCostString() + "\n" +
					  "Sell: $" + girl.getSellPrice() + "\n" +
					  " \n" +
					  " "
					);
				}
			});
			rightDataBackground.addAction(Actions.repeat(RepeatAction.FOREVER, rightDataLabelAction));
			rightDataActor = new RenderableLabel(rightDataBackground, ZIndex.MENU_ITEM_Z_INDEX);

			Label upgradeBackground = new Label("UPGRADE", skin);
			upgradeBackground.setBounds(1504, 4, 144, 50);
			upgradeBackground.setAlignment(Align.center);
			upgradeBackground.setColor(Color.BLUE);
			upgradeBackground.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					upgradeSelectedGirl();
				}

				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					hoveringOverUpgrade = true;
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					hoveringOverUpgrade = false;
				}
			});
			final RunnableAction upgradeLabelAction = new RunnableAction();
			upgradeLabelAction.setRunnable(() -> {
				if (getSelectedGirl() != null && !getSelectedGirl().isActive()) {
					upgradeBackground.setColor(Color.GRAY);
				} else if (getSelectedGirl().getGirl().getUpgradeCost() == Girl.NO_UPGRADES_AVAILABLE) {
					upgradeBackground.setText("FULLY UPGRADED");
					upgradeBackground.setColor(Color.GRAY);
				} else {
					Player player = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer();
					upgradeBackground.setText("UPGRADE");
					if (player.getMoney() >= getSelectedGirl().getGirl().getUpgradeCost()) {
						upgradeBackground.setColor(Color.BLUE);
					} else {
						upgradeBackground.setColor(Color.RED);
					}
				}
			});
			upgradeBackground.addAction(Actions.repeat(RepeatAction.FOREVER, upgradeLabelAction));
			upgradeActor = new RenderableLabel(upgradeBackground, ZIndex.MENU_ITEM_Z_INDEX);

			Label sellBackground = new Label("SELL", skin);
			sellBackground.setBounds(1652, 4, 144, 50);
			sellBackground.setAlignment(Align.center);
			sellBackground.setColor(Color.RED);
			sellBackground.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					sellSelectedGirl();
				}
			});

			final RunnableAction sellLabelAction = new RunnableAction();
			sellLabelAction.setRunnable(() -> {
				if (getSelectedGirl() != null && !getSelectedGirl().isActive()) {
					sellBackground.setColor(Color.GRAY);
				} else {
					sellBackground.setColor(Color.RED);
				}
			});
			sellBackground.addAction(Actions.repeat(RepeatAction.FOREVER, sellLabelAction));
			sellActor = new RenderableLabel(sellBackground, ZIndex.MENU_ITEM_Z_INDEX);
		}
	}

	public void setWaypoints(List<Vector2> waypoints) {
		this.waypoints = waypoints;
	}

	public List<Vector2> getWaypoints() {
		return waypoints;
	}

	public void setDirections(Pair<Float, Float>[][] directions) {
		// Retained for backward compatibility
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getBackgroundImageFilePath() {
		return BACKGROUND_MAPS_FOLDER + backgroundImage;
	}

	public GirlActor getSelectedGirl() {
		return selectedGirl;
	}

	public void setSelectedGirl(GirlActor girlActor) {
		if (selectedGirl != null && !selectedGirl.isActive()) {
			selectedGirl.remove();
		}

		selectedGirl = girlActor;
		if (girlActor == null) {
			hideGirlDetailsModule();
		} else {
			showGirlDetailsModule();
		}
	}

	public Pair<Float, Float> getDirection(float balloonX, float balloonY) {
		if (waypoints == null || waypoints.size() < 2) {
			return new Pair<>(0f, 0f);
		}

		float minDist2 = Float.MAX_VALUE;
		float bestUx = 0f;
		float bestUy = 0f;

		for (int i = 0; i < waypoints.size() - 1; i++) {
			Vector2 a = waypoints.get(i);
			Vector2 b = waypoints.get(i + 1);

			float vx = b.x - a.x;
			float vy = b.y - a.y;
			float len2 = vx * vx + vy * vy;
			if (len2 == 0) continue;

			float len = (float) Math.sqrt(len2);
			float ux = vx / len;
			float uy = vy / len;

			float t = ((balloonX - a.x) * vx + (balloonY - a.y) * vy) / len2;
			float tClamped = Math.max(0f, Math.min(1f, t));

			float projX = a.x + tClamped * vx;
			float projY = a.y + tClamped * vy;

			float dx = balloonX - projX;
			float dy = balloonY - projY;
			float dist2 = dx * dx + dy * dy;

			if (dist2 < minDist2 || (dist2 == minDist2 && t >= 1.0f)) {
				minDist2 = dist2;
				bestUx = ux;
				bestUy = uy;
			}
		}

		return new Pair<>(bestUx, bestUy);
	}

	public BloonManager getBloonManager() {
		return bloonManager;
	}

	public boolean canPlaceGirl(GirlActor girlActor) {
		float x = girlActor.getCenterX();
		float y = girlActor.getCenterY();
		float r = girlActor.getCollisionRadius();

		if (y < 0 || y > 900 || x < 0 || x > 1500) {
			return false;
		}

		float trackRadius = TILE_LENGTH / 2f;

		if (waypoints != null) {
			for (int i = 0; i < waypoints.size() - 1; i++) {
				Vector2 a = waypoints.get(i);
				Vector2 b = waypoints.get(i + 1);
				if (distanceToSegment(x, y, a.x, a.y, b.x, b.y) < r + trackRadius) {
					return false;
				}
			}
		}

		for (GirlActor stageActor : onStageGirls) {
			if (distanceBetweenActors(girlActor, stageActor) < r + stageActor.getCollisionRadius()) {
				return false;
			}
		}

		return true;
	}

	public void placeGirl(GirlActor girlActor) {
		girlActor.setActive(true);
		onStageGirls.add(girlActor);
		stage.addActor(girlActor);
		selectedGirl = girlActor;
		girlActor.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				setSelectedGirl(girlActor);
				event.setStage(null); // a somewhat hacky way of communicating to the stage that this event has already handled.
			}
		});
	}

	public void placeSpellCard() {
		if (selectedGirl != null) {
			stage.addActor(selectedGirl.createSpellCardActor());
		}
	}

	public static float distanceToSegment(float px, float py, float ax, float ay, float bx, float by) {
		float vx = bx - ax;
		float vy = by - ay;
		float len2 = vx * vx + vy * vy;
		if (len2 == 0) {
			return (float) Math.hypot(px - ax, py - ay);
		}
		float t = ((px - ax) * vx + (py - ay) * vy) / len2;
		if (t < 0) t = 0;
		else if (t > 1) t = 1;
		float projX = ax + t * vx;
		float projY = ay + t * vy;
		return (float) Math.hypot(px - projX, py - projY);
	}
	
	public static float distanceBetweenActors(RenderableActor actor1, RenderableActor actor2) {
		return (float)Math.sqrt(
		 Math.pow(actor1.getCenterX() - actor2.getCenterX(), 2) + Math.pow(actor1.getCenterY() - actor2.getCenterY(), 2));
	}

	public void showGirlDetailsModule() {
		hideGirlDetailsModule();

		Girl girl = getSelectedGirl().getGirl();

		leftDataActor.getActor().setText(
		 		girl.getName() + " " + (girl.getLevel() + 1) + "\n" +
				"Damage: " + girl.getDamage() + "\n" +
				"Pierce: " + girl.getPierce() +"\n" +
				"Cooldown: " + girl.getAttackDelay() +"\n" +
				"Sight: " + (int)girl.getVisualRange()
		);
		rightDataActor.getActor().setText(
				"Range: " + (int)girl.getRange() +"\n" +
				"Upgrade: " + girl.getUpgradeCostString() + "\n" +
				"Sell: $" + girl.getSellPrice() + "\n" +
				" \n" +
				" "
		);
		stage.addActor(infoBackground);
		stage.addActor(leftDataActor);
		stage.addActor(rightDataActor);
		stage.addActor(upgradeActor);
		stage.addActor(sellActor);
	}

	public void hideGirlDetailsModule() {
		infoBackground.remove();
		leftDataActor.remove();
		rightDataActor.remove();
		upgradeActor.remove();
		sellActor.remove();
	}

	public void upgradeSelectedGirl() {
        if (getSelectedGirl() != null && !getSelectedGirl().isActive()) {
            // We're in the middle of placing a girl, so we shouldn't be able to set it yet.
            return;
        }

		Player player = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer();
		GirlActor selectedGirl = getSelectedGirl();
		
		if (selectedGirl.getGirl().canUpgrade(player.getMoney())) {
			int cost = selectedGirl.getGirl().upgrade();
			player.spendMoney(cost);
			showGirlDetailsModule();
		}
	}

	public void sellSelectedGirl() {
        if (getSelectedGirl() != null && !getSelectedGirl().isActive()) {
            // We're in the middle of placing a girl, so we shouldn't be able to set it yet.
            return;
        }

        Player player = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer();
        GirlActor selectedGirl = getSelectedGirl();

		player.earnMoney(selectedGirl.getGirl().getSellPrice());
		hideGirlDetailsModule();
		onStageGirls.remove(selectedGirl);
		selectedGirl.remove();
		setSelectedGirl(null);
	}
	
}
