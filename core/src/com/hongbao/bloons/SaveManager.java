package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.factories.GirlFactory;

import java.io.File;
import java.util.Set;

public class SaveManager {

    private static final String SAVE_FILE_NAME = "save.json";
    private static boolean isDirty = false;

    public static void markDirty() {
        isDirty = true;
    }

    public static void update(BloonsTouhouDefense game) {
        if (isDirty) {
            saveSessionState(game);
        }
    }

    public static FileHandle getSaveFileHandle() {
        try {
            if (Gdx.files != null) {
                return Gdx.files.local(SAVE_FILE_NAME);
            }
        } catch (Throwable ignored) {
        }
        return new FileHandle(new File(SAVE_FILE_NAME));
    }

    public static void saveSessionState(BloonsTouhouDefense game) {
        if (game == null) {
            return;
        }

        try {
            Player player = game.getPlayer();
            Map map = game.getMap();
            if (player == null || map == null || map.getBloonManager() == null) {
                return;
            }

            JsonValue root = new JsonValue(JsonValue.ValueType.object);
            root.addChild("money", new JsonValue(player.getMoney()));
            root.addChild("health", new JsonValue(player.getHealth()));
            root.addChild("level", new JsonValue(map.getBloonManager().getLevel()));

            JsonValue towersArray = new JsonValue(JsonValue.ValueType.array);
            Set<GirlActor> onStageGirls = map.getOnStageGirls();
            if (onStageGirls != null) {
                for (GirlActor actor : onStageGirls) {
                    if (actor != null && actor.isActive() && actor.getGirl() != null) {
                        JsonValue towerObj = new JsonValue(JsonValue.ValueType.object);
                        towerObj.addChild("name", new JsonValue(actor.getGirl().getName()));
                        towerObj.addChild("centerX", new JsonValue(actor.getCenterX()));
                        towerObj.addChild("centerY", new JsonValue(actor.getCenterY()));
                        towerObj.addChild("level", new JsonValue(actor.getGirl().getLevel()));
                        towersArray.addChild(towerObj);
                    }
                }
            }
            root.addChild("towers", towersArray);

            String jsonString = root.toJson(JsonWriter.OutputType.json);
            FileHandle fileHandle = getSaveFileHandle();
            fileHandle.writeString(jsonString, false);
            isDirty = false;
        } catch (Throwable t) {
            // Fail gracefully without interrupting active rendering or gameplay
            System.err.println("Failed to save game session state: " + t.getMessage());
        }
    }

    public static boolean loadSessionState(BloonsTouhouDefense game) {
        if (game == null) {
            return false;
        }

        FileHandle fileHandle = getSaveFileHandle();
        if (fileHandle == null || !fileHandle.exists()) {
            return false;
        }

        try {
            String jsonString = fileHandle.readString();
            if (jsonString == null || jsonString.trim().isEmpty()) {
                return false;
            }

            JsonReader reader = new JsonReader();
            JsonValue root = reader.parse(jsonString);
            if (root == null || !root.isObject()) {
                throw new IllegalArgumentException("Invalid JSON root in save file");
            }

            int money = root.getInt("money", BloonsTouhouDefense.MONEY);
            int health = root.getInt("health", BloonsTouhouDefense.HEALTH);
            int level = root.getInt("level", 0);

            Player player = game.getPlayer();
            Map map = game.getMap();

            if (player != null) {
                player.setMoney(money);
                player.setHealth(health);
            }

            if (map != null && map.getBloonManager() != null) {
                map.getBloonManager().setLevel(level);
            }

            if (map != null) {
                map.clearOnStageGirls();

                JsonValue towers = root.get("towers");
                if (towers != null && towers.isArray()) {
                    for (JsonValue t = towers.child; t != null; t = t.next) {
                        String name = t.getString("name", null);
                        if (name == null) {
                            continue;
                        }

                        float centerX = t.getFloat("centerX", 0f);
                        float centerY = t.getFloat("centerY", 0f);
                        int towerLevel = t.getInt("level", 0);

                        Girl girl = GirlFactory.createByName(name);
                        if (girl == null) {
                            continue;
                        }

                        for (int i = 0; i < towerLevel; i++) {
                            if (girl.getUpgradeCost() != Girl.NO_UPGRADES_AVAILABLE) {
                                girl.upgrade();
                            }
                        }

                        GirlActor actor = new GirlActor(girl, centerX, centerY);
                        map.placeGirl(actor);
                    }
                }
                map.setSelectedGirl(null);
            }

            return true;
        } catch (Throwable t) {
            // Corrupted save file or parse error -> fall back safely to default starting session state
            System.err.println("Corrupted or invalid save file. Falling back to defaults: " + t.getMessage());
            resetToDefaultState(game);
            return false;
        }
    }

    public static void clearSaveState() {
        isDirty = false;
        try {
            FileHandle fileHandle = getSaveFileHandle();
            if (fileHandle != null && fileHandle.exists()) {
                fileHandle.delete();
            }
        } catch (Throwable t) {
            System.err.println("Failed to clear save state: " + t.getMessage());
        }
    }

    public static boolean hasSaveState() {
        try {
            FileHandle fileHandle = getSaveFileHandle();
            return fileHandle != null && fileHandle.exists();
        } catch (Throwable t) {
            return false;
        }
    }

    public static void resetToDefaultState(BloonsTouhouDefense game) {
        if (game == null) {
            return;
        }

        try {
            if (game.getPlayer() != null) {
                game.getPlayer().setMoney(BloonsTouhouDefense.MONEY);
                game.getPlayer().setHealth(BloonsTouhouDefense.HEALTH);
            }
            if (game.getMap() != null) {
                if (game.getMap().getBloonManager() != null) {
                    game.getMap().getBloonManager().setLevel(0);
                }
                game.getMap().clearOnStageGirls();
            }
        } catch (Throwable ignored) {
        }
    }

}
