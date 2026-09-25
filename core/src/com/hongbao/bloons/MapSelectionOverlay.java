package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.hongbao.bloons.actors.RenderableImageButton;
import com.hongbao.bloons.actors.RenderableLabel;
import com.hongbao.bloons.helpers.ZIndex;

import java.util.ArrayList;
import java.util.List;

public class MapSelectionOverlay {

	private final BloonsTouhouDefense game;
	private final Stage stage;
	private final Skin skin;

	private boolean visible;
	private int currentIndex;
	private final List<Actor> overlayActors;

	public MapSelectionOverlay(BloonsTouhouDefense game, Stage stage) {
		this.game = game;
		this.stage = stage;
		this.skin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));
		this.visible = false;
		this.currentIndex = 0;
		this.overlayActors = new ArrayList<>();
	}

	public boolean isVisible() {
		return visible;
	}

	public void show() {
		show(game.getMap() != null ? game.getMap().getMapType() : MapType.BASIC_MAP);
	}

	public void show(MapType focusMap) {
		hide();
		visible = true;
		game.paused = true;

		MapType[] maps = MapType.values();
		for (int i = 0; i < maps.length; i++) {
			if (maps[i] == focusMap) {
				currentIndex = i;
				break;
			}
		}

		refreshUI();
	}

	public void hide() {
		for (Actor actor : overlayActors) {
			actor.remove();
		}
		overlayActors.clear();
		visible = false;
		game.paused = false;
	}

	private void refreshUI() {
		for (Actor actor : overlayActors) {
			actor.remove();
		}
		overlayActors.clear();

		MapType[] maps = MapType.values();
		if (currentIndex < 0) currentIndex = 0;
		if (currentIndex >= maps.length) currentIndex = maps.length - 1;

		MapType focusedMap = maps[currentIndex];
		boolean isUnlocked = MapProgressManager.isMapUnlocked(focusedMap);
		int highestLevel = MapProgressManager.getHighestLevel(focusedMap);

		// 1. Dark background panel for overlay
		ImageButton panel = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/ui/header.png")))));
		panel.setPosition(300, 100);
		panel.setSize(900, 700);
		panel.setColor(new Color(0.12f, 0.12f, 0.22f, 0.95f));
		RenderableImageButton panelActor = new RenderableImageButton(panel, ZIndex.OVERLAY_Z_INDEX);
		stage.addActor(panelActor);
		overlayActors.add(panelActor);

		// 2. Title Label
		Label title = new Label("SELECT MAP", skin);
		title.setBounds(300, 720, 900, 60);
		title.setFontScale(2.0f);
		title.setAlignment(Align.center);
		title.setColor(Color.GOLD);
		RenderableLabel titleActor = new RenderableLabel(title, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(titleActor);
		overlayActors.add(titleActor);

		// 3. Map Name Label
		Label mapNameLabel = new Label(focusedMap.getDisplayName(), skin);
		mapNameLabel.setBounds(300, 660, 900, 50);
		mapNameLabel.setFontScale(1.6f);
		mapNameLabel.setAlignment(Align.center);
		mapNameLabel.setColor(Color.WHITE);
		RenderableLabel mapNameActor = new RenderableLabel(mapNameLabel, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(mapNameActor);
		overlayActors.add(mapNameActor);

		// 4. Map Preview Thumbnail
		ImageButton preview = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(focusedMap.getImagePath())))));
		preview.setPosition(450, 280);
		preview.setSize(600, 360);
		if (!isUnlocked) {
			preview.setColor(new Color(0.3f, 0.3f, 0.3f, 0.7f));
		} else {
			preview.setColor(Color.WHITE);
		}
		RenderableImageButton previewActor = new RenderableImageButton(preview, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(previewActor);
		overlayActors.add(previewActor);

		// 5. Left Arrow (<) Button
		Label leftArrow = new Label("< PREV", skin);
		leftArrow.setBounds(320, 430, 110, 80);
		leftArrow.setFontScale(1.6f);
		leftArrow.setAlignment(Align.center);
		leftArrow.setColor(currentIndex > 0 ? Color.CYAN : Color.GRAY);
		leftArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentIndex > 0) {
					currentIndex--;
					refreshUI();
				}
			}
		});
		RenderableLabel leftArrowActor = new RenderableLabel(leftArrow, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(leftArrowActor);
		overlayActors.add(leftArrowActor);

		// 6. Right Arrow (>) Button
		Label rightArrow = new Label("NEXT >", skin);
		rightArrow.setBounds(1070, 430, 110, 80);
		rightArrow.setFontScale(1.6f);
		rightArrow.setAlignment(Align.center);
		rightArrow.setColor(currentIndex < maps.length - 1 ? Color.CYAN : Color.GRAY);
		rightArrow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (currentIndex < maps.length - 1) {
					currentIndex++;
					refreshUI();
				}
			}
		});
		RenderableLabel rightArrowActor = new RenderableLabel(rightArrow, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(rightArrowActor);
		overlayActors.add(rightArrowActor);

		// 7. Status & Level Info
		String infoText;
		Color infoColor;
		if (isUnlocked) {
			infoText = "STATUS: UNLOCKED\nHighest Level Reached: " + highestLevel;
			infoColor = Color.GREEN;
		} else {
			String reqName = focusedMap.getRequiredMap() != null ? focusedMap.getRequiredMap().getDisplayName() : "Previous Map";
			infoText = "STATUS: LOCKED\nRequires winning " + reqName + " to unlock!";
			infoColor = Color.RED;
		}

		Label statusLabel = new Label(infoText, skin);
		statusLabel.setBounds(300, 205, 900, 60);
		statusLabel.setFontScale(1.4f);
		statusLabel.setAlignment(Align.center);
		statusLabel.setColor(infoColor);
		RenderableLabel statusActor = new RenderableLabel(statusLabel, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(statusActor);
		overlayActors.add(statusActor);

		// 8. Start Map / Select Button
		Label startButton = new Label(isUnlocked ? "START GAME" : "LOCKED", skin);
		startButton.setBounds(600, 130, 300, 60);
		startButton.setFontScale(1.6f);
		startButton.setAlignment(Align.center);
		startButton.setColor(isUnlocked ? Color.GOLD : Color.GRAY);
		if (isUnlocked) {
			startButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.loadMap(focusedMap);
					hide();
				}
			});
		}
		RenderableLabel startActor = new RenderableLabel(startButton, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(startActor);
		overlayActors.add(startActor);

		// 9. Close Button (X)
		Label closeButton = new Label("X", skin);
		closeButton.setBounds(1160, 730, 30, 30);
		closeButton.setFontScale(1.8f);
		closeButton.setAlignment(Align.center);
		closeButton.setColor(Color.RED);
		closeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
			}
		});
		RenderableLabel closeActor = new RenderableLabel(closeButton, ZIndex.OVERLAY_ITEM_Z_INDEX);
		stage.addActor(closeActor);
		overlayActors.add(closeActor);
	}
}
