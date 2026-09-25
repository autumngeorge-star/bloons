package com.hongbao.bloons;

import com.badlogic.gdx.math.Vector2;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SpatialPathProgressTest {

    @Test
    public void testMapWaypointDistanceCaching() {
        Map map = MapFactory.createBasicMap(null);
        List<Vector2> waypoints = map.getWaypoints();
        assertNotNull(waypoints);
        assertTrue(waypoints.size() >= 2);

        float[] segmentLengths = map.getSegmentLengths();
        float[] cumulativeDistances = map.getCumulativeDistances();

        assertNotNull(segmentLengths);
        assertNotNull(cumulativeDistances);
        assertEquals(waypoints.size() - 1, segmentLengths.length);
        assertEquals(waypoints.size(), cumulativeDistances.length);

        assertEquals(0f, cumulativeDistances[0], 0.001f);
        float expectedCumulative = 0f;
        for (int i = 0; i < segmentLengths.length; i++) {
            assertTrue(segmentLengths[i] > 0);
            expectedCumulative += segmentLengths[i];
            assertEquals(expectedCumulative, cumulativeDistances[i + 1], 0.001f);
        }
        assertEquals(expectedCumulative, map.getTotalPathDistance(), 0.001f);
    }

    @Test
    public void testCustomWaypointsAndSegmentProgress() {
        Map map = new Map("basic_map.png", null);
        List<Vector2> waypoints = new ArrayList<>();
        waypoints.add(new Vector2(0, 0));
        waypoints.add(new Vector2(100, 0));
        waypoints.add(new Vector2(100, 200));
        map.setWaypoints(waypoints);

        assertEquals(2, map.getSegmentLengths().length);
        assertEquals(100f, map.getSegmentLengths()[0], 0.001f);
        assertEquals(200f, map.getSegmentLengths()[1], 0.001f);

        assertEquals(0f, map.getCumulativeDistances()[0], 0.001f);
        assertEquals(100f, map.getCumulativeDistances()[1], 0.001f);
        assertEquals(300f, map.getCumulativeDistances()[2], 0.001f);

        // Test position (50, 0) -> segment 0, offset 50, progress 50
        Map.PathProgressResult res1 = map.calculatePathProgress(50, 0);
        assertEquals(0, res1.getSegmentIndex());
        assertEquals(50f, res1.getOffsetAlongSegment(), 0.001f);
        assertEquals(50f, res1.getTotalProgress(), 0.001f);

        // Test position (100, 100) -> segment 1, offset 100, progress 200
        Map.PathProgressResult res2 = map.calculatePathProgress(100, 100);
        assertEquals(1, res2.getSegmentIndex());
        assertEquals(100f, res2.getOffsetAlongSegment(), 0.001f);
        assertEquals(200f, res2.getTotalProgress(), 0.001f);
    }

    @Test
    public void testMonotonicProgressAlongTurnMap() {
        Map map = MapFactory.createMapWithTurn(null);
        
        Vector2[] testPoints = new Vector2[] {
            new Vector2(-25, 425),
            new Vector2(50, 425),
            new Vector2(225, 425),
            new Vector2(225, 500),
            new Vector2(225, 575),
            new Vector2(500, 575),
            new Vector2(1000, 575)
        };

        float previousProgress = -1f;
        for (Vector2 pt : testPoints) {
            Map.PathProgressResult res = map.calculatePathProgress(pt.x, pt.y);
            assertTrue("Progress should be strictly monotonic: " + res.getTotalProgress() + " > " + previousProgress, res.getTotalProgress() > previousProgress);
            previousProgress = res.getTotalProgress();
        }
    }

    @Test
    public void testChildBloonProgressCalculation() {
        Map map = MapFactory.createMapWithTurn(null);

        // Parent bloon at corner (225, 500)
        Bloon parent = BloonFactory.createBloonOfType("blue", 2);
        Map.PathProgressResult parentProgress = map.calculatePathProgress(225, 500);
        parent.updateProgress(parentProgress.getSegmentIndex(), parentProgress.getOffsetAlongSegment(), parentProgress.getTotalProgress());

        assertTrue(parent.getPathProgress() > 0);

        // Pop parent bloon
        BloonPoppedResult poppedResult = parent.pop(1);
        assertEquals(1, poppedResult.getBloonsGenerated().size());
        Bloon child = poppedResult.getBloonsGenerated().iterator().next();

        // Calculate child progress from spatial spawn coordinates (225, 500)
        Map.PathProgressResult childProgress = map.calculatePathProgress(225, 500);
        child.updateProgress(childProgress.getSegmentIndex(), childProgress.getOffsetAlongSegment(), childProgress.getTotalProgress());

        assertEquals(parent.getPathProgress(), child.getPathProgress(), 0.001f);
        assertEquals(parent.getCurrentSegmentIndex(), child.getCurrentSegmentIndex());
        assertEquals(parent.getDistanceAlongSegment(), child.getDistanceAlongSegment(), 0.001f);
    }
}
