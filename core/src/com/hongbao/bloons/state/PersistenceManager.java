package com.hongbao.bloons.state;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PersistenceManager {

    private final File saveDir;

    public PersistenceManager() {
        this(new File("saves"));
    }

    public PersistenceManager(File saveDir) {
        this.saveDir = saveDir;
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    public File getSaveDir() {
        return saveDir;
    }

    public File getSlotFile(String slotName) {
        if (slotName.equalsIgnoreCase("savegame") || slotName.equalsIgnoreCase("savegame.json")) {
            return new File(saveDir, "savegame.json");
        }
        return new File(saveDir, slotName.endsWith(".json") ? slotName : slotName + ".json");
    }

    public File getTempFile(String slotName) {
        if (slotName.equalsIgnoreCase("savegame") || slotName.equalsIgnoreCase("savegame.json")) {
            return new File(saveDir, "savegame.tmp");
        }
        return new File(saveDir, (slotName.endsWith(".json") ? slotName : slotName + ".json") + ".tmp");
    }

    public synchronized boolean saveState(String slotName, GameStateData data) {
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File targetFile = getSlotFile(slotName);
        File tempFile = getTempFile(slotName);

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String jsonText = json.prettyPrint(data);

        try {
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(jsonText);
                writer.flush();
            }

            try {
                Files.move(tempFile.toPath(), targetFile.toPath(),
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                // Fallback graceful move
                Files.move(tempFile.toPath(), targetFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Files.move(tempFile.toPath(), targetFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (tempFile.exists()) {
                tempFile.delete();
            }
            return false;
        }
    }

    public synchronized GameStateData loadState(String slotName) {
        File targetFile = getSlotFile(slotName);
        if (!targetFile.exists()) {
            return null;
        }
        try {
            Json json = new Json();
            String jsonText = new String(Files.readAllBytes(targetFile.toPath()), java.nio.charset.StandardCharsets.UTF_8);
            return json.fromJson(GameStateData.class, jsonText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized boolean deleteSlot(String slotName) {
        File targetFile = getSlotFile(slotName);
        File tempFile = getTempFile(slotName);
        boolean success = true;
        if (targetFile.exists()) {
            success &= targetFile.delete();
        }
        if (tempFile.exists()) {
            success &= tempFile.delete();
        }
        return success;
    }

    public boolean hasSlot(String slotName) {
        return getSlotFile(slotName).exists();
    }
}
