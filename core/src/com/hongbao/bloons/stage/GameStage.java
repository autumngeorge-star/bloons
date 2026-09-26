package com.hongbao.bloons.stage;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStage extends Stage {

	private boolean dirty = true;

	public GameStage() {
		super();
		setRoot(new RootGroup());
	}

	public GameStage(Viewport viewport) {
		super(viewport);
		setRoot(new RootGroup());
	}

	public GameStage(Viewport viewport, Batch batch) {
		super(viewport, batch);
		setRoot(new RootGroup());
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public void markDirty() {
		this.dirty = true;
	}

	@Override
	public void addActor(Actor actor) {
		super.addActor(actor);
		markDirty();
	}

	private class RootGroup extends Group {
		@Override
		protected void childrenChanged() {
			super.childrenChanged();
			markDirty();
		}
	}
}
