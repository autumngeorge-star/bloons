package com.hongbao.bloons.helpers;

import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayDeque;
import java.util.Queue;

public class DamageEvent {

	private static final Queue<DamageEvent> POOL = new ArrayDeque<>();

	private Bloon bloon;
	private int damage;

	private DamageEvent(Bloon bloon, int damage) {
		this.bloon = bloon;
		this.damage = damage;
	}

	public static DamageEvent obtain(Bloon bloon, int damage) {
		DamageEvent event = POOL.poll();
		if (event == null) {
			return new DamageEvent(bloon, damage);
		}
		event.bloon = bloon;
		event.damage = damage;
		return event;
	}

	public static void free(DamageEvent event) {
		if (event != null) {
			event.bloon = null;
			event.damage = 0;
			POOL.offer(event);
		}
	}

	public Bloon getBloon() {
		return bloon;
	}

	public int getDamage() {
		return damage;
	}

}
