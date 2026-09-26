package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;

import java.util.Comparator;


public class SortByZIndex implements Comparator<Actor> {

	public static final SortByZIndex INSTANCE = new SortByZIndex();

	public static SortByZIndex getInstance() {
		return INSTANCE;
	}

	public int compare(Actor a, Actor b) {
		if (a instanceof RenderableActor && b instanceof RenderableActor) {
			return a.getZIndex() - b.getZIndex();
		} else {
			return 0; // idk
		}
	}
}
