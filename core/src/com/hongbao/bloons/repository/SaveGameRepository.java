package com.hongbao.bloons.repository;

import com.hongbao.bloons.dto.SaveProfile;

import java.util.List;

public interface SaveGameRepository {

    /**
     * Saves a profile for the specified profile slot.
     *
     * @param slotId  The ID or name of the save slot.
     * @param profile The SaveProfile data to persist.
     */
    void save(String slotId, SaveProfile profile);

    /**
     * Loads the save profile for the specified profile slot.
     *
     * @param slotId The ID or name of the save slot.
     * @return The restored SaveProfile, or a safe default profile if not found or corrupted.
     */
    SaveProfile load(String slotId);

    /**
     * Checks if a save game profile exists for the given slot.
     *
     * @param slotId The ID or name of the save slot.
     * @return True if a save profile file exists for slotId, false otherwise.
     */
    boolean exists(String slotId);

    /**
     * Deletes the save file for the specified slot.
     *
     * @param slotId The ID or name of the save slot.
     */
    void delete(String slotId);

    /**
     * Lists all existing save slot IDs.
     *
     * @return List of slot IDs.
     */
    List<String> listSlots();
}
