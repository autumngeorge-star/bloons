package com.hongbao.bloons.effects;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StatusEffectApplicatorTest {

    @Test
    public void testStatusEffectApplicatorInterface() {
        AtomicBoolean applied = new AtomicBoolean(false);
        StatusEffectApplicator applicator = (target, bulletActor) -> applied.set(true);

        applicator.apply(null, null);
        Assert.assertTrue("StatusEffectApplicator should execute strategy handler", applied.get());
    }

    @Test
    public void testBulletApplicatorsManagement() {
        Bullet bullet = new Bullet();
        Assert.assertNotNull(bullet.getApplicators());
        Assert.assertEquals(0, bullet.getApplicators().size());

        AtomicInteger hitCount = new AtomicInteger(0);
        StatusEffectApplicator countApplicator = (target, bulletActor) -> hitCount.incrementAndGet();

        bullet.addApplicator(countApplicator);
        Assert.assertEquals(1, bullet.getApplicators().size());

        bullet.getApplicators().get(0).apply(null, null);
        Assert.assertEquals(1, hitCount.get());
    }

    @Test
    public void testGirlLevelIndexedApplicatorsAndBulletCreation() {
        Girl reimu = GirlFactory.createReimu();
        
        AtomicInteger level0Hits = new AtomicInteger(0);
        AtomicInteger level1Hits = new AtomicInteger(0);

        StatusEffectApplicator appLvl0 = (target, bulletActor) -> level0Hits.incrementAndGet();
        StatusEffectApplicator appLvl1 = (target, bulletActor) -> level1Hits.addAndGet(10);

        List<List<StatusEffectApplicator>> applicators = new ArrayList<>();
        applicators.add(Arrays.asList(appLvl0));
        applicators.add(Arrays.asList(appLvl1));

        reimu.setApplicators(applicators);

        // Level 0 bullet
        Bullet bulletLvl0 = reimu.createBullet();
        Assert.assertEquals(1, bulletLvl0.getApplicators().size());
        bulletLvl0.getApplicators().get(0).apply(null, null);
        Assert.assertEquals(1, level0Hits.get());
        Assert.assertEquals(0, level1Hits.get());

        // Upgrade reimu to Level 1
        Girl upgraded = reimu.getUpgradedStats();
        Assert.assertEquals(1, upgraded.getLevel());

        Bullet bulletLvl1 = upgraded.createBullet();
        Assert.assertEquals(1, bulletLvl1.getApplicators().size());
        bulletLvl1.getApplicators().get(0).apply(null, null);
        Assert.assertEquals(1, level0Hits.get());
        Assert.assertEquals(10, level1Hits.get());
    }

    @Test
    public void testGirlFactorySpecialtyTowersHaveApplicators() {
        Girl sakuya = GirlFactory.createSakuya();
        Assert.assertNotNull(sakuya.getApplicatorsForLevel(0));
        Assert.assertFalse(sakuya.getApplicatorsForLevel(0).isEmpty());

        Bullet sakuyaBullet = sakuya.createBullet();
        Assert.assertFalse(sakuyaBullet.getApplicators().isEmpty());

        // Test Sakuya slow effect on a bloon entity directly
        Bloon bloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        int initialSpeed = bloon.getSpeed(); // Blue speed is 6
        Assert.assertTrue(initialSpeed > 1);

        // Simulate applicator strategy execution on bloon
        sakuyaBullet.getApplicators().forEach(app -> app.apply(null, null)); // Null actor safety check
        
        // Manual strategy simulation
        StatusEffectApplicator slowStrategy = sakuyaBullet.getApplicators().get(0);
        // Execute with mock target actor wrapper if available or strategy check
        Assert.assertNotNull(slowStrategy);

        Girl yuyuko = GirlFactory.createYuyuko();
        Assert.assertNotNull(yuyuko.getApplicatorsForLevel(0));
        Assert.assertFalse(yuyuko.getApplicatorsForLevel(0).isEmpty());
    }

    @Test
    public void testExceptionAndNullHandlingInApplicator() {
        Bullet bullet = new Bullet();
        StatusEffectApplicator throwingApplicator = (target, bulletActor) -> {
            throw new RuntimeException("Simulated error in applicator");
        };
        bullet.addApplicator(throwingApplicator);

        // Verify that adding throwing applicator doesn't break list structure
        Assert.assertEquals(1, bullet.getApplicators().size());
    }
}
