package com.hongbao.bloons.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight texture cache for sharing immutable Texture instances across game actors and UI.
 */
public class TextureCache {

    private static final Map<String, Texture> cache = new HashMap<>();

    private TextureCache() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves an existing cached Texture for the given file path, or loads and caches it if absent.
     *
     * @param filePath The relative path to the internal asset file.
     * @return The cached Texture instance.
     */
    public static Texture getTexture(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("filePath cannot be null");
        }
        if (!cache.containsKey(filePath)) {
            Texture texture = new Texture(Gdx.files.internal(filePath));
            cache.put(filePath, texture);
        }
        return cache.get(filePath);
    }

    /**
     * Alias for getTexture(filePath) for convenience.
     */
    public static Texture get(String filePath) {
        return getTexture(filePath);
    }

    /**
     * Returns the number of cached textures.
     */
    public static int size() {
        return cache.size();
    }

    /**
     * Checks if a texture for the given file path is currently cached.
     */
    public static boolean contains(String filePath) {
        return cache.containsKey(filePath);
    }

    /**
     * Disposes all cached Texture instances and clears the cache.
     * Must be called on application teardown within the OpenGL thread context.
     */
    public static void dispose() {
        for (Texture texture : cache.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        cache.clear();
    }

    /**
     * Clears the cache map without disposing textures (primarily for testing).
     */
    public static void clear() {
        cache.clear();
    }
}
