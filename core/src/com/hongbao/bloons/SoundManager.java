package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Production SoundListener implementation using LibGDX audio API.
 */
public class SoundManager implements SoundListener {

	private Sound popSound;

	public SoundManager() {
		if (Gdx.audio != null && Gdx.files != null) {
			try {
				popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
			} catch (Exception e) {
				popSound = null;
			}
		}
	}

	@Override
	public void onBloonPopped() {
		if (popSound != null) {
			popSound.play(0.5f);
		}
	}

	@Override
	public void onBloonDamaged() {
		// Reserved for future damage sound playback
	}

	public void dispose() {
		if (popSound != null) {
			popSound.dispose();
			popSound = null;
		}
	}

}
