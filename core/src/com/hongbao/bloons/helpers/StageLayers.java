package com.hongbao.bloons.helpers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.RenderableActor;

import java.util.HashMap;
import java.util.Map;

public class StageLayers {

	private final Group mapLayer = new Group();
	private final Group spellCardLayer = new Group();
	private final Group girlLayer = new Group();
	private final Group bloonLayer = new Group();
	private final Group bulletLayer = new Group();
	private final Group menuLayer = new Group();
	private final Group menuItemLayer = new Group();

	private final Map<Integer, Group> layersByZIndex = new HashMap<>();

	public StageLayers(Stage stage) {
		this(stage.getRoot());
	}

	public StageLayers(Group root) {
		mapLayer.setName("MapLayer");
		spellCardLayer.setName("SpellCardLayer");
		girlLayer.setName("GirlLayer");
		bloonLayer.setName("BloonLayer");
		bulletLayer.setName("BulletLayer");
		menuLayer.setName("MenuLayer");
		menuItemLayer.setName("MenuItemLayer");

		// Add layers to root group in ascending depth order
		root.addActor(mapLayer);
		root.addActor(spellCardLayer);
		root.addActor(girlLayer);
		root.addActor(bloonLayer);
		root.addActor(bulletLayer);
		root.addActor(menuLayer);
		root.addActor(menuItemLayer);

		layersByZIndex.put(ZIndex.MAP_Z_INDEX, mapLayer);
		layersByZIndex.put(ZIndex.SPELL_CARD_Z_INDEX, spellCardLayer);
		layersByZIndex.put(ZIndex.GIRL_Z_INDEX, girlLayer);
		layersByZIndex.put(ZIndex.BLOON_Z_INDEX, bloonLayer);
		layersByZIndex.put(ZIndex.BULLET_Z_INDEX, bulletLayer);
		layersByZIndex.put(ZIndex.MENU_Z_INDEX, menuLayer);
		layersByZIndex.put(ZIndex.MENU_ITEM_Z_INDEX, menuItemLayer);
	}

	public Group getMapLayer() {
		return mapLayer;
	}

	public Group getSpellCardLayer() {
		return spellCardLayer;
	}

	public Group getGirlLayer() {
		return girlLayer;
	}

	public Group getBloonLayer() {
		return bloonLayer;
	}

	public Group getBulletLayer() {
		return bulletLayer;
	}

	public Group getMenuLayer() {
		return menuLayer;
	}

	public Group getMenuItemLayer() {
		return menuItemLayer;
	}

	public Group getLayer(int zIndex) {
		Group group = layersByZIndex.get(zIndex);
		if (group == null) {
			throw new IllegalArgumentException("No layer group configured for Z-index: " + zIndex);
		}
		return group;
	}

	public void addActor(Actor actor) {
		if (actor instanceof RenderableActor) {
			int zIndex = ((RenderableActor) actor).getZIndex();
			getLayer(zIndex).addActor(actor);
		} else {
			mapLayer.addActor(actor);
		}
	}

	public void addActor(Actor actor, int zIndex) {
		getLayer(zIndex).addActor(actor);
	}
}
