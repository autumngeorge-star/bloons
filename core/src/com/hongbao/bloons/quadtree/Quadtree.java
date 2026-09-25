package com.hongbao.bloons.quadtree;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;

import java.util.Collection;
import java.util.List;

/**
 * Main Quadtree manager that manages the spatial partitioning tree over stage coordinates.
 */
public class Quadtree {

    public static final int DEFAULT_CAPACITY = 4;
    public static final int DEFAULT_MAX_DEPTH = 8;
    public static final int DEFAULT_POOL_CAPACITY = 512;

    private float x;
    private float y;
    private float width;
    private float height;

    private final int capacity;
    private final int maxDepth;
    private final QuadtreeNodePool pool;
    private QuadtreeNode root;

    public Quadtree(float x, float y, float width, float height) {
        this(x, y, width, height, DEFAULT_CAPACITY, DEFAULT_MAX_DEPTH);
    }

    public Quadtree(float x, float y, float width, float height, int capacity, int maxDepth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
        this.maxDepth = maxDepth;
        this.pool = new QuadtreeNodePool(DEFAULT_POOL_CAPACITY, capacity, maxDepth);
        this.root = pool.obtain(x, y, width, height, 0);
    }

    public void clear() {
        if (root != null) {
            pool.free(root);
        }
        root = pool.obtain(x, y, width, height, 0);
    }

    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        clear();
    }

    public void insert(BloonActor bloonActor) {
        if (bloonActor != null) {
            root.insert(bloonActor);
        }
    }

    public void rebuild(Collection<BloonActor> bloons) {
        clear();
        if (bloons != null) {
            for (BloonActor bloon : bloons) {
                insert(bloon);
            }
        }
    }

    public void query(float qx, float qy, float qw, float qh, List<BloonActor> result) {
        if (root != null) {
            root.query(qx, qy, qw, qh, result);
        }
    }

    public void query(BulletActor bulletActor, List<BloonActor> result) {
        float radius = bulletActor.getCollisionRadius();
        float qx = bulletActor.getCenterX() - radius;
        float qy = bulletActor.getCenterY() - radius;
        float qw = 2f * radius;
        float qh = 2f * radius;
        query(qx, qy, qw, qh, result);
    }

    public QuadtreeNode getRoot() {
        return root;
    }

    public QuadtreeNodePool getPool() {
        return pool;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
