package com.hongbao.bloons.rendering;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.entities.StatusEffectType;

import java.util.Set;

public interface StatusEffectRenderer {

	/**
	 * Renders status visual passes for a given set of status effect conditions over the target bounds.
	 *
	 * @param batch The batch to render to.
	 * @param statusEffects Active status effect conditions.
	 * @param x Target bounding box X coordinate.
	 * @param y Target bounding box Y coordinate.
	 * @param width Target bounding box width.
	 * @param height Target bounding box height.
	 */
	void render(Batch batch, Set<StatusEffectType> statusEffects, float x, float y, float width, float height);

	/**
	 * Renders status visual passes for a given RenderableActor over the target bounds.
	 *
	 * @param batch The batch to render to.
	 * @param actor Target actor containing status state.
	 * @param x Target bounding box X coordinate.
	 * @param y Target bounding box Y coordinate.
	 * @param width Target bounding box width.
	 * @param height Target bounding box height.
	 */
	void render(Batch batch, RenderableActor actor, float x, float y, float width, float height);
}
