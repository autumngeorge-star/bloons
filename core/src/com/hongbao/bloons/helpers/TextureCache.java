package com.hongbao.bloons.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {

	private static final Map<String, Texture> textures = new HashMap<>();
	private static final Map<String, TextureRegion> textureRegions = new HashMap<>();

	private TextureCache() {
	}

	public static Texture getTexture(String filePath) {
		if (filePath == null || filePath.isEmpty()) {
			return null;
		}
		Texture texture = textures.get(filePath);
		if (texture == null) {
			texture = new Texture(Gdx.files.internal(filePath));
			textures.put(filePath, texture);
		}
		return texture;
	}

	public static TextureRegion getTextureRegion(String filePath) {
		if (filePath == null || filePath.isEmpty()) {
			return null;
		}
		TextureRegion region = textureRegions.get(filePath);
		if (region == null) {
			Texture texture = getTexture(filePath);
			if (texture != null) {
				region = new TextureRegion(texture);
				textureRegions.put(filePath, region);
			}
		}
		return region;
	}

	public static void dispose() {
		for (Texture texture : textures.values()) {
			if (texture != null) {
				texture.dispose();
			}
		}
		textures.clear();
		textureRegions.clear();
	}

	public static void clear() {
		dispose();
	}
}
