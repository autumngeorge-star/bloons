package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BloonCollisionTest {

    private BloonManager bloonManager;

    @Before
    public void setUp() {
        BloonActor.resetIdGenerator();
        bloonManager = new BloonManager(null, null);
    }

    @Test
    public void testNearestBloonHitFirst() {
        // Create 3 red bloons at different X coordinates
        Bloon b1 = BloonFactory.createRedBloon(); // at x=200
        Bloon b2 = BloonFactory.createRedBloon(); // at x=100 (closest)
        Bloon b3 = BloonFactory.createRedBloon(); // at x=300

        BloonActor actorFar = new BloonActor(b1, 200, 100, null);
        BloonActor actorNear = new BloonActor(b2, 100, 100, null);
        BloonActor actorFarthest = new BloonActor(b3, 300, 100, null);

        bloonManager.addBloonActor(actorFar);
        bloonManager.addBloonActor(actorNear);
        bloonManager.addBloonActor(actorFarthest);

        // Bullet at x=80, y=100 with pierce 1
        Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 80, 100, 1, 0);

        bloonManager.checkCollision(bulletActor);

        // The nearest bloon (actorNear at x=100) should be popped/removed
        assertFalse("Nearest bloon should have been popped", bloonManager.containsBloon(actorNear));
        assertTrue("Far bloon should remain", bloonManager.containsBloon(actorFar));
        assertTrue("Farthest bloon should remain", bloonManager.containsBloon(actorFarthest));
        assertEquals(0, bullet.getPierce());
    }

    @Test
    public void testMultiPierceHitsProximityOrder() {
        // Bloons at x=150 (far), x=100 (closest), x=110 (second closest)
        Bloon b1 = BloonFactory.createRedBloon();
        Bloon b2 = BloonFactory.createRedBloon();
        Bloon b3 = BloonFactory.createRedBloon();

        BloonActor actor1 = new BloonActor(b1, 150, 100, null);
        BloonActor actor2 = new BloonActor(b2, 100, 100, null);
        BloonActor actor3 = new BloonActor(b3, 110, 100, null);

        bloonManager.addBloonActor(actor1);
        bloonManager.addBloonActor(actor2);
        bloonManager.addBloonActor(actor3);

        // Bullet at x=80, y=100 with pierce=2 (in range of x=100 and x=110)
        Bullet bullet = new Bullet(20f, 1, 2, 500f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 80, 100, 1, 0);

        bloonManager.checkCollision(bulletActor);

        // Pierce = 2 should pop closest (x=100) and second closest (x=110)
        assertFalse("Closest bloon (x=100) should be popped", bloonManager.containsBloon(actor2));
        assertFalse("Second closest bloon (x=110) should be popped", bloonManager.containsBloon(actor3));
        assertTrue("Far bloon (x=150) should remain", bloonManager.containsBloon(actor1));
        assertEquals(0, bullet.getPierce());
    }

    @Test
    public void testTieBreakingTrackProgressDistance() {
        // Two bloons at exact same position (100, 100)
        Bloon bSlow = BloonFactory.createRedBloon();
        bSlow.setDistanceTravelled(50);

        Bloon bFast = BloonFactory.createRedBloon();
        bFast.setDistanceTravelled(200); // Further along track

        BloonActor actorSlow = new BloonActor(bSlow, 100, 100, null);
        BloonActor actorFast = new BloonActor(bFast, 100, 100, null);

        bloonManager.addBloonActor(actorSlow);
        bloonManager.addBloonActor(actorFast);

        // Bullet at x=80 with pierce=1
        Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 80, 100, 1, 0);

        bloonManager.checkCollision(bulletActor);

        // Bloon with higher track progress distance (actorFast) should be popped first
        assertFalse("Bloon with higher distanceTravelled should be popped", bloonManager.containsBloon(actorFast));
        assertTrue("Bloon with lower distanceTravelled should remain", bloonManager.containsBloon(actorSlow));
    }

    @Test
    public void testTieBreakingSequenceCounter() {
        // Two bloons at exact same position (100, 100) and same distanceTravelled
        Bloon b1 = BloonFactory.createRedBloon();
        Bloon b2 = BloonFactory.createRedBloon();

        BloonActor actorFirst = new BloonActor(b1, 100, 100, null); // created first, lower sequence ID
        BloonActor actorSecond = new BloonActor(b2, 100, 100, null); // created second, higher sequence ID

        bloonManager.addBloonActor(actorFirst);
        bloonManager.addBloonActor(actorSecond);

        // Bullet at x=80 with pierce=1
        Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
        BulletActor bulletActor = new BulletActor(bullet, 80, 100, 1, 0);

        bloonManager.checkCollision(bulletActor);

        // Earlier created bloon (actorFirst) should be popped first
        assertFalse("Earlier created bloon should be popped", bloonManager.containsBloon(actorFirst));
        assertTrue("Later created bloon should remain", bloonManager.containsBloon(actorSecond));
    }

    @Test
    public void testDeterminismAcrossMultipleRuns() {
        for (int i = 0; i < 50; i++) {
            BloonActor.resetIdGenerator();
            BloonManager bm = new BloonManager(null, null);

            Bloon b1 = BloonFactory.createRedBloon();
            Bloon b2 = BloonFactory.createRedBloon();
            Bloon b3 = BloonFactory.createRedBloon();

            BloonActor a1 = new BloonActor(b1, 150, 100, null);
            BloonActor a2 = new BloonActor(b2, 100, 100, null);
            BloonActor a3 = new BloonActor(b3, 200, 100, null);

            // Add in arbitrary / varying order
            if (i % 2 == 0) {
                bm.addBloonActor(a3);
                bm.addBloonActor(a1);
                bm.addBloonActor(a2);
            } else {
                bm.addBloonActor(a1);
                bm.addBloonActor(a2);
                bm.addBloonActor(a3);
            }

            Bullet bullet = new Bullet(20f, 1, 1, 500f, false, "red_spell_card.png");
            BulletActor bulletActor = new BulletActor(bullet, 80, 100, 1, 0);

            bm.checkCollision(bulletActor);

            // Always a2 (closest at x=100) must be popped
            assertFalse("a2 must always be popped regardless of set insertion order", bm.containsBloon(a2));
            assertTrue("a1 must remain", bm.containsBloon(a1));
            assertTrue("a3 must remain", bm.containsBloon(a3));
        }
    }
}
