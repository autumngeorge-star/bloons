package com.hongbao.bloons;

/**
 * Interface for loading level data and creating a BloonQueue from a wave asset source.
 */
@FunctionalInterface
public interface WaveLoader {
	/**
	 * Loads and creates a BloonQueue instance.
	 *
	 * @return A new BloonQueue populated with wave level data and metadata.
	 */
	BloonQueue load();

	/**
	 * Convenience method delegating to load().
	 *
	 * @return A new BloonQueue instance.
	 */
	default BloonQueue loadWaveQueue() {
		return load();
	}
}
