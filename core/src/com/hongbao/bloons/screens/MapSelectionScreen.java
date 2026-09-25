package com.hongbao.bloons.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.MapType;
import com.hongbao.bloons.ProgressionService;

import java.util.ArrayList;
import java.util.List;

public class MapSelectionScreen implements Screen {

    private final BloonsTouhouDefense game;
    private Stage stage;
    private Skin skin;
    private final List<Texture> mapTextures;

    public MapSelectionScreen(BloonsTouhouDefense game) {
        this.game = game;
        this.mapTextures = new ArrayList<>();
    }

    @Override
    public void show() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        game.getMusicPlayer().playTitleMusic();

        Table root = new Table();
        root.setFillParent(true);
        root.pad(20);
        stage.addActor(root);

        Label titleLabel = new Label("MAP SELECTION", skin);
        titleLabel.setFontScale(2.2f);
        root.add(titleLabel).colspan(3).padBottom(30).row();

        Table cardsTable = new Table();
        cardsTable.defaults().pad(15);

        ProgressionService progression = game.getProgressionService();

        for (final MapType mapType : MapType.values()) {
            Table card = createMapCard(mapType, progression);
            cardsTable.add(card).top();
        }

        root.add(cardsTable).expand().center().row();

        TextButton backButton = new TextButton("BACK TO TITLE", skin);
        backButton.getLabel().setFontScale(1.3f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });
        root.add(backButton).width(220).height(50).padTop(20).row();
    }

    private Table createMapCard(final MapType mapType, ProgressionService progression) {
        Table card = new Table(skin);
        card.setBackground("window");
        card.pad(15);

        // Map Image Preview
        Texture mapTex = new Texture(Gdx.files.internal(mapType.getImagePath()));
        mapTextures.add(mapTex);
        Image mapImage = new Image(mapTex);

        boolean isUnlocked = progression.isMapUnlocked(mapType);
        int highestLevel = progression.getHighestCompletedLevel(mapType);

        Label nameLabel = new Label(mapType.getDisplayName(), skin);
        nameLabel.setFontScale(1.4f);

        Label statusLabel;
        Label highestLevelLabel;
        TextButton playButton;

        if (isUnlocked) {
            statusLabel = new Label("UNLOCKED", skin);
            statusLabel.setColor(Color.GREEN);
            highestLevelLabel = new Label("Highest Level: " + highestLevel, skin);

            playButton = new TextButton("START MAP", skin);
            playButton.getLabel().setFontScale(1.2f);
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameplayScreen(game, mapType));
                }
            });
        } else {
            statusLabel = new Label("LOCKED", skin);
            statusLabel.setColor(Color.RED);
            highestLevelLabel = new Label("Complete previous map to unlock", skin);
            highestLevelLabel.setColor(Color.LIGHT_GRAY);

            playButton = new TextButton("LOCKED", skin);
            playButton.getLabel().setFontScale(1.2f);
            playButton.setDisabled(true);
            playButton.setColor(Color.GRAY);
        }

        card.add(nameLabel).padBottom(10).row();
        card.add(mapImage).width(280).height(180).padBottom(10).row();
        card.add(statusLabel).padBottom(5).row();
        card.add(highestLevelLabel).padBottom(15).row();
        card.add(playButton).width(180).height(45).row();

        return card;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.12f, 0.12f, 0.18f, 1f);
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
        for (Texture tex : mapTextures) {
            if (tex != null) {
                tex.dispose();
            }
        }
        mapTextures.clear();
    }
}
