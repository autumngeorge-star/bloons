package com.hongbao.bloons.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class PathProgressTest {

    private PathProgress progress;

    @BeforeEach
    void setUp() {
        progress = new PathProgress();
    }

    @Test
    @DisplayName("Should initialize with zero waypoint index, segment ratio, and accumulated distance")
    void testInitialState() {
        assertEquals(0, progress.getWaypointIndex());
        assertEquals(0.0f, progress.getSegmentRatio(), 0.0001f);
        assertEquals(0.0f, progress.getAccumulatedDistance(), 0.0001f);
        assertEquals(0.0f, progress.getNormalizedProgress(), 0.0001f);
    }

    @Test
    @DisplayName("Should update progress along a single segment correctly")
    void testUpdateProgressWithinSegment() {
        progress.updateProgress(25.0f, 50.0f);

        assertEquals(0, progress.getWaypointIndex());
        assertEquals(0.5f, progress.getSegmentRatio(), 0.0001f);
        assertEquals(25.0f, progress.getAccumulatedDistance(), 0.0001f);
        assertEquals(0.5f, progress.getNormalizedProgress(), 0.0001f);
    }

    @Test
    @DisplayName("Should wrap segment ratio and advance waypoint index when crossing segment boundary")
    void testUpdateProgressCrossingSegmentBoundary() {
        progress.updateProgress(60.0f, 50.0f);

        assertEquals(1, progress.getWaypointIndex());
        assertEquals(0.2f, progress.getSegmentRatio(), 0.0001f);
        assertEquals(60.0f, progress.getAccumulatedDistance(), 0.0001f);
        assertEquals(1.2f, progress.getNormalizedProgress(), 0.0001f);
    }

    @Test
    @DisplayName("Should ignore zero-velocity updates")
    void testZeroVelocityUpdateIgnored() {
        progress.updateProgress(25.0f, 50.0f);
        float normalizedBefore = progress.getNormalizedProgress();
        float accumulatedBefore = progress.getAccumulatedDistance();

        // Zero displacement update
        progress.updateProgress(0.0f, 50.0f);

        assertEquals(normalizedBefore, progress.getNormalizedProgress(), 0.0001f);
        assertEquals(accumulatedBefore, progress.getAccumulatedDistance(), 0.0001f);

        // Negative displacement update
        progress.updateProgress(-10.0f, 50.0f);

        assertEquals(normalizedBefore, progress.getNormalizedProgress(), 0.0001f);
        assertEquals(accumulatedBefore, progress.getAccumulatedDistance(), 0.0001f);
    }

    @Test
    @DisplayName("Should clone PathProgress state cleanly")
    void testClone() {
        progress.updateProgress(75.0f, 50.0f); // waypoint 1, ratio 0.5, accumulated 75

        PathProgress cloned = progress.clone();

        assertNotSame(progress, cloned);
        assertEquals(progress.getWaypointIndex(), cloned.getWaypointIndex());
        assertEquals(progress.getSegmentRatio(), cloned.getSegmentRatio(), 0.0001f);
        assertEquals(progress.getAccumulatedDistance(), cloned.getAccumulatedDistance(), 0.0001f);
        assertEquals(progress.getNormalizedProgress(), cloned.getNormalizedProgress(), 0.0001f);

        // Mutating clone should not affect original
        cloned.updateProgress(50.0f, 50.0f);
        assertEquals(1, progress.getWaypointIndex());
        assertEquals(2, cloned.getWaypointIndex());
    }
}
