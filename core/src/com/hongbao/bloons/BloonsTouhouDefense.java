package com.hongbao.bloons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.hongbao.bloons.actors.RenderableImageButton;
import com.hongbao.bloons.screens.GameScreen;
import com.hongbao.bloons.screens.MapSelectionScreen;
import com.hongbao.bloons.screens.TitleScreen;

import java.util.ArrayList;
import java.util.List;

public class BloonsTouhouDefense extends Game {

	public static final int MONEY = 1000;
	public static final int HEALTH = 100;
	public static final boolean HELLA_BLOONS = false;

	public List<RenderableImageButton> instructions = new ArrayList<>();

	private SaveProfileManager saveProfileManager;
	private MusicPlayer musicPlayer;
	private Skin uiSkin;
	private Player player;

	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(1800, 900);
		saveProfileManager = new SaveProfileManager();
		musicPlayer = new MusicPlayer();
		uiSkin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));

		showTitleScreen();
	}

	public void showTitleScreen() {
		setScreen(new TitleScreen(this));
	}

	public void showMapSelectionScreen() {
		setScreen(new MapSelectionScreen(this));
	}

	public void showGameScreen(MapType mapType) {
		setScreen(new GameScreen(this, mapType));
	}

	public SaveProfileManager getSaveProfileManager() {
		return saveProfileManager;
	}

	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}

	public Skin getUiSkin() {
		if (uiSkin == null) {
			uiSkin = new Skin(Gdx.files.internal("uiskins/uiskin.json"));
		}
		return uiSkin;
	}

	public Player getPlayer() {
		if (getScreen() instanceof GameScreen) {
			return ((GameScreen) getScreen()).getPlayer();
		}
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Map getMap() {
		if (getScreen() instanceof GameScreen) {
			return ((GameScreen) getScreen()).getMap();
		}
		return null;
	}

	public List<RenderableImageButton> getInstructions() {
		if (getScreen() instanceof GameScreen) {
			return ((GameScreen) getScreen()).instructions;
		}
		return instructions;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (uiSkin != null) {
			uiSkin.dispose();
			uiSkin = null;
		}
		if (musicPlayer != null) {
			musicPlayer.stopMusic();
		}
	}
}
