package com.hongbao.bloons.state;

import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.Map;
import com.hongbao.bloons.Player;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.event.GameEvent;
import com.hongbao.bloons.event.GameEventListener;
import com.hongbao.bloons.event.GameOverEvent;
import com.hongbao.bloons.event.PauseEvent;
import com.hongbao.bloons.event.WaveCompleteEvent;
import com.hongbao.bloons.factories.GirlFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameStateManager implements GameEventListener {

    public static final String AUTO_SAVE_SLOT = "auto_save";
    public static final String SLOT_1 = "slot_1";
    public static final String SLOT_2 = "slot_2";
    public static final String SLOT_3 = "slot_3";

    private final PersistenceManager persistenceManager;
    private final ExecutorService asyncSaveExecutor;
    private final List<GameEventListener> listeners;
    private BloonsTouhouDefense game;

    public GameStateManager() {
        this(new PersistenceManager());
    }

    public GameStateManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
        this.asyncSaveExecutor = Executors.newSingleThreadExecutor();
        this.listeners = new CopyOnWriteArrayList<>();
        registerListener(this);
    }

    public GameStateManager(BloonsTouhouDefense game) {
        this(new PersistenceManager());
        this.game = game;
    }

    public void setGame(BloonsTouhouDefense game) {
        this.game = game;
    }

    public BloonsTouhouDefense getGame() {
        return game;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public void registerListener(GameEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void dispatchEvent(GameEvent event) {
        for (GameEventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof PauseEvent) {
            saveStateAsync(AUTO_SAVE_SLOT);
        } else if (event instanceof WaveCompleteEvent) {
            saveStateAsync(AUTO_SAVE_SLOT);
        } else if (event instanceof GameOverEvent) {
            clearSlotAsync(AUTO_SAVE_SLOT);
        }
    }

    /**
     * Captures current game state on the main thread.
     * Guaranteed to copy state synchronously before background execution starts.
     */
    public GameStateData captureSnapshot() {
        if (game == null) {
            return new GameStateData(200, 200, 0, new ArrayList<>(), System.currentTimeMillis());
        }

        Player player = game.getPlayer();
        Map map = game.getMap();

        int money = player != null ? player.getMoney() : 200;
        int health = player != null ? player.getHealth() : 200;
        int level = (map != null && map.getBloonManager() != null) ? map.getBloonManager().getLevel() : 0;

        List<GirlState> girlStates = new ArrayList<>();
        if (map != null && map.getOnStageGirls() != null) {
            for (GirlActor actor : map.getOnStageGirls()) {
                if (actor.isActive() && actor.getGirl() != null) {
                    Girl g = actor.getGirl();
                    girlStates.add(new GirlState(g.getName(), actor.getX(), actor.getY(), g.getLevel(), actor.getRotationAngle()));
                }
            }
        }

        return new GameStateData(money, health, level, girlStates, System.currentTimeMillis());
    }

    public Future<Boolean> saveStateAsync(final String slotName) {
        final GameStateData snapshot = captureSnapshot();
        return asyncSaveExecutor.submit(() -> persistenceManager.saveState(slotName, snapshot));
    }

    public Future<Boolean> saveStateAsync(final String slotName, final GameStateData snapshot) {
        return asyncSaveExecutor.submit(() -> persistenceManager.saveState(slotName, snapshot));
    }

    public Future<Boolean> clearSlotAsync(final String slotName) {
        return asyncSaveExecutor.submit(() -> persistenceManager.deleteSlot(slotName));
    }

    public boolean loadState(String slotName) {
        GameStateData data = persistenceManager.loadState(slotName);
        if (data == null) {
            return false;
        }
        return applyState(data);
    }

    public boolean applyState(GameStateData data) {
        if (game == null || data == null) {
            return false;
        }

        Player player = game.getPlayer();
        if (player != null) {
            player.setMoney(data.getMoney());
            player.setHealth(data.getHealth());
        }

        Map map = game.getMap();
        if (map != null) {
            if (map.getBloonManager() != null) {
                map.getBloonManager().setLevel(data.getLevel());
            }

            map.clearGirls();

            if (data.getGirls() != null) {
                for (GirlState gs : data.getGirls()) {
                    Girl girl = GirlFactory.createGirlByName(gs.getName());
                    if (girl != null) {
                        for (int i = 0; i < gs.getLevel(); i++) {
                            girl.upgrade();
                        }
                        GirlActor girlActor = new GirlActor(girl, 0, 0);
                        girlActor.setPosition(gs.getX(), gs.getY());
                        girlActor.setRotationAngle(gs.getRotationAngle());
                        map.placeGirl(girlActor);
                    }
                }
            }
            map.setSelectedGirl(null);
        }

        return true;
    }

    public void shutdown() {
        asyncSaveExecutor.shutdown();
    }
}
