package com.hongbao.bloons.actors;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class RenderableTextButton extends RenderableActor {

    public RenderableTextButton(TextButton button, int zIndex) {
        setActor(button);
        setZIndex(zIndex);
    }

    public TextButton getActor() {
        return (TextButton) super.getActor();
    }
}
