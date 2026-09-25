package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.RenderableImageButton;
import com.hongbao.bloons.actors.RenderableLabel;
import com.hongbao.bloons.comparators.SortByZIndex;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.factories.TowerDefinition;
import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.ZIndex;

import java.util.ArrayList;
import java.util.List;


public class BloonsTouhouDefense implements ApplicationListener {
	
	public static final int MONEY = 1000;
	public static final int HEALTH = 100;
	public static final boolean HELLA_BLOONS = false;
	
	public boolean paused;
	public boolean tripleSpeed;
	public boolean autoContinue;

	private Stage stage;
	private Player player;
	private Map map;
	private MusicPlayer musicPlayer;
	private ShapeRenderer shapeRenderer;
	public List<RenderableImageButton> instructions;
	
	
	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(1800, 900);
		paused = false;
		tripleSpeed = false;
		autoContinue = false;
		stage = new Stage();
		player = new Player(MONEY, HEALTH);
		musicPlayer = new MusicPlayer();
		shapeRenderer = new ShapeRenderer();
		instructions = new ArrayList<>();

		final RunnableAction bloonCreationAction = new RunnableAction();
		bloonCreationAction.setRunnable(() -> map.getBloonManager().createBloons());
		stage.addAction(Actions.repeat(RepeatAction.FOREVER, bloonCreationAction));

		Gdx.input.setInputProcessor(stage);
		
		createMap();
		createMenu();
		createInstructions();
		musicPlayer.playTitleMusic();
	}

	private void createInstructions() {
		ImageButton instructions1 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/instructions/title.png")))));
		instructions1.setPosition(376, 300);
		instructions1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				updateInstructions();
			}
		});
		ImageButton instructions2 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/instructions/objective.png")))));
		instructions2.setPosition(376, 300);
		instructions2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				updateInstructions();
			}
		});
		ImageButton instructions3 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/instructions/bloons.png")))));
		instructions3.setPosition(376, 300);
		instructions3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				updateInstructions();
			}
		});
		ImageButton instructions4 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/instructions/blimps.png")))));
		instructions4.setPosition(376, 300);
		instructions4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				updateInstructions();
			}
		});
		ImageButton instructions5 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/instructions/girls.png")))));
		instructions5.setPosition(376, 300);
		instructions5.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				updateInstructions();
			}
		});
		ImageButton instructions6 = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/instructions/shortcuts.png")))));
		instructions6.setPosition(376, 300);
		instructions6.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				updateInstructions();
			}
		});
		
		instructions.add(new RenderableImageButton(instructions1, ZIndex.MENU_ITEM_Z_INDEX));
		instructions.add(new RenderableImageButton(instructions2, ZIndex.MENU_ITEM_Z_INDEX));
		instructions.add(new RenderableImageButton(instructions3, ZIndex.MENU_ITEM_Z_INDEX));
		instructions.add(new RenderableImageButton(instructions4, ZIndex.MENU_ITEM_Z_INDEX));
		instructions.add(new RenderableImageButton(instructions5, ZIndex.MENU_ITEM_Z_INDEX));
		instructions.add(new RenderableImageButton(instructions6, ZIndex.MENU_ITEM_Z_INDEX));
		
		stage.addActor(instructions.get(0));
	}
	
	private void updateInstructions() {
		instructions.get(0).remove();
		instructions.remove(0);

		if (instructions.size() != 0) {
			stage.addActor(instructions.get(0));
		}
	}
	
	private void createMenu() {
		Skin skin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));

		ImageButton background = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/header.png")))));
		background.setPosition(1500, 0);
		stage.addActor(new RenderableImageButton(background, ZIndex.MENU_Z_INDEX));

		Label title = new Label("Bloons Touhou Defense\nLevel 1", skin);
		title.setPosition(1600, 820);
		title.setBounds(1500, 800, 300, 100);
		title.setFontScale(1.5f,1.5f);
		title.setAlignment(Align.center);
		title.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				map.getBloonManager().nextLevel();
			}
		});
		final RunnableAction titleAction = new RunnableAction();
		titleAction.setRunnable(() -> {
			Label titleActor = (Label) titleAction.getActor();
			if (map.getBloonManager().canGoToNextLevel()) {
				if (autoContinue) {
					map.getBloonManager().nextLevel();
				} else {
					if (map.getBloonManager().getLevel() == 0) {
						titleActor.setText("START\n(click here)");
					} else {
						titleActor.setText("NEXT LEVEL\n(click here)");
					}
					titleActor.setColor(Color.BLACK);
				}
			} else {
				if (map.getBloonManager().hasWonGame()) {
					titleActor.setText("YOU WIN!");
					titleActor.setColor(Color.GOLD);
				} else {
					titleActor.setText("Bloons Touhou Defense\nLevel " + (map.getBloonManager().getLevel()));
					titleActor.setColor(Color.WHITE);
				}
			}
		});
		title.addAction(Actions.repeat(RepeatAction.FOREVER, titleAction));
		stage.addActor(new RenderableLabel(title, ZIndex.MENU_ITEM_Z_INDEX));


		Label moneyLabel = new Label(String.valueOf(player.getMoney()), skin);
		moneyLabel.setPosition(1680, 765);
		moneyLabel.setFontScale(1.5f,1.5f);
		final RunnableAction moneyLabelAction = new RunnableAction();
		moneyLabelAction.setRunnable(() -> ((Label)moneyLabelAction.getActor()).setText(String.valueOf(player.getMoney())));
		moneyLabel.addAction(Actions.repeat(RepeatAction.FOREVER, moneyLabelAction));
		stage.addActor(new RenderableLabel(moneyLabel, ZIndex.MENU_ITEM_Z_INDEX));
		
		Label healthLabel = new Label(String.valueOf(player.getHealth()), skin);
		healthLabel.setPosition(1540, 765);
		healthLabel.setFontScale(1.5f,1.5f);
		final RunnableAction healthLabelAction = new RunnableAction();
		healthLabelAction.setRunnable(() -> {
			((Label)healthLabelAction.getActor()).setText(String.valueOf(player.getHealth()));
			
			if (player.getHealth() == 0) {
				pause();
			}
		});
		healthLabel.addAction(Actions.repeat(RepeatAction.FOREVER, healthLabelAction));
		stage.addActor(new RenderableLabel(healthLabel, ZIndex.MENU_ITEM_Z_INDEX));
		
		int startY = 676;
		int rowHeight = 72;
		int costYOffset = 24;
		int buttonX = 1504;
		int costX = 1680;

		List<TowerDefinition> registeredTowers = GirlFactory.getRegisteredTowers();
		for (int i = 0; i < registeredTowers.size(); i++) {
			final TowerDefinition towerDef = registeredTowers.get(i);
			float buttonY = startY - i * rowHeight;
			if (buttonY < 0) {
				continue;
			}
			float costY = buttonY + costYOffset;

			ImageButton purchaseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(towerDef.getUiIconPath())))));
			purchaseButton.setPosition(buttonX, buttonY);
			purchaseButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (instructions.isEmpty()) {
						map.setSelectedGirl(null);
						Girl girl = towerDef.createGirl();
						if (player.canPurchaseGirl(girl)) {
							GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
							map.setSelectedGirl(girlActor);
							stage.addActor(girlActor);
						}
					}
				}
			});
			stage.addActor(new RenderableImageButton(purchaseButton, ZIndex.MENU_Z_INDEX));

			Label costLabel = new Label(String.valueOf(towerDef.createGirl().getCost()), skin);
			costLabel.setPosition(costX, costY);
			costLabel.setFontScale(1.5f, 1.5f);
			costLabel.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
			stage.addActor(new RenderableLabel(costLabel, ZIndex.MENU_ITEM_Z_INDEX));
		}
	}
	
	private RunnableAction createNewCostLabelAction() {
		// Apparently they don't like sharing
		RunnableAction costLabelAction = new RunnableAction();
		costLabelAction.setRunnable(() -> {
			Label label = (Label)costLabelAction.getActor();
			int cost = Integer.parseInt(label.getText().toString());
			
			if (cost <= player.getMoney()) {
				label.setColor(Color.WHITE);
			} else {
				label.setColor(Color.RED);
			}
		});
		return costLabelAction;
	}
	
	public void createMap() {
		stage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (event.getStage() != null) {
					map.setSelectedGirl(null);
				}
			}
		});
		
		map = MapFactory.createHeaterMap(stage);
		
		Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(map.getBackgroundImageFilePath()))));
		ImageButton backgroundMap = new ImageButton(drawable);
		backgroundMap.setPosition(0, 0);
		stage.addActor(backgroundMap);
	}
	
	public Map getMap() {
		return map;
	}
	
	public Player getPlayer() {
		return player;
	}

	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}
	
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		if (!paused) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			if (tripleSpeed) {
				for (int x = 0; x < 9; x++) {
					stage.act(Gdx.graphics.getDeltaTime());
				}
			} else {
				for (int x = 0; x < 3; x++) {
					stage.act(Gdx.graphics.getDeltaTime());
				}
			}

			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				if (map.getSelectedGirl() != null && !map.getSelectedGirl().isActive()) {
					if (map.canPlaceGirl(map.getSelectedGirl())) {
						map.placeGirl(map.getSelectedGirl());
						player.purchaseGirl(map.getSelectedGirl().getGirl());
					}
				}
			}
			
			if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
				if (map.getSelectedGirl() != null && !map.getSelectedGirl().isActive()) {
					map.getSelectedGirl().remove();
					map.setSelectedGirl(null);
				}
			}
			
			Girl girl = null;
			
			int[] numKeys = {
				Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4,
				Input.Keys.NUM_5, Input.Keys.NUM_6, Input.Keys.NUM_7, Input.Keys.NUM_8,
				Input.Keys.NUM_9, Input.Keys.NUM_0
			};
			List<TowerDefinition> registeredTowers = GirlFactory.getRegisteredTowers();
			for (int i = 0; i < registeredTowers.size() && i < numKeys.length; i++) {
				if (Gdx.input.isKeyJustPressed(numKeys[i])) {
					map.setSelectedGirl(null);
					girl = registeredTowers.get(i).createGirl();
					break;
				}
			}

			if (girl == null) {
				if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
					tripleSpeed = !tripleSpeed;
				} else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
					if (instructions.isEmpty()) {
						map.getBloonManager().nextLevel();
					} else {
						updateInstructions();
					}
				} else if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
					musicPlayer.toggleMusic();
				} else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
					getMap().placeSpellCard();
				} else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
					autoContinue = !autoContinue;
				}
			}
			
			if (girl != null) {
				if (player.canPurchaseGirl(girl)) {
					GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
					map.setSelectedGirl(girlActor);
					stage.addActor(girlActor);
				}
			}
			
			stage.getActors().sort(new SortByZIndex());
		}
		stage.draw();
		
		if (map.getSelectedGirl() != null) {
			// Draw the range of collision, the range of sight, and range of... well, range
			if (!map.getSelectedGirl().isActive()) {
				map.getSelectedGirl().setX(Gdx.input.getX() - map.getSelectedGirl().getTextureRegion().getTexture().getWidth() / 2f);
				map.getSelectedGirl().setY(Gdx.graphics.getHeight() - (Gdx.input.getY() + map.getSelectedGirl().getTextureRegion().getTexture().getHeight() / 2f));
				if (!map.canPlaceGirl(map.getSelectedGirl())) {
					shapeRenderer.setColor(Color.RED);
				} else {
					shapeRenderer.setColor(Color.WHITE);
				}
			}
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.circle(map.getSelectedGirl().getCenterX(), map.getSelectedGirl().getCenterY(), map.getSelectedGirl().getCollisionRadius());
			shapeRenderer.circle(map.getSelectedGirl().getCenterX(), map.getSelectedGirl().getCenterY(), map.getSelectedGirl().getGirl().getVisualRange());
			shapeRenderer.circle(map.getSelectedGirl().getCenterX(), map.getSelectedGirl().getCenterY(), map.getSelectedGirl().getGirl().getRange());
			shapeRenderer.end();
		}
	}

	@Override
	public void pause() {
		paused = true;
		musicPlayer.pause();
	}

	@Override
	public void resume() {
		paused = false;
		musicPlayer.resume();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
