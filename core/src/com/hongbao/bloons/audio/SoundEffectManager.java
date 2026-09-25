package com.hongbao.bloons.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameEventBus;
import com.hongbao.bloons.events.GameEventListener;

/**
 * Manages sound effects played in response to domain game events.
 */
public class SoundEffectManager implements GameEventListener<BloonPoppedEvent> {

    private Sound popSound;
    private GameEventBus currentEventBus;

    public SoundEffectManager() {
        if (Gdx.audio != null && Gdx.files != null) {
            popSound = Gdx.audio.newSound(Gdx.files.internal("music/pop.mp3"));
        }
    }

    public void subscribe(GameEventBus eventBus) {
        if (eventBus == null) {
            return;
        }
        if (currentEventBus != null && currentEventBus != eventBus) {
            unsubscribe(currentEventBus);
        }
        this.currentEventBus = eventBus;
        eventBus.subscribe(BloonPoppedEvent.class, this);
    }

    public void unsubscribe(GameEventBus eventBus) {
        if (eventBus == null) {
            return;
        }
        eventBus.unsubscribe(BloonPoppedEvent.class, this);
        if (this.currentEventBus == eventBus) {
            this.currentEventBus = null;
        }
    }

    @Override
    public void onEvent(BloonPoppedEvent event) {
        if (popSound != null) {
            popSound.play(0.5f);
        }
    }

    public void dispose() {
        if (currentEventBus != null) {
            unsubscribe(currentEventBus);
        }
        if (popSound != null) {
            popSound.dispose();
            popSound = null;
        }
    }
}
