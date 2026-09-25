package com.hongbao.bloons.score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreRepository {

    private final String fileName;

    public ScoreRepository() {
        this("scores.json");
    }

    public ScoreRepository(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @SuppressWarnings("unchecked")
    public synchronized List<ScoreEntry> loadScores() {
        Json json = new Json();
        json.setIgnoreUnknownFields(true);

        try {
            if (Gdx.files != null) {
                FileHandle file = Gdx.files.local(fileName);
                if (!file.exists() || file.length() == 0) {
                    return new ArrayList<>();
                }
                ArrayList<ScoreEntry> entries = json.fromJson(ArrayList.class, ScoreEntry.class, file);
                if (entries == null) {
                    return new ArrayList<>();
                }
                sortScores(entries);
                return entries;
            } else {
                File file = new File(fileName);
                if (!file.exists() || file.length() == 0) {
                    return new ArrayList<>();
                }
                try (FileReader reader = new FileReader(file)) {
                    ArrayList<ScoreEntry> entries = json.fromJson(ArrayList.class, ScoreEntry.class, reader);
                    if (entries == null) {
                        return new ArrayList<>();
                    }
                    sortScores(entries);
                    return entries;
                }
            }
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("ScoreRepository", "Failed to load scores from " + fileName + ". Resetting to default state.", e);
            } else {
                System.err.println("ScoreRepository: Failed to load scores from " + fileName + ": " + e.getMessage());
            }
            return new ArrayList<>();
        }
    }

    public synchronized void saveScores(List<ScoreEntry> scores) {
        if (scores == null) {
            scores = new ArrayList<>();
        }
        sortScores(scores);

        final List<ScoreEntry> finalScores = new ArrayList<>(scores);
        Runnable writeTask = () -> {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            json.setIgnoreUnknownFields(true);

            try {
                if (Gdx.files != null) {
                    FileHandle file = Gdx.files.local(fileName);
                    file.writeString(json.prettyPrint(finalScores), false);
                } else {
                    File file = new File(fileName);
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(json.prettyPrint(finalScores));
                    }
                }
            } catch (Exception e) {
                if (Gdx.app != null) {
                    Gdx.app.error("ScoreRepository", "Failed to save scores to " + fileName, e);
                } else {
                    System.err.println("ScoreRepository: Failed to save scores to " + fileName + ": " + e.getMessage());
                }
            }
        };

        // Non-blocking file IO during gameplay if Gdx app is running
        if (Gdx.app != null) {
            new Thread(writeTask, "ScoreSaveThread").start();
        } else {
            writeTask.run();
        }
    }

    public void saveScoresSync(List<ScoreEntry> scores) {
        if (scores == null) {
            scores = new ArrayList<>();
        }
        sortScores(scores);

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setIgnoreUnknownFields(true);

        try {
            if (Gdx.files != null) {
                FileHandle file = Gdx.files.local(fileName);
                file.writeString(json.prettyPrint(scores), false);
            } else {
                File file = new File(fileName);
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(json.prettyPrint(scores));
                }
            }
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.error("ScoreRepository", "Failed to save scores to " + fileName, e);
            } else {
                System.err.println("ScoreRepository: Failed to save scores to " + fileName + ": " + e.getMessage());
            }
        }
    }

    public void sortScores(List<ScoreEntry> scores) {
        if (scores != null) {
            scores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        }
    }
}
