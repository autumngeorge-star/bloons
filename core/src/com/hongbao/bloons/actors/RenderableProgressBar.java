package com.hongbao.bloons.actors;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;


public class RenderableProgressBar extends RenderableActor {

	public RenderableProgressBar(ProgressBar progressBar, int zIndex) {
		setActor(progressBar);
		setZIndex(zIndex);
	}

	public ProgressBar getActor() {
		return (ProgressBar) super.getActor();
	}

}
