package com.hongbao.bloons.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.RenderableActor;
import com.hongbao.bloons.entities.StatusEffectType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class DefaultStatusEffectRenderer implements StatusEffectRenderer, Disposable {

	// Pre-allocated variables to avoid garbage collection during render loops
	private final Color originalColor = new Color();
	private final Color layerColor = new Color();
	private final Map<StatusEffectType, TextureRegion> overlayTextures = new EnumMap<>(StatusEffectType.class);
	private final Map<StatusEffectType, Texture> generatedTextures = new EnumMap<>(StatusEffectType.class);

	public DefaultStatusEffectRenderer() {
		initializeOverlayTextures();
	}

	private void initializeOverlayTextures() {
		// Only generate fallback procedurally created textures if GDX OpenGL environment is active
		if (Gdx.gl == null) {
			return;
		}

		try {
			// Freeze overlay texture (semi-transparent ice grid/frost pattern)
			Texture freezeTex = createProceduralOverlayTexture(StatusEffectType.FREEZE, new Color(0.3f, 0.7f, 1.0f, 0.6f));
			if (freezeTex != null) {
				generatedTextures.put(StatusEffectType.FREEZE, freezeTex);
				overlayTextures.put(StatusEffectType.FREEZE, new TextureRegion(freezeTex));
			}

			// Glue overlay texture (semi-transparent yellow blob pattern)
			Texture glueTex = createProceduralOverlayTexture(StatusEffectType.GLUE, new Color(1.0f, 0.9f, 0.2f, 0.6f));
			if (glueTex != null) {
				generatedTextures.put(StatusEffectType.GLUE, glueTex);
				overlayTextures.put(StatusEffectType.GLUE, new TextureRegion(glueTex));
			}

			// Burn overlay texture (semi-transparent orange/red flame aura pattern)
			Texture burnTex = createProceduralOverlayTexture(StatusEffectType.BURN, new Color(1.0f, 0.3f, 0.1f, 0.7f));
			if (burnTex != null) {
				generatedTextures.put(StatusEffectType.BURN, burnTex);
				overlayTextures.put(StatusEffectType.BURN, new TextureRegion(burnTex));
			}

			// Slow overlay texture (semi-transparent purple aura)
			Texture slowTex = createProceduralOverlayTexture(StatusEffectType.SLOW, new Color(0.7f, 0.3f, 0.9f, 0.5f));
			if (slowTex != null) {
				generatedTextures.put(StatusEffectType.SLOW, slowTex);
				overlayTextures.put(StatusEffectType.SLOW, new TextureRegion(slowTex));
			}

			// Stun overlay texture (semi-transparent golden star/ring pattern)
			Texture stunTex = createProceduralOverlayTexture(StatusEffectType.STUN, new Color(1.0f, 0.85f, 0.2f, 0.6f));
			if (stunTex != null) {
				generatedTextures.put(StatusEffectType.STUN, stunTex);
				overlayTextures.put(StatusEffectType.STUN, new TextureRegion(stunTex));
			}
		} catch (Exception ignored) {
			// Fallback gracefully on lower-end rendering hardware or missing GL capabilities
		}
	}

	private Texture createProceduralOverlayTexture(StatusEffectType type, Color primaryColor) {
		int width = 32;
		int height = 32;
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

		pixmap.setColor(0, 0, 0, 0);
		pixmap.fill();

		pixmap.setColor(primaryColor);
		switch (type) {
			case FREEZE:
				// Frost crosshatch border
				pixmap.drawRectangle(0, 0, width, height);
				pixmap.drawRectangle(2, 2, width - 4, height - 4);
				pixmap.drawLine(0, 0, width, height);
				pixmap.drawLine(width, 0, 0, height);
				break;
			case GLUE:
				// Dripping droplets / blob center
				pixmap.fillCircle(width / 2, height / 2, width / 3);
				pixmap.fillCircle(width / 4, height * 3 / 4, width / 6);
				pixmap.fillCircle(width * 3 / 4, height * 3 / 4, width / 6);
				break;
			case BURN:
				// Flame aura circle and diagonal embers
				pixmap.fillCircle(width / 2, height / 2, width / 2 - 2);
				pixmap.setColor(1.0f, 0.8f, 0.2f, 0.8f);
				pixmap.fillCircle(width / 2, height / 2, width / 4);
				break;
			case SLOW:
				// Concentric spiral/rings
				pixmap.drawCircle(width / 2, height / 2, width / 2 - 1);
				pixmap.drawCircle(width / 2, height / 2, width / 3);
				break;
			case STUN:
				// Star motif / top crown
				pixmap.drawRectangle(4, 4, width - 8, height - 8);
				pixmap.fillCircle(width / 2, height / 2, width / 5);
				break;
		}

		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		return texture;
	}

	public void setOverlayTexture(StatusEffectType type, TextureRegion textureRegion) {
		if (type != null) {
			if (textureRegion != null) {
				overlayTextures.put(type, textureRegion);
			} else {
				overlayTextures.remove(type);
			}
		}
	}

	@Override
	public void render(Batch batch, Set<StatusEffectType> statusEffects, float x, float y, float width, float height) {
		if (batch == null || statusEffects == null || statusEffects.isEmpty()) {
			return;
		}

		// Save original batch state (colors and shaders)
		originalColor.set(batch.getColor());
		ShaderProgram originalShader = batch.getShader();

		try {
			// Calculate composite tint color based on active conditions
			layerColor.set(originalColor);
			if (statusEffects.contains(StatusEffectType.FREEZE)) {
				layerColor.mul(0.6f, 0.8f, 1.0f, 1.0f);
			}
			if (statusEffects.contains(StatusEffectType.BURN)) {
				layerColor.mul(1.0f, 0.6f, 0.4f, 1.0f);
			}
			if (statusEffects.contains(StatusEffectType.GLUE)) {
				layerColor.mul(0.9f, 0.9f, 0.4f, 1.0f);
			}
			if (statusEffects.contains(StatusEffectType.SLOW)) {
				layerColor.mul(0.8f, 0.7f, 0.9f, 1.0f);
			}
			if (statusEffects.contains(StatusEffectType.STUN)) {
				layerColor.mul(1.0f, 1.0f, 0.6f, 1.0f);
			}

			// Render composite visual layers in defined pipeline order to avoid z-fighting
			// Pass 1: Freeze overlay
			if (statusEffects.contains(StatusEffectType.FREEZE)) {
				TextureRegion freezeRegion = overlayTextures.get(StatusEffectType.FREEZE);
				if (freezeRegion != null) {
					batch.setColor(layerColor.r, layerColor.g, layerColor.b, layerColor.a * 0.7f);
					batch.draw(freezeRegion, x, y, width, height);
				}
			}

			// Pass 2: Glue overlay
			if (statusEffects.contains(StatusEffectType.GLUE)) {
				TextureRegion glueRegion = overlayTextures.get(StatusEffectType.GLUE);
				if (glueRegion != null) {
					batch.setColor(layerColor.r, layerColor.g, layerColor.b, layerColor.a * 0.7f);
					batch.draw(glueRegion, x, y, width, height);
				}
			}

			// Pass 3: Burn aura overlay
			if (statusEffects.contains(StatusEffectType.BURN)) {
				TextureRegion burnRegion = overlayTextures.get(StatusEffectType.BURN);
				if (burnRegion != null) {
					batch.setColor(layerColor.r, layerColor.g, layerColor.b, layerColor.a * 0.8f);
					batch.draw(burnRegion, x, y, width, height);
				}
			}

			// Pass 4: Slow overlay
			if (statusEffects.contains(StatusEffectType.SLOW)) {
				TextureRegion slowRegion = overlayTextures.get(StatusEffectType.SLOW);
				if (slowRegion != null) {
					batch.setColor(layerColor.r, layerColor.g, layerColor.b, layerColor.a * 0.5f);
					batch.draw(slowRegion, x, y, width, height);
				}
			}

			// Pass 5: Stun overlay
			if (statusEffects.contains(StatusEffectType.STUN)) {
				TextureRegion stunRegion = overlayTextures.get(StatusEffectType.STUN);
				if (stunRegion != null) {
					batch.setColor(layerColor.r, layerColor.g, layerColor.b, layerColor.a * 0.6f);
					batch.draw(stunRegion, x, y, width, height);
				}
			}
		} catch (Exception e) {
			// Fallback gracefully if any shader or rendering pipeline pass fails
		} finally {
			// ALWAYS restore batch color and shader state cleanly
			batch.setColor(originalColor);
			if (batch.getShader() != originalShader) {
				batch.setShader(originalShader);
			}
		}
	}

	@Override
	public void render(Batch batch, RenderableActor actor, float x, float y, float width, float height) {
		if (actor == null) {
			return;
		}

		if (actor instanceof BloonActor) {
			BloonActor bloonActor = (BloonActor) actor;
			if (bloonActor.getBloon() != null) {
				render(batch, bloonActor.getBloon().getStatusEffects(), x, y, width, height);
				return;
			}
		}

		Set<StatusEffectType> actorEffects = actor.getStatusEffects();
		if (actorEffects != null) {
			render(batch, actorEffects, x, y, width, height);
		}
	}

	@Override
	public void dispose() {
		for (Texture texture : generatedTextures.values()) {
			if (texture != null) {
				texture.dispose();
			}
		}
		generatedTextures.clear();
		overlayTextures.clear();
	}
}
