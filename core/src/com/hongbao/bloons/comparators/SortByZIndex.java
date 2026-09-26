package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;

import java.util.Comparator;


public class SortByZIndex implements Comparator<Actor> {

	@Override
	public int compare(Actor a, Actor b) {
		if (a == b) {
			return 0;
		}

		int z1 = (a instanceof RenderableActor) ? ((RenderableActor) a).getZIndex() : 0;
		int z2 = (b instanceof RenderableActor) ? ((RenderableActor) b).getZIndex() : 0;

		int diff = Integer.compare(z1, z2);
		if (diff != 0) {
			return diff;
		}

		int hashCompare = Integer.compare(System.identityHashCode(a), System.identityHashCode(b));
		return hashCompare != 0 ? hashCompare : 1;
	}
}
