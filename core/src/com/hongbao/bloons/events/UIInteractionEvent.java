package com.hongbao.bloons.events;

/**
 * Domain event emitted when user UI interactions occur.
 */
public class UIInteractionEvent {

    public enum InteractionType {
        TOGGLE_MUSIC,
        UPDATE_INSTRUCTIONS,
        PURCHASE_GIRL,
        PLACE_GIRL,
        USE_SPELL_CARD
    }

    private final InteractionType interactionType;
    private final String details;

    public UIInteractionEvent(InteractionType interactionType) {
        this(interactionType, null);
    }

    public UIInteractionEvent(InteractionType interactionType, String details) {
        this.interactionType = interactionType;
        this.details = details;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public String getDetails() {
        return details;
    }
}
