package com.hongbao.bloons;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.factories.GirlFactory;
import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(HeadlessGdxExtension.class)
public class MapTest {

    private Stage stage;
    private Map map;

    @BeforeEach
    public void setUp() {
        stage = new Stage();
        map = MapFactory.createBasicMap(stage);
        HeadlessGdxExtension.setCurrentMap(map);
    }

    @Test
    public void testMapInitialization() {
        assertNotNull(map);
        assertEquals("img/maps/basic_map.png", map.getBackgroundImageFilePath());
        assertNotNull(map.getBloonManager());
    }

    @Test
    public void testGetDirection() {
        Pair<Float, Float> dir = map.getDirection(0f, 400f);
        assertNotNull(dir);
    }

    @Test
    public void testGirlPlacement() {
        GirlActor girlActor = new GirlActor(GirlFactory.createReimu(), 500f, 500f);
        assertTrue(map.canPlaceGirl(girlActor));

        map.placeGirl(girlActor);
        assertTrue(girlActor.isActive());
        assertEquals(girlActor, map.getSelectedGirl());
    }

    @Test
    public void testCannotPlaceOutOfBounds() {
        GirlActor outOfBoundsActor = new GirlActor(GirlFactory.createReimu(), -200f, -200f);
        assertFalse(map.canPlaceGirl(outOfBoundsActor));
    }

    @Test
    public void testShowAndHideGirlDetails() {
        GirlActor girlActor = new GirlActor(GirlFactory.createReimu(), 500f, 500f);
        map.placeGirl(girlActor);

        assertDoesNotThrow(() -> map.showGirlDetailsModule());
        assertDoesNotThrow(() -> map.hideGirlDetailsModule());
    }
}
