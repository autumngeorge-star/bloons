package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.descriptors.LevelWaveDescriptor;
import com.hongbao.bloons.descriptors.SpawnDescriptor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for lazy streaming and parsing of level wave descriptors
 * from disk assets, text files, or JSON resources.
 */
public class WaveLoader {

    public enum Format {
        TEXT,
        JSON,
        UNKNOWN
    }

    private final String sourceName;
    private final FileHandle fileHandle;
    private final String rawContent;
    private List<List<SpawnDescriptor>> textLevelDescriptors;
    private List<List<SpawnDescriptor>> jsonLevelDescriptors;
    private boolean initialized = false;

    public WaveLoader(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
        this.sourceName = fileHandle != null ? fileHandle.name() : "unknown";
        this.rawContent = null;
    }

    public WaveLoader(String rawContent, String sourceName) {
        this.fileHandle = null;
        this.sourceName = sourceName != null ? sourceName : "string_source";
        this.rawContent = rawContent;
    }

    /**
     * Creates a WaveLoader from an internal asset path in bloon_queues folder or absolute path.
     */
    public static WaveLoader fromAsset(String fileName) {
        try {
            if (Gdx.files != null) {
                FileHandle handle = Gdx.files.internal("bloon_queues/" + fileName);
                if (handle.exists()) {
                    return new WaveLoader(handle);
                }
                FileHandle directHandle = Gdx.files.internal(fileName);
                if (directHandle.exists()) {
                    return new WaveLoader(directHandle);
                }
                // Fallback check for local files or absolute files
                FileHandle absoluteHandle = Gdx.files.absolute(fileName);
                if (absoluteHandle.exists()) {
                    return new WaveLoader(absoluteHandle);
                }
            }
        } catch (Exception e) {
            System.err.println("WaveLoader: Error accessing file " + fileName + ": " + e.getMessage());
        }
        // Fallback: empty wave loader
        return new WaveLoader((FileHandle) null);
    }

    private synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }
        initialized = true;

        String content = null;
        try {
            if (fileHandle != null && fileHandle.exists()) {
                content = fileHandle.readString();
            } else if (rawContent != null) {
                content = rawContent;
            }
        } catch (Exception e) {
            System.err.println("WaveLoader: Failed to read wave content for " + sourceName + ": " + e.getMessage());
        }

        if (content == null || content.trim().isEmpty()) {
            textLevelDescriptors = new ArrayList<>();
            return;
        }

        String trimmed = content.trim();
        if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            parseJsonContent(trimmed);
        } else {
            parseTextContent(content);
        }
    }

    private void parseTextContent(String content) {
        textLevelDescriptors = new ArrayList<>();
        String[] lines = content.split("\n");
        List<SpawnDescriptor> currentLevel = new ArrayList<>();

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }
            if (line.contains("END")) {
                textLevelDescriptors.add(currentLevel);
                currentLevel = new ArrayList<>();
            } else if (line.contains(" ")) {
                String[] parts = line.split("\\s+");
                if (parts.length == 3) {
                    try {
                        int amount = Integer.parseInt(parts[0]);
                        long delay = Long.parseLong(parts[1]);
                        String bloonTypes = parts[2];
                        currentLevel.add(new SpawnDescriptor(amount, delay, bloonTypes));
                    } catch (NumberFormatException e) {
                        System.err.println("WaveLoader: Corrupted line in " + sourceName + ": " + line);
                    }
                } else {
                    System.err.println("WaveLoader: Malformed line in " + sourceName + ": " + line);
                }
            }
        }

        if (!currentLevel.isEmpty()) {
            textLevelDescriptors.add(currentLevel);
        }
    }

    private void parseJsonContent(String jsonContent) {
        jsonLevelDescriptors = new ArrayList<>();
        try {
            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(jsonContent);

            if (root.isArray()) {
                // Check if array of level objects or array of spawns for level 0
                for (JsonValue levelVal = root.child; levelVal != null; levelVal = levelVal.next) {
                    if (levelVal.has("spawns")) {
                        jsonLevelDescriptors.add(parseSpawnsFromJson(levelVal.get("spawns")));
                    } else if (levelVal.has("amount") && levelVal.has("delay")) {
                        // Single level array of spawns
                        if (jsonLevelDescriptors.isEmpty()) {
                            jsonLevelDescriptors.add(parseSpawnsFromJson(root));
                        }
                        break;
                    }
                }
            } else if (root.isObject()) {
                if (root.has("levels")) {
                    for (JsonValue levelVal = root.get("levels").child; levelVal != null; levelVal = levelVal.next) {
                        jsonLevelDescriptors.add(parseSpawnsFromJson(levelVal.get("spawns")));
                    }
                } else if (root.has("spawns")) {
                    jsonLevelDescriptors.add(parseSpawnsFromJson(root.get("spawns")));
                }
            }
        } catch (Exception e) {
            System.err.println("WaveLoader: Error parsing JSON content in " + sourceName + ": " + e.getMessage());
            jsonLevelDescriptors = new ArrayList<>();
        }
    }

    private List<SpawnDescriptor> parseSpawnsFromJson(JsonValue spawnsVal) {
        List<SpawnDescriptor> spawns = new ArrayList<>();
        if (spawnsVal == null) {
            return spawns;
        }
        for (JsonValue spawn = spawnsVal.child; spawn != null; spawn = spawn.next) {
            try {
                int amount = spawn.getInt("amount", 1);
                long delay = spawn.getLong("delay", 0);
                String types = spawn.getString("types", spawn.getString("bloonTypes", "red"));
                spawns.add(new SpawnDescriptor(amount, delay, types));
            } catch (Exception e) {
                System.err.println("WaveLoader: Failed to parse spawn item in JSON: " + e.getMessage());
            }
        }
        return spawns;
    }

    public boolean hasLevel(int levelIndex) {
        ensureInitialized();
        if (levelIndex < 0) {
            return false;
        }
        List<List<SpawnDescriptor>> descriptors = getActiveDescriptors();
        return levelIndex < descriptors.size();
    }

    public int getTotalLevels() {
        ensureInitialized();
        List<List<SpawnDescriptor>> descriptors = getActiveDescriptors();
        return descriptors.size();
    }

    private List<List<SpawnDescriptor>> getActiveDescriptors() {
        if (jsonLevelDescriptors != null) {
            return jsonLevelDescriptors;
        }
        if (textLevelDescriptors != null) {
            return textLevelDescriptors;
        }
        return new ArrayList<>();
    }

    /**
     * Loads and parses Bloon spawn descriptors strictly for the requested levelIndex.
     */
    public LevelWaveDescriptor loadLevel(int levelIndex) {
        ensureInitialized();
        if (!hasLevel(levelIndex)) {
            System.err.println("WaveLoader: Level " + levelIndex + " not found in " + sourceName + ". Returning empty wave.");
            return LevelWaveDescriptor.empty();
        }

        List<SpawnDescriptor> spawns = getActiveDescriptors().get(levelIndex);
        List<Bloon> bloons = new ArrayList<>();
        List<Long> intervals = new ArrayList<>();
        long timer = 0;

        for (SpawnDescriptor spawn : spawns) {
            for (int i = 0; i < spawn.getAmount(); i++) {
                String[] types = spawn.getBloonTypes().split(",");
                for (String type : types) {
                    try {
                        Bloon bloon = BloonFactory.createBloonOfType(type);
                        bloons.add(bloon);
                        intervals.add(timer);
                        timer += spawn.getDelay();
                    } catch (Exception e) {
                        System.err.println("WaveLoader: Failed to create bloon type '" + type + "' in " + sourceName + ": " + e.getMessage());
                    }
                }
            }
        }

        return new LevelWaveDescriptor(bloons, intervals);
    }

    public String getSourceName() {
        return sourceName;
    }
}
