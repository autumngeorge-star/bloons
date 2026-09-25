package com.hongbao.bloons.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class RefCountedTextureCache {

    private static RefCountedTextureCache instance;

    public static class TextureEntry {
        private final String fileName;
        private final Texture texture;
        private int refCount;
        private boolean disposed;
        private float unusedTimer;

        public TextureEntry(String fileName, Texture texture) {
            this.fileName = fileName;
            this.texture = texture;
            this.refCount = 1;
            this.disposed = false;
            this.unusedTimer = 0f;
        }

        public String getFileName() {
            return fileName;
        }

        public Texture getTexture() {
            return texture;
        }

        public int getRefCount() {
            return refCount;
        }

        public boolean isDisposed() {
            return disposed;
        }

        public void incrementRef() {
            refCount++;
            unusedTimer = 0f;
        }

        public int decrementRef() {
            refCount--;
            if (refCount < 0) {
                refCount = 0;
            }
            return refCount;
        }

        public float getUnusedTime() {
            return unusedTimer;
        }

        public void addUnusedTime(float delta) {
            this.unusedTimer += delta;
        }

        public void resetUnusedTimer() {
            this.unusedTimer = 0f;
        }

        public synchronized void dispose() {
            if (!disposed) {
                disposed = true;
                if (texture != null) {
                    try {
                        texture.dispose();
                    } catch (Exception e) {
                        // Safe guard against OpenGL context issues or double disposal in GDX
                    }
                }
            }
        }
    }

    private final Map<String, TextureEntry> cache = new HashMap<>();
    private float gracePeriodSeconds = 0f;

    protected RefCountedTextureCache() {
    }

    public static synchronized RefCountedTextureCache getInstance() {
        if (instance == null) {
            instance = new RefCountedTextureCache();
        }
        return instance;
    }

    public static synchronized void setInstance(RefCountedTextureCache newInstance) {
        instance = newInstance;
    }

    public synchronized Texture getTexture(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        TextureEntry entry = cache.get(fileName);
        if (entry != null && !entry.isDisposed()) {
            entry.incrementRef();
            return entry.getTexture();
        }

        // Texture not in cache or was disposed, load new instance
        Texture texture = loadTextureFromFile(fileName);
        if (texture == null) {
            return null;
        }

        entry = new TextureEntry(fileName, texture);
        cache.put(fileName, entry);
        return texture;
    }

    protected Texture loadTextureFromFile(String fileName) {
        return new Texture(Gdx.files.internal(fileName));
    }

    public synchronized int release(String fileName) {
        if (fileName == null) {
            return 0;
        }

        TextureEntry entry = cache.get(fileName);
        if (entry == null) {
            return 0;
        }

        int remaining = entry.decrementRef();
        if (remaining == 0) {
            if (gracePeriodSeconds <= 0) {
                entry.dispose();
                cache.remove(fileName);
            }
        }
        return remaining;
    }

    public synchronized int release(Texture texture) {
        if (texture == null) {
            return 0;
        }

        for (Map.Entry<String, TextureEntry> entry : cache.entrySet()) {
            if (entry.getValue().getTexture() == texture) {
                return release(entry.getKey());
            }
        }
        return 0;
    }

    public synchronized int getRefCount(String fileName) {
        if (fileName == null) {
            return 0;
        }
        TextureEntry entry = cache.get(fileName);
        if (entry == null || entry.isDisposed()) {
            return 0;
        }
        return entry.getRefCount();
    }

    public synchronized boolean isLoaded(String fileName) {
        if (fileName == null) {
            return false;
        }
        TextureEntry entry = cache.get(fileName);
        return entry != null && !entry.isDisposed() && entry.getRefCount() > 0;
    }

    public synchronized int getLoadedTextureCount() {
        int count = 0;
        for (TextureEntry entry : cache.values()) {
            if (!entry.isDisposed() && entry.getRefCount() > 0) {
                count++;
            }
        }
        return count;
    }

    public synchronized void update(float delta) {
        if (gracePeriodSeconds <= 0) {
            return;
        }

        cache.entrySet().removeIf(e -> {
            TextureEntry entry = e.getValue();
            if (entry.getRefCount() <= 0) {
                entry.addUnusedTime(delta);
                if (entry.getUnusedTime() >= gracePeriodSeconds) {
                    entry.dispose();
                    return true;
                }
            }
            return false;
        });
    }

    public synchronized void flushUnused() {
        cache.entrySet().removeIf(e -> {
            TextureEntry entry = e.getValue();
            if (entry.getRefCount() <= 0) {
                entry.dispose();
                return true;
            }
            return false;
        });
    }

    public synchronized void clear() {
        for (TextureEntry entry : cache.values()) {
            entry.dispose();
        }
        cache.clear();
    }

    public float getGracePeriodSeconds() {
        return gracePeriodSeconds;
    }

    public void setGracePeriodSeconds(float gracePeriodSeconds) {
        this.gracePeriodSeconds = gracePeriodSeconds;
    }
}
