package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.hongbao.bloons.actors.RenderableActor;

import java.util.Comparator;


public class SortByZIndex implements Comparator<Actor> {

	public static final SortByZIndex INSTANCE = new SortByZIndex();
	public static final Comparator<Actor> COMPARATOR = INSTANCE;

	public static int getZIndex(Actor a) {
		if (a instanceof RenderableActor) {
			return ((RenderableActor) a).getZIndex();
		}
		return Integer.MIN_VALUE;
	}

	@Override
	public int compare(Actor a, Actor b) {
		return Integer.compare(getZIndex(a), getZIndex(b));
	}

	public static void addActorInOrder(Stage stage, Actor actor) {
		if (stage == null || actor == null) {
			return;
		}
		Array<Actor> actors = stage.getActors();
		int low = 0;
		int high = actors.size;
		int targetZ = getZIndex(actor);

		while (low < high) {
			int mid = (low + high) >>> 1;
			int midZ = getZIndex(actors.get(mid));
			if (midZ <= targetZ) {
				low = mid + 1;
			} else {
				high = mid;
			}
		}
		stage.getRoot().addActorAt(low, actor);
	}
}
