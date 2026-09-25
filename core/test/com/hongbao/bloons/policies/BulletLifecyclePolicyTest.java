package com.hongbao.bloons.policies;

import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.SpellCard;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class BulletLifecyclePolicyTest {

    @Test
    public void testRangeLifecyclePolicyDefault() {
        Bullet bullet = new Bullet();
        bullet.setMaxRange(100f);
        bullet.setLifecyclePolicy(RangeLifecyclePolicy.DEFAULT);

        assertFalse(bullet.isExpired());

        bullet.incrementDistanceTraveled(); // speed=20, increment=4
        assertFalse(bullet.isExpired());

        // Travel until maxRange is reached
        for (int i = 0; i < 30; i++) {
            bullet.incrementDistanceTraveled();
        }
        assertTrue(bullet.getDistanceTraveled() >= 100f);
        assertTrue(bullet.isExpired());
    }

    @Test
    public void testRangeLifecyclePolicyCustom() {
        Bullet bullet = new Bullet();
        bullet.setMaxRange(500f);
        RangeLifecyclePolicy customRangePolicy = new RangeLifecyclePolicy(50f);
        bullet.setLifecyclePolicy(customRangePolicy);

        for (int i = 0; i < 12; i++) {
            bullet.incrementDistanceTraveled(); // 12 * 4 = 48
        }
        assertFalse(bullet.isExpired());

        bullet.incrementDistanceTraveled(); // 52 >= 50
        assertTrue(bullet.isExpired());
        assertTrue(customRangePolicy.isExpired(52f, 0f));
    }

    @Test
    public void testDurationLifecyclePolicyDefault() {
        Bullet bullet = new Bullet();
        bullet.setMaxDuration(2.0f);
        bullet.setLifecyclePolicy(DurationLifecyclePolicy.DEFAULT);

        assertFalse(bullet.isExpired());

        bullet.incrementDuration(1.0f);
        assertFalse(bullet.isExpired());

        bullet.incrementDuration(1.0f);
        assertTrue(bullet.isExpired());
    }

    @Test
    public void testDurationLifecyclePolicyCustom() {
        Bullet bullet = new Bullet();
        bullet.setMaxDuration(10.0f);
        DurationLifecyclePolicy customDurationPolicy = new DurationLifecyclePolicy(1.5f);
        bullet.setLifecyclePolicy(customDurationPolicy);

        bullet.incrementDuration(1.0f);
        assertFalse(bullet.isExpired());

        bullet.incrementDuration(0.6f);
        assertTrue(bullet.isExpired());
        assertTrue(customDurationPolicy.isExpired(0f, 1.6f));
    }

    @Test
    public void testHybridLifecyclePolicy() {
        Bullet bullet = new Bullet();
        bullet.setMaxRange(100f);
        bullet.setMaxDuration(5.0f);

        HybridLifecyclePolicy hybridPolicy = new HybridLifecyclePolicy(100f, 5.0f);
        bullet.setLifecyclePolicy(hybridPolicy);

        assertFalse(bullet.isExpired());

        // Expire by duration first
        bullet.incrementDuration(5.0f);
        assertTrue(bullet.isExpired());

        // Reset duration and expire by range
        bullet.setDuration(0f);
        assertFalse(bullet.isExpired());

        for (int i = 0; i < 30; i++) {
            bullet.incrementDistanceTraveled();
        }
        assertTrue(bullet.isExpired());
    }

    @Test
    public void testGirlBulletLifecyclePolicy() {
        Girl reimu = GirlFactory.createReimu();
        Bullet bullet = reimu.createBullet();

        assertNotNull(bullet.getLifecyclePolicy());
        assertTrue(bullet.getLifecyclePolicy() instanceof RangeLifecyclePolicy);
    }

    @Test
    public void testSpellCardBulletLifecyclePolicies() {
        SpellCard reimuSpellCard = SpellCard.createReimuSpellCard();
        Bullet reimuBullet = reimuSpellCard.getBulletsToCreateAndIncrementFrame().get(0);

        assertNotNull(reimuBullet.getLifecyclePolicy());
        assertTrue(reimuBullet.getLifecyclePolicy() instanceof DurationLifecyclePolicy);

        SpellCard yuyukoSpellCard = SpellCard.createYuyukoSpellCard();
        Bullet yuyukoBullet = yuyukoSpellCard.getBulletsToCreateAndIncrementFrame().get(0);

        assertNotNull(yuyukoBullet.getLifecyclePolicy());
        assertTrue(yuyukoBullet.getLifecyclePolicy() instanceof HybridLifecyclePolicy);
    }
}
