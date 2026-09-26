package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class BloonManagerTest {

    @BeforeClass
    public static void setUpGdx() {
        Gdx.files = Mockito.mock(Files.class);
        Gdx.audio = Mockito.mock(Audio.class);
        FileHandle fileHandle = Mockito.mock(FileHandle.class);
        Mockito.when(fileHandle.readString()).thenReturn("END\n");
        Mockito.when(Gdx.files.internal(Mockito.anyString())).thenReturn(fileHandle);
        Mockito.when(Gdx.audio.newSound(Mockito.any())).thenReturn(Mockito.mock(Sound.class));
    }

    private BloonActor createDummyBloonActor(float x, float y, int distanceTravelled, long id) {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        bloon.setDistanceTravelled(distanceTravelled);
        BloonActor actor = Mockito.mock(BloonActor.class);
        Mockito.when(actor.getCenterX()).thenReturn(x);
        Mockito.when(actor.getCenterY()).thenReturn(y);
        Mockito.when(actor.getBloon()).thenReturn(bloon);
        Mockito.when(actor.getBloonId()).thenReturn(id);
        return actor;
    }

    private BulletActor createDummyBulletActor(float x, float y, float dx, float dy) {
        BulletActor actor = Mockito.mock(BulletActor.class);
        Mockito.when(actor.getDx()).thenReturn(dx);
        Mockito.when(actor.getDy()).thenReturn(dy);
        Mockito.when(actor.getCenterX()).thenReturn(x);
        Mockito.when(actor.getCenterY()).thenReturn(y);
        Mockito.when(actor.hasDamagedBloon(Mockito.any())).thenReturn(false);
        return actor;
    }

    @Test
    public void testForwardSectorTrackProgressTargeting() {
        BloonManager manager = new BloonManager(null, null);

        // Bullet at (100, 100) facing right (dx=1, dy=0)
        BulletActor bulletActor = createDummyBulletActor(100f, 100f, 1.0f, 0.0f);

        // Bloon A: at (200, 100) -> 0 deg ahead, progress = 100
        BloonActor bloonA = createDummyBloonActor(200f, 100f, 100, 1L);
        // Bloon B: at (200, 200) -> 45 deg ahead (inside 60 deg cone), progress = 200
        BloonActor bloonB = createDummyBloonActor(200f, 200f, 200, 2L);
        // Bloon C: at (100, 200) -> 90 deg (outside 60 deg cone), progress = 300
        BloonActor bloonC = createDummyBloonActor(100f, 200f, 300, 3L);
        // Bloon D: at (0, 100) -> 180 deg behind, progress = 400
        BloonActor bloonD = createDummyBloonActor(0f, 100f, 400, 4L);

        manager.removeBloonFromStage(bloonA); // clear onstage bloons if any
        manager.addBloonToOnstageForTesting(bloonA);
        manager.addBloonToOnstageForTesting(bloonB);
        manager.addBloonToOnstageForTesting(bloonC);
        manager.addBloonToOnstageForTesting(bloonD);

        BloonActor target = manager.getNewHomingTarget(bulletActor);
        assertNotNull(target);
        // Between bloonA (0 deg, progress 100) and bloonB (45 deg, progress 200), bloonB has higher track progress
        assertEquals(bloonB, target);
    }

    @Test
    public void testFallbackTargetingWhenNoForwardTargets() {
        BloonManager manager = new BloonManager(null, null);

        // Bullet at (100, 100) facing right (dx=1, dy=0)
        BulletActor bulletActor = createDummyBulletActor(100f, 100f, 1.0f, 0.0f);

        // Bloon A: at (0, 100) -> behind (180 deg), distance = 100, progress = 100
        BloonActor bloonA = createDummyBloonActor(0f, 100f, 100, 1L);
        // Bloon B: at (-200, 100) -> behind (180 deg), distance = 300, progress = 500
        BloonActor bloonB = createDummyBloonActor(-200f, 100f, 500, 2L);

        manager.addBloonToOnstageForTesting(bloonA);
        manager.addBloonToOnstageForTesting(bloonB);

        BloonActor target = manager.getNewHomingTarget(bulletActor);
        assertNotNull(target);
        // No bloons in 120-degree forward cone; fallback selects closest bloon (bloonA at distance 100)
        assertEquals(bloonA, target);
    }

    @Test
    public void testDamagedBloonsIgnored() {
        BloonManager manager = new BloonManager(null, null);
        BulletActor bulletActor = createDummyBulletActor(100f, 100f, 1.0f, 0.0f);

        BloonActor bloonA = createDummyBloonActor(200f, 100f, 100, 1L); // ahead
        BloonActor bloonB = createDummyBloonActor(300f, 100f, 200, 2L); // ahead, higher progress

        // Mark bloonB as damaged
        Mockito.when(bulletActor.hasDamagedBloon(bloonB)).thenReturn(true);

        manager.addBloonToOnstageForTesting(bloonA);
        manager.addBloonToOnstageForTesting(bloonB);

        BloonActor target = manager.getNewHomingTarget(bulletActor);
        assertEquals(bloonA, target);
    }

    @Test
    public void testExact60DegreeConeBoundary() {
        BloonManager manager = new BloonManager(null, null);
        // Bullet facing right (dx=1, dy=0) at (0, 0)
        BulletActor bulletActor = createDummyBulletActor(0f, 0f, 1.0f, 0.0f);

        // Bloon exactly at 60 degrees: x = 100 * cos(60 deg) = 50, y = 100 * sin(60 deg) = 86.6025
        BloonActor bloon60 = createDummyBloonActor(50f, 86.6025f, 100, 1L);

        // Bloon at 61 degrees: x = 100 * cos(61 deg) = 48.48, y = 100 * sin(61 deg) = 87.46
        BloonActor bloon61 = createDummyBloonActor(48.48f, 87.46f, 200, 2L);

        manager.addBloonToOnstageForTesting(bloon60);
        manager.addBloonToOnstageForTesting(bloon61);

        BloonActor target = manager.getNewHomingTarget(bulletActor);
        // bloon61 has higher progress but is outside 60 deg cone (61 deg > 60 deg), so bloon60 inside cone should be chosen
        assertEquals(bloon60, target);
    }
}
