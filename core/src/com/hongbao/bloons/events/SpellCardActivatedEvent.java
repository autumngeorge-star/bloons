package com.hongbao.bloons.events;

import com.hongbao.bloons.actors.SpellCardActor;

public class SpellCardActivatedEvent {
    private final SpellCardActor spellCardActor;

    public SpellCardActivatedEvent(SpellCardActor spellCardActor) {
        this.spellCardActor = spellCardActor;
    }

    public SpellCardActor getSpellCardActor() {
        return spellCardActor;
    }
}
