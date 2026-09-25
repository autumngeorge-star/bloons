package com.hongbao.bloons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.hongbao.bloons.actors.RenderableImageButton;
import com.hongbao.bloons.screens.GameplayScreen;
import com.hongbao.bloons.screens.TitleScreen;

import java.util.ArrayList;
import java.util.List;

public class BloonsTouhouDefense extends Game {

    public static final int MONEY = 1000;
    public static final int HEALTH = 100;
    public static final boolean HELLA_BLOONS = false;

    private MusicPlayer musicPlayer;
    private ProgressionService progressionService;
    public List<RenderableImageButton> instructions = new ArrayList<>();

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(1800, 900);
        musicPlayer = new MusicPlayer();
        progressionService = new ProgressionService();

        setScreen(new TitleScreen(this));
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public ProgressionService getProgressionService() {
        return progressionService;
    }

    public Player getPlayer() {
        if (getScreen() instanceof GameplayScreen) {
            return ((GameplayScreen) getScreen()).getPlayer();
        }
        return null;
    }

    public Map getMap() {
        if (getScreen() instanceof GameplayScreen) {
            return ((GameplayScreen) getScreen()).getMap();
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
