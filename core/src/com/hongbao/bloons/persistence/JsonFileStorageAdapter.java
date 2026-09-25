package com.hongbao.bloons.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Storage adapter implementation backed by JSON file persistence.
 */
public class JsonFileStorageAdapter implements PersistenceService {

    public static final String DEFAULT_FILE_PATH = "player_data.json";

    private final String filePath;
    private final File targetFile;
    private final FileHandle fileHandle;
    private final Json json = new Json();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JsonFileStorageAdapter() {
        this(DEFAULT_FILE_PATH);
    }

    public JsonFileStorageAdapter(String filePath) {
        this.filePath = filePath;
        this.targetFile = new File(filePath);
        this.fileHandle = null;
    }

    public JsonFileStorageAdapter(File file) {
        this.filePath = file.getPath();
        this.targetFile = file;
        this.fileHandle = null;
    }

    public JsonFileStorageAdapter(FileHandle fileHandle) {
        this.filePath = fileHandle.path();
        this.targetFile = null;
        this.fileHandle = fileHandle;
    }

    private boolean fileExists() {
        if (fileHandle != null) {
            return fileHandle.exists();
        }
        if (Gdx.files != null) {
            return Gdx.files.local(filePath).exists();
        }
        return targetFile != null && targetFile.exists();
    }

    private String readString() throws Exception {
        if (fileHandle != null) {
            return fileHandle.readString(StandardCharsets.UTF_8.name());
        }
        if (Gdx.files != null && Gdx.files.local(filePath).exists()) {
            return Gdx.files.local(filePath).readString(StandardCharsets.UTF_8.name());
        }
        if (targetFile != null && targetFile.exists()) {
            byte[] bytes = Files.readAllBytes(targetFile.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        }
        throw new PersistenceException("File does not exist: " + filePath);
    }

    private void writeString(String content) throws Exception {
        if (fileHandle != null) {
            fileHandle.writeString(content, false, StandardCharsets.UTF_8.name());
            return;
        }
        if (Gdx.files != null) {
            Gdx.files.local(filePath).writeString(content, false, StandardCharsets.UTF_8.name());
            return;
        }
        if (targetFile != null) {
            File parent = targetFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(targetFile, false)) {
                writer.write(content);
            }
            return;
        }
        throw new PersistenceException("No valid file handle or path to write to: " + filePath);
    }

    private void deleteFile() throws Exception {
        if (fileHandle != null && fileHandle.exists()) {
            fileHandle.delete();
            return;
        }
        if (Gdx.files != null && Gdx.files.local(filePath).exists()) {
            Gdx.files.local(filePath).delete();
            return;
        }
        if (targetFile != null && targetFile.exists()) {
            if (!targetFile.delete()) {
                throw new PersistenceException("Failed to delete file: " + targetFile.getAbsolutePath());
            }
        }
    }

    @Override
    public PlayerData loadData() {
        try {
            if (!fileExists()) {
                return new PlayerData();
            }
            String jsonContent = readString();
            if (jsonContent == null || jsonContent.trim().isEmpty()) {
                return new PlayerData();
            }
            PlayerData data = json.fromJson(PlayerData.class, jsonContent);
            return data != null ? data : new PlayerData();
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PersistenceException("Failed to load JSON file persistence from: " + filePath, e);
        }
    }

    @Override
    public void saveData(PlayerData data) {
        if (data == null) {
            throw new PersistenceException("Cannot save null PlayerData");
        }
        try {
            String jsonContent = json.toJson(data);
            writeString(jsonContent);
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PersistenceException("Failed to save JSON file persistence to: " + filePath, e);
        }
    }

    @Override
    public void saveDataAsync(PlayerData data) {
        executorService.submit(() -> {
            try {
                saveData(data);
            } catch (Exception e) {
                if (Gdx.app != null) {
                    Gdx.app.error("JsonFileStorageAdapter", "Async save failed", e);
                }
            }
        });
    }

    @Override
    public void resetData() {
        try {
            if (fileExists()) {
                deleteFile();
            }
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PersistenceException("Failed to reset JSON file persistence at: " + filePath, e);
        }
    }

    @Override
    public PersistenceService getActiveAdapter() {
        return this;
    }
}
