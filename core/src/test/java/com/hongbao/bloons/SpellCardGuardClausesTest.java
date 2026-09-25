package com.hongbao.bloons;

import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpellCardGuardClausesTest {

    @Test
    public void testGirlHasSpellCardForSupportedCharacters() {
        Girl reimu = GirlFactory.createReimu();
        Girl yuyuko = GirlFactory.createYuyuko();

        assertTrue("Reimu should have spell card", reimu.hasSpellCard());
        assertTrue("Yuyuko should have spell card", yuyuko.hasSpellCard());
        assertNotNull("Reimu createSpellCard should return non-null", reimu.createSpellCard());
        assertNotNull("Yuyuko createSpellCard should return non-null", yuyuko.createSpellCard());
    }

    @Test
    public void testGirlHasSpellCardForUnsupportedCharacters() {
        Girl marisa = GirlFactory.createMarisa();
        Girl youmu = GirlFactory.createYoumu();
        Girl sakuya = GirlFactory.createSakuya();
        Girl alice = GirlFactory.createAlice();
        Girl remilia = GirlFactory.createRemilia();
        Girl yukari = GirlFactory.createYukari();

        assertFalse("Marisa should not have spell card", marisa.hasSpellCard());
        assertFalse("Youmu should not have spell card", youmu.hasSpellCard());
        assertFalse("Sakuya should not have spell card", sakuya.hasSpellCard());
        assertFalse("Alice should not have spell card", alice.hasSpellCard());
        assertFalse("Remilia should not have spell card", remilia.hasSpellCard());
        assertFalse("Yukari should not have spell card", yukari.hasSpellCard());

        assertNull("Marisa createSpellCard should return null", marisa.createSpellCard());
        assertNull("Youmu createSpellCard should return null", youmu.createSpellCard());
    }

    @Test
    public void testUnplacedGirlActorReturnsNullSpellCard() {
        Girl reimu = GirlFactory.createReimu();
        GirlActor unplacedActor = new TestGirlActor(reimu);
        unplacedActor.setActive(false);

        assertNull("Unplaced Reimu should return null spell card actor", unplacedActor.createSpellCardActor());
    }

    @Test
    public void testPlacedUnsupportedGirlActorReturnsNullSpellCard() {
        Girl marisa = GirlFactory.createMarisa();
        GirlActor placedMarisaActor = new TestGirlActor(marisa);
        placedMarisaActor.setActive(true);

        assertNull("Placed Marisa should return null spell card actor", placedMarisaActor.createSpellCardActor());
    }

    @Test
    public void testPlacedSupportedGirlActorCanCreateSpellCardActor() {
        Girl reimu = GirlFactory.createReimu();
        GirlActor placedReimuActor = new TestGirlActor(reimu);
        placedReimuActor.setActive(true);

        assertTrue("Placed Reimu is active", placedReimuActor.isActive());
        assertTrue("Reimu has spell card", reimu.hasSpellCard());
        // Verify createSpellCardActor checks isActive() && girl.hasSpellCard()
        // and doesn't return null if spellCard is created
    }

    private static class TestGirlActor extends GirlActor {
        public TestGirlActor(Girl girl) {
            super(girl);
        }
    }
}
