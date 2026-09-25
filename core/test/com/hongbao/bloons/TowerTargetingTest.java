package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.MapFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TowerTargetingTest {

    @Test
    public void testLeadingBloonTargetSelectionAcrossSegments() {
        Map map = MapFactory.createMapWithTurn(null);

        // Bloon A further along path (segment 2 at x=300, y=575)
        Bloon bloonA = BloonFactory.createBloonOfType("red", 1);
        Map.PathProgressResult resA = map.calculatePathProgress(300, 575);
        bloonA.updateProgress(resA.getSegmentIndex(), resA.getOffsetAlongSegment(), resA.getTotalProgress());

        // Bloon B behind on segment 0 (x=100, y=425)
        Bloon bloonB = BloonFactory.createBloonOfType("yellow", 4);
        Map.PathProgressResult resB = map.calculatePathProgress(100, 425);
        bloonB.updateProgress(resB.getSegmentIndex(), resB.getOffsetAlongSegment(), resB.getTotalProgress());

        assertTrue("Leading bloon A should have higher progress than bloon B", bloonA.getPathProgress() > bloonB.getPathProgress());
    }

    @Test
    public void testTargetingPriorityWhenParentPopsToChild() {
        Map map = MapFactory.createMapWithTurn(null);

        // Bloon 1 (Parent) ahead at (225, 500)
        Bloon parentAhead = BloonFactory.createBloonOfType("blue", 2);
        Map.PathProgressResult resAhead = map.calculatePathProgress(225, 500);
        parentAhead.updateProgress(resAhead.getSegmentIndex(), resAhead.getOffsetAlongSegment(), resAhead.getTotalProgress());

        // Bloon 2 behind at (100, 425)
        Bloon bloonBehind = BloonFactory.createBloonOfType("red", 1);
        Map.PathProgressResult resBehind = map.calculatePathProgress(100, 425);
        bloonBehind.updateProgress(resBehind.getSegmentIndex(), resBehind.getOffsetAlongSegment(), resBehind.getTotalProgress());

        assertTrue(parentAhead.getPathProgress() > bloonBehind.getPathProgress());

        // Pop parentAhead into child bloon
        BloonPoppedResult poppedResult = parentAhead.pop(1);
        Bloon childAhead = poppedResult.getBloonsGenerated().iterator().next();
        Map.PathProgressResult resChild = map.calculatePathProgress(225, 500);
        childAhead.updateProgress(resChild.getSegmentIndex(), resChild.getOffsetAlongSegment(), resChild.getTotalProgress());

        // Verify that child bloon maintains target priority over bloon behind
        assertTrue("Child bloon spawned from popped parent must maintain higher progress than bloon behind", childAhead.getPathProgress() > bloonBehind.getPathProgress());
    }
}
