package com.hongbao.bloons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.hongbao.bloons.BloonsTouhouDefense;

public class TitleScreen implements Screen {

	private final BloonsTouhouDefense game;
	private Stage stage;
	private Skin skin;

	public TitleScreen(BloonsTouhouDefense game) {
		this.game = game;
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = game.getUiSkin();

		// Banner / Logo
		Texture logoTexture = new Texture(Gdx.files.internal("img/instructions/title.png"));
		ImageButton logo = new ImageButton(new TextureRegionDrawable(new TextureRegion(logoTexture)));
		logo.setPosition(Gdx.graphics.getWidth() / 2f - logo.getWidth() / 2f, 400);
		stage.addActor(logo);

		Label titleLabel = new Label("BLOONS TOUHOU DEFENSE", skin);
		titleLabel.setFontScale(2.5f);
		titleLabel.setAlignment(Align.center);
		titleLabel.setPosition(Gdx.graphics.getWidth() / 2f - 300, 750);
		titleLabel.setSize(600, 80);
		stage.addActor(titleLabel);

		Label subtitle = new Label("A Touhou Project Tower Defense Fan Game", skin);
		subtitle.setFontScale(1.3f);
		subtitle.setAlignment(Align.center);
		subtitle.setPosition(Gdx.graphics.getWidth() / 2f - 300, 700);
		subtitle.setSize(600, 40);
		stage.addActor(subtitle);

		TextButton playButton = new TextButton("PLAY GAME", skin);
		playButton.setPosition(Gdx.graphics.getWidth() / 2f - 150, 250);
		playButton.setSize(300, 80);
		playButton.getLabel().setFontScale(1.8f);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.showMapSelectionScreen();
			}
		});
		stage.addActor(playButton);

		game.getMusicPlayer().playTitleMusic();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.08f, 0.08f, 0.15f, 1f);
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
	}
}
