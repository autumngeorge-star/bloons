package com.hongbao.bloons.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;

public class LibGdxAssetProviderService implements AssetProviderService {

	private final AssetManager assetManager;

	public LibGdxAssetProviderService() {
		this(new AssetManager());
	}

	public LibGdxAssetProviderService(AssetManager assetManager) {
		this.assetManager = assetManager;
		loadAssets();
		finishLoading();
	}

	@Override
	public void loadAssets() {
		if (Gdx.files == null) {
			return;
		}
		// Instructions
		assetManager.load("img/instructions/title.png", Texture.class);
		assetManager.load("img/instructions/objective.png", Texture.class);
		assetManager.load("img/instructions/bloons.png", Texture.class);
		assetManager.load("img/instructions/blimps.png", Texture.class);
		assetManager.load("img/instructions/girls.png", Texture.class);
		assetManager.load("img/instructions/shortcuts.png", Texture.class);

		// UI
		assetManager.load("img/ui/header.png", Texture.class);
		assetManager.load("img/ui/girl_details_template.png", Texture.class);
		assetManager.load("img/ui/reimu_box.png", Texture.class);
		assetManager.load("img/ui/yukari_box.png", Texture.class);
		assetManager.load("img/ui/marisa_box.png", Texture.class);
		assetManager.load("img/ui/alice_box.png", Texture.class);
		assetManager.load("img/ui/sakuya_box.png", Texture.class);
		assetManager.load("img/ui/remilia_box.png", Texture.class);
		assetManager.load("img/ui/youmu_box.png", Texture.class);
		assetManager.load("img/ui/yuyuko_box.png", Texture.class);

		// Maps
		assetManager.load("img/maps/basic_map.png", Texture.class);
		assetManager.load("img/maps/map_with_turn.png", Texture.class);
		assetManager.load("img/maps/heater.png", Texture.class);

		// Characters
		assetManager.load("img/characters/reimu.png", Texture.class);
		assetManager.load("img/characters/yukari.png", Texture.class);
		assetManager.load("img/characters/marisa.png", Texture.class);
		assetManager.load("img/characters/alice.png", Texture.class);
		assetManager.load("img/characters/sakuya.png", Texture.class);
		assetManager.load("img/characters/remilia.png", Texture.class);
		assetManager.load("img/characters/youmu.png", Texture.class);
		assetManager.load("img/characters/yuyuko.png", Texture.class);

		// Projectiles
		assetManager.load("img/projectiles/red_spell_card.png", Texture.class);
		assetManager.load("img/projectiles/purple_energy.png", Texture.class);
		assetManager.load("img/projectiles/blue_magic_missile.png", Texture.class);
		assetManager.load("img/projectiles/magic_spike.png", Texture.class);
		assetManager.load("img/projectiles/blue_knives.png", Texture.class);
		assetManager.load("img/projectiles/bat.png", Texture.class);
		assetManager.load("img/projectiles/sword_slash.png", Texture.class);
		assetManager.load("img/projectiles/pink_butterfly.png", Texture.class);

		// Spellcards
		assetManager.load("img/spellcards/reimu_spell.png", Texture.class);
		assetManager.load("img/spellcards/yuyuko_fan.png", Texture.class);

		// Bloons - queue all color / camo / regen combinations
		String[] colors = {"red", "blue", "green", "yellow", "pink", "black", "lead", "zebra", "rainbow", "ceramic", "moab", "bfb", "zomg", "white"};
		boolean[] options = {false, true};

		for (String color : colors) {
			for (boolean camo : options) {
				for (boolean regen : options) {
					StringBuilder sb = new StringBuilder("img/bloons/");
					sb.append(color);
					if (camo) {
						sb.append(Bloon.CAMO_BLOON_DENOTATION);
					}
					if (regen) {
						sb.append(Bloon.REGROWTH_BLOON_DENOTATION);
					}
					sb.append("_bloon.png");
					String path = sb.toString();
					if (Gdx.files != null && Gdx.files.internal(path).exists()) {
						assetManager.load(path, Texture.class);
					}
				}
			}
		}

		// UI Skin
		if (Gdx.files != null && Gdx.files.internal("uiskins/uiskin.json").exists()) {
			assetManager.load("uiskins/uiskin.json", Skin.class);
		}

		// Sound & Music
		if (Gdx.files != null && Gdx.files.internal("music/pop.mp3").exists()) {
			assetManager.load("music/pop.mp3", Sound.class);
		}
		if (Gdx.files != null && Gdx.files.internal("music/title.mp3").exists()) {
			assetManager.load("music/title.mp3", Music.class);
		}
		if (Gdx.files != null && Gdx.files.internal("music/demystify_feast.mp3").exists()) {
			assetManager.load("music/demystify_feast.mp3", Music.class);
		}
		if (Gdx.files != null && Gdx.files.internal("music/night_falls.mp3").exists()) {
			assetManager.load("music/night_falls.mp3", Music.class);
		}
	}

	@Override
	public void finishLoading() {
		assetManager.finishLoading();
	}

	@Override
	public Texture getTexture(String filePath) {
		if (!assetManager.isLoaded(filePath, Texture.class)) {
			assetManager.load(filePath, Texture.class);
			assetManager.finishLoadingAsset(filePath);
		}
		return assetManager.get(filePath, Texture.class);
	}

	@Override
	public Texture getBloonTexture(Bloon bloon) {
		return getTexture(bloon.getImageFileName());
	}

	@Override
	public Texture getGirlTexture(Girl girl) {
		return getTexture(girl.getImageFileName());
	}

	@Override
	public Texture getBulletTexture(Bullet bullet) {
		return getTexture(bullet.getImageFileName());
	}

	@Override
	public Texture getSpellCardTexture(SpellCard spellCard) {
		return getTexture(spellCard.getImageFileName());
	}

	@Override
	public Texture getInstructionTexture(String instructionName) {
		return getTexture("img/instructions/" + instructionName);
	}

	@Override
	public Texture getUiTexture(String uiName) {
		return getTexture("img/ui/" + uiName);
	}

	@Override
	public Skin getUiSkin() {
		String skinPath = "uiskins/uiskin.json";
		if (!assetManager.isLoaded(skinPath, Skin.class)) {
			assetManager.load(skinPath, Skin.class);
			assetManager.finishLoadingAsset(skinPath);
		}
		return assetManager.get(skinPath, Skin.class);
	}

	@Override
	public Sound getSound(String soundPath) {
		if (!assetManager.isLoaded(soundPath, Sound.class)) {
			assetManager.load(soundPath, Sound.class);
			assetManager.finishLoadingAsset(soundPath);
		}
		return assetManager.get(soundPath, Sound.class);
	}

	@Override
	public Sound getPopSound() {
		return getSound("music/pop.mp3");
	}

	@Override
	public Music getMusic(String musicPath) {
		if (!assetManager.isLoaded(musicPath, Music.class)) {
			assetManager.load(musicPath, Music.class);
			assetManager.finishLoadingAsset(musicPath);
		}
		return assetManager.get(musicPath, Music.class);
	}

	@Override
	public Music getTitleMusic() {
		return getMusic("music/title.mp3");
	}

	@Override
	public Music getStageMusic() {
		return getMusic("music/demystify_feast.mp3");
	}

	@Override
	public Music getFinalBossMusic() {
		return getMusic("music/night_falls.mp3");
	}

	@Override
	public FileHandle getFileHandle(String filePath) {
		return Gdx.files.internal(filePath);
	}

	@Override
	public FileHandle getBloonQueueFile(String fileName) {
		return getFileHandle("bloon_queues/" + fileName);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}
}
