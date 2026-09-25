package com.hongbao.bloons.quadtree;

import com.hongbao.bloons.actors.BloonActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Hierarchical Quadtree Node that holds spatial bounds and bloon entity references.
 */
public class QuadtreeNode {

    private float x;
    private float y;
    private float width;
    private float height;
    private int depth;

    private final int maxCapacity;
    private final int maxDepth;
    private final QuadtreeNodePool pool;

    private final List<BloonActor> bloons;
    private final QuadtreeNode[] children;
    private boolean hasSplit;

    public QuadtreeNode(QuadtreeNodePool pool, int maxCapacity, int maxDepth) {
        this.pool = pool;
        this.maxCapacity = maxCapacity;
        this.maxDepth = maxDepth;
        this.bloons = new ArrayList<>(maxCapacity + 1);
        this.children = new QuadtreeNode[4];
        this.hasSplit = false;
    }

    public void init(float x, float y, float width, float height, int depth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.bloons.clear();
        this.hasSplit = false;
        for (int i = 0; i < 4; i++) {
            this.children[i] = null;
        }
    }

    public void reset() {
        bloons.clear();
        if (hasSplit) {
            for (int i = 0; i < 4; i++) {
                if (children[i] != null) {
                    pool.free(children[i]);
                    children[i] = null;
                }
            }
            hasSplit = false;
        }
    }

    private void split() {
        hasSplit = true;
        float subWidth = width / 2f;
        float subHeight = height / 2f;
        float midX = x + subWidth;
        float midY = y + subHeight;

        children[0] = pool.obtain(x, midY, subWidth, subHeight, depth + 1);     // NW (Top-Left)
        children[1] = pool.obtain(midX, midY, subWidth, subHeight, depth + 1);  // NE (Top-Right)
        children[2] = pool.obtain(x, y, subWidth, subHeight, depth + 1);        // SW (Bottom-Left)
        children[3] = pool.obtain(midX, y, subWidth, subHeight, depth + 1);     // SE (Bottom-Right)
    }

    private int getQuadrantIndex(float bx, float by, float bw, float bh) {
        float midX = x + width / 2f;
        float midY = y + height / 2f;

        boolean topHalf = (by >= midY) && (by + bh <= y + height);
        boolean bottomHalf = (by >= y) && (by + bh <= midY);
        boolean leftHalf = (bx >= x) && (bx + bw <= midX);
        boolean rightHalf = (bx >= midX) && (bx + bw <= x + width);

        if (topHalf) {
            if (leftHalf) return 0;  // NW
            if (rightHalf) return 1; // NE
        } else if (bottomHalf) {
            if (leftHalf) return 2;  // SW
            if (rightHalf) return 3; // SE
        }
        return -1; // Spans quadrant boundary or outside quadrant bounds
    }

    public void insert(BloonActor bloonActor) {
        float bx = bloonActor.getCenterX() - bloonActor.getCollisionRadius();
        float by = bloonActor.getCenterY() - bloonActor.getCollisionRadius();
        float bw = 2f * bloonActor.getCollisionRadius();
        float bh = 2f * bloonActor.getCollisionRadius();

        if (hasSplit) {
            int index = getQuadrantIndex(bx, by, bw, bh);
            if (index != -1) {
                children[index].insert(bloonActor);
                return;
            }
        }

        bloons.add(bloonActor);

        if (!hasSplit && bloons.size() > maxCapacity && depth < maxDepth) {
            split();
            for (int i = bloons.size() - 1; i >= 0; i--) {
                BloonActor actor = bloons.get(i);
                float abx = actor.getCenterX() - actor.getCollisionRadius();
                float aby = actor.getCenterY() - actor.getCollisionRadius();
                float abw = 2f * actor.getCollisionRadius();
                float abh = 2f * actor.getCollisionRadius();

                int index = getQuadrantIndex(abx, aby, abw, abh);
                if (index != -1) {
                    bloons.remove(i);
                    children[index].insert(actor);
                }
            }
        }
    }

    public void query(float qx, float qy, float qw, float qh, List<BloonActor> candidateList) {
        if (!intersects(qx, qy, qw, qh)) {
            return;
        }

        for (int i = 0; i < bloons.size(); i++) {
            BloonActor bloonActor = bloons.get(i);
            float bx = bloonActor.getCenterX() - bloonActor.getCollisionRadius();
            float by = bloonActor.getCenterY() - bloonActor.getCollisionRadius();
            float bw = 2f * bloonActor.getCollisionRadius();
            float bh = 2f * bloonActor.getCollisionRadius();

            if (boundingBoxIntersects(bx, by, bw, bh, qx, qy, qw, qh)) {
                candidateList.add(bloonActor);
            }
        }

        if (hasSplit) {
            for (int i = 0; i < 4; i++) {
                if (children[i] != null) {
                    children[i].query(qx, qy, qw, qh, candidateList);
                }
            }
        }
    }

    public boolean intersects(float qx, float qy, float qw, float qh) {
        return !(qx > x + width || qx + qw < x || qy > y + height || qy + qh < y);
    }

    public static boolean boundingBoxIntersects(float bMinX, float bMinY, float bWidth, float bHeight,
                                                float qMinX, float qMinY, float qWidth, float qHeight) {
        return !(qMinX > bMinX + bWidth || qMinX + qWidth < bMinX ||
                 qMinY > bMinY + bHeight || qMinY + qHeight < bMinY);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getDepth() { return depth; }
    public List<BloonActor> getBloons() { return bloons; }
    public QuadtreeNode[] getChildren() { return children; }
    public boolean hasSplit() { return hasSplit; }
}
