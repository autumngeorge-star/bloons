package com.hongbao.bloons.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class RenderableTable extends RenderableActor {
    public RenderableTable(Table table, int zIndex) {
        setActor(table);
        setZIndex(zIndex);
    }
}
