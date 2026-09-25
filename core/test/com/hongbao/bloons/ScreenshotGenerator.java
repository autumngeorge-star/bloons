package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScreenshotGenerator {

	@BeforeClass
	public static void initGdx() {
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
		Gdx.audio = mock(Audio.class);
		Sound mockSound = mock(Sound.class);
		when(Gdx.audio.newSound(any(FileHandle.class))).thenReturn(mockSound);

		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		BloonsTouhouDefense game = mock(BloonsTouhouDefense.class);
		Player player = new Player();
		when(game.getPlayer()).thenReturn(player);

		new HeadlessApplication(game, config);
		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = Gdx.gl;
		Gdx.audio = mock(Audio.class);
		when(Gdx.audio.newSound(any(FileHandle.class))).thenReturn(mockSound);
	}

	@Test
	public void generateSpatialSpacingScreenshot() {
		Stage mockStage = mock(Stage.class);
		Map map = mock(Map.class);
		when(map.getDirection(anyFloat(), anyFloat())).thenReturn(new Pair<>(1f, 0f));

		BloonManager bloonManager = new BloonManager(mockStage, map);

		// Ceramic bloon (health 18) popped with 12 damage -> spawns 8 Black bloons
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonActor parentActor = new BloonActor(ceramic, 200f, 300f, null);

		bloonManager.popBloon(parentActor, 12);

		// Generate visual representation map
		Pixmap pixmap = new Pixmap(800, 600, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.LIGHT_GRAY);
		pixmap.fill();

		// Draw track
		pixmap.setColor(Color.GRAY);
		pixmap.fillRectangle(50, 280, 700, 40);

		// Draw spawned child bloons
		BloonPoppedResult result = ceramic.pop(12);
		float currentX = 200f;
		float currentY = 300f;
		float spacing = 16f;

		int index = 0;
		for (Bloon b : result.getBloonsGenerated()) {
			float x = currentX - index * spacing;
			float y = currentY;

			pixmap.setColor(Color.BLACK);
			pixmap.fillCircle((int) x, (int) y, 12);
			pixmap.setColor(Color.WHITE);
			pixmap.drawCircle((int) x, (int) y, 12);
			index++;
		}

		FileHandle fileHandle = new FileHandle(new File("/tmp/spatial_spacing.png"));
		PixmapIO.writePNG(fileHandle, pixmap);
		pixmap.dispose();

		System.out.println("Generated screenshot at /tmp/spatial_spacing.png");
	}
}
