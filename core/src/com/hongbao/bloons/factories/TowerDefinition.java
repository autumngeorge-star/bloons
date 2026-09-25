package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;

import java.util.Objects;
import java.util.function.Supplier;

public class TowerDefinition {

	private final String id;
	private final Supplier<Girl> supplier;
	private final String uiIconPath;

	public TowerDefinition(String id, Supplier<Girl> supplier, String uiIconPath) {
		this.id = Objects.requireNonNull(id, "id cannot be null");
		this.supplier = Objects.requireNonNull(supplier, "supplier cannot be null");
		this.uiIconPath = Objects.requireNonNull(uiIconPath, "uiIconPath cannot be null");
	}

	public String getId() {
		return id;
	}

	public Supplier<Girl> getSupplier() {
		return supplier;
	}

	public String getUiIconPath() {
		return uiIconPath;
	}

	public Girl createGirl() {
		return supplier.get();
	}

}
