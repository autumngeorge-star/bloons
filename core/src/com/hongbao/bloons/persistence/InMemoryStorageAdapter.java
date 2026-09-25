package com.hongbao.bloons.persistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * In-memory storage adapter implementation for testing and last-resort fallback.
 */
public class InMemoryStorageAdapter implements PersistenceService {

    private PlayerData storedData;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public InMemoryStorageAdapter() {
        this.storedData = new PlayerData();
    }

    public InMemoryStorageAdapter(PlayerData initialData) {
        this.storedData = initialData != null ? initialData : new PlayerData();
    }

    @Override
    public PlayerData loadData() {
        return new PlayerData(storedData.getMoney(), storedData.getHealth(), storedData.getLevel());
    }

    @Override
    public void saveData(PlayerData data) {
        if (data == null) {
            throw new PersistenceException("Cannot save null PlayerData");
        }
        this.storedData = new PlayerData(data.getMoney(), data.getHealth(), data.getLevel());
    }

    @Override
    public void saveDataAsync(PlayerData data) {
        executorService.submit(() -> saveData(data));
    }

    @Override
    public void resetData() {
        this.storedData = new PlayerData();
    }

    @Override
    public PersistenceService getActiveAdapter() {
        return this;
    }
}
