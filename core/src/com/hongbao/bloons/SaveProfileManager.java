package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveProfileManager {

	public static final String SAVE_FILE_NAME = "save_profile.json";

	private final Json json;
	private SaveProfile profile;

	public SaveProfileManager() {
		this.json = new Json();
		this.json.setOutputType(JsonWriter.OutputType.json);
		loadProfile();
	}

	public SaveProfile loadProfile() {
		FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
		if (file.exists()) {
			try {
				profile = json.fromJson(SaveProfile.class, file);
				if (profile == null) {
					profile = createDefaultProfile();
					saveProfile();
				} else {
					ensureDefaults(profile);
				}
			} catch (Exception e) {
				Gdx.app.error("SaveProfileManager", "Error parsing " + SAVE_FILE_NAME + ", recovering default save data", e);
				profile = createDefaultProfile();
				saveProfile();
			}
		} else {
			profile = createDefaultProfile();
			saveProfile();
		}
		return profile;
	}

	public void saveProfile() {
		try {
			FileHandle file = Gdx.files.local(SAVE_FILE_NAME);
			file.writeString(json.prettyPrint(profile), false);
		} catch (Exception e) {
			Gdx.app.error("SaveProfileManager", "Failed to save profile to " + SAVE_FILE_NAME, e);
		}
	}

	public SaveProfile getProfile() {
		if (profile == null) {
			loadProfile();
		}
		return profile;
	}

	private SaveProfile createDefaultProfile() {
		SaveProfile defaultProfile = new SaveProfile();
		defaultProfile.getMapProfile(MapType.BASIC.getId()).setUnlocked(true);
		defaultProfile.getMapProfile(MapType.TURN.getId()).setUnlocked(false);
		defaultProfile.getMapProfile(MapType.HEATER.getId()).setUnlocked(false);
		return defaultProfile;
	}

	private void ensureDefaults(SaveProfile p) {
		p.getMapProfile(MapType.BASIC.getId()).setUnlocked(true);
		p.getMapProfile(MapType.TURN.getId());
		p.getMapProfile(MapType.HEATER.getId());
	}

	public SaveProfile.MapProfile getMapProfile(String mapId) {
		return getProfile().getMapProfile(mapId);
	}

	public boolean isMapUnlocked(String mapId) {
		SaveProfile.MapProfile mapProf = getMapProfile(mapId);
		return mapProf != null && mapProf.isUnlocked();
	}

	public void recordWaveCompletion(String mapId, int wave) {
		SaveProfile.MapProfile mapProf = getMapProfile(mapId);
		if (wave > mapProf.getHighestWave()) {
			mapProf.setHighestWave(wave);
			saveProfile();
		}
	}

	public void recordMapVictory(String mapId, int score) {
		SaveProfile.MapProfile mapProf = getMapProfile(mapId);
		mapProf.setCompleted(true);
		if (score > mapProf.getHighScore()) {
			mapProf.setHighScore(score);
		}
		unlockNextMap(mapId);
		saveProfile();
	}

	public void unlockNextMap(String currentMapId) {
		if (MapType.BASIC.getId().equalsIgnoreCase(currentMapId)) {
			getMapProfile(MapType.TURN.getId()).setUnlocked(true);
		} else if (MapType.TURN.getId().equalsIgnoreCase(currentMapId)) {
			getMapProfile(MapType.HEATER.getId()).setUnlocked(true);
		}
	}
}
