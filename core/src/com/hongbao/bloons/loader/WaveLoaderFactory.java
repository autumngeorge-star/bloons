package com.hongbao.bloons.loader;

import com.badlogic.gdx.files.FileHandle;

public class WaveLoaderFactory {

	public static WaveLoader getLoader(FileHandle file) {
		if (file == null) {
			throw new IllegalArgumentException("FileHandle cannot be null");
		}
		String ext = file.extension().toLowerCase();
		if ("json".equals(ext)) {
			return new JsonWaveLoader();
		} else if ("txt".equals(ext)) {
			return new LegacyTextWaveLoader();
		} else {
			throw new IllegalArgumentException("Unsupported wave file extension '" + ext + "' for file: " + file.name());
		}
	}

}
