package com.hongbao.bloons.entities;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

public class BloonTest {

    @Test
    public void testListenerNotificationOnDamage() {
        Bloon ceramicBloon = new Bloon(Bloon.Color.CERAMIC, 18, false, false);
        AtomicInteger notificationCount = new AtomicInteger(0);

        BloonStateChangeListener listener = new BloonStateChangeListener() {
            @Override
            public void onBloonStateChanged(Bloon bloon) {
                notificationCount.incrementAndGet();
            }
        };

        ceramicBloon.addStateChangeListener(listener);

        assertEquals(18, ceramicBloon.getHealth());
        assertEquals(Bloon.Color.CERAMIC, ceramicBloon.getColor());
        assertEquals("img/bloons/ceramic_bloon.png", ceramicBloon.getImageFileName());

        // Apply non-popping damage
        ceramicBloon.damage(5);

        assertEquals(13, ceramicBloon.getHealth());
        assertEquals(1, notificationCount.get());
        assertEquals(Bloon.Color.CERAMIC, ceramicBloon.getColor());

        // Apply damage that changes color/layer from Ceramic (13) to Yellow (4)
        ceramicBloon.damage(9);

        assertEquals(4, ceramicBloon.getHealth());
        assertEquals(2, notificationCount.get());
        assertEquals(Bloon.Color.YELLOW, ceramicBloon.getColor());
        assertEquals(8, ceramicBloon.getSpeed());
        assertEquals("img/bloons/yellow_bloon.png", ceramicBloon.getImageFileName());
    }

    @Test
    public void testListenerUnregistration() {
        Bloon moabBloon = new Bloon(Bloon.Color.MOAB, 200, false, false);
        AtomicInteger notificationCount = new AtomicInteger(0);

        BloonStateChangeListener listener = new BloonStateChangeListener() {
            @Override
            public void onBloonStateChanged(Bloon bloon) {
                notificationCount.incrementAndGet();
            }
        };

        moabBloon.addStateChangeListener(listener);
        moabBloon.damage(10);
        assertEquals(1, notificationCount.get());

        // Unregister listener
        moabBloon.removeStateChangeListener(listener);
        moabBloon.damage(10);
        // Count should remain 1 because listener was unsubscribed
        assertEquals(1, notificationCount.get());
    }

    @Test
    public void testStatePropertyUpdatesOnSetters() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        AtomicInteger notificationCount = new AtomicInteger(0);

        bloon.addStateChangeListener(b -> notificationCount.incrementAndGet());

        bloon.setCamo(true);
        assertEquals(1, notificationCount.get());
        assertTrue(bloon.getImageFileName().contains("_camo"));

        bloon.setRegen(true);
        assertEquals(2, notificationCount.get());
        assertTrue(bloon.getImageFileName().contains("_regrowth"));

        bloon.setHealth(5);
        assertEquals(3, notificationCount.get());
        assertEquals(Bloon.Color.PINK, bloon.getColor());
        assertEquals(9, bloon.getSpeed());
    }
}
