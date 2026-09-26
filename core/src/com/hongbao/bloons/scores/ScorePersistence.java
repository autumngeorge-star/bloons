package com.hongbao.bloons.scores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class ScorePersistence {
    public static final String DEFAULT_FILE_NAME = "scores.json";
    private final FileHandle fileHandle;

    public ScorePersistence() {
        this(null);
    }

    public ScorePersistence(FileHandle customFileHandle) {
        if (customFileHandle != null) {
            this.fileHandle = customFileHandle;
        } else if (Gdx.files != null) {
            this.fileHandle = Gdx.files.local(DEFAULT_FILE_NAME);
        } else {
            this.fileHandle = null;
        }
    }

    public LeaderboardData load() {
        if (fileHandle == null || !fileHandle.exists()) {
            return new LeaderboardData();
        }

        try {
            Json json = new Json();
            json.setIgnoreUnknownFields(true);
            LeaderboardData data = json.fromJson(LeaderboardData.class, fileHandle);
            if (data == null) {
                return new LeaderboardData();
            }
            data.sortAndTrim();
            return data;
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("ScorePersistence", "Error loading scores.json. Falling back to clean state.", e);
            }
            return new LeaderboardData();
        }
    }

    public boolean save(LeaderboardData data) {
        if (fileHandle == null || data == null) {
            return false;
        }

        try {
            data.sortAndTrim(100);
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            String jsonString = json.prettyPrint(data);
            fileHandle.writeString(jsonString, false);
            return true;
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("ScorePersistence", "Error saving scores.json.", e);
            }
            return false;
        }
    }

    public LeaderboardData loadFromRawJson(String rawJson) {
        if (rawJson == null || rawJson.trim().isEmpty()) {
            return new LeaderboardData();
        }
        try {
            Json json = new Json();
            json.setIgnoreUnknownFields(true);
            LeaderboardData data = json.fromJson(LeaderboardData.class, rawJson);
            if (data == null) {
                return new LeaderboardData();
            }
            data.sortAndTrim();
            return data;
        } catch (Exception e) {
            return new LeaderboardData();
        }
    }
}
