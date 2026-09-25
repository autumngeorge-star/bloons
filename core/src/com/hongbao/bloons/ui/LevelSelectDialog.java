package com.hongbao.bloons.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.LevelSaveService;

public class LevelSelectDialog extends Dialog {

    private final BloonManager bloonManager;
    private final BloonsTouhouDefense app;

    public LevelSelectDialog(Skin skin, BloonManager bloonManager, BloonsTouhouDefense app) {
        super("Campaign Stage Select", skin, "dialog");
        this.bloonManager = bloonManager;
        this.app = app;

        setModal(true);
        setMovable(false);

        getContentTable().pad(15);

        Label titleLabel = new Label("Select Stage", skin);
        titleLabel.setFontScale(1.3f);
        getContentTable().add(titleLabel).padBottom(10).row();

        Table gridTable = new Table();
        gridTable.pad(10);

        int maxUnlocked = LevelSaveService.getInstance().getMaxUnlockedLevel();
        int totalLevels = bloonManager.getBloonQueue().getTotalLevels();
        if (totalLevels < 1) {
            totalLevels = 1;
        }

        int columns = 5;
        int colCount = 0;

        for (int i = 1; i <= totalLevels; i++) {
            final int levelNum = i;
            boolean isUnlocked = (i <= maxUnlocked);

            String buttonText = "Level " + i + "\n" + (isUnlocked ? "[UNLOCKED]" : "[LOCKED]");
            TextButton tileButton = new TextButton(buttonText, skin);

            if (isUnlocked) {
                tileButton.getLabel().setColor(Color.WHITE);
                tileButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        bloonManager.setLevel(levelNum);
                        if (app != null) {
                            app.paused = false;
                        }
                        hide();
                    }
                });
            } else {
                tileButton.setDisabled(true);
                tileButton.getLabel().setColor(Color.GRAY);
                tileButton.setColor(0.5f, 0.5f, 0.5f, 0.6f);
            }

            gridTable.add(tileButton).width(120).height(65).pad(5);

            colCount++;
            if (colCount % columns == 0) {
                gridTable.row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        getContentTable().add(scrollPane).width(700).height(420).pad(10).row();

        TextButton closeButton = new TextButton("Close", skin);
        button(closeButton, "close");

        key(com.badlogic.gdx.Input.Keys.ESCAPE, "close");
    }

    @Override
    protected void result(Object object) {
        if (app != null) {
            app.paused = false;
        }
    }
}
