package com.hongbao.bloons.events;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Event published when a new level starts, containing level index and associated metadata.
 */
public class LevelStartedEvent {

    private final int level;
    private final Map<String, Object> metadata;

    public LevelStartedEvent(int level) {
        this(level, Collections.emptyMap());
    }

    public LevelStartedEvent(int level, Map<String, Object> metadata) {
        this.level = level;
        Map<String, Object> meta = new HashMap<>();
        if (metadata != null) {
            meta.putAll(metadata);
        }
        meta.put("level", level);
        this.metadata = Collections.unmodifiableMap(meta);
    }

    public int getLevel() {
        return level;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }
}
