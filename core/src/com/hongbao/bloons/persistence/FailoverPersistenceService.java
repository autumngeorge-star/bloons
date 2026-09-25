package com.hongbao.bloons.persistence;

import com.badlogic.gdx.Gdx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * High-availability PersistenceService that manages failover between primary,
 * secondary, and fallback storage adapters.
 */
public class FailoverPersistenceService implements PersistenceService {

    private final PersistenceService primaryAdapter;
    private final PersistenceService secondaryAdapter;
    private final PersistenceService fallbackAdapter;
    private PersistenceService activeAdapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FailoverPersistenceService(PersistenceService primaryAdapter, PersistenceService secondaryAdapter) {
        this(primaryAdapter, secondaryAdapter, new InMemoryStorageAdapter());
    }

    public FailoverPersistenceService(PersistenceService primaryAdapter,
                                      PersistenceService secondaryAdapter,
                                      PersistenceService fallbackAdapter) {
        if (primaryAdapter == null || secondaryAdapter == null) {
            throw new IllegalArgumentException("Primary and secondary adapters must not be null");
        }
        this.primaryAdapter = primaryAdapter;
        this.secondaryAdapter = secondaryAdapter;
        this.fallbackAdapter = fallbackAdapter != null ? fallbackAdapter : new InMemoryStorageAdapter();
        this.activeAdapter = primaryAdapter;
    }

    @Override
    public synchronized PlayerData loadData() {
        try {
            return activeAdapter.loadData();
        } catch (Exception e) {
            logWarning("Primary/active storage adapter load failed. Failing over to secondary adapter.", e);
            switchToNextAdapter();
            try {
                return activeAdapter.loadData();
            } catch (Exception e2) {
                logWarning("Secondary storage adapter load failed. Failing over to fallback adapter.", e2);
                activeAdapter = fallbackAdapter;
                return activeAdapter.loadData();
            }
        }
    }

    @Override
    public synchronized void saveData(PlayerData data) {
        if (data == null) {
            throw new PersistenceException("Cannot save null PlayerData");
        }
        try {
            activeAdapter.saveData(data);
        } catch (Exception e) {
            logWarning("Storage adapter (" + activeAdapter.getClass().getSimpleName() + ") save failed. Failing over.", e);
            switchToNextAdapter();
            try {
                activeAdapter.saveData(data);
            } catch (Exception e2) {
                logWarning("Secondary storage adapter save failed. Falling back to in-memory adapter.", e2);
                activeAdapter = fallbackAdapter;
                activeAdapter.saveData(data);
            }
        }
    }

    @Override
    public void saveDataAsync(PlayerData data) {
        executorService.submit(() -> saveData(data));
    }

    @Override
    public synchronized void resetData() {
        try {
            primaryAdapter.resetData();
        } catch (Exception e) {
            logWarning("Primary adapter reset failed", e);
        }
        try {
            secondaryAdapter.resetData();
        } catch (Exception e) {
            logWarning("Secondary adapter reset failed", e);
        }
        try {
            fallbackAdapter.resetData();
        } catch (Exception e) {
            logWarning("Fallback adapter reset failed", e);
        }
    }

    @Override
    public PersistenceService getActiveAdapter() {
        return activeAdapter;
    }

    private void switchToNextAdapter() {
        if (activeAdapter == primaryAdapter) {
            activeAdapter = secondaryAdapter;
        } else if (activeAdapter == secondaryAdapter) {
            activeAdapter = fallbackAdapter;
        }
    }

    private void logWarning(String message, Throwable t) {
        if (Gdx.app != null) {
            Gdx.app.error("FailoverPersistenceService", message, t);
        } else {
            System.err.println("FailoverPersistenceService: " + message + " -> " + t.getMessage());
        }
    }
}
