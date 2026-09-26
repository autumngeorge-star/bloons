package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(HeadlessGdxExtension.class)
public class GirlActorTest {

    private Girl girl;
    private GirlActor girlActor;

    @BeforeEach
    public void setUp() {
        girl = GirlFactory.createReimu();
        girlActor = new GirlActor(girl, 200f, 200f);
    }

    @Test
    public void testGirlActorInitialization() {
        assertNotNull(girlActor);
        assertEquals(girl, girlActor.getGirl());
        assertFalse(girlActor.isActive());
        assertTrue(girlActor.getCollisionRadius() > 0);
        assertNotNull(girlActor.getTextureRegion());
    }

    @Test
    public void testActiveAndRotation() {
        girlActor.setActive(true);
        assertTrue(girlActor.isActive());

        girlActor.setRotationAngle(45f);
        assertEquals(45f, girlActor.getRotationAngle(), 0.001f);
    }

    @Test
    public void testCreateBulletActor() {
        Bloon bloon = BloonFactory.createRedBloon();
        BloonActor targetActor = new BloonActor(bloon, 300f, 300f, null);

        BulletActor bulletActor = girlActor.createBulletActor(targetActor);
        assertNotNull(bulletActor);
        assertNotNull(bulletActor.getBullet());
    }

    @Test
    public void testCreateSpellCardActor() {
        SpellCardActor spellCardActor = girlActor.createSpellCardActor();
        assertNotNull(spellCardActor);
        assertNotNull(spellCardActor.getSpellCard());
    }
}
