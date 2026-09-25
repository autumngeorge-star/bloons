package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveManager {

    public static final String SAVE_FILE_NAME = "gamestate.json";
    public static final String BACKUP_FILE_NAME = "gamestate.json.bak";
    public static final String TEMP_FILE_NAME = "gamestate.json.tmp";

    private static FileHandle getFileHandle(String fileName) {
        if (Gdx.files != null) {
            return Gdx.files.local(fileName);
        }
        return new FileHandle(fileName);
    }

    private static void logError(String message, Throwable t) {
        if (Gdx.app != null) {
            if (t != null) {
                Gdx.app.error("SaveManager", message, t);
            } else {
                Gdx.app.error("SaveManager", message);
            }
        } else {
            System.err.println("SaveManager: " + message);
            if (t != null) {
                t.printStackTrace();
            }
        }
    }

    public static synchronized boolean saveGameState(GameState state) {
        if (state == null) {
            return false;
        }

        try {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            json.setUsePrototypes(false);
            String jsonString = json.prettyPrint(state);

            FileHandle saveFile = getFileHandle(SAVE_FILE_NAME);
            FileHandle backupFile = getFileHandle(BACKUP_FILE_NAME);
            FileHandle tempFile = getFileHandle(TEMP_FILE_NAME);

            // 1. Create a backup copy of existing save file
            if (saveFile.exists()) {
                try {
                    saveFile.copyTo(backupFile);
                } catch (Exception e) {
                    logError("Failed to create backup save file before overwrite", e);
                }
            }

            // 2. Write JSON to temporary file (atomic write preparation)
            tempFile.writeString(jsonString, false, "UTF-8");

            // 3. Atomically replace save file with temp file
            tempFile.moveTo(saveFile);

            return true;
        } catch (Exception e) {
            logError("Error saving game state", e);
            return false;
        }
    }

    public static synchronized GameState loadGameState() {
        FileHandle saveFile = getFileHandle(SAVE_FILE_NAME);
        FileHandle backupFile = getFileHandle(BACKUP_FILE_NAME);

        // Try primary save file first
        if (saveFile.exists()) {
            try {
                GameState state = parseAndValidate(saveFile);
                if (state != null) {
                    return state;
                }
                logError("Primary save file corrupted or invalid. Attempting backup restore...", null);
            } catch (Exception e) {
                logError("Failed reading primary save file. Attempting backup restore...", e);
            }
        }

        // Try backup file if primary save failed or is missing
        if (backupFile.exists()) {
            try {
                GameState state = parseAndValidate(backupFile);
                if (state != null) {
                    logError("Successfully restored game state from backup file.", null);
                    return state;
                }
            } catch (Exception e) {
                logError("Failed reading backup save file.", e);
            }
        }

        // Safe fallback to default initial game state
        logError("Corrupted or missing JSON save files trigger automated fallback to default state.", null);
        return new GameState();
    }

    private static GameState parseAndValidate(FileHandle file) {
        try {
            String content = file.readString("UTF-8");
            if (content == null || content.trim().isEmpty()) {
                return null;
            }
            Json json = new Json();
            json.setIgnoreUnknownFields(true);
            GameState state = json.fromJson(GameState.class, content);
            if (state != null && state.isValid()) {
                return state;
            }
        } catch (Exception e) {
            logError("Error parsing JSON save file: " + file.name(), e);
        }
        return null;
    }
}
