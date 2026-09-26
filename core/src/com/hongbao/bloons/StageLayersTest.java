package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.hongbao.bloons.actors.RenderableLabel;
import com.hongbao.bloons.helpers.StageLayers;
import com.hongbao.bloons.helpers.ZIndex;

public class StageLayersTest {

	public static void main(String[] args) {
		Group root = new Group();
		StageLayers stageLayers = new StageLayers(root);

		// Assert 7 layer groups exist on root in ascending depth order
		if (root.getChildren().size != 7) {
			throw new AssertionError("Expected 7 layer groups on root group, found: " + root.getChildren().size);
		}

		Group mapLayer = (Group) root.getChildren().get(0);
		Group spellCardLayer = (Group) root.getChildren().get(1);
		Group girlLayer = (Group) root.getChildren().get(2);
		Group bloonLayer = (Group) root.getChildren().get(3);
		Group bulletLayer = (Group) root.getChildren().get(4);
		Group menuLayer = (Group) root.getChildren().get(5);
		Group menuItemLayer = (Group) root.getChildren().get(6);

		if (stageLayers.getMapLayer() != mapLayer ||
			stageLayers.getSpellCardLayer() != spellCardLayer ||
			stageLayers.getGirlLayer() != girlLayer ||
			stageLayers.getBloonLayer() != bloonLayer ||
			stageLayers.getBulletLayer() != bulletLayer ||
			stageLayers.getMenuLayer() != menuLayer ||
			stageLayers.getMenuItemLayer() != menuItemLayer) {
			throw new AssertionError("Layer group mismatch or incorrect ordering on root group.");
		}

		if (!"MapLayer".equals(mapLayer.getName()) ||
			!"SpellCardLayer".equals(spellCardLayer.getName()) ||
			!"GirlLayer".equals(girlLayer.getName()) ||
			!"BloonLayer".equals(bloonLayer.getName()) ||
			!"BulletLayer".equals(bulletLayer.getName()) ||
			!"MenuLayer".equals(menuLayer.getName()) ||
			!"MenuItemLayer".equals(menuItemLayer.getName())) {
			throw new AssertionError("Layer group names do not match expected Z-index layer tier names.");
		}

		// Test adding actor to layer
		RenderableLabel label = new RenderableLabel(null, ZIndex.MENU_ITEM_Z_INDEX);
		stageLayers.getMenuItemLayer().addActor(label);
		if (stageLayers.getMenuItemLayer().getChildren().size != 1 || label.getParent() != stageLayers.getMenuItemLayer()) {
			throw new AssertionError("Actor was not properly added to menuItemLayer group.");
		}

		// Test generic addActor routing
		RenderableLabel girlActor = new RenderableLabel(null, ZIndex.GIRL_Z_INDEX);
		stageLayers.addActor(girlActor);
		if (stageLayers.getGirlLayer().getChildren().size != 1 || girlActor.getParent() != stageLayers.getGirlLayer()) {
			throw new AssertionError("Actor was not properly routed to girlLayer group.");
		}

		// Test removing actor from layer
		if (!label.remove() || stageLayers.getMenuItemLayer().getChildren().size != 0) {
			throw new AssertionError("Actor removal from menuItemLayer failed.");
		}

		System.out.println("ALL STAGE LAYERS TESTS PASSED SUCCESSFULLY!");
	}
}
