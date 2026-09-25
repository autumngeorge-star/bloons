package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CollisionDeterminismTest {

    private BloonManager bloonManager;

    @BeforeClass
    public static void initGdx() {
        if (Gdx.app == null) {
            new HeadlessApplication(new ApplicationAdapter() {});
        }
        Gdx.audio = new Audio() {
            @Override
            public AudioDevice newAudioDevice(int samplingRate, boolean isMono) { return null; }
            @Override
            public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) { return null; }
            @Override
            public Sound newSound(FileHandle fileHandle) {
                return new Sound() {
                    @Override public long play() { return 0; }
                    @Override public long play(float volume) { return 0; }
                    @Override public long play(float volume, float pitch, float pan) { return 0; }
                    @Override public long loop() { return 0; }
                    @Override public long loop(float volume) { return 0; }
                    @Override public long loop(float volume, float pitch, float pan) { return 0; }
                    @Override public void stop() {}
                    @Override public void pause() {}
                    @Override public void resume() {}
                    @Override public void dispose() {}
                    @Override public void stop(long soundId) {}
                    @Override public void pause(long soundId) {}
                    @Override public void resume(long soundId) {}
                    @Override public void setLooping(long soundId, boolean looping) {}
                    @Override public void setPitch(long soundId, float pitch) {}
                    @Override public void setVolume(long soundId, float volume) {}
                    @Override public void setPan(long soundId, float pan, float volume) {}
                };
            }
            @Override
            public Music newMusic(FileHandle fileHandle) { return null; }
        };
    }

    @Before
    public void setUp() {
        BloonActor.resetIdGenerator();
        bloonManager = new BloonManager(null, null);
    }

    private BloonActor createBloonActorWithDistance(int distanceTravelled, float x, float y) {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        bloon.setDistanceTravelled(distanceTravelled);
        return new BloonActor(bloon, x, y, 15f);
    }

    private BulletActor createBulletActorWithPierce(int pierce, float x, float y) {
        Bullet bullet = new Bullet(20f, 1, pierce, 500f, false, "red_spell_card.png");
        return new BulletActor(bullet, x, y, 0f, 0f, 15f);
    }

    @Test
    public void testCollisionSortingByTrackDistanceDescending() {
        // Create candidate bloons in overlapping range (all at x=100, y=100)
        BloonActor b1 = createBloonActorWithDistance(50, 100, 100);
        BloonActor b2 = createBloonActorWithDistance(200, 100, 100);
        BloonActor b3 = createBloonActorWithDistance(100, 100, 100);
        BloonActor b4 = createBloonActorWithDistance(150, 100, 100);

        Set<BloonActor> onstage = getOnstageBloons(bloonManager);
        onstage.add(b1);
        onstage.add(b2);
        onstage.add(b3);
        onstage.add(b4);

        BulletActor bulletActor = createBulletActorWithPierce(2, 100, 100);

        bloonManager.checkCollision(bulletActor);

        // Pierce = 2 should hit top 2 distance bloons: b2 (200) and b4 (150)
        Assert.assertTrue("b2 (distance 200) should be damaged", bulletActor.hasDamagedBloon(b2));
        Assert.assertTrue("b4 (distance 150) should be damaged", bulletActor.hasDamagedBloon(b4));
        Assert.assertFalse("b3 (distance 100) should NOT be damaged", bulletActor.hasDamagedBloon(b3));
        Assert.assertFalse("b1 (distance 50) should NOT be damaged", bulletActor.hasDamagedBloon(b1));
        Assert.assertEquals("Pierce should be reduced to 0", 0, bulletActor.getBullet().getPierce());
    }

    @Test
    public void testSecondaryTieBreakerByBloonId() {
        // Create 3 bloons with IDENTICAL track distance (100)
        BloonActor b1 = createBloonActorWithDistance(100, 100, 100); // ID 1
        BloonActor b2 = createBloonActorWithDistance(100, 100, 100); // ID 2
        BloonActor b3 = createBloonActorWithDistance(100, 100, 100); // ID 3

        Set<BloonActor> onstage = getOnstageBloons(bloonManager);
        onstage.add(b3);
        onstage.add(b1);
        onstage.add(b2);

        BulletActor bulletActor = createBulletActorWithPierce(2, 100, 100);

        bloonManager.checkCollision(bulletActor);

        // Tie breaker ascending by bloonId: b1 (ID 1) and b2 (ID 2) must be damaged first
        Assert.assertTrue("b1 (ID 1) should be damaged", bulletActor.hasDamagedBloon(b1));
        Assert.assertTrue("b2 (ID 2) should be damaged", bulletActor.hasDamagedBloon(b2));
        Assert.assertFalse("b3 (ID 3) should NOT be damaged", bulletActor.hasDamagedBloon(b3));
    }

    @Test
    public void testMultiPierceExhaustionSequenceConsistency() {
        // Run collision check multiple times with different initial collection orders
        for (int i = 0; i < 10; i++) {
            BloonActor.resetIdGenerator();
            BloonManager manager = new BloonManager(null, null);
            Set<BloonActor> onstage = getOnstageBloons(manager);

            List<BloonActor> bloons = new ArrayList<>();
            bloons.add(createBloonActorWithDistance(500, 100, 100));
            bloons.add(createBloonActorWithDistance(400, 100, 100));
            bloons.add(createBloonActorWithDistance(300, 100, 100));
            bloons.add(createBloonActorWithDistance(200, 100, 100));
            bloons.add(createBloonActorWithDistance(100, 100, 100));

            // Shuffle insertion order to simulate HashSet non-determinism
            Collections.shuffle(bloons, new java.util.Random(i * 100L));
            onstage.addAll(bloons);

            BulletActor bulletActor = createBulletActorWithPierce(3, 100, 100);
            manager.checkCollision(bulletActor);

            // Top 3 distance bloons (500, 400, 300) must ALWAYS be damaged
            for (BloonActor b : bloons) {
                int dist = b.getBloon().getDistanceTravelled();
                if (dist >= 300) {
                    Assert.assertTrue("Bloon with distance " + dist + " must be damaged", bulletActor.hasDamagedBloon(b));
                } else {
                    Assert.assertFalse("Bloon with distance " + dist + " must NOT be damaged", bulletActor.hasDamagedBloon(b));
                }
            }
        }
    }

    @Test
    public void testHomingTargetDeterminism() {
        BloonActor b1 = createBloonActorWithDistance(100, 100, 100); // ID 1
        BloonActor b2 = createBloonActorWithDistance(200, 100, 100); // ID 2
        BloonActor b3 = createBloonActorWithDistance(200, 100, 100); // ID 3

        Set<BloonActor> onstage = getOnstageBloons(bloonManager);
        onstage.add(b3);
        onstage.add(b1);
        onstage.add(b2);

        BulletActor bulletActor = createBulletActorWithPierce(1, 100, 100);
        BloonActor target = bloonManager.getNewHomingTarget(bulletActor);

        // Distance to bullet is equal for all 3.
        // Between b2 and b3 (both distance 200), b2 has lower bloonId (ID 2 vs ID 3), so b2 is selected.
        Assert.assertEquals("b2 should be selected as homing target", b2, target);
    }

    @SuppressWarnings("unchecked")
    private Set<BloonActor> getOnstageBloons(BloonManager manager) {
        try {
            java.lang.reflect.Field field = BloonManager.class.getDeclaredField("onstageBloons");
            field.setAccessible(true);
            return (Set<BloonActor>) field.get(manager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
