package com.hongbao.bloons.helpers;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.PathProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class BloonPoppedResultTest {

    @Test
    @DisplayName("Should preserve and clone PathProgress state when popping parent bloon into child bloons")
    void testChildProgressPreservation() {
        Bloon parent = new Bloon(Bloon.Color.BLUE, 2, false, false);
        parent.updatePathProgress(75.0f, 50.0f); // waypoint 1, ratio 0.5, accumulated 75

        PathProgress parentProgress = parent.getPathProgress();
        assertEquals(1, parentProgress.getWaypointIndex());
        assertEquals(0.5f, parentProgress.getSegmentRatio(), 0.0001f);
        assertEquals(75.0f, parentProgress.getAccumulatedDistance(), 0.0001f);

        BloonPoppedResult result = parent.pop(1);

        Set<Bloon> children = result.getBloonsGenerated();
        assertFalse(children.isEmpty());

        for (Bloon child : children) {
            assertNotNull(child.getPathProgress());
            assertNotSame(parentProgress, child.getPathProgress());
            assertEquals(parentProgress.getWaypointIndex(), child.getPathProgress().getWaypointIndex());
            assertEquals(parentProgress.getSegmentRatio(), child.getPathProgress().getSegmentRatio(), 0.0001f);
            assertEquals(parentProgress.getAccumulatedDistance(), child.getPathProgress().getAccumulatedDistance(), 0.0001f);
            assertEquals(parentProgress.getNormalizedProgress(), child.getPathProgress().getNormalizedProgress(), 0.0001f);
        }
    }
}
