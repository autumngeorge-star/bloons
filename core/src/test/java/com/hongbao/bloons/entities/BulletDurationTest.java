package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BulletDurationTest {

    @Test
    public void testBulletDefaultConstructorDurationFields() {
        Bullet bullet = new Bullet();
        Assert.assertEquals(0f, bullet.getMaxDuration(), 0.001f);
        Assert.assertEquals(0f, bullet.getElapsedDuration(), 0.001f);

        bullet.setMaxDuration(100f);
        Assert.assertEquals(100f, bullet.getMaxDuration(), 0.001f);

        bullet.setElapsedDuration(25f);
        Assert.assertEquals(25f, bullet.getElapsedDuration(), 0.001f);

        bullet.incrementElapsedDuration();
        Assert.assertEquals(26f, bullet.getElapsedDuration(), 0.001f);

        bullet.incrementElapsedDuration(4f);
        Assert.assertEquals(30f, bullet.getElapsedDuration(), 0.001f);
    }

    @Test
    public void testBulletOverloadedConstructorWithDuration() {
        Bullet bullet = new Bullet(15f, 3, 5, 400f, 150f, true, "test.png");
        Assert.assertEquals(15f, bullet.getSpeed(), 0.001f);
        Assert.assertEquals(3, bullet.getDamage());
        Assert.assertEquals(5, bullet.getPierce());
        Assert.assertEquals(400f, bullet.getMaxRange(), 0.001f);
        Assert.assertEquals(150f, bullet.getMaxDuration(), 0.001f);
        Assert.assertEquals(0f, bullet.getElapsedDuration(), 0.001f);
        Assert.assertTrue(bullet.isHoming());
    }

    @Test
    public void testGirlDefinesAndPassesBulletDuration() {
        Girl girl = new Girl(
            "TestGirl",
            Arrays.asList(30, 20),
            Arrays.asList(10f, 15f),
            Arrays.asList(1, 2),
            Arrays.asList(1, 2),
            Arrays.asList(300f, 400f),
            Arrays.asList(150f, 200f),
            Arrays.asList(false, true),
            Arrays.asList(120f, 240f),
            "test_girl.png",
            "test_bullet.png",
            100,
            Arrays.asList(50, Girl.NO_UPGRADES_AVAILABLE)
        );

        Assert.assertEquals(120f, girl.getBulletDuration(), 0.001f);
        Bullet b1 = girl.createBullet();
        Assert.assertEquals(120f, b1.getMaxDuration(), 0.001f);

        girl.upgrade();
        Assert.assertEquals(240f, girl.getBulletDuration(), 0.001f);
        Bullet b2 = girl.createBullet();
        Assert.assertEquals(240f, b2.getMaxDuration(), 0.001f);
    }

    @Test
    public void testGirlUpgradedStatsPreservesDurationList() {
        Girl girl = new Girl(
            "TestGirl",
            Arrays.asList(30, 20),
            Arrays.asList(10f, 15f),
            Arrays.asList(1, 2),
            Arrays.asList(1, 2),
            Arrays.asList(300f, 400f),
            Arrays.asList(150f, 200f),
            Arrays.asList(false, true),
            Arrays.asList(100f, 200f),
            "test_girl.png",
            "test_bullet.png",
            100,
            Arrays.asList(50, Girl.NO_UPGRADES_AVAILABLE)
        );

        Girl upgraded = girl.getUpgradedStats();
        Assert.assertNotNull(upgraded);
        Assert.assertEquals(1, upgraded.getLevel());
        Assert.assertEquals(200f, upgraded.getBulletDuration(), 0.001f);
    }

    @Test
    public void testSpellCardsSetExplicitDurationAndNotMaxRange5000() {
        SpellCard reimuCard = SpellCard.createReimuSpellCard();
        List<Bullet> reimuBullets = reimuCard.getBulletsToCreateAndIncrementFrame();
        Assert.assertNotNull(reimuBullets);
        Assert.assertFalse(reimuBullets.isEmpty());
        for (Bullet b : reimuBullets) {
            Assert.assertEquals(1250f, b.getMaxDuration(), 0.001f);
            Assert.assertNotEquals(5000f, b.getMaxRange(), 0.001f);
        }

        SpellCard yuyukoCard = SpellCard.createYuyukoSpellCard();
        List<Bullet> yuyukoBullets = yuyukoCard.getBulletsToCreateAndIncrementFrame();
        Assert.assertNotNull(yuyukoBullets);
        Assert.assertFalse(yuyukoBullets.isEmpty());
        for (Bullet b : yuyukoBullets) {
            Assert.assertEquals(5000f, b.getMaxDuration(), 0.001f);
            Assert.assertNotEquals(5000f, b.getMaxRange(), 0.001f);
        }
    }

    @Test
    public void testStationaryBulletExpirationLogic() {
        Bullet stationaryBullet = new Bullet(0f, 1, 1, 500f, 50f, false, "stationary.png");
        Assert.assertEquals(0f, stationaryBullet.getSpeed(), 0.001f);
        Assert.assertEquals(50f, stationaryBullet.getMaxDuration(), 0.001f);

        for (int i = 0; i < 49; i++) {
            stationaryBullet.incrementElapsedDuration();
            stationaryBullet.incrementDistanceTraveled();
            Assert.assertTrue(stationaryBullet.getElapsedDuration() < stationaryBullet.getMaxDuration());
            Assert.assertEquals(0f, stationaryBullet.getDistanceTraveled(), 0.001f);
        }

        stationaryBullet.incrementElapsedDuration();
        Assert.assertEquals(50f, stationaryBullet.getElapsedDuration(), 0.001f);
        Assert.assertTrue(stationaryBullet.getElapsedDuration() >= stationaryBullet.getMaxDuration());
    }

    @Test
    public void testZeroOrNegativeDurationDefaultsToDistanceCheck() {
        Bullet bZero = new Bullet(10f, 1, 1, 100f, 0f, false, "bullet.png");
        for (int i = 0; i < 50; i++) {
            bZero.incrementElapsedDuration();
            bZero.incrementDistanceTraveled();
        }
        Assert.assertEquals(100f, bZero.getDistanceTraveled(), 0.001f);
        Assert.assertTrue(bZero.getDistanceTraveled() >= bZero.getMaxRange());

        Bullet bNeg = new Bullet(10f, 1, 1, 100f, -10f, false, "bullet.png");
        Assert.assertFalse(bNeg.getMaxDuration() > 0);
    }
}
