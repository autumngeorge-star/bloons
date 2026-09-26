package com.hongbao.bloons.actors;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class RenderableTextButton extends RenderableActor {

	public RenderableTextButton(TextButton textButton, int zIndex) {
		setActor(textButton);
		setZIndex(zIndex);
	}

	public TextButton getActor() {
		return (TextButton) super.getActor();
	}

}
