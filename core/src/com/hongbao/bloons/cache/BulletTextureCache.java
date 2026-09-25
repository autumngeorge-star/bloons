package com.hongbao.bloons.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight texture cache for bullet assets.
 * Stores shared Texture and TextureRegion objects indexed by bullet file path
 * to prevent native memory leaks during high firing rates.
 */
public class BulletTextureCache {

	private static final Map<String, TextureRegion> cache = new HashMap<>();

	private BulletTextureCache() {
		// Utility class
	}

	/**
	 * Retrieves or loads a shared TextureRegion for the specified bullet image file path.
	 *
	 * @param filePath internal file path to the texture asset
	 * @return cached TextureRegion instance, or null if filePath is null
	 */
	public static TextureRegion getTextureRegion(String filePath) {
		if (filePath == null) {
			return null;
		}

		TextureRegion region = cache.get(filePath);
		if (region == null) {
			Texture texture = new Texture(Gdx.files.internal(filePath));
			region = new TextureRegion(texture);
			cache.put(filePath, region);
		}

		return region;
	}

	/**
	 * Disposes all cached GPU texture handles and clears the cache.
	 * Called during game shutdown.
	 */
	public static void dispose() {
		for (TextureRegion region : cache.values()) {
			if (region != null && region.getTexture() != null) {
				region.getTexture().dispose();
			}
		}
		cache.clear();
	}

	/**
	 * Returns the number of unique texture assets currently cached.
	 *
	 * @return number of entries in the cache
	 */
	public static int getCacheSize() {
		return cache.size();
	}
}
