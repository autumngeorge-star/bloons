package com.hongbao.bloons;

public class NullAudioService implements AudioService {

	@Override
	public void playPopSound() {
		// No-op for headless and testing environments
	}

	@Override
	public void playDamageSound() {
		// No-op for headless and testing environments
	}

	@Override
	public void dispose() {
		// No-op for headless and testing environments
	}
}
