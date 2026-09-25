package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class SoundEventListenerTest {

    @Test
    public void testMapEvents() {
        AtomicBoolean placed = new AtomicBoolean(false);
        AtomicBoolean upgraded = new AtomicBoolean(false);
        AtomicBoolean sold = new AtomicBoolean(false);

        SoundEventListener listener = new SoundEventListener() {
            @Override
            public void onTowerPlaced(GirlActor girlActor) {
                placed.set(true);
            }

            @Override
            public void onTowerUpgraded(GirlActor girlActor) {
                upgraded.set(true);
            }

            @Override
            public void onTowerSold(GirlActor girlActor) {
                sold.set(true);
            }
        };

        Map map = new Map("heater.png", null);
        map.addSoundEventListener(listener);

        Girl girl = GirlFactory.createReimu();
        GirlActor girlActor = new GirlActor(girl, 100, 100);

        map.placeGirl(girlActor);
        assertTrue("onTowerPlaced should be called when placeGirl is invoked", placed.get());

        // Test upgrade notification helper
        try {
            java.lang.reflect.Method method = Map.class.getDeclaredMethod("notifyTowerUpgraded", GirlActor.class);
            method.setAccessible(true);
            method.invoke(map, girlActor);
            assertTrue("onTowerUpgraded should be called on upgrade", upgraded.get());
        } catch (Exception e) {
            fail("Failed to invoke notifyTowerUpgraded: " + e.getMessage());
        }

        // Test sell notification helper
        try {
            java.lang.reflect.Method method = Map.class.getDeclaredMethod("notifyTowerSold", GirlActor.class);
            method.setAccessible(true);
            method.invoke(map, girlActor);
            assertTrue("onTowerSold should be called on sell", sold.get());
        } catch (Exception e) {
            fail("Failed to invoke notifyTowerSold: " + e.getMessage());
        }
    }

    @Test
    public void testBloonManagerEvents() {
        AtomicBoolean popped = new AtomicBoolean(false);
        AtomicBoolean damaged = new AtomicBoolean(false);

        SoundEventListener listener = new SoundEventListener() {
            @Override
            public void onBloonPopped(BloonActor bloonActor, int damage) {
                popped.set(true);
            }

            @Override
            public void onBloonDamaged(BloonActor bloonActor, int damage) {
                damaged.set(true);
            }
        };

        BloonManager bloonManager = new BloonManager(null, null);
        bloonManager.addSoundEventListener(listener);

        Bloon bloon = BloonFactory.createRedBloon();
        BloonActor bloonActor = new BloonActor(bloon, 100, 100, null);

        // Notify bloon damaged
        try {
            java.lang.reflect.Method method = BloonManager.class.getDeclaredMethod("notifyBloonDamaged", BloonActor.class, int.class);
            method.setAccessible(true);
            method.invoke(bloonManager, bloonActor, 1);
            assertTrue("onBloonDamaged should be called", damaged.get());
        } catch (Exception e) {
            fail("Failed to invoke notifyBloonDamaged: " + e.getMessage());
        }

        // Notify bloon popped
        try {
            java.lang.reflect.Method method = BloonManager.class.getDeclaredMethod("notifyBloonPopped", BloonActor.class, int.class);
            method.setAccessible(true);
            method.invoke(bloonManager, bloonActor, 1);
            assertTrue("onBloonPopped should be called", popped.get());
        } catch (Exception e) {
            fail("Failed to invoke notifyBloonPopped: " + e.getMessage());
        }
    }

    @Test
    public void testGirlActorAndSpellCardEvents() {
        AtomicBoolean fired = new AtomicBoolean(false);
        AtomicBoolean spellActivated = new AtomicBoolean(false);

        SoundEventListener listener = new SoundEventListener() {
            @Override
            public void onBulletFired(BulletActor bulletActor) {
                fired.set(true);
            }

            @Override
            public void onSpellActivated(SpellCardActor spellCardActor) {
                spellActivated.set(true);
            }
        };

        Girl girl = GirlFactory.createReimu();
        GirlActor girlActor = new GirlActor(girl, 100, 100);
        girlActor.addSoundEventListener(listener);

        Bloon bloon = BloonFactory.createRedBloon();
        BloonActor target = new BloonActor(bloon, 200, 200, null);

        girlActor.createBulletActor(target);
        assertTrue("onBulletFired should be called when createBulletActor is invoked", fired.get());

        girlActor.createSpellCardActor();
        assertTrue("onSpellActivated should be called when createSpellCardActor is invoked", spellActivated.get());
    }

    @Test
    public void testBloonsTouhouDefenseDispose() {
        BloonsTouhouDefense game = new BloonsTouhouDefense();
        AtomicBoolean soundDisposed = new AtomicBoolean(false);

        SoundManager mockSoundManager = new SoundManager() {
            @Override
            public void dispose() {
                soundDisposed.set(true);
            }
        };

        try {
            java.lang.reflect.Field field = BloonsTouhouDefense.class.getDeclaredField("soundManager");
            field.setAccessible(true);
            field.set(game, mockSoundManager);
        } catch (Exception e) {
            fail("Failed to set mock soundManager: " + e.getMessage());
        }

        game.dispose();
        assertTrue("BloonsTouhouDefense.dispose() must dispose soundManager", soundDisposed.get());
    }
}
