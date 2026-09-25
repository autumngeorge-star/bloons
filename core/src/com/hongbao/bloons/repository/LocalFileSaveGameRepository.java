package com.hongbao.bloons.repository;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.hongbao.bloons.dto.GirlPlacementData;
import com.hongbao.bloons.dto.MapStateData;
import com.hongbao.bloons.dto.PlayerStateData;
import com.hongbao.bloons.dto.SaveProfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSaveGameRepository implements SaveGameRepository {

    public static final String DEFAULT_SAVE_DIR = "saves/";
    private final String saveDir;
    private final Json json;

    public LocalFileSaveGameRepository() {
        this(DEFAULT_SAVE_DIR);
    }

    public LocalFileSaveGameRepository(String saveDir) {
        this.saveDir = saveDir.endsWith("/") ? saveDir : saveDir + "/";
        this.json = new Json();
        this.json.setOutputType(JsonWriter.OutputType.json);
        this.json.setIgnoreUnknownFields(true);
    }

    private String getSlotFileName(String slotId) {
        String cleanSlot = slotId.replaceAll("[^a-zA-Z0-9_-]", "_");
        return saveDir + cleanSlot + ".json";
    }

    @Override
    public void save(String slotId, SaveProfile profile) {
        if (profile == null) {
            return;
        }

        profile.setSlotId(slotId);
        profile.setTimestamp(System.currentTimeMillis());

        try {
            FileHandle dirHandle = Gdx.files.local(saveDir);
            if (!dirHandle.exists()) {
                dirHandle.mkdirs();
            }

            String targetPath = getSlotFileName(slotId);
            String tmpPath = targetPath + ".tmp";

            FileHandle tmpHandle = Gdx.files.local(tmpPath);
            FileHandle targetHandle = Gdx.files.local(targetPath);

            String formattedJson = json.prettyPrint(profile);
            tmpHandle.writeString(formattedJson, false, "UTF-8");

            // Atomic file operation: move .tmp to target file
            File tmpFile = tmpHandle.file();
            File targetFile = targetHandle.file();

            try {
                Files.move(
                        tmpFile.toPath(),
                        targetFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (IOException e) {
                // Fallback if filesystem does not support ATOMIC_MOVE
                tmpHandle.moveTo(targetHandle);
            }
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("LocalFileSaveGameRepository", "Error saving game slot " + slotId, e);
            } else {
                System.err.println("Error saving game slot " + slotId + ": " + e.getMessage());
            }
        }
    }

    @Override
    public SaveProfile load(String slotId) {
        String targetPath = getSlotFileName(slotId);
        FileHandle targetHandle = Gdx.files.local(targetPath);

        if (!targetHandle.exists() || targetHandle.isDirectory()) {
            return createDefaultProfile(slotId);
        }

        try {
            String jsonString = targetHandle.readString("UTF-8");
            SaveProfile profile = json.fromJson(SaveProfile.class, jsonString);

            if (profile == null) {
                return createDefaultProfile(slotId);
            }

            // Schema versioning check and migration
            return validateAndMigrateProfile(profile, slotId);
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("LocalFileSaveGameRepository", "Failed to load/parse save file for slot " + slotId + ", initializing fallback state", e);
            } else {
                System.err.println("Failed to load save file for slot " + slotId + ": " + e.getMessage());
            }
            return createDefaultProfile(slotId);
        }
    }

    @Override
    public boolean exists(String slotId) {
        try {
            FileHandle targetHandle = Gdx.files.local(getSlotFileName(slotId));
            return targetHandle.exists() && !targetHandle.isDirectory();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void delete(String slotId) {
        try {
            FileHandle targetHandle = Gdx.files.local(getSlotFileName(slotId));
            if (targetHandle.exists()) {
                targetHandle.delete();
            }
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("LocalFileSaveGameRepository", "Failed to delete slot " + slotId, e);
            }
        }
    }

    @Override
    public List<String> listSlots() {
        List<String> slotList = new ArrayList<>();
        try {
            FileHandle dirHandle = Gdx.files.local(saveDir);
            if (dirHandle.exists() && dirHandle.isDirectory()) {
                for (FileHandle file : dirHandle.list(".json")) {
                    String name = file.nameWithoutExtension();
                    slotList.add(name);
                }
            }
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("LocalFileSaveGameRepository", "Error listing save slots", e);
            }
        }
        return slotList;
    }

    private SaveProfile validateAndMigrateProfile(SaveProfile profile, String slotId) {
        int profileVersion = profile.getVersion();

        if (profileVersion == SaveProfile.CURRENT_VERSION) {
            ensureProfileDefaults(profile, slotId);
            return profile;
        }

        if (profileVersion < SaveProfile.CURRENT_VERSION) {
            // Perform schema migration for older versions
            migrate(profile, profileVersion);
            ensureProfileDefaults(profile, slotId);
            return profile;
        }

        // Newer/unsupported schema version -> initialize safe default state
        if (Gdx.app != null) {
            Gdx.app.log("LocalFileSaveGameRepository", "Save profile version " + profileVersion + " is higher than supported version " + SaveProfile.CURRENT_VERSION + ". Initializing safe default state.");
        }
        return createDefaultProfile(slotId);
    }

    private void migrate(SaveProfile profile, int oldVersion) {
        // Migration logic from oldVersion to CURRENT_VERSION
        if (oldVersion < 1) {
            profile.setVersion(1);
        }
        if (profile.getPlayerData() == null) {
            profile.setPlayerData(new PlayerStateData(1000, 100));
        }
        if (profile.getMapData() == null) {
            profile.setMapData(new MapStateData(0, false, false, new ArrayList<GirlPlacementData>()));
        }
        profile.setVersion(SaveProfile.CURRENT_VERSION);
    }

    private void ensureProfileDefaults(SaveProfile profile, String slotId) {
        if (profile.getSlotId() == null || profile.getSlotId().isEmpty()) {
            profile.setSlotId(slotId);
        }
        if (profile.getPlayerData() == null) {
            profile.setPlayerData(new PlayerStateData(1000, 100));
        }
        if (profile.getMapData() == null) {
            profile.setMapData(new MapStateData(0, false, false, new ArrayList<GirlPlacementData>()));
        }
        if (profile.getMapData().getGirls() == null) {
            profile.getMapData().setGirls(new ArrayList<GirlPlacementData>());
        }
    }

    public SaveProfile createDefaultProfile(String slotId) {
        PlayerStateData playerData = new PlayerStateData(1000, 100);
        MapStateData mapData = new MapStateData(0, false, false, new ArrayList<GirlPlacementData>());
        SaveProfile profile = new SaveProfile(slotId, playerData, mapData);
        return profile;
    }
}
