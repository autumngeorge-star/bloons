package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized UI Asset Management system for loading, caching, sharing,
 * and disposing UI textures, skins, and fonts safely.
 */
public class UIManager implements Disposable {

	public static final String DEFAULT_SKIN_PATH = "uiskins/uiskin.json";

	private static UIManager instance;

	private final Map<String, Skin> skinCache;
	private final Map<String, Texture> textureCache;
	private final Map<String, BitmapFont> fontCache;

	public UIManager() {
		skinCache = new HashMap<>();
		textureCache = new HashMap<>();
		fontCache = new HashMap<>();
	}

	public static UIManager getInstance() {
		if (instance == null) {
			instance = new UIManager();
		}
		return instance;
	}

	public static void resetInstance() {
		if (instance != null) {
			instance.dispose();
			instance = null;
		}
	}

	/**
	 * Retrieves or loads the default UI Skin ("uiskins/uiskin.json").
	 */
	public Skin getSkin() {
		return getSkin(DEFAULT_SKIN_PATH);
	}

	/**
	 * Retrieves or loads a UI Skin given its internal file path.
	 */
	public Skin getSkin(String skinPath) {
		if (!skinCache.containsKey(skinPath)) {
			Skin skin = new Skin(Gdx.files.internal(skinPath));
			skinCache.put(skinPath, skin);
		}
		return skinCache.get(skinPath);
	}

	/**
	 * Retrieves or loads a Texture given its internal file path.
	 */
	public Texture getTexture(String path) {
		if (!textureCache.containsKey(path)) {
			Texture texture = new Texture(Gdx.files.internal(path));
			textureCache.put(path, texture);
		}
		return textureCache.get(path);
	}

	/**
	 * Retrieves a TextureRegion wrapping the cached Texture for the given path.
	 */
	public TextureRegion getTextureRegion(String path) {
		return new TextureRegion(getTexture(path));
	}

	/**
	 * Retrieves a Drawable (TextureRegionDrawable) wrapping the cached Texture for the given path.
	 */
	public Drawable getDrawable(String path) {
		return new TextureRegionDrawable(getTextureRegion(path));
	}

	/**
	 * Explicitly unloads and disposes a texture from the cache.
	 */
	public void unloadTexture(String path) {
		Texture texture = textureCache.remove(path);
		if (texture != null) {
			texture.dispose();
		}
	}

	/**
	 * Retrieves or loads a BitmapFont from the font cache or default skin.
	 */
	public BitmapFont getFont(String fontName) {
		if (!fontCache.containsKey(fontName)) {
			Skin defaultSkin = getSkin();
			if (defaultSkin.has(fontName, BitmapFont.class)) {
				fontCache.put(fontName, defaultSkin.getFont(fontName));
			} else {
				BitmapFont font = new BitmapFont(Gdx.files.internal(fontName));
				fontCache.put(fontName, font);
			}
		}
		return fontCache.get(fontName);
	}

	/**
	 * Disposes all cached UI textures, skins, and fonts.
	 */
	@Override
	public void dispose() {
		for (Skin skin : skinCache.values()) {
			if (skin != null) {
				skin.dispose();
			}
		}
		skinCache.clear();

		for (Texture texture : textureCache.values()) {
			if (texture != null) {
				texture.dispose();
			}
		}
		textureCache.clear();

		for (BitmapFont font : fontCache.values()) {
			if (font != null && !isFontFromSkin(font)) {
				font.dispose();
			}
		}
		fontCache.clear();
	}

	private boolean isFontFromSkin(BitmapFont font) {
		for (Skin skin : skinCache.values()) {
			if (skin != null) {
				for (BitmapFont skinFont : skin.getAll(BitmapFont.class).values()) {
					if (skinFont == font) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
