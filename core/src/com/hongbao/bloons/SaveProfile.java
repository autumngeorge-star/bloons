package com.hongbao.bloons;

import java.util.HashMap;

public class SaveProfile {

	public HashMap<String, MapProfile> maps = new HashMap<>();

	public SaveProfile() {
	}

	public static class MapProfile {
		public boolean unlocked;
		public int highestWave;
		public int highScore;
		public boolean completed;

		public MapProfile() {
		}

		public MapProfile(boolean unlocked, int highestWave, int highScore, boolean completed) {
			this.unlocked = unlocked;
			this.highestWave = highestWave;
			this.highScore = highScore;
			this.completed = completed;
		}

		public boolean isUnlocked() {
			return unlocked;
		}

		public void setUnlocked(boolean unlocked) {
			this.unlocked = unlocked;
		}

		public int getHighestWave() {
			return highestWave;
		}

		public void setHighestWave(int highestWave) {
			this.highestWave = highestWave;
		}

		public int getHighScore() {
			return highScore;
		}

		public void setHighScore(int highScore) {
			this.highScore = highScore;
		}

		public boolean isCompleted() {
			return completed;
		}

		public void setCompleted(boolean completed) {
			this.completed = completed;
		}
	}

	public MapProfile getMapProfile(String mapId) {
		if (maps == null) {
			maps = new HashMap<>();
		}
		MapProfile profile = maps.get(mapId);
		if (profile == null) {
			boolean isDefaultUnlocked = MapType.BASIC.getId().equalsIgnoreCase(mapId);
			profile = new MapProfile(isDefaultUnlocked, 0, 0, false);
			maps.put(mapId, profile);
		}
		return profile;
	}
}
