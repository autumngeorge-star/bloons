package com.hongbao.bloons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hongbao.bloons.BloonsTouhouDefense;

public class TitleScreen implements Screen {

    private final BloonsTouhouDefense game;
    private Stage stage;
    private Skin skin;
    private Texture titleTexture;

    public TitleScreen(BloonsTouhouDefense game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        game.getMusicPlayer().playTitleMusic();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        titleTexture = new Texture(Gdx.files.internal("img/instructions/title.png"));
        ImageButton titleImage = new ImageButton(new TextureRegionDrawable(new TextureRegion(titleTexture)));

        Label titleLabel = new Label("BLOONS TOUHOU DEFENSE", skin);
        titleLabel.setFontScale(2.5f);

        TextButton startButton = new TextButton("SELECT MAP", skin);
        startButton.getLabel().setFontScale(1.8f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MapSelectionScreen(game));
            }
        });

        rootTable.add(titleImage).padBottom(30).row();
        rootTable.add(titleLabel).padBottom(40).row();
        rootTable.add(startButton).width(300).height(60).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (titleTexture != null) {
            titleTexture.dispose();
        }
    }
}
