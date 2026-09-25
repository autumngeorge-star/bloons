package com.hongbao.bloons.registry;

import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Application-scoped wave registry for registering and providing {@link BloonQueue} templates and instances by key.
 */
public class WaveRegistry {

    public static final String DEFAULT_KEY = "default";
    public static final String HELLA_BLOONS_KEY = "hella_bloons";

    private static final WaveRegistry INSTANCE = new WaveRegistry();

    public static WaveRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<String, Supplier<BloonQueue>> registry = new ConcurrentHashMap<>();
    private String activeProfileKey = DEFAULT_KEY;

    public WaveRegistry() {
        registerDefaultQueues();
    }

    public void registerDefaultQueues() {
        registerWaveQueue(DEFAULT_KEY, () -> BloonFactory.createBloonQueueFromFile("default.txt"));
        registerWaveQueue("default.txt", () -> BloonFactory.createBloonQueueFromFile("default.txt"));
        registerWaveQueue(HELLA_BLOONS_KEY, () -> BloonFactory.createBloonQueueFromFile("hella_bloons.txt"));
        registerWaveQueue("hella_bloons.txt", () -> BloonFactory.createBloonQueueFromFile("hella_bloons.txt"));
    }

    public void registerWaveQueue(String key, Supplier<BloonQueue> supplier) {
        if (key != null && supplier != null) {
            registry.put(key, supplier);
        }
    }

    public void registerWaveQueue(String key, BloonQueue queue) {
        if (key != null && queue != null) {
            registry.put(key, () -> queue);
        }
    }

    public BloonQueue getWaveQueue(String key) {
        Supplier<BloonQueue> supplier = registry.get(key);
        if (supplier == null) {
            supplier = registry.get(DEFAULT_KEY);
        }
        if (supplier == null) {
            throw new IllegalArgumentException("No wave queue registered for key: " + key);
        }
        return supplier.get();
    }

    public boolean hasWaveQueue(String key) {
        return key != null && registry.containsKey(key);
    }

    public void setActiveProfileKey(String key) {
        if (key != null) {
            this.activeProfileKey = key;
        }
    }

    public String getActiveProfileKey() {
        return activeProfileKey;
    }

    public BloonQueue getActiveWaveQueue() {
        return getWaveQueue(activeProfileKey);
    }

    public void clear() {
        registry.clear();
    }
}
