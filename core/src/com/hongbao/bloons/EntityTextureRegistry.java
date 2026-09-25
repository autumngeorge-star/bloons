package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class EntityTextureRegistry {

	private static final EntityTextureRegistry INSTANCE = new EntityTextureRegistry();

	private final Map<String, TextureRegion> registry = new HashMap<>();
	private final Map<String, Texture> textures = new HashMap<>();

	public static EntityTextureRegistry getInstance() {
		return INSTANCE;
	}

	public static void preloadAll() {
		INSTANCE.loadAll();
	}

	public void loadAll() {
		// Preload bloons
		String[] bloonColors = {"red", "blue", "green", "yellow", "pink", "black", "lead", "zebra", "rainbow", "ceramic", "moab", "bfb", "zomg"};
		for (String color : bloonColors) {
			register("img/bloons/" + color + "_bloon.png");
			register("img/bloons/" + color + "_camo_bloon.png");
			register("img/bloons/" + color + "_regrowth_bloon.png");
			register("img/bloons/" + color + "_camo_regrowth_bloon.png");
		}

		// Preload characters
		String[] characters = {"alice.png", "characters.png", "marisa.png", "reimu.png", "remilia.png", "sakuya.png", "youmu.png", "yukari.png", "yuyuko.png"};
		for (String chara : characters) {
			register("img/characters/" + chara);
		}

		// Preload projectiles
		String[] projectiles = {"bat.png", "black_spell_card.png", "blue_knives.png", "blue_magic_missile.png", "magic_spike.png", "pink_butterfly.png", "projectiles.png", "purple_energy.png", "red_spell_card.png", "sword_slash.png"};
		for (String proj : projectiles) {
			register("img/projectiles/" + proj);
		}

		// Preload spellcards
		String[] spellcards = {"reimu_spell.png", "yuyuko_fan.png"};
		for (String card : spellcards) {
			register("img/spellcards/" + card);
		}
	}

	public void register(String path) {
		if (!registry.containsKey(path)) {
			FileHandle handle = Gdx.files != null ? Gdx.files.internal(path) : null;
			if (handle != null && handle.exists()) {
				Texture texture = new Texture(handle);
				TextureRegion region = new TextureRegion(texture);
				textures.put(path, texture);
				registry.put(path, region);
			}
		}
	}

	public void register(String path, Texture texture) {
		if (texture != null) {
			textures.put(path, texture);
			registry.put(path, new TextureRegion(texture));
		}
	}

	public void register(String path, TextureRegion region) {
		if (region != null) {
			registry.put(path, region);
		}
	}

	public static TextureRegion getTextureRegion(String path) {
		return INSTANCE.get(path);
	}

	public TextureRegion get(String path) {
		if (!registry.containsKey(path)) {
			register(path);
		}
		return registry.get(path);
	}

	public static boolean isRegistered(String path) {
		return INSTANCE.registry.containsKey(path);
	}

	public static int getRegisteredCount() {
		return INSTANCE.registry.size();
	}

	public static void dispose() {
		INSTANCE.disposeInternal();
	}

	public void disposeInternal() {
		for (Texture texture : textures.values()) {
			if (texture != null) {
				texture.dispose();
			}
		}
		textures.clear();
		registry.clear();
	}
}
