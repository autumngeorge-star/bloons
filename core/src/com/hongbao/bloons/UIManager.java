package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized manager for UI assets including skins, textures, drawables, and fonts.
 * Caches loaded UI assets to prevent duplicate loads and memory leaks,
 * and provides explicit unloading for transient UI elements (such as instruction screens).
 */
public class UIManager implements Disposable {

	public static final String DEFAULT_SKIN_PATH = "uiskins/uiskin.json";

	private static UIManager instance;

	private final Map<String, Texture> textureCache;
	private final Map<String, Skin> skinCache;

	public UIManager() {
		this.textureCache = new HashMap<>();
		this.skinCache = new HashMap<>();
	}

	public static synchronized UIManager getInstance() {
		if (instance == null) {
			instance = new UIManager();
		}
		return instance;
	}

	public static synchronized void setInstance(UIManager manager) {
		instance = manager;
	}

	/**
	 * Retrieves the default UI skin ("uiskins/uiskin.json"), loading and caching it if not already loaded.
	 */
	public Skin getSkin() {
		return getSkin(DEFAULT_SKIN_PATH);
	}

	/**
	 * Retrieves a UI skin by path, loading and caching it if not already loaded.
	 */
	public Skin getSkin(String skinPath) {
		if (!skinCache.containsKey(skinPath)) {
			Skin skin = new Skin(Gdx.files.internal(skinPath));
			skinCache.put(skinPath, skin);
		}
		return skinCache.get(skinPath);
	}

	/**
	 * Retrieves a UI texture by file path, loading and caching it if not already loaded.
	 */
	public Texture getTexture(String path) {
		if (!textureCache.containsKey(path)) {
			Texture texture = new Texture(Gdx.files.internal(path));
			textureCache.put(path, texture);
		}
		return textureCache.get(path);
	}

	/**
	 * Retrieves a TextureRegion for a texture path.
	 */
	public TextureRegion getTextureRegion(String path) {
		return new TextureRegion(getTexture(path));
	}

	/**
	 * Retrieves a Drawable (TextureRegionDrawable) for a texture path.
	 */
	public Drawable getDrawable(String path) {
		return new TextureRegionDrawable(getTextureRegion(path));
	}

	/**
	 * Explicitly unloads and disposes a texture by path from the cache.
	 */
	public void unloadTexture(String path) {
		Texture texture = textureCache.remove(path);
		if (texture != null) {
			texture.dispose();
		}
	}

	/**
	 * Explicitly unloads an instruction screen texture when dismissed.
	 */
	public void unloadInstructionTexture(String path) {
		unloadTexture(path);
	}

	/**
	 * Checks if a texture path is currently loaded in the cache.
	 */
	public boolean isTextureLoaded(String path) {
		return textureCache.containsKey(path);
	}

	/**
	 * Checks if a skin path is currently loaded in the cache.
	 */
	public boolean isSkinLoaded(String skinPath) {
		return skinCache.containsKey(skinPath);
	}

	/**
	 * Releases all cached UI textures, skin atlases, and skin resources.
	 */
	@Override
	public void dispose() {
		for (Texture texture : textureCache.values()) {
			if (texture != null) {
				texture.dispose();
			}
		}
		textureCache.clear();

		for (Skin skin : skinCache.values()) {
			if (skin != null) {
				skin.dispose();
			}
		}
		skinCache.clear();

		synchronized (UIManager.class) {
			if (instance == this) {
				instance = null;
			}
		}
	}
}
