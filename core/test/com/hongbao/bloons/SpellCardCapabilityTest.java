package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpellCardCapabilityTest {

    @Test
    public void testGirlHasSpellCardCapability() {
        Girl reimu = GirlFactory.createReimu();
        Girl yuyuko = GirlFactory.createYuyuko();
        Girl marisa = GirlFactory.createMarisa();
        Girl youmu = GirlFactory.createYoumu();

        assertTrue("Reimu should have spell card capability", reimu.hasSpellCardCapability());
        assertTrue("Yuyuko should have spell card capability", yuyuko.hasSpellCardCapability());
        assertFalse("Marisa should NOT have spell card capability", marisa.hasSpellCardCapability());
        assertFalse("Youmu should NOT have spell card capability", youmu.hasSpellCardCapability());
    }

    @Test
    public void testGirlCreateSpellCardReturnsOptional() {
        Girl reimu = GirlFactory.createReimu();
        Optional<SpellCard> reimuSpell = reimu.createSpellCard();
        assertTrue("Reimu spell card should be present", reimuSpell.isPresent());
        assertEquals("Reimu", reimuSpell.get().getOverrideName());

        Girl yuyuko = GirlFactory.createYuyuko();
        Optional<SpellCard> yuyukoSpell = yuyuko.createSpellCard();
        assertTrue("Yuyuko spell card should be present", yuyukoSpell.isPresent());
        assertEquals("Yuyuko", yuyukoSpell.get().getOverrideName());

        Girl marisa = GirlFactory.createMarisa();
        Optional<SpellCard> marisaSpell = marisa.createSpellCard();
        assertFalse("Marisa spell card should be empty", marisaSpell.isPresent());

        Girl youmu = GirlFactory.createYoumu();
        Optional<SpellCard> youmuSpell = youmu.createSpellCard();
        assertFalse("Youmu spell card should be empty", youmuSpell.isPresent());
    }

    @Test
    public void testGirlActorCreateSpellCardActorWhenInactive() throws Exception {
        GirlActor reimuActor = mock(GirlActor.class);
        Girl reimu = GirlFactory.createReimu();
        Field girlField = GirlActor.class.getDeclaredField("girl");
        girlField.setAccessible(true);
        girlField.set(reimuActor, reimu);

        when(reimuActor.isActive()).thenReturn(false);
        when(reimuActor.createSpellCardActor()).thenCallRealMethod();

        Optional<SpellCardActor> spellCardActor = reimuActor.createSpellCardActor();
        assertFalse("Inactive tower preview should return empty Optional<SpellCardActor>", spellCardActor.isPresent());
    }

    @Test
    public void testGirlActorCreateSpellCardActorWhenActiveForUnsupportedTower() throws Exception {
        GirlActor marisaActor = mock(GirlActor.class);
        Girl marisa = GirlFactory.createMarisa();
        Field girlField = GirlActor.class.getDeclaredField("girl");
        girlField.setAccessible(true);
        girlField.set(marisaActor, marisa);

        when(marisaActor.isActive()).thenReturn(true);
        when(marisaActor.createSpellCardActor()).thenCallRealMethod();

        Optional<SpellCardActor> spellCardActor = marisaActor.createSpellCardActor();
        assertFalse("Active unsupported tower should return empty Optional<SpellCardActor>", spellCardActor.isPresent());
    }

    @Test
    public void testMapPlaceSpellCardWithEmptyOptional() {
        Stage stage = mock(Stage.class);
        GirlActor selectedGirl = mock(GirlActor.class);

        when(selectedGirl.createSpellCardActor()).thenReturn(Optional.empty());

        // Simulate Map.placeSpellCard logic
        selectedGirl.createSpellCardActor().ifPresent(stage::addActor);

        verify(stage, never()).addActor(any());
    }

    @Test
    public void testMapPlaceSpellCardWithPresentOptional() {
        Stage stage = mock(Stage.class);
        GirlActor selectedGirl = mock(GirlActor.class);
        SpellCardActor spellCardActor = mock(SpellCardActor.class);

        when(selectedGirl.createSpellCardActor()).thenReturn(Optional.of(spellCardActor));

        // Simulate Map.placeSpellCard logic
        selectedGirl.createSpellCardActor().ifPresent(stage::addActor);

        verify(stage, times(1)).addActor(spellCardActor);
    }
}
