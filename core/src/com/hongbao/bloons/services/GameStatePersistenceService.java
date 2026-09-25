package com.hongbao.bloons.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.BloonManager;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.Player;
import com.hongbao.bloons.actors.GirlActor;

import java.util.ArrayList;
import java.util.List;

public class GameStatePersistenceService {

    public static final String PREFERENCES_NAME = "bloons_game_state";
    public static final String KEY_HAS_ACTIVE_SESSION = "session.has_active_session";
    public static final String KEY_PLAYER_MONEY = "player.money";
    public static final String KEY_PLAYER_HEALTH = "player.health";
    public static final String KEY_LEVEL_CURRENT = "level.current_level";
    public static final String KEY_LEVEL_INDEX = "level.current_index";
    public static final String KEY_LEVEL_CLOCK = "level.clock";
    public static final String KEY_TOWERS_COUNT = "towers.count";

    private Preferences preferences;

    public GameStatePersistenceService() {
        this.preferences = null;
    }

    public GameStatePersistenceService(Preferences preferences) {
        this.preferences = preferences;
    }

    private Preferences getPreferences() {
        if (this.preferences != null) {
            return this.preferences;
        }
        if (Gdx.app != null) {
            return Gdx.app.getPreferences(PREFERENCES_NAME);
        }
        return null;
    }

    public boolean hasActiveSession() {
        try {
            Preferences prefs = getPreferences();
            if (prefs == null) {
                return false;
            }
            return prefs.getBoolean(KEY_HAS_ACTIVE_SESSION, false);
        } catch (Exception e) {
            return false;
        }
    }

    public void clearSession() {
        try {
            Preferences prefs = getPreferences();
            if (prefs != null) {
                prefs.putBoolean(KEY_HAS_ACTIVE_SESSION, false);
                prefs.flush();
            }
        } catch (Exception ignored) {
        }
    }

    public void saveGameState(Player player, Map map, BloonManager bloonManager) {
        try {
            Preferences prefs = getPreferences();
            if (prefs == null) {
                return;
            }

            prefs.putBoolean(KEY_HAS_ACTIVE_SESSION, true);

            if (player != null) {
                prefs.putInteger(KEY_PLAYER_MONEY, player.getMoney());
                prefs.putInteger(KEY_PLAYER_HEALTH, player.getHealth());
            } else {
                prefs.putInteger(KEY_PLAYER_MONEY, BloonsTouhouDefense.MONEY);
                prefs.putInteger(KEY_PLAYER_HEALTH, BloonsTouhouDefense.HEALTH);
            }

            if (bloonManager != null) {
                prefs.putInteger(KEY_LEVEL_CURRENT, bloonManager.getLevel());
                prefs.putInteger(KEY_LEVEL_INDEX, bloonManager.getCurrentIndex());
                prefs.putInteger(KEY_LEVEL_CLOCK, bloonManager.getClock());
            } else {
                prefs.putInteger(KEY_LEVEL_CURRENT, 0);
                prefs.putInteger(KEY_LEVEL_INDEX, 0);
                prefs.putInteger(KEY_LEVEL_CLOCK, 0);
            }

            if (map != null && map.getOnStageGirls() != null) {
                List<GirlActor> girls = new ArrayList<>(map.getOnStageGirls());
                prefs.putInteger(KEY_TOWERS_COUNT, girls.size());
                for (int i = 0; i < girls.size(); i++) {
                    GirlActor g = girls.get(i);
                    prefs.putString("tower." + i + ".name", g.getGirl().getName());
                    prefs.putInteger("tower." + i + ".level", g.getGirl().getLevel());
                    prefs.putFloat("tower." + i + ".center_x", g.getCenterX());
                    prefs.putFloat("tower." + i + ".center_y", g.getCenterY());
                    prefs.putFloat("tower." + i + ".rotation", g.getRotationAngle());
                }
            } else {
                prefs.putInteger(KEY_TOWERS_COUNT, 0);
            }

            prefs.flush();
        } catch (Exception e) {
            Gdx.app.log("GameStatePersistenceService", "Error saving game state", e);
        }
    }

    public boolean loadGameState(Player player, Map map, BloonManager bloonManager) {
        try {
            Preferences prefs = getPreferences();
            if (prefs == null || !hasActiveSession()) {
                return false;
            }

            int money = prefs.getInteger(KEY_PLAYER_MONEY, BloonsTouhouDefense.MONEY);
            int health = prefs.getInteger(KEY_PLAYER_HEALTH, BloonsTouhouDefense.HEALTH);

            if (health <= 0) {
                clearSession();
                return false;
            }

            if (player != null) {
                player.setMoney(money);
                player.setHealth(health);
            }

            int level = prefs.getInteger(KEY_LEVEL_CURRENT, 0);
            int index = prefs.getInteger(KEY_LEVEL_INDEX, 0);
            int clock = prefs.getInteger(KEY_LEVEL_CLOCK, 0);

            if (bloonManager != null) {
                bloonManager.restoreLevelState(level, index, clock);
            }

            if (map != null) {
                map.clearOnStageGirls();
                int count = prefs.getInteger(KEY_TOWERS_COUNT, 0);
                for (int i = 0; i < count; i++) {
                    String name = prefs.getString("tower." + i + ".name", null);
                    int girlLevel = prefs.getInteger("tower." + i + ".level", 0);
                    float cx = prefs.getFloat("tower." + i + ".center_x", 0f);
                    float cy = prefs.getFloat("tower." + i + ".center_y", 0f);
                    float rot = prefs.getFloat("tower." + i + ".rotation", 0f);

                    if (name != null) {
                        map.restoreGirl(name, girlLevel, cx, cy, rot);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            if (Gdx.app != null) {
                Gdx.app.log("GameStatePersistenceService", "Error loading game state", e);
            }
            return false;
        }
    }

    public void flush() {
        try {
            Preferences prefs = getPreferences();
            if (prefs != null) {
                prefs.flush();
            }
        } catch (Exception ignored) {
        }
    }
}
