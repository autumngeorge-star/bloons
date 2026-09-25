package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.TowerType;


public class GirlFactory {

	public static Girl create(TowerType type) {
		return type.create();
	}

	public static Girl createReimu() {
		return create(TowerType.REIMU);
	}

	public static Girl createYukari() {
		return create(TowerType.YUKARI);
	}

	public static Girl createMarisa() {
		return create(TowerType.MARISA);
	}

	public static Girl createAlice() {
		return create(TowerType.ALICE);
	}

	public static Girl createSakuya() {
		return create(TowerType.SAKUYA);
	}

	public static Girl createRemilia() {
		return create(TowerType.REMILIA);
	}

	public static Girl createYoumu() {
		return create(TowerType.YOUMU);
	}

	public static Girl createYuyuko() {
		return create(TowerType.YUYUKO);
	}

}
