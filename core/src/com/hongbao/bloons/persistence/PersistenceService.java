package com.hongbao.bloons.persistence;

/**
 * Service interface for persisting and loading player/game state.
 */
public interface PersistenceService {

    /**
     * Loads the persisted player data, or default data if none exists.
     *
     * @return loaded PlayerData
     * @throws PersistenceException if loading fails unrecoverably
     */
    PlayerData loadData();

    /**
     * Synchronously saves the given player data.
     *
     * @param data player data to save
     * @throws PersistenceException if saving fails
     */
    void saveData(PlayerData data);

    /**
     * Asynchronously saves the given player data in a background thread.
     *
     * @param data player data to save
     */
    void saveDataAsync(PlayerData data);

    /**
     * Resets stored player data back to default initial state.
     *
     * @throws PersistenceException if reset fails
     */
    void resetData();

    /**
     * Returns the active storage adapter or self.
     *
     * @return active PersistenceService adapter
     */
    PersistenceService getActiveAdapter();
}
