package com.hongbao.bloons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.MapType;
import com.hongbao.bloons.SaveProfile;
import com.hongbao.bloons.SaveProfileManager;

import java.util.ArrayList;
import java.util.List;

public class MapSelectionScreen implements Screen {

	private final BloonsTouhouDefense game;
	private Stage stage;
	private Skin skin;
	private final List<Texture> mapTextures = new ArrayList<>();

	public MapSelectionScreen(BloonsTouhouDefense game) {
		this.game = game;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = game.getUiSkin();

		Label titleLabel = new Label("SELECT A MAP", skin);
		titleLabel.setFontScale(2.2f);
		titleLabel.setAlignment(Align.center);
		titleLabel.setPosition(Gdx.graphics.getWidth() / 2f - 300, 800);
		titleLabel.setSize(600, 60);
		stage.addActor(titleLabel);

		SaveProfileManager saveManager = game.getSaveProfileManager();
		MapType[] maps = MapType.values();

		float startX = 120;
		float cardWidth = 480;
		float cardGap = 70;
		float cardY = 180;
		float cardHeight = 560;

		for (int i = 0; i < maps.length; i++) {
			final MapType mapType = maps[i];
			SaveProfile.MapProfile mapProf = saveManager.getMapProfile(mapType.getId());
			boolean isUnlocked = mapProf != null && mapProf.isUnlocked();

			float cardX = startX + i * (cardWidth + cardGap);

			Table cardTable = new Table(skin);
			cardTable.setPosition(cardX, cardY);
			cardTable.setSize(cardWidth, cardHeight);
			cardTable.top().pad(15);

			// Card Title
			Label mapNameLabel = new Label(mapType.getDisplayName(), skin);
			mapNameLabel.setFontScale(1.4f);
			mapNameLabel.setAlignment(Align.center);
			cardTable.add(mapNameLabel).padBottom(10).row();

			// Map Thumbnail Image
			Texture imgTexture = new Texture(Gdx.files.internal("img/maps/" + mapType.getImageFileName()));
			mapTextures.add(imgTexture);
			Image mapImg = new Image(new TextureRegionDrawable(new TextureRegion(imgTexture)));
			cardTable.add(mapImg).size(400, 200).padBottom(15).row();

			// Description
			Label descLabel = new Label(mapType.getDescription(), skin);
			descLabel.setWrap(true);
			descLabel.setAlignment(Align.center);
			cardTable.add(descLabel).width(420).padBottom(10).row();

			// Status Badge (Unlocked / Locked / Completed)
			String statusText;
			Color statusColor;
			if (!isUnlocked) {
				statusText = "LOCKED";
				statusColor = Color.RED;
			} else if (mapProf.isCompleted()) {
				statusText = "COMPLETED";
				statusColor = Color.GOLD;
			} else {
				statusText = "UNLOCKED";
				statusColor = Color.GREEN;
			}

			Label statusLabel = new Label("[" + statusText + "]", skin);
			statusLabel.setColor(statusColor);
			statusLabel.setFontScale(1.2f);
			statusLabel.setAlignment(Align.center);
			cardTable.add(statusLabel).padBottom(10).row();

			// High Score Badges & Waves
			int wave = mapProf != null ? mapProf.getHighestWave() : 0;
			int score = mapProf != null ? mapProf.getHighScore() : 0;

			Label waveBadge = new Label("Highest Wave: " + wave, skin);
			waveBadge.setFontScale(1.1f);
			cardTable.add(waveBadge).padBottom(5).row();

			Label scoreBadge = new Label("High Score: $" + score, skin);
			scoreBadge.setFontScale(1.1f);
			cardTable.add(scoreBadge).padBottom(15).row();

			// Play Button
			TextButton playButton = new TextButton(isUnlocked ? "START MAP" : "LOCKED", skin);
			playButton.getLabel().setFontScale(1.3f);
			playButton.setDisabled(!isUnlocked);
			if (isUnlocked) {
				playButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						game.showGameScreen(mapType);
					}
				});
			}
			cardTable.add(playButton).size(220, 50).row();

			stage.addActor(cardTable);
		}

		TextButton backButton = new TextButton("BACK TO TITLE", skin);
		backButton.setPosition(Gdx.graphics.getWidth() / 2f - 120, 50);
		backButton.setSize(240, 60);
		backButton.getLabel().setFontScale(1.4f);
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.showTitleScreen();
			}
		});
		stage.addActor(backButton);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.12f, 0.12f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		if (stage != null) {
			stage.getViewport().update(width, height, true);
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		if (stage != null) {
			stage.dispose();
			stage = null;
		}
		for (Texture t : mapTextures) {
			t.dispose();
		}
		mapTextures.clear();
	}
}
