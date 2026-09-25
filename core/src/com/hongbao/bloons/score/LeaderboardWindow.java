package com.hongbao.bloons.score;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.hongbao.bloons.helpers.ZIndex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LeaderboardWindow extends Window {

    private final ScoreManager scoreManager;
    private final Skin skin;
    private final Table contentTable;

    public LeaderboardWindow(Skin skin, ScoreManager scoreManager) {
        super("LEADERBOARD", skin);
        this.skin = skin;
        this.scoreManager = scoreManager;

        setZIndex(ZIndex.MENU_ITEM_Z_INDEX + 10);
        setSize(600, 500);
        setPosition(450, 200);
        setMovable(true);
        setResizable(false);

        contentTable = new Table(skin);
        contentTable.pad(10);

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setFadeScrollBars(false);

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        add(scrollPane).expand().fill().pad(10).row();
        add(closeButton).padBottom(10).row();

        refreshScores();
        setVisible(false);
    }

    public void refreshScores() {
        contentTable.clearChildren();

        Label rankHeader = new Label("#", skin);
        Label scoreHeader = new Label("Score", skin);
        Label levelHeader = new Label("Level", skin);
        Label resultHeader = new Label("Status", skin);

        rankHeader.setColor(Color.GOLD);
        scoreHeader.setColor(Color.GOLD);
        levelHeader.setColor(Color.GOLD);
        resultHeader.setColor(Color.GOLD);

        contentTable.add(rankHeader).width(40).align(Align.center);
        contentTable.add(scoreHeader).width(150).align(Align.center);
        contentTable.add(levelHeader).width(100).align(Align.center);
        contentTable.add(resultHeader).width(120).align(Align.center).row();

        if (scoreManager == null) {
            return;
        }

        List<ScoreEntry> scores = scoreManager.getScoreHistory();
        if (scores.isEmpty()) {
            Label emptyLabel = new Label("No scores saved yet!", skin);
            emptyLabel.setAlignment(Align.center);
            contentTable.add(emptyLabel).colspan(4).padTop(20).row();
            return;
        }

        int rank = 1;
        for (ScoreEntry entry : scores) {
            Label rankLabel = new Label(String.valueOf(rank), skin);
            Label scoreLabel = new Label(String.valueOf(entry.getScore()), skin);
            Label levelLabel = new Label("Lvl " + entry.getLevel(), skin);
            Label resultLabel = new Label(entry.isVictory() ? "VICTORY" : "GAME OVER", skin);

            if (entry.isVictory()) {
                resultLabel.setColor(Color.GREEN);
            } else {
                resultLabel.setColor(Color.LIGHT_GRAY);
            }

            rankLabel.setAlignment(Align.center);
            scoreLabel.setAlignment(Align.center);
            levelLabel.setAlignment(Align.center);
            resultLabel.setAlignment(Align.center);

            contentTable.add(rankLabel).width(40).pad(4);
            contentTable.add(scoreLabel).width(150).pad(4);
            contentTable.add(levelLabel).width(100).pad(4);
            contentTable.add(resultLabel).width(120).pad(4).row();

            rank++;
            if (rank > 25) {
                break; // Show top 25
            }
        }
    }

    public void toggleVisibility() {
        if (!isVisible()) {
            refreshScores();
            toFront();
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}
