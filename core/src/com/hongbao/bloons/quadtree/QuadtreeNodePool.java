package com.hongbao.bloons.quadtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Object pool for QuadtreeNode instances to prevent runtime memory allocations and GC pauses.
 */
public class QuadtreeNodePool {

    private final List<QuadtreeNode> freeNodes;
    private final int maxCapacity;
    private final int maxDepth;

    public QuadtreeNodePool(int initialCapacity, int maxCapacity, int maxDepth) {
        this.maxCapacity = maxCapacity;
        this.maxDepth = maxDepth;
        this.freeNodes = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            freeNodes.add(new QuadtreeNode(this, maxCapacity, maxDepth));
        }
    }

    public QuadtreeNode obtain(float x, float y, float width, float height, int depth) {
        QuadtreeNode node;
        if (!freeNodes.isEmpty()) {
            node = freeNodes.remove(freeNodes.size() - 1);
        } else {
            node = new QuadtreeNode(this, maxCapacity, maxDepth);
        }
        node.init(x, y, width, height, depth);
        return node;
    }

    public void free(QuadtreeNode node) {
        if (node != null) {
            node.reset();
            freeNodes.add(node);
        }
    }

    public int getFreeCount() {
        return freeNodes.size();
    }
}
