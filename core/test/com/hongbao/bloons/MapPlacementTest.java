package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.helpers.Pair;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class MapPlacementTest {

    private Map map;

    @BeforeClass
    public static void initGdx() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, config);
        Gdx.files = new HeadlessFiles();
        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Gdx.gl;
    }

    @Before
    public void setUp() throws Exception {
        // Create map with empty directions grid
        Stage stage = Mockito.mock(Stage.class);
        map = Mockito.mock(Map.class, Mockito.CALLS_REAL_METHODS);
        Field onStageGirlsField = Map.class.getDeclaredField("onStageGirls");
        onStageGirlsField.setAccessible(true);
        onStageGirlsField.set(map, new HashSet<GirlActor>());
        // Set directions array so path check loop runs without NPE
        Pair<Float, Float>[][] directions = new Pair[33][19];
        map.setDirections(directions);
    }

    private GirlActor createMockGirlActor(float centerX, float centerY, float collisionRadius) {
        GirlActor girlActor = Mockito.mock(GirlActor.class);
        when(girlActor.getCenterX()).thenReturn(centerX);
        when(girlActor.getCenterY()).thenReturn(centerY);
        when(girlActor.getCollisionRadius()).thenReturn(collisionRadius);
        return girlActor;
    }

    @Test
    public void testValidPlacementInPlayArea() {
        // Tower completely within [0, 1500] x [0, 900]
        GirlActor girl = createMockGirlActor(100f, 100f, 30f);
        assertTrue("Tower within bounds should be valid", map.canPlaceGirl(girl));
    }

    @Test
    public void testPlacementExceedsRightBoundaryAt1500() {
        // Center at 1500 with radius 30 -> centerX + r = 1530 > 1500
        GirlActor girlAt1500 = createMockGirlActor(1500f, 450f, 30f);
        assertFalse("Tower placed at x=1500 with radius>0 must be rejected", map.canPlaceGirl(girlAt1500));

        // Center at 1480 with radius 30 -> centerX + r = 1510 > 1500 (overlaps UI sidebar at x >= 1500)
        GirlActor girlOverlappingRightUI = createMockGirlActor(1480f, 450f, 30f);
        assertFalse("Tower overlapping right UI sidebar (x >= 1500) must be rejected", map.canPlaceGirl(girlOverlappingRightUI));
    }

    @Test
    public void testPlacementExceedsLeftBoundary() {
        // Center near 0 with centerX - r < 0
        GirlActor girlOverlappingLeftEdge = createMockGirlActor(20f, 450f, 30f);
        assertFalse("Tower extending past left screen border must be rejected", map.canPlaceGirl(girlOverlappingLeftEdge));
    }

    @Test
    public void testPlacementExceedsTopAndBottomBoundaries() {
        // Center near 0 with centerY - r < 0
        GirlActor girlOverlappingBottomEdge = createMockGirlActor(500f, 20f, 30f);
        assertFalse("Tower extending past bottom screen border must be rejected", map.canPlaceGirl(girlOverlappingBottomEdge));

        // Center near 900 with centerY + r > 900
        GirlActor girlOverlappingTopEdge = createMockGirlActor(500f, 880f, 30f);
        assertFalse("Tower extending past top screen border must be rejected", map.canPlaceGirl(girlOverlappingTopEdge));
    }

    @Test
    public void testPlacementExactlyAtBoundaries() {
        // Exactly touching boundaries: centerX = 30, radius = 30 -> left bound = 0
        GirlActor girlTouchingLeft = createMockGirlActor(30f, 450f, 30f);
        assertTrue("Tower exactly touching left border (x=0) should be valid", map.canPlaceGirl(girlTouchingLeft));

        // Exactly touching right boundary: centerX = 1470, radius = 30 -> right bound = 1500
        GirlActor girlTouchingRight = createMockGirlActor(1470f, 450f, 30f);
        assertTrue("Tower exactly touching right boundary (x=1500) should be valid", map.canPlaceGirl(girlTouchingRight));
    }
}
