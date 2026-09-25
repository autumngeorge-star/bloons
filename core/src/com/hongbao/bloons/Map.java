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
import com.hongbao.bloons.entities.UpgradeNode;
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
	private Pair<Float, Float>[][] directions;
	private Set<GirlActor> onStageGirls;
	private GirlActor selectedGirl;
	private Stage stage;
	private Skin skin;
	private RenderableImageButton infoBackground;
	private RenderableLabel leftDataActor;
	private RenderableLabel rightDataActor;
	private List<RenderableLabel> upgradeActors;
	private RenderableLabel sellActor;
	private boolean hoveringOverUpgrade;
	private UpgradeNode hoveredUpgradeNode;

	public Map(String backgroundImage, Stage stage) {
		this.backgroundImage = backgroundImage;
		this.bloonManager = new BloonManager(stage, this);
		onStageGirls = new HashSet<>();
		selectedGirl = null;
		this.stage = stage;
		hoveringOverUpgrade = false;
		hoveredUpgradeNode = null;
		upgradeActors = new ArrayList<>();

		skin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));

		ImageButton infoBackgroundButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/girl_details_template.png")))));
		infoBackgroundButton.setPosition(1504, 4);
		this.infoBackground = new RenderableImageButton(infoBackgroundButton, ZIndex.MENU_ITEM_Z_INDEX);

		Label leftDataBackground = new Label("DATA", skin);
		leftDataBackground.setBounds(1510, 56, 292, 110);
		leftDataBackground.setColor(Color.BLACK);
		final RunnableAction leftDataLabelAction = new RunnableAction();
		leftDataLabelAction.setRunnable(() -> {
			if (getSelectedGirl() == null || getSelectedGirl().getGirl() == null) {
				return;
			}
			Girl girl = getSelectedGirl().getGirl();
			UpgradeNode previewNode = hoveredUpgradeNode;
			if (previewNode == null && hoveringOverUpgrade && !girl.getAvailableUpgrades().isEmpty()) {
				previewNode = girl.getAvailableUpgrades().get(0);
			}
			if (previewNode != null) {
				Girl upgradedStats = girl.getUpgradedStats(previewNode);
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
			if (getSelectedGirl() == null || getSelectedGirl().getGirl() == null) {
				return;
			}
			Girl girl = getSelectedGirl().getGirl();
			UpgradeNode previewNode = hoveredUpgradeNode;
			if (previewNode == null && hoveringOverUpgrade && !girl.getAvailableUpgrades().isEmpty()) {
				previewNode = girl.getAvailableUpgrades().get(0);
			}
			if (previewNode != null) {
				Girl upgradedStats = girl.getUpgradedStats(previewNode);
				rightDataActor.getActor().setText(
				  "Range: " + (int)girl.getRange() + " (" + (int)upgradedStats.getRange() + ")\n" +
				  "Upgrade: $" + previewNode.getUpgradeCost() + "\n" +
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

	public boolean canPlaceGirl(GirlActor girlActor) {
		float x = girlActor.getCenterX() + TILE_LENGTH;
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
				event.setStage(null);
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

		List<UpgradeNode> availableUpgrades = girl.getAvailableUpgrades();
		if (availableUpgrades.isEmpty()) {
			Label upgradeBackground = new Label("FULLY UPGRADED", skin);
			upgradeBackground.setBounds(1504, 4, 144, 50);
			upgradeBackground.setAlignment(Align.center);
			upgradeBackground.setColor(Color.GRAY);
			RenderableLabel renderableLabel = new RenderableLabel(upgradeBackground, ZIndex.MENU_ITEM_Z_INDEX);
			upgradeActors.add(renderableLabel);
		} else {
			int n = availableUpgrades.size();
			float totalHeight = 50f;
			float btnHeight = totalHeight / n;
			for (int i = 0; i < n; i++) {
				final UpgradeNode branch = availableUpgrades.get(i);
				float btnY = 4 + (n - 1 - i) * btnHeight;
				String buttonText = (n == 1) ? "UPGRADE" : branch.getTitle();
				Label upgradeBackground = new Label(buttonText, skin);
				upgradeBackground.setBounds(1504, btnY, 144, btnHeight);
				upgradeBackground.setAlignment(Align.center);
				upgradeBackground.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						upgradeSelectedGirl(branch);
					}

					@Override
					public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
						hoveringOverUpgrade = true;
						hoveredUpgradeNode = branch;
					}

					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
						hoveringOverUpgrade = false;
						if (hoveredUpgradeNode == branch) {
							hoveredUpgradeNode = null;
						}
					}
				});

				final RunnableAction upgradeLabelAction = new RunnableAction();
				upgradeLabelAction.setRunnable(() -> {
					if (getSelectedGirl() != null && !getSelectedGirl().isActive()) {
						upgradeBackground.setColor(Color.GRAY);
					} else {
						Player player = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer();
						if (player.getMoney() >= branch.getUpgradeCost()) {
							upgradeBackground.setColor(Color.BLUE);
						} else {
							upgradeBackground.setColor(Color.RED);
						}
					}
				});
				upgradeBackground.addAction(Actions.repeat(RepeatAction.FOREVER, upgradeLabelAction));
				RenderableLabel renderableLabel = new RenderableLabel(upgradeBackground, ZIndex.MENU_ITEM_Z_INDEX);
				upgradeActors.add(renderableLabel);
			}
		}

		stage.addActor(infoBackground);
		stage.addActor(leftDataActor);
		stage.addActor(rightDataActor);
		for (RenderableLabel actor : upgradeActors) {
			stage.addActor(actor);
		}
		stage.addActor(sellActor);
	}

	public void hideGirlDetailsModule() {
		infoBackground.remove();
		leftDataActor.remove();
		rightDataActor.remove();
		sellActor.remove();
		for (RenderableLabel actor : upgradeActors) {
			actor.remove();
		}
		upgradeActors.clear();
		hoveredUpgradeNode = null;
		hoveringOverUpgrade = false;
	}

	public void upgradeSelectedGirl() {
		if (getSelectedGirl() == null || getSelectedGirl().getGirl() == null) {
			return;
		}
		List<UpgradeNode> available = getSelectedGirl().getGirl().getAvailableUpgrades();
		if (!available.isEmpty()) {
			upgradeSelectedGirl(available.get(0));
		}
	}

	public void upgradeSelectedGirl(UpgradeNode targetNode) {
		if (getSelectedGirl() != null && !getSelectedGirl().isActive()) {
			return;
		}
		if (targetNode == null) {
			return;
		}

		Player player = ((BloonsTouhouDefense)Gdx.app.getApplicationListener()).getPlayer();
		GirlActor selectedGirl = getSelectedGirl();
		
		if (selectedGirl.getGirl().canUpgrade(targetNode, player.getMoney())) {
			int cost = selectedGirl.getGirl().upgrade(targetNode);
			player.spendMoney(cost);
			showGirlDetailsModule();
		}
	}

	public void sellSelectedGirl() {
		if (getSelectedGirl() != null && !getSelectedGirl().isActive()) {
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
