package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import com.hongbao.bloons.helpers.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(HeadlessGdxExtension.class)
public class BloonActorTest {

    private Bloon bloon;
    private BloonActor bloonActor;

    @BeforeEach
    public void setUp() {
        bloon = BloonFactory.createBlueBloon();
        bloonActor = new BloonActor(bloon, 100f, 100f, null);
    }

    @Test
    public void testBloonActorInitialization() {
        assertNotNull(bloonActor);
        assertEquals(bloon, bloonActor.getBloon());
        assertNotNull(bloonActor.getBloonId());
        assertTrue(bloonActor.getParentBloonIds().isEmpty());
        assertTrue(bloonActor.getCollisionRadius() > 0);
    }

    @Test
    public void testChildBloonActorInheritsParentIds() {
        Bloon childBloon = BloonFactory.createRedBloon();
        BloonActor childActor = new BloonActor(childBloon, 100f, 100f, bloonActor);

        assertTrue(childActor.getParentBloonIds().contains(bloonActor.getBloonId()));
    }

    @Test
    public void testMovement() {
        float initialX = bloonActor.getX();
        float initialY = bloonActor.getY();

        bloonActor.move(new Pair<>(5f, 0f));

        assertTrue(bloonActor.getX() > initialX);
        assertEquals(initialY, bloonActor.getY(), 0.001f);
    }

    @Test
    public void testDamageAndPop() {
        bloonActor.damage(1);
        assertEquals(1, bloonActor.getBloon().getHealth());

        BloonPoppedResult result = bloonActor.pop(1);
        assertNotNull(result);
    }
}
