package com.hongbao.bloons.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;


public abstract class RenderableActor extends Actor {
	
	// This class exists because libgdx doesn't have a decent way to manage z-indexes. Any actor that is drawn
	// in the game (so really, all of them) needs to extend this class so that z indexes can be done properly.

	private int zIndex;
	private Actor actor; // This is kind of stupid, but I want this thing to be able to accommodate many superclasses of actors. If I think of a better way to do things I'll change it.
	public TextureRegion textureRegion;
	
	public int getZIndex() {
		return zIndex;
	}
	
	public boolean setZIndex(int zIndex) {
		this.zIndex = zIndex;
		return true;
	}
	
	public Actor getActor() {
		return actor;
	}
	
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
	
	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
	public float getCenterX() {
		return getX() + (textureRegion != null ? textureRegion.getRegionWidth() / 2f : getWidth() / 2f);
	}
	
	public float getCenterY() {
		return getY() + (textureRegion != null ? textureRegion.getRegionHeight() / 2f : getHeight() / 2f);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (actor == null) {
			super.draw(batch, parentAlpha);
		} else {
			actor.draw(batch, parentAlpha);
		}
	}
	
	@Override
	public void act(float delta) {
		if (actor == null) {
			super.act(delta);
		} else {
			actor.act(delta);
		}
	}
	
	@Override
	public boolean fire(Event event) {
		if (actor == null) {
			return super.fire(event);
		} else {
			return actor.fire(event);
		}
	}
	
	@Override
	public void addAction(Action action) {
		if (actor == null) {
			super.addAction(action);
		} else {
			actor.addAction(action);
		}
	}
	
	@Override
	public void removeAction(Action action) {
		if (actor == null) {
			super.removeAction(action);
		} else {
			actor.removeAction(action);
		}
	}
	
	@Override
	public Array<Action> getActions() {
		if (actor == null) {
			return super.getActions();
		} else {
			return actor.getActions();
		}
	}
	
	@Override
	public boolean hasActions() {
		if (actor == null) {
			return super.hasActions();
		} else {
			return actor.hasActions();
		}
	}
	
	@Override
	public void clearActions() {
		if (actor == null) {
			super.clearActions();
		} else {
			actor.clearActions();
		}
	}
	
	@Override
	public void clearListeners() {
		if (actor == null) {
			super.clearListeners();
		} else {
			actor.clearListeners();
		}
	}
	
	@Override
	public void clear() {
		if (actor == null) {
			super.clear();
		} else {
			actor.clear();
		}
	}
	
	@Override
	public Stage getStage() {
		if (actor == null) {
			return super.getStage();
		} else {
			return actor.getStage();
		}
	}
	
	@Override
	public boolean isDescendantOf(Actor actor) {
		if (this.actor == null) {
			return super.isDescendantOf(actor);
		} else {
			return this.actor.isDescendantOf(actor);
		}
	}
	
	@Override
	public boolean isAscendantOf(Actor actor) {
		if (this.actor == null) {
			return super.isAscendantOf(actor);
		} else {
			return this.actor.isAscendantOf(actor);
		}
	}
	
	@Override
	public <T extends Actor> T firstAscendant(Class<T> type) {
		if (actor == null) {
			return super.firstAscendant(type);
		} else {
			return actor.firstAscendant(type);
		}
	}
	
	@Override
	public boolean hasParent() {
		if (actor == null) {
			return super.hasParent();
		} else {
			return actor.hasParent();
		}
	}
	
	@Override
	public Group getParent() {
		if (actor == null) {
			return super.getParent();
		} else {
			return actor.getParent();
		}
	}
	
	@Override
	public boolean isTouchable() {
		if (actor == null) {
			return super.isTouchable();
		} else {
			return actor.isTouchable();
		}
	}
	
	@Override
	public Touchable getTouchable() {
		if (actor == null) {
			return super.getTouchable();
		} else {
			return actor.getTouchable();
		}
	}
	
	@Override
	public void setTouchable(Touchable touchable) {
		if (actor == null) {
			super.setTouchable(touchable);
		} else {
			actor.setTouchable(touchable);
		}
	}
	
	@Override
	public boolean isVisible() {
		if (actor == null) {
			return super.isVisible();
		} else {
			return actor.isVisible();
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (actor == null) {
			super.setVisible(visible);
		} else {
			actor.setVisible(visible);
		}
	}
	
	@Override
	public boolean ancestorsVisible() {
		if (actor == null) {
			return super.ancestorsVisible();
		} else {
			return actor.ancestorsVisible();
		}
	}
	
	@Override
	public boolean hasKeyboardFocus() {
		if (actor == null) {
			return super.hasKeyboardFocus();
		} else {
			return actor.hasKeyboardFocus();
		}
	}
	
	@Override
	public boolean hasScrollFocus() {
		if (actor == null) {
			return super.hasScrollFocus();
		} else {
			return actor.hasScrollFocus();
		}
	}
	
	@Override
	public boolean isTouchFocusTarget() {
		if (actor == null) {
			return super.isTouchFocusTarget();
		} else {
			return actor.isTouchFocusTarget();
		}
	}
	
	@Override
	public boolean isTouchFocusListener() {
		if (actor == null) {
			return super.isTouchFocusListener();
		} else {
			return actor.isTouchFocusListener();
		}
	}
	
	@Override
	public Object getUserObject() {
		if (actor == null) {
			return super.getUserObject();
		} else {
			return actor.getUserObject();
		}
	}
	
	@Override
	public void setUserObject(Object userObject) {
		if (actor == null) {
			super.setUserObject(userObject);
		} else {
			actor.setUserObject(userObject);
		}
	}
	
	@Override
	public float getX() {
		if (actor == null) {
			return super.getX();
		} else {
			return actor.getX();
		}
	}
	
	@Override
	public float getX(int alignment) {
		if (actor == null) {
			return super.getX(alignment);
		} else {
			return actor.getX(alignment);
		}
	}
	
	@Override
	public void setX(float x) {
		if (actor == null) {
			super.setX(x);
		} else {
			actor.setX(x);
		}
	}
	
	@Override
	public void setX(float x, int alignment) {
		if (actor == null) {
			super.setX(x, alignment);
		} else {
			actor.setX(x, alignment);
		}
	}
	
	@Override
	public float getY() {
		if (actor == null) {
			return super.getY();
		} else {
			return actor.getY();
		}
	}
	
	@Override
	public void setY(float y) {
		if (actor == null) {
			super.setY(y);
		} else {
			actor.setY(y);
		}
	}
	
	@Override
	public void setY(float y, int alignment) {
		if (actor == null) {
			super.setY(y, alignment);
		} else {
			actor.setY(y, alignment);
		}
	}
	
	@Override
	public float getY(int alignment) {
		if (actor == null) {
			return super.getY(alignment);
		} else {
			return actor.getY(alignment);
		}
	}
	
	@Override
	public void setPosition(float x, float y) {
		if (actor == null) {
			super.setPosition(x, y);
		} else {
			actor.setPosition(x, y);
		}
	}
	
	@Override
	public void setPosition(float x, float y, int alignment) {
		if (actor == null) {
			super.setPosition(x, y, alignment);
		} else {
			actor.setPosition(x, y, alignment);
		}
	}
	
	@Override
	public void moveBy(float x, float y) {
		if (actor == null) {
			super.moveBy(x, y);
		} else {
			actor.moveBy(x, y);
		}
	}
	
	@Override
	public float getWidth() {
		if (actor == null) {
			return super.getWidth();
		} else {
			return actor.getWidth();
		}
	}
	
	@Override
	public void setWidth(float width) {
		if (actor == null) {
			super.setWidth(width);
		} else {
			actor.setWidth(width);
		}
	}
	
	@Override
	public float getHeight() {
		if (actor == null) {
			return super.getHeight();
		} else {
			return actor.getHeight();
		}
	}
	
	@Override
	public void setHeight(float height) {
		if (actor == null) {
			super.setHeight(height);
		} else {
			actor.setHeight(height);
		}
	}
	
	@Override
	public float getTop() {
		if (actor == null) {
			return super.getTop();
		} else {
			return actor.getTop();
		}
	}
	
	@Override
	public float getRight() {
		if (actor == null) {
			return super.getRight();
		} else {
			return actor.getRight();
		}
	}
	
	@Override
	public void setSize(float width, float height) {
		if (actor == null) {
			super.setSize(width, height);
		} else {
			actor.setSize(width, height);
		}
	}
	
	@Override
	public void sizeBy(float size) {
		if (actor == null) {
			super.sizeBy(size);
		} else {
			actor.sizeBy(size);
		}
	}
	
	@Override
	public void sizeBy(float width, float height) {
		if (actor == null) {
			super.sizeBy(width, height);
		} else {
			actor.sizeBy(width, height);
		}
	}
	
	@Override
	public void setBounds(float x, float y, float width, float height) {
		if (actor == null) {
			super.setBounds(x, y, width, height);
		} else {
			actor.setBounds(x, y, width, height);
		}
	}
	
	@Override
	public float getOriginX() {
		if (actor == null) {
			return super.getOriginX();
		} else {
			return actor.getOriginX();
		}
	}
	
	@Override
	public void setOriginX(float originX) {
		if (actor == null) {
			super.setOriginX(originX);
		} else {
			actor.setOriginX(originX);
		}
	}
	
	@Override
	public float getOriginY() {
		if (actor == null) {
			return super.getOriginY();
		} else {
			return actor.getOriginY();
		}
	}
	
	@Override
	public void setOriginY(float originY) {
		if (actor == null) {
			super.setOriginY(originY);
		} else {
			actor.setOriginY(originY);
		}
	}
	
	@Override
	public void setOrigin(float originX, float originY) {
		if (actor == null) {
			super.setOrigin(originX, originY);
		} else {
			actor.setOrigin(originX, originY);
		}
	}
	
	@Override
	public void setOrigin(int alignment) {
		if (actor == null) {
			super.setOrigin(alignment);
		} else {
			actor.setOrigin(alignment);
		}
	}
	
	@Override
	public float getScaleX() {
		if (actor == null) {
			return super.getScaleX();
		} else {
			return actor.getScaleX();
		}
	}
	
	@Override
	public void setScaleX(float scaleX) {
		if (actor == null) {
			super.setScaleX(scaleX);
		} else {
			actor.setScaleX(scaleX);
		}
	}
	
	@Override
	public float getScaleY() {
		if (actor == null) {
			return super.getScaleY();
		} else {
			return actor.getScaleY();
		}
	}
	
	@Override
	public void setScaleY(float scaleY) {
		if (actor == null) {
			super.setScaleY(scaleY);
		} else {
			actor.setScaleY(scaleY);
		}
	}
	
	@Override
	public void setScale(float scaleXY) {
		if (actor == null) {
			super.setScale(scaleXY);
		} else {
			actor.setScale(scaleXY);
		}
	}
	
	@Override
	public void setScale(float scaleX, float scaleY) {
		if (actor == null) {
			super.setScale(scaleX, scaleY);
		} else {
			actor.setScale(scaleX, scaleY);
		}
	}
	
	@Override
	public void scaleBy(float scale) {
		if (actor == null) {
			super.scaleBy(scale);
		} else {
			actor.scaleBy(scale);
		}
	}
	
	@Override
	public void scaleBy(float scaleX, float scaleY) {
		if (actor == null) {
			super.scaleBy(scaleX, scaleY);
		} else {
			actor.scaleBy(scaleX, scaleY);
		}
	}
	
	@Override
	public float getRotation() {
		if (actor == null) {
			return super.getRotation();
		} else {
			return actor.getRotation();
		}
	}
	
	@Override
	public void setRotation(float degrees) {
		if (actor == null) {
			super.setRotation(degrees);
		} else {
			actor.setRotation(degrees);
		}
	}
	
	@Override
	public void rotateBy(float amountInDegrees) {
		if (actor == null) {
			super.rotateBy(amountInDegrees);
		} else {
			actor.rotateBy(amountInDegrees);
		}
	}
	
	@Override
	public void setColor(Color color) {
		if (actor == null) {
			super.setColor(color);
		} else {
			actor.setColor(color);
		}
	}
	
	@Override
	public void setColor(float r, float g, float b, float a) {
		if (actor == null) {
			super.setColor(r, g, b, a);
		} else {
			actor.setColor(r, g, b, a);
		}
	}
	
	@Override
	public Color getColor() {
		if (actor == null) {
			return super.getColor();
		} else {
			return actor.getColor();
		}
	}
	
	@Override
	public String getName() {
		if (actor == null) {
			return super.getName();
		} else {
			return actor.getName();
		}
	}
	
	@Override
	public void setName(String name) {
		if (actor == null) {
			super.setName(name);
		} else {
			actor.setName(name);
		}
	}
	
	@Override
	public void toFront() {
		if (actor == null) {
			super.toFront();
		} else {
			actor.toFront();
		}
	}
	
	@Override
	public void toBack() {
		if (actor == null) {
			super.toBack();
		} else {
			actor.toBack();
		}
	}
	
	@Override
	public boolean clipBegin() {
		if (actor == null) {
			return super.clipBegin();
		} else {
			return actor.clipBegin();
		}
	}
	
	@Override
	public boolean clipBegin(float x, float y, float width, float height) {
		if (actor == null) {
			return super.clipBegin(x, y, width, height);
		} else {
			return actor.clipBegin(x, y, width, height);
		}
	}
	
	@Override
	public void clipEnd() {
		if (actor == null) {
			super.clipEnd();
		} else {
			actor.clipEnd();
		}
	}
	
	@Override
	public Vector2 screenToLocalCoordinates(Vector2 screenCoords) {
		if (actor == null) {
			return super.screenToLocalCoordinates(screenCoords);
		} else {
			return actor.screenToLocalCoordinates(screenCoords);
		}
	}
	
	@Override
	public Vector2 stageToLocalCoordinates(Vector2 stageCoords) {
		if (actor == null) {
			return super.stageToLocalCoordinates(stageCoords);
		} else {
			return actor.stageToLocalCoordinates(stageCoords);
		}
	}
	
	@Override
	public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
		if (actor == null) {
			return super.parentToLocalCoordinates(parentCoords);
		} else {
			return actor.parentToLocalCoordinates(parentCoords);
		}
	}
	
	@Override
	public Vector2 localToScreenCoordinates(Vector2 localCoords) {
		if (actor == null) {
			return super.localToScreenCoordinates(localCoords);
		} else {
			return actor.localToScreenCoordinates(localCoords);
		}
	}
	
	@Override
	public Vector2 localToStageCoordinates(Vector2 localCoords) {
		if (actor == null) {
			return super.localToStageCoordinates(localCoords);
		} else {
			return actor.localToStageCoordinates(localCoords);
		}
	}
	
	@Override
	public Vector2 localToParentCoordinates(Vector2 localCoords) {
		if (actor == null) {
			return super.localToParentCoordinates(localCoords);
		} else {
			return actor.localToParentCoordinates(localCoords);
		}
	}
	
	@Override
	public Vector2 localToAscendantCoordinates(Actor ascendant, Vector2 localCoords) {
		if (actor == null) {
			return super.localToAscendantCoordinates(ascendant, localCoords);
		} else {
			return actor.localToAscendantCoordinates(ascendant, localCoords);
		}
	}
	
	@Override
	public Vector2 localToActorCoordinates(Actor actor, Vector2 localCoords) {
		if (actor == null) {
			return super.localToActorCoordinates(actor, localCoords);
		} else {
			return actor.localToActorCoordinates(actor, localCoords);
		}
	}
	
	@Override
	public void drawDebug(ShapeRenderer shapes) {
		if (actor == null) {
			super.drawDebug(shapes);
		} else {
			actor.drawDebug(shapes);
		}
	}

	@Override
	public void setDebug(boolean enabled) {
		if (actor == null) {
			super.setDebug(enabled);
		} else {
			actor.setDebug(enabled);
		}
	}
	
	@Override
	public boolean getDebug() {
		if (actor == null) {
			return super.getDebug();
		} else {
			return actor.getDebug();
		}
	}
	
	@Override
	public Actor debug() {
		if (actor == null) {
			return super.debug();
		} else {
			return actor.debug();
		}
	}
	
	@Override
	public boolean notify(Event event, boolean capture) {
		if (actor == null) {
			return super.notify(event, capture);
		} else {
			return actor.notify(event, capture);
		}
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (actor == null) {
			return super.hit(x, y, touchable);
		} else {
			return actor.hit(x, y, touchable);
		}
	}

	@Override
	public boolean remove() {
		return super.remove();
	}
	
	@Override
	public boolean addListener(EventListener listener) {
		if (actor == null) {
			return super.addListener(listener);
		} else {
			return actor.addListener(listener);
		}
	}
	
	@Override
	public boolean removeListener(EventListener listener) {
		if (actor == null) {
			return super.removeListener(listener);
		} else {
			return actor.removeListener(listener);
		}
	}
	
	@Override
	public DelayedRemovalArray<EventListener> getListeners() {
		if (actor == null) {
			return super.getListeners();
		} else {
			return actor.getListeners();
		}
	}
	
	@Override
	public boolean addCaptureListener(EventListener listener) {
		if (actor == null) {
			return super.addCaptureListener(listener);
		} else {
			return actor.addCaptureListener(listener);
		}
	}
	
	@Override
	public boolean removeCaptureListener(EventListener listener) {
		if (actor == null) {
			return super.removeCaptureListener(listener);
		} else {
			return actor.removeCaptureListener(listener);
		}
	}
	
	@Override
	public DelayedRemovalArray<EventListener> getCaptureListeners() {
		if (actor == null) {
			return super.getCaptureListeners();
		} else {
			return actor.getCaptureListeners();
		}
	}
	
}
