package com.hongbao.bloons;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.entities.TowerType;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TowerTypeTest {

	@Test
	public void testTowerTypeEnumValuesAndOrder() {
		TowerType[] values = TowerType.values();
		assertEquals(8, values.length);
		assertEquals(TowerType.REIMU, values[0]);
		assertEquals(TowerType.YUKARI, values[1]);
		assertEquals(TowerType.MARISA, values[2]);
		assertEquals(TowerType.ALICE, values[3]);
		assertEquals(TowerType.SAKUYA, values[4]);
		assertEquals(TowerType.REMILIA, values[5]);
		assertEquals(TowerType.YOUMU, values[6]);
		assertEquals(TowerType.YUYUKO, values[7]);
	}

	@Test
	public void testTowerTypeMetadata() {
		assertEquals("Reimu", TowerType.REIMU.getDisplayName());
		assertEquals("img/ui/reimu_box.png", TowerType.REIMU.getBoxImagePath());
		assertEquals(325, TowerType.REIMU.getCost());
		assertEquals(325, TowerType.REIMU.getBaseCost());

		assertEquals("Yukari", TowerType.YUKARI.getDisplayName());
		assertEquals("img/ui/yukari_box.png", TowerType.YUKARI.getBoxImagePath());
		assertEquals(2500, TowerType.YUKARI.getCost());

		assertEquals("Marisa", TowerType.MARISA.getDisplayName());
		assertEquals("img/ui/marisa_box.png", TowerType.MARISA.getBoxImagePath());
		assertEquals(200, TowerType.MARISA.getCost());

		assertEquals("Alice", TowerType.ALICE.getDisplayName());
		assertEquals("img/ui/alice_box.png", TowerType.ALICE.getBoxImagePath());
		assertEquals(450, TowerType.ALICE.getCost());

		assertEquals("Sakuya", TowerType.SAKUYA.getDisplayName());
		assertEquals("img/ui/sakuya_box.png", TowerType.SAKUYA.getBoxImagePath());
		assertEquals(500, TowerType.SAKUYA.getCost());

		assertEquals("Remilia", TowerType.REMILIA.getDisplayName());
		assertEquals("img/ui/remilia_box.png", TowerType.REMILIA.getBoxImagePath());
		assertEquals(2500, TowerType.REMILIA.getCost());

		assertEquals("Youmu", TowerType.YOUMU.getDisplayName());
		assertEquals("img/ui/youmu_box.png", TowerType.YOUMU.getBoxImagePath());
		assertEquals(600, TowerType.YOUMU.getCost());

		assertEquals("Yuyuko", TowerType.YUYUKO.getDisplayName());
		assertEquals("img/ui/yuyuko_box.png", TowerType.YUYUKO.getBoxImagePath());
		assertEquals(2000, TowerType.YUYUKO.getCost());
	}

	@Test
	public void testGirlFactoryCreateWithTowerType() {
		for (TowerType type : TowerType.values()) {
			Girl girl = GirlFactory.create(type);
			assertNotNull(girl);
			assertEquals(type.getDisplayName(), girl.getName());
			assertEquals(type.getCost(), girl.getCost());
		}
	}

	@Test
	public void testGirlFactoryBackwardCompatibility() {
		assertEquals("Reimu", GirlFactory.createReimu().getName());
		assertEquals("Yukari", GirlFactory.createYukari().getName());
		assertEquals("Marisa", GirlFactory.createMarisa().getName());
		assertEquals("Alice", GirlFactory.createAlice().getName());
		assertEquals("Sakuya", GirlFactory.createSakuya().getName());
		assertEquals("Remilia", GirlFactory.createRemilia().getName());
		assertEquals("Youmu", GirlFactory.createYoumu().getName());
		assertEquals("Yuyuko", GirlFactory.createYuyuko().getName());
	}

	@Test
	public void testSpellCardCreationWithDelegates() {
		SpellCard reimuCard = SpellCard.createReimuSpellCard();
		assertNotNull(reimuCard);
		assertEquals("Reimu", reimuCard.getOverrideName());

		SpellCard yuyukoCard = SpellCard.createYuyukoSpellCard();
		assertNotNull(yuyukoCard);
		assertEquals("Yuyuko", yuyukoCard.getOverrideName());
	}

}
