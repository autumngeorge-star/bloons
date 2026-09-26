package com.hongbao.bloons.helpers;

import com.badlogic.gdx.Gdx;

public class ZIndex {
	
	public static final int BASE_LAYER_STEP = 100000;

	public static final int SPELL_CARD_Z_INDEX = 100000;
	public static final int GIRL_Z_INDEX = 200000;
	public static final int BLOON_Z_INDEX = 300000;
	public static final int BULLET_Z_INDEX = 400000;
	
	public static final int MENU_Z_INDEX = 1000000;
	public static final int MENU_ITEM_Z_INDEX = 1001000;
	
	public static int calculateDynamicZIndex(int baseLayer, float y, float distanceTraveled, long tieBreaker) {
		float screenHeight = 900f;
		if (Gdx.graphics != null && Gdx.graphics.getHeight() > 0) {
			screenHeight = Gdx.graphics.getHeight();
		}
		float clampedY = Math.max(0f, Math.min(screenHeight, y));
		float normalizedY = clampedY / screenHeight;

		// Lower Y screen position yields higher Z-index for 2.5D orthographic depth sorting
		float yOffset = (1.0f - normalizedY) * 80000f;

		// Distance traveled contribution
		float distOffset = (Math.abs(distanceTraveled) % 10000f) * 1.5f;

		// Deterministic tie-breaker contribution
		int tie = Math.abs((int) (tieBreaker % 1000));

		int subLayer = (int) (yOffset + distOffset + tie);
		if (subLayer > 99999) {
			subLayer = 99999;
		}

		return baseLayer + subLayer;
	}

}
