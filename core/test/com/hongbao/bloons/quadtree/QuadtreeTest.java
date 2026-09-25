package com.hongbao.bloons.quadtree;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QuadtreeTest {

    private Quadtree quadtree;

    @Before
    public void setUp() {
        // Stage bounds: 0, 0, 1800, 900
        quadtree = new Quadtree(0, 0, 1800, 900, 4, 6);
    }

    @Test
    public void testInsertionAndNodeSplitting() {
        assertFalse(quadtree.getRoot().hasSplit());

        // Insert 5 bloons into the top-left quadrant
        List<BloonActor> bloons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            BloonActor actor = new BloonActor(bloon, 200 + i * 10, 600 + i * 10, null);
            bloons.add(actor);
            quadtree.insert(actor);
        }

        assertTrue("Root node should split when capacity (4) is exceeded", quadtree.getRoot().hasSplit());
        assertNotNull(quadtree.getRoot().getChildren());
        assertEquals(4, quadtree.getRoot().getChildren().length);
    }

    @Test
    public void testCandidateFiltering() {
        Bloon bloonNear = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor nearActor = new BloonActor(bloonNear, 100, 100, null);

        Bloon bloonFar = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor farActor = new BloonActor(bloonFar, 1500, 800, null);

        quadtree.insert(nearActor);
        quadtree.insert(farActor);

        List<BloonActor> candidates = new ArrayList<>();
        // Query near (100, 100) with a 50x50 box
        quadtree.query(80, 80, 50, 50, candidates);

        assertEquals("Query should return only the nearby bloon", 1, candidates.size());
        assertTrue("Candidate should be nearActor", candidates.contains(nearActor));
        assertFalse("Candidate list must filter out farActor", candidates.contains(farActor));
    }

    @Test
    public void testObjectPoolRecycling() {
        QuadtreeNodePool pool = quadtree.getPool();
        int initialFreeCount = pool.getFreeCount();

        List<BloonActor> bloons = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            BloonActor actor = new BloonActor(bloon, (i * 30) % 1700, (i * 20) % 800, null);
            bloons.add(actor);
        }

        quadtree.rebuild(bloons);
        assertTrue("Quadtree root should have split for 50 bloons", quadtree.getRoot().hasSplit());

        // Clear quadtree
        quadtree.clear();
        assertEquals("Pool should recover free nodes after clear", initialFreeCount, pool.getFreeCount());
        assertFalse("New root after clear should be fresh and unsplit", quadtree.getRoot().hasSplit());
    }

    @Test
    public void testMaxDepthCap() {
        // Insert 20 bloons at the EXACT same coordinate (200, 200)
        List<BloonActor> bloons = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            BloonActor actor = new BloonActor(bloon, 200, 200, null);
            bloons.add(actor);
            quadtree.insert(actor);
        }

        // Verify that depth never exceeds maxDepth (6)
        checkMaxDepth(quadtree.getRoot(), 6);
    }

    private void checkMaxDepth(QuadtreeNode node, int maxAllowedDepth) {
        if (node == null) return;
        assertTrue("Node depth (" + node.getDepth() + ") should not exceed maxAllowedDepth (" + maxAllowedDepth + ")",
                node.getDepth() <= maxAllowedDepth);
        if (node.hasSplit()) {
            for (QuadtreeNode child : node.getChildren()) {
                if (child != null) {
                    checkMaxDepth(child, maxAllowedDepth);
                }
            }
        }
    }

    @Test
    public void testBruteForceEquivalence() {
        List<BloonActor> bloons = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            float x = (float) (Math.random() * 1600);
            float y = (float) (Math.random() * 800);
            BloonActor actor = new BloonActor(bloon, x, y, null);
            bloons.add(actor);
        }

        quadtree.rebuild(bloons);

        // Query a random box
        float qx = 400, qy = 400, qw = 200, qh = 200;

        List<BloonActor> quadtreeCandidates = new ArrayList<>();
        quadtree.query(qx, qy, qw, qh, quadtreeCandidates);

        // Brute force check
        List<BloonActor> bruteForceCandidates = new ArrayList<>();
        for (BloonActor bloon : bloons) {
            float bx = bloon.getCenterX() - bloon.getCollisionRadius();
            float by = bloon.getCenterY() - bloon.getCollisionRadius();
            float bw = 2f * bloon.getCollisionRadius();
            float bh = 2f * bloon.getCollisionRadius();

            if (QuadtreeNode.boundingBoxIntersects(bx, by, bw, bh, qx, qy, qw, qh)) {
                bruteForceCandidates.add(bloon);
            }
        }

        assertEquals("Quadtree query must return same number of candidates as brute force",
                bruteForceCandidates.size(), quadtreeCandidates.size());
        assertTrue("Quadtree query must contain all brute force candidates",
                quadtreeCandidates.containsAll(bruteForceCandidates));
    }

    @Test
    public void testEmptyQuadtreeQuery() {
        List<BloonActor> candidates = new ArrayList<>();
        quadtree.query(0, 0, 500, 500, candidates);
        assertTrue("Querying empty quadtree should return 0 candidates", candidates.isEmpty());
    }

    @Test
    public void testOffscreenBloonInsertion() {
        // Bloon spawning offscreen at x = -25
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actor = new BloonActor(bloon, -25, 425, null);
        quadtree.insert(actor);

        List<BloonActor> candidates = new ArrayList<>();
        quadtree.query(-50, 400, 100, 100, candidates);
        assertEquals("Offscreen bloon should be queryable", 1, candidates.size());
        assertTrue("Candidate should be the offscreen bloon", candidates.contains(actor));
    }

    @Test
    public void testRepeatedRebuildStability() {
        List<BloonActor> bloons = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
            bloons.add(new BloonActor(bloon, i * 50, i * 25, null));
        }

        int initialFreeCount = quadtree.getPool().getFreeCount();
        for (int frame = 0; frame < 100; frame++) {
            quadtree.rebuild(bloons);
        }

        quadtree.clear();
        assertEquals("Pool free count should remain fully restored after 100 rebuilds",
                initialFreeCount, quadtree.getPool().getFreeCount());
    }
}
