package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.hongbao.bloons.comparators.YDepthComparator;
import com.hongbao.bloons.helpers.StageLayers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class StageLayersTest {

	static class DummyActor extends Actor {
		private final String name;
		public DummyActor(String name, float y) {
			this.name = name;
			setY(y);
		}
		@Override
		public String toString() {
			return name + "(y=" + getY() + ")";
		}
	}

	@Test
	public void testFixedGroupDrawOrder() {
		Group root = new Group();
		StageLayers layers = new StageLayers(root);

		assertEquals("Stage root should contain 6 layer groups", 6, root.getChildren().size);
		assertEquals("BackgroundLayer", root.getChildren().get(0).getName());
		assertEquals("SpellCardLayer", root.getChildren().get(1).getName());
		assertEquals("TowerLayer", root.getChildren().get(2).getName());
		assertEquals("BloonLayer", root.getChildren().get(3).getName());
		assertEquals("BulletLayer", root.getChildren().get(4).getName());
		assertEquals("UILayer", root.getChildren().get(5).getName());

		assertSame(layers.getBackgroundGroup(), root.getChildren().get(0));
		assertSame(layers.getSpellCardGroup(), root.getChildren().get(1));
		assertSame(layers.getTowerGroup(), root.getChildren().get(2));
		assertSame(layers.getBloonGroup(), root.getChildren().get(3));
		assertSame(layers.getBulletGroup(), root.getChildren().get(4));
		assertSame(layers.getUiGroup(), root.getChildren().get(5));
	}

	@Test
	public void testActorRoutingToLayers() {
		Group root = new Group();
		StageLayers layers = new StageLayers(root);

		DummyActor bg = new DummyActor("bg", 0);
		DummyActor spell = new DummyActor("spell", 100);
		DummyActor tower = new DummyActor("tower", 200);
		DummyActor bloon = new DummyActor("bloon", 300);
		DummyActor bullet = new DummyActor("bullet", 400);
		DummyActor ui = new DummyActor("ui", 500);

		layers.addBackgroundActor(bg);
		layers.addSpellCardActor(spell);
		layers.addTowerActor(tower);
		layers.addBloonActor(bloon);
		layers.addBulletActor(bullet);
		layers.addUiActor(ui);

		assertEquals(1, layers.getBackgroundGroup().getChildren().size);
		assertEquals(1, layers.getSpellCardGroup().getChildren().size);
		assertEquals(1, layers.getTowerGroup().getChildren().size);
		assertEquals(1, layers.getBloonGroup().getChildren().size);
		assertEquals(1, layers.getBulletGroup().getChildren().size);
		assertEquals(1, layers.getUiGroup().getChildren().size);

		assertSame(bg, layers.getBackgroundGroup().getChildren().get(0));
		assertSame(spell, layers.getSpellCardGroup().getChildren().get(0));
		assertSame(tower, layers.getTowerGroup().getChildren().get(0));
		assertSame(bloon, layers.getBloonGroup().getChildren().get(0));
		assertSame(bullet, layers.getBulletGroup().getChildren().get(0));
		assertSame(ui, layers.getUiGroup().getChildren().get(0));
	}

	@Test
	public void testYDepthComparatorSortingAndContract() {
		YDepthComparator comparator = new YDepthComparator();

		DummyActor a1 = new DummyActor("a1", 100);
		DummyActor a2 = new DummyActor("a2", 500);
		DummyActor a3 = new DummyActor("a3", 300);

		// Reflexivity
		assertEquals(0, comparator.compare(a1, a1));

		// Higher Y (500) should be drawn first (smaller index) than lower Y (100)
		assertTrue(comparator.compare(a2, a1) < 0);
		assertTrue(comparator.compare(a1, a2) > 0);

		// Anti-symmetry
		assertEquals(-comparator.compare(a2, a1), comparator.compare(a1, a2));

		// Transitivity: a2(500) < a3(300) and a3(300) < a1(100) => a2(500) < a1(100)
		assertTrue(comparator.compare(a2, a3) < 0);
		assertTrue(comparator.compare(a3, a1) < 0);
		assertTrue(comparator.compare(a2, a1) < 0);

		// Test sorting list of actors
		List<Actor> list = new ArrayList<>();
		list.add(a1); // Y=100
		list.add(a2); // Y=500
		list.add(a3); // Y=300

		Collections.sort(list, comparator);

		assertSame(a2, list.get(0)); // Y=500 drawn first
		assertSame(a3, list.get(1)); // Y=300 drawn second
		assertSame(a1, list.get(2)); // Y=100 drawn last (on top)
	}

	@Test
	public void testDeterministicTieBreakerSameY() {
		YDepthComparator comparator = new YDepthComparator();

		DummyActor t1 = new DummyActor("t1", 200);
		DummyActor t2 = new DummyActor("t2", 200);

		int res1 = comparator.compare(t1, t2);
		assertNotEquals("Distinct instances with same Y must have deterministic non-zero order", 0, res1);
		assertEquals("Anti-symmetric comparison", -res1, comparator.compare(t2, t1));

		// Repeated comparisons must yield identical result
		assertEquals(res1, comparator.compare(t1, t2));
	}

	@Test
	public void testDynamicLayerSorting() {
		Group root = new Group();
		StageLayers layers = new StageLayers(root);

		DummyActor tLow = new DummyActor("towerLow", 100);
		DummyActor tHigh = new DummyActor("towerHigh", 400);

		layers.addTowerActor(tLow);
		layers.addTowerActor(tHigh);

		// Before sort: insertion order
		assertSame(tLow, layers.getTowerGroup().getChildren().get(0));
		assertSame(tHigh, layers.getTowerGroup().getChildren().get(1));

		layers.sortDynamicLayers();

		// After sort: higher Y (400) comes first
		assertSame(tHigh, layers.getTowerGroup().getChildren().get(0));
		assertSame(tLow, layers.getTowerGroup().getChildren().get(1));
	}
}
