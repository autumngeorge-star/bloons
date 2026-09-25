package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class LevelSaveService {

    public static final String SAVE_FILE_NAME = "save_data.json";
    public static final int CURRENT_SCHEMA_VERSION = 1;

    private static LevelSaveService instance;

    private int version;
    private int maxUnlockedLevel;

    public LevelSaveService() {
        this.version = CURRENT_SCHEMA_VERSION;
        this.maxUnlockedLevel = 1;
    }

    public static synchronized LevelSaveService getInstance() {
        if (instance == null) {
            instance = new LevelSaveService();
        }
        return instance;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMaxUnlockedLevel() {
        return maxUnlockedLevel;
    }

    public void setMaxUnlockedLevel(int maxUnlockedLevel) {
        if (maxUnlockedLevel < 1) {
            maxUnlockedLevel = 1;
        }
        this.maxUnlockedLevel = maxUnlockedLevel;
    }

    public void unlockLevel(int level) {
        if (level > this.maxUnlockedLevel) {
            this.maxUnlockedLevel = level;
            save();
        }
    }

    public synchronized void load() {
        try {
            String content = null;
            if (Gdx.files != null) {
                FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
                if (file.exists()) {
                    content = file.readString();
                }
            } else {
                File file = new File(SAVE_FILE_NAME);
                if (file.exists()) {
                    StringBuilder sb = new StringBuilder();
                    try (FileReader reader = new FileReader(file)) {
                        char[] buf = new char[1024];
                        int num;
                        while ((num = reader.read(buf)) != -1) {
                            sb.append(buf, 0, num);
                        }
                    }
                    content = sb.toString();
                }
            }

            if (content != null && !content.trim().isEmpty()) {
                JsonValue jsonValue = new JsonReader().parse(content);
                if (jsonValue.has("version")) {
                    this.version = jsonValue.getInt("version");
                } else {
                    this.version = CURRENT_SCHEMA_VERSION;
                }

                if (jsonValue.has("maxUnlockedLevel")) {
                    this.maxUnlockedLevel = Math.max(1, jsonValue.getInt("maxUnlockedLevel"));
                } else {
                    this.maxUnlockedLevel = 1;
                }
                return;
            }
        } catch (Exception e) {
            System.err.println("Failed to load save file, creating default: " + e.getMessage());
        }

        this.version = CURRENT_SCHEMA_VERSION;
        this.maxUnlockedLevel = 1;
        save();
    }

    public synchronized void save() {
        try {
            JsonValue root = new JsonValue(JsonValue.ValueType.object);
            root.addChild("version", new JsonValue(this.version));
            root.addChild("maxUnlockedLevel", new JsonValue(this.maxUnlockedLevel));

            String jsonString = root.toJson(JsonWriter.OutputType.json);

            if (Gdx.files != null) {
                FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
                file.writeString(jsonString, false);
            } else {
                File file = new File(SAVE_FILE_NAME);
                try (FileWriter writer = new FileWriter(file, false)) {
                    writer.write(jsonString);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to save data to " + SAVE_FILE_NAME + ": " + e.getMessage());
        }
    }

    public void reset() {
        this.version = CURRENT_SCHEMA_VERSION;
        this.maxUnlockedLevel = 1;
        save();
    }
}
