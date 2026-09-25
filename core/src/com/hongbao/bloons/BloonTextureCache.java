package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized texture cache for Bloon sprite textures to prevent duplicate
 * texture loads and native memory leaks across actor lifecycles.
 */
public class BloonTextureCache {

	private static BloonTextureCache instance;
	private final Map<String, TextureRegion> cache;

	public BloonTextureCache() {
		this.cache = new HashMap<>();
	}

	public static synchronized BloonTextureCache getInstance() {
		if (instance == null) {
			instance = new BloonTextureCache();
		}
		return instance;
	}

	public static TextureRegion getTextureRegion(String fileName) {
		return getInstance().get(fileName);
	}

	public TextureRegion get(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (!cache.containsKey(fileName)) {
			Texture texture = new Texture(Gdx.files.internal(fileName));
			cache.put(fileName, new TextureRegion(texture));
		}
		return cache.get(fileName);
	}

	public boolean containsTexture(String fileName) {
		return cache.containsKey(fileName);
	}

	public int size() {
		return cache.size();
	}

	public void dispose() {
		for (TextureRegion region : cache.values()) {
			if (region != null && region.getTexture() != null) {
				region.getTexture().dispose();
			}
		}
		cache.clear();
	}

	public static void clear() {
		if (instance != null) {
			instance.dispose();
			instance = null;
		}
	}
}
