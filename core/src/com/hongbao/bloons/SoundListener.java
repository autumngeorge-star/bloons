package com.hongbao.bloons;

/**
 * Event listener interface for game sound events.
 */
public interface SoundListener {

	/**
	 * Called when a bloon is popped completely.
	 */
	void onBloonPopped();

	/**
	 * Called when a bloon takes damage without being completely popped.
	 */
	void onBloonDamaged();

}
