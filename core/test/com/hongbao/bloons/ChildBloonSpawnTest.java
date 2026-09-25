package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChildBloonSpawnTest {

    private static BloonsTouhouDefense dummyApp;

    @BeforeClass
    public static void setUpGdx() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        dummyApp = mock(BloonsTouhouDefense.class);
        Player dummyPlayer = new Player();
        when(dummyApp.getPlayer()).thenReturn(dummyPlayer);

        new HeadlessApplication(dummyApp, config);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
        Gdx.audio = mock(Audio.class);
        when(Gdx.audio.newSound(any())).thenReturn(mock(Sound.class));
    }

    @Test
    public void testChildBloonSpawnOffsetAndDistanceTravelledHorizontal() {
        Stage mockStage = mock(Stage.class);
        Map mockMap = mock(Map.class);

        // Direction vector pointing right (1.0, 0.0)
        when(mockMap.getDirection(anyFloat(), anyFloat())).thenReturn(new Pair<>(1f, 0f));

        BloonManager bloonManager = new BloonManager(mockStage, mockMap);

        // Rainbow bloon (health 8) pops into 2 Zebra bloons (health 7)
        Bloon rainbowBloon = BloonFactory.createBloonOfType("rainbow", 8);
        rainbowBloon.setDistanceTravelled(500);

        BloonActor rainbowActor = new BloonActor(rainbowBloon, 300f, 400f, null);

        // Pop rainbow bloon with 1 damage
        bloonManager.popBloon(rainbowActor, 1);

        // Capture added child bloon actors
        ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
        verify(mockStage, times(2)).addActor(captor.capture());

        List<Actor> addedActors = captor.getAllValues();
        assertEquals(2, addedActors.size());

        BloonActor leadChild = (BloonActor) addedActors.get(0);
        BloonActor trailingChild = (BloonActor) addedActors.get(1);

        // Lead child should be at the parent's position (300, 400)
        assertEquals(300f, leadChild.getCenterX(), 0.01f);
        assertEquals(400f, leadChild.getCenterY(), 0.01f);
        assertEquals(500, leadChild.getBloon().getDistanceTravelled());

        // Trailing child should be offset by 20 pixels behind along the reverse direction (-1 * 20)
        // (300 - 20 = 280)
        assertEquals(280f, trailingChild.getCenterX(), 0.01f);
        assertEquals(400f, trailingChild.getCenterY(), 0.01f);
        assertEquals(500 - BloonManager.CHILD_BLOON_OFFSET, trailingChild.getBloon().getDistanceTravelled());

        // Generate diagram screenshot artifact
        Pixmap pixmap = new Pixmap(400, 200, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIGHT_GRAY);
        pixmap.fill();

        // Track path line
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.drawLine(50, 100, 350, 100);

        // Lead bloon
        pixmap.setColor(Color.RED);
        pixmap.fillCircle((int) leadChild.getCenterX() - 100, 100, 15);

        // Trailing bloon
        pixmap.setColor(Color.BLACK);
        pixmap.fillCircle((int) trailingChild.getCenterX() - 100, 100, 15);

        PixmapIO.writePNG(Gdx.files.absolute("/tmp/child_bloons_spawn.png"), pixmap);
        pixmap.dispose();
        assertTrue(new File("/tmp/child_bloons_spawn.png").exists());
    }

    @Test
    public void testChildBloonSpawnOffsetAndDistanceTravelledVertical() {
        Stage mockStage = mock(Stage.class);
        Map mockMap = mock(Map.class);

        // Direction vector pointing up (0.0, 1.0)
        when(mockMap.getDirection(anyFloat(), anyFloat())).thenReturn(new Pair<>(0f, 1f));

        BloonManager bloonManager = new BloonManager(mockStage, mockMap);

        Bloon rainbowBloon = BloonFactory.createBloonOfType("rainbow", 8);
        rainbowBloon.setDistanceTravelled(300);

        BloonActor rainbowActor = new BloonActor(rainbowBloon, 200f, 200f, null);

        bloonManager.popBloon(rainbowActor, 1);

        ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
        verify(mockStage, times(2)).addActor(captor.capture());

        List<Actor> addedActors = captor.getAllValues();
        assertEquals(2, addedActors.size());

        BloonActor leadChild = (BloonActor) addedActors.get(0);
        BloonActor trailingChild = (BloonActor) addedActors.get(1);

        // Lead child at (200, 200)
        assertEquals(200f, leadChild.getCenterX(), 0.01f);
        assertEquals(200f, leadChild.getCenterY(), 0.01f);
        assertEquals(300, leadChild.getBloon().getDistanceTravelled());

        // Trailing child offset by 20 pixels down (200 - 20 = 180)
        assertEquals(200f, trailingChild.getCenterX(), 0.01f);
        assertEquals(180f, trailingChild.getCenterY(), 0.01f);
        assertEquals(300 - BloonManager.CHILD_BLOON_OFFSET, trailingChild.getBloon().getDistanceTravelled());
    }

    @Test
    public void testTowerTargetingPrioritizesLeadChild() {
        Stage mockStage = mock(Stage.class);
        Map mockMap = mock(Map.class);

        when(mockMap.getDirection(anyFloat(), anyFloat())).thenReturn(new Pair<>(1f, 0f));

        BloonManager bloonManager = new BloonManager(mockStage, mockMap);

        Bloon rainbowBloon = BloonFactory.createBloonOfType("rainbow", 8);
        rainbowBloon.setDistanceTravelled(400);

        BloonActor rainbowActor = new BloonActor(rainbowBloon, 300f, 400f, null);
        bloonManager.popBloon(rainbowActor, 1);

        Girl mockGirl = mock(Girl.class);
        when(mockGirl.getVisualRange()).thenReturn(500f);

        GirlActor mockGirlActor = mock(GirlActor.class);
        when(mockGirlActor.getGirl()).thenReturn(mockGirl);
        when(mockGirlActor.getCenterX()).thenReturn(300f);
        when(mockGirlActor.getCenterY()).thenReturn(300f);

        BulletActor mockBulletActor = mock(BulletActor.class);
        ArgumentCaptor<BloonActor> targetCaptor = ArgumentCaptor.forClass(BloonActor.class);
        when(mockGirlActor.createBulletActor(targetCaptor.capture())).thenReturn(mockBulletActor);

        boolean attacked = bloonManager.attackBloonIfInRange(mockGirlActor);

        assertTrue(attacked);
        BloonActor targetedBloon = targetCaptor.getValue();
        // Should target lead child with distanceTravelled = 400 (trailing child has 380)
        assertEquals(400, targetedBloon.getBloon().getDistanceTravelled());
        assertEquals(300f, targetedBloon.getCenterX(), 0.01f);
    }
}
