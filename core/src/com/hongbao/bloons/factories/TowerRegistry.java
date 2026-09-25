package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TowerRegistry {

	private static final Map<String, TowerDefinition> REGISTRY = new LinkedHashMap<>();

	public static synchronized void registerTower(TowerDefinition definition) {
		REGISTRY.put(definition.getId().toLowerCase(), definition);
	}

	public static void registerTower(String id, Supplier<Girl> supplier, String uiIconPath) {
		registerTower(new TowerDefinition(id, supplier, uiIconPath));
	}

	public static synchronized List<TowerDefinition> getRegisteredTowers() {
		return Collections.unmodifiableList(new ArrayList<>(REGISTRY.values()));
	}

	public static synchronized TowerDefinition getTower(String id) {
		if (id == null) {
			return null;
		}
		return REGISTRY.get(id.toLowerCase());
	}

	public static synchronized TowerDefinition getTower(int index) {
		List<TowerDefinition> towers = getRegisteredTowers();
		if (index < 0 || index >= towers.size()) {
			return null;
		}
		return towers.get(index);
	}

	public static synchronized void clear() {
		REGISTRY.clear();
	}

}
