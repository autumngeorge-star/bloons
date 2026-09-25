package com.hongbao.bloons.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.hongbao.bloons.MapType;
import com.hongbao.bloons.ProgressionManager;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.helpers.ZIndex;

import java.util.function.Consumer;

public class MapSelectionOverlay extends RenderableActor {

	private final ProgressionManager progressionManager;
	private final MapType currentMapType;
	private final Consumer<MapType> onMapSelected;
	private final Runnable onClose;
	private Table mainTable;
	private Label feedbackLabel;
	private Skin skin;

	public MapSelectionOverlay(ProgressionManager progressionManager, MapType currentMapType, Consumer<MapType> onMapSelected, Runnable onClose) {
		this.progressionManager = progressionManager;
		this.currentMapType = currentMapType;
		this.onMapSelected = onMapSelected;
		this.onClose = onClose;

		skin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));
		mainTable = new Table(skin);
		mainTable.setBounds(100, 75, 1300, 750);
		if (skin.has("dialogDim", com.badlogic.gdx.scenes.scene2d.utils.Drawable.class)) {
			mainTable.setBackground(skin.getDrawable("dialogDim"));
		} else if (skin.has("default-window", com.badlogic.gdx.scenes.scene2d.utils.Drawable.class)) {
			mainTable.setBackground(skin.getDrawable("default-window"));
		}

		setActor(mainTable);
		setZIndex(ZIndex.MENU_ITEM_Z_INDEX + 10);

		buildUI();
	}

	private void buildUI() {
		mainTable.clear();

		Label titleLabel = new Label("MAP SELECTION", skin);
		titleLabel.setFontScale(2.0f);
		titleLabel.setAlignment(Align.center);
		mainTable.add(titleLabel).colspan(3).padTop(20).padBottom(20).expandX().fillX();
		mainTable.row();

		Table cardsTable = new Table(skin);
		cardsTable.defaults().pad(15);

		for (MapType mapType : MapType.values()) {
			Table card = createMapCard(mapType);
			cardsTable.add(card).top();
		}

		mainTable.add(cardsTable).colspan(3).expand().fill();
		mainTable.row();

		feedbackLabel = new Label("", skin);
		feedbackLabel.setColor(Color.RED);
		feedbackLabel.setFontScale(1.2f);
		feedbackLabel.setAlignment(Align.center);
		mainTable.add(feedbackLabel).colspan(3).pad(10);
		mainTable.row();

		TextButton closeButton = new TextButton("Close", skin);
		closeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				remove();
				if (onClose != null) {
					onClose.run();
				}
			}
		});
		mainTable.add(closeButton).colspan(3).width(150).height(40).padBottom(20);
	}

	private Table createMapCard(final MapType mapType) {
		Table card = new Table(skin);
		if (skin.has("textfield", com.badlogic.gdx.scenes.scene2d.utils.Drawable.class)) {
			card.setBackground(skin.getDrawable("textfield"));
		}
		card.pad(15);

		Label nameLabel = new Label(mapType.getDisplayName(), skin);
		nameLabel.setFontScale(1.4f);
		nameLabel.setAlignment(Align.center);
		card.add(nameLabel).colspan(1).padBottom(10).row();

		try {
			Texture mapTexture = new Texture(Gdx.files.internal("img/maps/" + mapType.getImageFileName()));
			Image mapImage = new Image(new TextureRegion(mapTexture));
			card.add(mapImage).width(300).height(180).padBottom(10).row();
		} catch (Exception e) {
			Label placeholder = new Label("[Map Preview]", skin);
			card.add(placeholder).width(300).height(180).padBottom(10).row();
		}

		boolean isUnlocked = progressionManager.isMapUnlocked(mapType);
		int highestLevel = progressionManager.getHighestCompletedLevel(mapType);

		Label statusLabel;
		if (isUnlocked) {
			statusLabel = new Label("Status: UNLOCKED\nHighest Level: " + highestLevel, skin);
			statusLabel.setColor(Color.GREEN);
		} else {
			statusLabel = new Label("Status: LOCKED\n" + mapType.getUnlockRequirementText(), skin);
			statusLabel.setColor(Color.RED);
		}
		statusLabel.setAlignment(Align.center);
		card.add(statusLabel).padBottom(15).row();

		if (mapType == currentMapType) {
			TextButton currentBtn = new TextButton("CURRENT MAP", skin);
			currentBtn.setColor(Color.LIGHT_GRAY);
			card.add(currentBtn).width(180).height(40);
		} else if (isUnlocked) {
			TextButton selectBtn = new TextButton("SELECT MAP", skin);
			selectBtn.setColor(Color.CYAN);
			selectBtn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					remove();
					if (onMapSelected != null) {
						onMapSelected.accept(mapType);
					}
				}
			});
			card.add(selectBtn).width(180).height(40);
		} else {
			TextButton lockedBtn = new TextButton("LOCKED", skin);
			lockedBtn.setColor(Color.RED);
			lockedBtn.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					feedbackLabel.setText("Map " + mapType.getDisplayName() + " is LOCKED! " + mapType.getUnlockRequirementText());
				}
			});
			card.add(lockedBtn).width(180).height(40);
		}

		return card;
	}
}
