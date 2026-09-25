package com.hongbao.bloons;

import com.badlogic.gdx.utils.Pool;
import com.hongbao.bloons.actors.BulletActor;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BulletPoolingTest {

    @Test
    public void testBulletActorImplementsPoolableAndReset() {
        BulletActor bulletActor = new BulletActor();
        assertTrue("BulletActor must implement Poolable", bulletActor instanceof Pool.Poolable);

        bulletActor.setSpellCardOverride("Reimu");
        bulletActor.setAllocated(true);
        bulletActor.setPosition(100, 200);

        bulletActor.reset();

        assertNull("Bullet must be null after reset", bulletActor.getBullet());
        assertNull("Target must be null after reset", bulletActor.getTarget());
        assertNull("TextureRegion must be null after reset", bulletActor.getTextureRegion());
        assertFalse("Allocated flag must be false after reset", bulletActor.isAllocated());
        assertEquals("X position should be reset", 0f, bulletActor.getX(), 0.001f);
        assertEquals("Y position should be reset", 0f, bulletActor.getY(), 0.001f);
    }

    @Test
    public void testBloonManagerBulletPoolObtainAndFree() {
        BloonManager bloonManager = new BloonManager(null, null);
        Pool<BulletActor> pool = bloonManager.getBulletPool();
        assertNotNull("Bullet pool must not be null", pool);

        BulletActor first = pool.obtain();
        assertNotNull("Obtained BulletActor must not be null", first);
        first.setAllocated(true);

        bloonManager.freeBulletActor(first);
        assertFalse("Freed bullet must not be allocated", first.isAllocated());

        BulletActor second = pool.obtain();
        assertSame("Subsequent obtain must recycle the freed BulletActor instance", first, second);
    }

    @Test
    public void testStateSanitizationOnReuse() {
        BloonManager bloonManager = new BloonManager(null, null);

        BulletActor b1 = bloonManager.getBulletPool().obtain();
        b1.setAllocated(true);
        b1.setSpellCardOverride("Yuyuko");

        bloonManager.freeBulletActor(b1);

        BulletActor b2 = bloonManager.getBulletPool().obtain();
        assertSame("Instance should be recycled", b1, b2);
        assertNull("State from previous use must be cleared", b2.getStage());
    }

    @Test
    public void testBulkObtainAndFreeZeroAllocations() {
        BloonManager bloonManager = new BloonManager(null, null);
        Pool<BulletActor> pool = bloonManager.getBulletPool();

        Set<BulletActor> initialSet = new HashSet<>();
        BulletActor[] batch1 = new BulletActor[100];
        for (int i = 0; i < 100; i++) {
            batch1[i] = pool.obtain();
            batch1[i].setAllocated(true);
            initialSet.add(batch1[i]);
        }

        for (int i = 0; i < 100; i++) {
            bloonManager.freeBulletActor(batch1[i]);
        }

        BulletActor[] batch2 = new BulletActor[100];
        for (int i = 0; i < 100; i++) {
            batch2[i] = pool.obtain();
            batch2[i].setAllocated(true);
            assertTrue("Recycled bullet must come from original pool set", initialSet.contains(batch2[i]));
        }
    }

    @Test
    public void testIdempotentFree() {
        BloonManager bloonManager = new BloonManager(null, null);
        BulletActor bullet = bloonManager.getBulletPool().obtain();
        bullet.setAllocated(true);

        bloonManager.freeBulletActor(bullet);
        bloonManager.freeBulletActor(bullet); // second call should be ignored safely

        BulletActor obtained = bloonManager.getBulletPool().obtain();
        assertSame("Pool size remains intact", bullet, obtained);
    }
}
