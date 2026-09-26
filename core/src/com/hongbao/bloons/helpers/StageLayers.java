package com.hongbao.bloons.helpers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.comparators.YDepthComparator;

/**
 * Manages stage actor rendering layers using distinct LibGDX Group containers
 * arranged in fixed bottom-to-top draw order:
 * 1. Background
 * 2. SpellCards
 * 3. Towers
 * 4. Bloons
 * 5. Bullets
 * 6. UI
 */
public class StageLayers {

	private final Group backgroundGroup;
	private final Group spellCardGroup;
	private final Group towerGroup;
	private final Group bloonGroup;
	private final Group bulletGroup;
	private final Group uiGroup;

	private final YDepthComparator yDepthComparator;

	public StageLayers(Stage stage) {
		this(stage != null ? stage.getRoot() : new Group());
	}

	public StageLayers(Group rootGroup) {
		backgroundGroup = new Group();
		backgroundGroup.setName("BackgroundLayer");

		spellCardGroup = new Group();
		spellCardGroup.setName("SpellCardLayer");

		towerGroup = new Group();
		towerGroup.setName("TowerLayer");

		bloonGroup = new Group();
		bloonGroup.setName("BloonLayer");

		bulletGroup = new Group();
		bulletGroup.setName("BulletLayer");

		uiGroup = new Group();
		uiGroup.setName("UILayer");

		yDepthComparator = new YDepthComparator();

		if (rootGroup != null) {
			rootGroup.addActor(backgroundGroup);
			rootGroup.addActor(spellCardGroup);
			rootGroup.addActor(towerGroup);
			rootGroup.addActor(bloonGroup);
			rootGroup.addActor(bulletGroup);
			rootGroup.addActor(uiGroup);
		}
	}

	public Group getBackgroundGroup() {
		return backgroundGroup;
	}

	public Group getSpellCardGroup() {
		return spellCardGroup;
	}

	public Group getTowerGroup() {
		return towerGroup;
	}

	public Group getBloonGroup() {
		return bloonGroup;
	}

	public Group getBulletGroup() {
		return bulletGroup;
	}

	public Group getUiGroup() {
		return uiGroup;
	}

	public void addBackgroundActor(Actor actor) {
		backgroundGroup.addActor(actor);
	}

	public void addSpellCardActor(Actor actor) {
		spellCardGroup.addActor(actor);
	}

	public void addTowerActor(Actor actor) {
		towerGroup.addActor(actor);
	}

	public void addBloonActor(Actor actor) {
		bloonGroup.addActor(actor);
	}

	public void addBulletActor(Actor actor) {
		bulletGroup.addActor(actor);
	}

	public void addUiActor(Actor actor) {
		uiGroup.addActor(actor);
	}

	public void sortDynamicLayers() {
		spellCardGroup.getChildren().sort(yDepthComparator);
		towerGroup.getChildren().sort(yDepthComparator);
		bloonGroup.getChildren().sort(yDepthComparator);
		bulletGroup.getChildren().sort(yDepthComparator);
	}
}
