package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class BloonTextureCache implements Disposable {

	private static BloonTextureCache instance;

	private final Map<String, Texture> textureMap = new HashMap<>();

	public static synchronized BloonTextureCache getInstance() {
		if (instance == null) {
			instance = new BloonTextureCache();
		}
		return instance;
	}

	public static synchronized void resetInstance() {
		if (instance != null) {
			instance.dispose();
			instance = null;
		}
	}

	public TextureRegion getTextureRegion(String fileName) {
		Texture texture = getTexture(fileName);
		return new TextureRegion(texture);
	}

	public Texture getTexture(String fileName) {
		if (!textureMap.containsKey(fileName)) {
			Texture texture = createTexture(fileName);
			textureMap.put(fileName, texture);
		}
		return textureMap.get(fileName);
	}

	protected Texture createTexture(String fileName) {
		return new Texture(Gdx.files.internal(fileName));
	}

	public boolean contains(String fileName) {
		return textureMap.containsKey(fileName);
	}

	public int size() {
		return textureMap.size();
	}

	public void clear() {
		dispose();
	}

	@Override
	public void dispose() {
		for (Texture texture : textureMap.values()) {
			if (texture != null) {
				texture.dispose();
			}
		}
		textureMap.clear();
	}
}
