package com.hongbao.bloons.comparators;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Comparator;


public class SortByZIndex implements Comparator<Actor> {

	private final YDepthComparator yDepthComparator = new YDepthComparator();

	@Override
	public int compare(Actor a, Actor b) {
		return yDepthComparator.compare(a, b);
	}
}
