package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class LibGdxAudioService implements AudioService {

	private Sound popSound;
	private Sound damageSound;

	public LibGdxAudioService() {
		if (Gdx.audio != null && Gdx.files != null) {
			popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
		}
	}

	@Override
	public void playPopSound() {
		if (popSound != null) {
			popSound.play(0.5f);
		}
	}

	@Override
	public void playDamageSound() {
		if (damageSound != null) {
			damageSound.play(0.5f);
		}
	}

	@Override
	public void dispose() {
		if (popSound != null) {
			popSound.dispose();
			popSound = null;
		}
		if (damageSound != null) {
			damageSound.dispose();
			damageSound = null;
		}
	}
}
