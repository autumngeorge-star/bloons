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
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.actors.RenderableImageButton;
import com.hongbao.bloons.actors.RenderableLabel;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.helpers.ZIndex;
import com.hongbao.bloons.helpers.Pair;

import java.util.HashSet;
import java.util.Set;


public class Map {

	public static final String BACKGROUND_MAPS_FOLDER = "img/maps/";
	public static final int TILE_LENGTH = 50;
	public static final int TILE_HEIGHT = 50;

	private String backgroundImage;
	private BloonManager bloonManager;
	private Pair<Float, Float>[][] directions;
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

	public void setDirections(Pair<Float, Float>[][] directions) {
		this.directions = directions;
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
		// Each "direction tile" is 50x50 px, maybe some minor tweaking later
		// There is an extra tile on the left and right of the screen so we have a smol x offset for that
		int xTile = (int)(balloonX + TILE_LENGTH) / TILE_LENGTH;
		int yTile = (int)balloonY / TILE_HEIGHT;
		if (xTile < directions.length && yTile < directions[xTile].length) {
			return directions[xTile][yTile];
		} else {
			return new Pair<>(0f, 0f);
		}
	}

	public BloonManager getBloonManager() {
		return bloonManager;
	}

	public Set<GirlActor> getOnStageGirls() {
		return onStageGirls;
	}

	public boolean canPlaceGirl(GirlActor girlActor) {
		// You can't place a girl down if it violates any of the following rules:
		// It's out of bounds
		// It's colliding with the bloon path
		// It's colliding with another girl

		float x = girlActor.getCenterX() + TILE_LENGTH; // x is always offset by one tile because we have that extra tile on the left
		float y = girlActor.getCenterY();
		float r = girlActor.getCollisionRadius();

		if (y < 0 || y > 900 || x < 0 || x > 1550) {
			return false;
		}
		
		for (int i = 0; i < directions.length; i++) {
			for (int j = 0; j < directions[i].length; j++) {
				if (directions[i][j] != null && (directions[i][j].getFirst() != 0 || directions[i][j].getSecond() != 0)) {
					if (Math.abs(x - getCenterXOfTile(i)) < r + (TILE_LENGTH / 2f) && Math.abs(y - getCenterYOfTile(j)) < r + (TILE_HEIGHT / 2f)) {
						return false;
					}
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
	
	private static float getCenterXOfTile(int tile) {
		return tile * TILE_LENGTH + (TILE_LENGTH / 2f);
	}
	
	private static float getCenterYOfTile(int tile) {
		return tile * TILE_HEIGHT + (TILE_HEIGHT / 2f);
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
