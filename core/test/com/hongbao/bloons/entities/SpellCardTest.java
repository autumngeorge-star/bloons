package com.hongbao.bloons.entities;

import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SpellCardTest {

    private static final float EPSILON = 1e-4f;

    @Test
    public void testReimuSpellCardUnrotated() {
        SpellCard card = SpellCard.createReimuSpellCard(0f);
        assertNotNull(card);
        assertEquals("Reimu", card.getOverrideName());

        List<Bullet> bullets = card.getBulletsToCreateAndIncrementFrame();
        assertNotNull(bullets);
        assertEquals(6, bullets.size());

        // Bullet 1: (0, 1)
        assertEquals(0f, bullets.get(0).getInitialDXOverride(), EPSILON);
        assertEquals(1f, bullets.get(0).getInitialDYOverride(), EPSILON);

        // Bullet 4: (0, -1)
        assertEquals(0f, bullets.get(3).getInitialDXOverride(), EPSILON);
        assertEquals(-1f, bullets.get(3).getInitialDYOverride(), EPSILON);
    }

    @Test
    public void testReimuSpellCardRotated90Degrees() {
        SpellCard card = SpellCard.createReimuSpellCard(90f);
        List<Bullet> bullets = card.getBulletsToCreateAndIncrementFrame();
        assertNotNull(bullets);

        // Bullet 1 originally (0, 1) facing Up -> rotated 90 deg clockwise to face Right (1, 0)
        assertEquals(1f, bullets.get(0).getInitialDXOverride(), EPSILON);
        assertEquals(0f, bullets.get(0).getInitialDYOverride(), EPSILON);

        // Bullet 4 originally (0, -1) facing Down -> rotated 90 deg clockwise to face Left (-1, 0)
        assertEquals(-1f, bullets.get(3).getInitialDXOverride(), EPSILON);
        assertEquals(0f, bullets.get(3).getInitialDYOverride(), EPSILON);
    }

    @Test
    public void testYuyukoSpellCardRotated90Degrees() {
        SpellCard card = SpellCard.createYuyukoSpellCard(90f);
        List<Bullet> bullets = card.getBulletsToCreateAndIncrementFrame();
        assertNotNull(bullets);
        assertEquals(8, bullets.size()); // 4 bullets for left side, 4 for right side at frame 0

        // Left side offset originally (-125, 0) -> rotated 90 deg clockwise to (0, 125)
        Bullet leftBullet = bullets.get(0);
        assertEquals(0f, leftBullet.getInitialXOffset(), EPSILON);
        assertEquals(125f, leftBullet.getInitialYOffset(), EPSILON);

        // Right side offset originally (125, 0) -> rotated 90 deg clockwise to (0, -125)
        Bullet rightBullet = bullets.get(4);
        assertEquals(0f, rightBullet.getInitialXOffset(), EPSILON);
        assertEquals(-125f, rightBullet.getInitialYOffset(), EPSILON);
    }

    @Test
    public void testGirlCreateSpellCardDelegation() {
        Girl reimu = GirlFactory.createReimu();
        SpellCard reimuCard = reimu.createSpellCard(90f);
        assertNotNull(reimuCard);
        assertEquals("Reimu", reimuCard.getOverrideName());

        List<Bullet> reimuBullets = reimuCard.getBulletsToCreateAndIncrementFrame();
        assertEquals(1f, reimuBullets.get(0).getInitialDXOverride(), EPSILON);
        assertEquals(0f, reimuBullets.get(0).getInitialDYOverride(), EPSILON);

        Girl yuyuko = GirlFactory.createYuyuko();
        SpellCard yuyukoCard = yuyuko.createSpellCard(180f);
        assertNotNull(yuyukoCard);
        assertEquals("Yuyuko", yuyukoCard.getOverrideName());

        List<Bullet> yuyukoBullets = yuyukoCard.getBulletsToCreateAndIncrementFrame();
        // Left offset (-125, 0) rotated 180 deg -> (125, 0)
        assertEquals(125f, yuyukoBullets.get(0).getInitialXOffset(), EPSILON);
        assertEquals(0f, yuyukoBullets.get(0).getInitialYOffset(), EPSILON);
    }
}
