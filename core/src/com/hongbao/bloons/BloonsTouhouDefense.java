package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
	private TextureAtlas actorsAtlas;
	
	public TextureAtlas getActorsAtlas() {
		return actorsAtlas;
	}
	
	
	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(1800, 900);
		paused = false;
		tripleSpeed = false;
		autoContinue = false;
		actorsAtlas = new TextureAtlas(Gdx.files.internal("actors.atlas"));
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
		
		ImageButton purchaseReimu = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/reimu_box.png")))));
		purchaseReimu.setPosition(1504, 676);
		purchaseReimu.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createReimu();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseReimu, ZIndex.MENU_Z_INDEX));
		
		Label reimuCost = new Label(String.valueOf(GirlFactory.createReimu().getCost()), skin);
		reimuCost.setPosition(1680, 700);
		reimuCost.setFontScale(1.5f,1.5f);
		reimuCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(reimuCost, ZIndex.MENU_ITEM_Z_INDEX));
		
		ImageButton purchaseYukari = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/yukari_box.png")))));
		purchaseYukari.setPosition(1504, 604);
		purchaseYukari.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createYukari();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseYukari, ZIndex.MENU_Z_INDEX));
		
		Label yukariCost = new Label(String.valueOf(GirlFactory.createYukari().getCost()), skin);
		yukariCost.setPosition(1680, 628);
		yukariCost.setFontScale(1.5f,1.5f);
		yukariCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(yukariCost, ZIndex.MENU_ITEM_Z_INDEX));
		
		ImageButton purchaseMarisa = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/marisa_box.png")))));
		purchaseMarisa.setPosition(1504, 532);
		purchaseMarisa.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createMarisa();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseMarisa, ZIndex.MENU_Z_INDEX));
		
		Label marisaCost = new Label(String.valueOf(GirlFactory.createMarisa().getCost()), skin);
		marisaCost.setPosition(1680, 556);
		marisaCost.setFontScale(1.5f,1.5f);
		marisaCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(marisaCost, ZIndex.MENU_ITEM_Z_INDEX));
		
		ImageButton purchaseAlice = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/alice_box.png")))));
		purchaseAlice.setPosition(1504, 460);
		purchaseAlice.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createAlice();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseAlice, ZIndex.MENU_Z_INDEX));
		
		Label aliceCost = new Label(String.valueOf(GirlFactory.createAlice().getCost()), skin);
		aliceCost.setPosition(1680, 484);
		aliceCost.setFontScale(1.5f,1.5f);
		aliceCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(aliceCost, ZIndex.MENU_ITEM_Z_INDEX));
		
		ImageButton purchaseSakuya = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/sakuya_box.png")))));
		purchaseSakuya.setPosition(1504, 388);
		purchaseSakuya.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createSakuya();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseSakuya, ZIndex.MENU_Z_INDEX));
		
		Label sakuyaCost = new Label(String.valueOf(GirlFactory.createSakuya().getCost()), skin);
		sakuyaCost.setPosition(1680, 412);
		sakuyaCost.setFontScale(1.5f,1.5f);
		sakuyaCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(sakuyaCost, ZIndex.MENU_ITEM_Z_INDEX));
		
		ImageButton purchaseRemilia = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/remilia_box.png")))));
		purchaseRemilia.setPosition(1504, 316);
		purchaseRemilia.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createRemilia();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseRemilia, ZIndex.MENU_Z_INDEX));
		
		Label remiliaCost = new Label(String.valueOf(GirlFactory.createRemilia().getCost()), skin);
		remiliaCost.setPosition(1680, 340);
		remiliaCost.setFontScale(1.5f,1.5f);
		remiliaCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(remiliaCost, ZIndex.MENU_ITEM_Z_INDEX));
		
		ImageButton purchaseYoumu = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/youmu_box.png")))));
		purchaseYoumu.setPosition(1504, 244);
		purchaseYoumu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createYoumu();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseYoumu, ZIndex.MENU_Z_INDEX));
		
		Label youmuCost = new Label(String.valueOf(GirlFactory.createYoumu().getCost()), skin);
		youmuCost.setPosition(1680, 268);
		youmuCost.setFontScale(1.5f,1.5f);
		youmuCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(youmuCost, ZIndex.MENU_ITEM_Z_INDEX));
		
		ImageButton purchaseYuyuko = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/yuyuko_box.png")))));
		purchaseYuyuko.setPosition(1504, 172);
		purchaseYuyuko.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (instructions.isEmpty()) {
					map.setSelectedGirl(null);
					Girl girl = GirlFactory.createYuyuko();
					if (player.canPurchaseGirl(girl)) {
						GirlActor girlActor = new GirlActor(girl, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
						map.setSelectedGirl(girlActor);
						stage.addActor(girlActor);
					}
				}
			}
		});
		stage.addActor(new RenderableImageButton(purchaseYuyuko, ZIndex.MENU_Z_INDEX));
		
		Label yuyukoCost = new Label(String.valueOf(GirlFactory.createYuyuko().getCost()), skin);
		yuyukoCost.setPosition(1680, 196);
		yuyukoCost.setFontScale(1.5f,1.5f);
		yuyukoCost.addAction(Actions.repeat(RepeatAction.FOREVER, createNewCostLabelAction()));
		stage.addActor(new RenderableLabel(yuyukoCost, ZIndex.MENU_ITEM_Z_INDEX));
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
			
			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createReimu();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createYukari();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createMarisa();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createAlice();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createSakuya();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createRemilia();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createYoumu();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
				map.setSelectedGirl(null);
				girl = GirlFactory.createYuyuko();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
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
				map.getSelectedGirl().setX(Gdx.input.getX() - map.getSelectedGirl().getTextureRegion().getRegionWidth() / 2f);
				map.getSelectedGirl().setY(Gdx.graphics.getHeight() - (Gdx.input.getY() + map.getSelectedGirl().getTextureRegion().getRegionHeight() / 2f));
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
		if (actorsAtlas != null) {
			actorsAtlas.dispose();
		}
		if (stage != null) {
			stage.dispose();
		}
	}

}
