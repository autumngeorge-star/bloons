package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.helpers.ZIndex;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class SortByZIndex implements Comparator<Actor> {

	private final Map<Actor, Long> instanceIds = Collections.synchronizedMap(new WeakHashMap<>());
	private static final AtomicLong idCounter = new AtomicLong(0);

	private long getUniqueId(Actor actor) {
		return instanceIds.computeIfAbsent(actor, k -> idCounter.getAndIncrement());
	}

	@Override
	public int compare(Actor a, Actor b) {
		if (a == b) {
			return 0;
		}
		if (a == null && b == null) {
			return 0;
		}
		if (a == null) {
			return -1;
		}
		if (b == null) {
			return 1;
		}

		int zIndexA = (a instanceof RenderableActor) ? ((RenderableActor) a).getZIndex() : ZIndex.DEFAULT_BACKGROUND_Z_INDEX;
		int zIndexB = (b instanceof RenderableActor) ? ((RenderableActor) b).getZIndex() : ZIndex.DEFAULT_BACKGROUND_Z_INDEX;

		if (zIndexA != zIndexB) {
			return Integer.compare(zIndexA, zIndexB);
		}

		int yCompare = Float.compare(b.getY(), a.getY());
		if (yCompare != 0) {
			return yCompare;
		}

		long idA = getUniqueId(a);
		long idB = getUniqueId(b);
		return Long.compare(idA, idB);
	}
}
